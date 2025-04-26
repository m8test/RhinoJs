package com.m8test.language.rhino

import com.m8test.script.builder.api.BuildPluginProvider
import com.m8test.script.builder.api.Plugin
import com.m8test.script.builder.api.Project
import com.m8test.script.builder.api.Settings
import com.m8test.script.core.api.engine.ScriptContext

/**
 * Description TODO
 *
 * @date 2025/02/08 15:14:10
 * @author M8Test, contact@m8test.com, https://m8test.com
 */
class RhinoLanguageBuildPluginProvider : BuildPluginProvider {
    override fun getProjectPlugins(): Map<String, (context: ScriptContext) -> Plugin<Project>> {
        return mapOf(RhinoProjectPlugin.ID to { context -> RhinoProjectPlugin(context) })
    }

    override fun getSettingsPlugins(): Map<String, (context: ScriptContext) -> Plugin<Settings>> {
        return emptyMap()
    }
}