package com.m8test.language.rhino.task

import com.m8test.script.builder.api.Project
import com.m8test.script.builder.impl.PropertyNames
import com.m8test.script.builder.impl.TaskNames
import java.io.File
import java.util.Locale

/**
 * Description TODO
 *
 * @date 2025/02/08 15:14:10
 * @author M8Test, contact@m8test.com, https://m8test.com
 */
abstract class CopyBaseCompileTask(project: Project, name: String) :
    CompileTask(project = project, name = name) {
    protected abstract fun isScriptFile(file: File): Boolean

    private fun replaceFirstChar(src: String): String {
        return src.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    private fun addSetInitScriptAction() {
        addAction("setInitScripts") {
            val compiledScripts = srcDir.walk().filter { isScriptFile(it) }
                .map { it.toRelativeString(srcDir) }.toList()
            val buildInitTask = getProject().getTasks().getByName(TaskNames.getBuildInit())!!
            buildInitTask.getOutputs()
                .addProperty(PropertyNames.getInitScripts(), compiledScripts, false)
        }
    }

    private fun addSetEntryAction() {
        addAction("setEntry") {
            val project = getProject()
            val entry = project.getConfig().getEntry()
            val buildSrcTask = project.getTasks().getByName(TaskNames.getBuildSrc())!!
            buildSrcTask.getOutputs().addProperty(PropertyNames.getEntry(), entry, false)
        }
    }

    private fun getCopyActionName(): String {
        return "copy${replaceFirstChar(dirName())}${
            replaceFirstChar(getProject().getContext().getCurrentScript().getLanguage().getName())
        }Files"
    }

    init {
        addAction(getCopyActionName()) {
            val scriptFiles = srcDir.walk().filter { isScriptFile(it) }.toList()
            val outputDir =
                project.getRelativeFile("${BuildDirs.getOutput()}/${dirName()}").getFile()
            outputDir.mkdirs()
            scriptFiles.forEach {
                val relativePath = it.toRelativeString(srcDir)
                println("relativePath = $relativePath , ${dirName()}")
                val dest = File(outputDir, relativePath)
                dest.mkdirs()
                dest.delete()
                it.copyTo(dest)
            }
            outputDir.listFiles()?.forEach {
                project.getTasks().getByName(dependentTaskName())!!.getOutputs().addFile(
                    getProject().getContext().getBindings().getFiles().buildFile { setFile(it) }
                )
            }
        }
        when (dirName()) {
            "init" -> addSetInitScriptAction()
            "src" -> addSetEntryAction()
        }
    }
}