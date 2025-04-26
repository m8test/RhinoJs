package com.m8test.language.rhino

import com.m8test.language.rhino.task.CompileRhinoInitTask
import com.m8test.language.rhino.task.CompileRhinoLibTask
import com.m8test.language.rhino.task.CompileRhinoSrcTask
import com.m8test.script.builder.api.Plugin
import com.m8test.script.builder.api.Project
import com.m8test.script.builder.impl.PluginIds
import com.m8test.script.core.api.engine.ScriptContext

/**
 * Description TODO
 *
 * @date 2025/02/08 15:14:10
 * @author M8Test, contact@m8test.com, https://m8test.com
 */
class RhinoProjectPlugin(private val context: ScriptContext) : Plugin<Project> {
    override fun apply(target: Project) {
        if (!target.getPlugins().has(PluginIds.getLibrary())) {
            target.getPlugins().apply(PluginIds.getLibrary())
        }
        // 添加一个task
        target.getTasks().add(CompileRhinoLibTask(target), null)
        target.getTasks().add(CompileRhinoInitTask(target), null)
        target.getTasks().add(CompileRhinoSrcTask(target), null)
    }

    override fun getId(): String {
        return ID
    }

    companion object {
        const val ID = "javascript"
    }
}