package com.m8test.language.rhino.task

import com.m8test.script.builder.api.Project
import com.m8test.script.builder.api.Task
import com.m8test.script.builder.impl.BaseTask
import java.io.File

/**
 * Description TODO
 *
 * @date 2025/02/08 15:14:10
 * @author M8Test, contact@m8test.com, https://m8test.com
 */
abstract class CompileTask(private val project: Project, name: String) :
    BaseTask(project = project, name = name) {
    protected val srcDir: File
        get() {
            return initSrcDir()
        }

    private fun initSrcDir(): File {
        // 如果只有一个源, 那么直接返回该源即可, 如果有多个的话那么就需要合并了
        val files = project.getTasks().getByName(dependentTaskName())!!.getInputs().getFiles()
        if (files.size == 1) return files.first().getFile()
        val src = project.getRelativeFile("${BuildDirs.getMergedSources()}/${dirName()}").getFile()
        if (src.exists()) src.deleteRecursively()
        src.mkdirs()
        files.forEach {
            if (it.isFile()) it.getFile().copyTo(File(src, it.getName()))
            else if (it.isDirectory()) {
                it.getFile().listFiles()?.forEach { f ->
                    f.copyRecursively(File(src, f.name))
                }
            }
        }
        return src
    }

    init {
        project.getTasks().getByName(dependentTaskName())!!.dependsOn(this)
        onlyIf { srcDir.exists() }
    }

    protected abstract fun dirName(): String
    protected abstract fun dependentTaskName(): String

    protected fun addAction(name: String, onPerform: Task.() -> Unit) {
        getActions().add(name) {
            onPerform(onPerform)
        }
    }
}