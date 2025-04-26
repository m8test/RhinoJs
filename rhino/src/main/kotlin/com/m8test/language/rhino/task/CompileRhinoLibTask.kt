package com.m8test.language.rhino.task

import com.m8test.script.builder.api.Project
import com.m8test.script.builder.impl.ProjectDirs
import com.m8test.script.builder.impl.TaskNames


/**
 * Description TODO
 *
 * @date 2025/02/08 15:14:10
 * @author M8Test, contact@m8test.com, https://m8test.com
 */
class CompileRhinoLibTask(project: Project) :
    CompileRhinoTask(project = project, name = TASK_NAME) {

    override fun dirName(): String {
        return ProjectDirs.getLib()
    }

    override fun dependentTaskName(): String {
        return TaskNames.getBuildLib()
    }

    companion object {
        const val TASK_NAME = "compileJavascriptLib"
    }
}