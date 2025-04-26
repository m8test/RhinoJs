package com.m8test.language.rhino.task

import com.m8test.script.builder.api.Project
import java.io.File

/**
 * Description TODO
 *
 * @date 2025/02/08 15:14:10
 * @author M8Test, contact@m8test.com, https://m8test.com
 */
abstract class CompileRhinoTask(project: Project, name: String) :
    CopyBaseCompileTask(project = project, name = name) {
    override fun isScriptFile(file: File): Boolean {
        return file.isFile && file.extension == "js"
    }
}