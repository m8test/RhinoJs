package com.m8test.language.rhino

import com.m8test.plugin.api.ApkPluginProvider
import com.m8test.script.core.api.config.ScriptConfig
import com.m8test.script.core.api.engine.Script
import com.m8test.script.core.api.engine.ScriptContext
import com.m8test.script.core.api.engine.ScriptEngine
import com.m8test.script.core.api.language.LanguageExtension
import com.m8test.script.core.impl.language.AbstractLanguage
import com.m8test.script.core.impl.language.DefaultLanguageExtension

/**
 * Description TODO
 *
 * @date 2025/02/08 15:14:10
 * @author M8Test, contact@m8test.com, https://m8test.com
 */
class RhinoLanguage(apkPluginProvider: ApkPluginProvider) : AbstractLanguage(apkPluginProvider) {
    private val extensions =
        listOf<LanguageExtension>(DefaultLanguageExtension("js", R.drawable.ic_javascript))

    override fun getExtensions(): List<LanguageExtension> {
        return extensions
    }

    override fun getBuildPluginProviderClass(): String {
        return "com.m8test.language.rhino.RhinoLanguageBuildPluginProvider"
    }

    override fun createScript(
        scriptEngine: ScriptEngine,
        scriptContext: ScriptContext,
        scriptConfig: ScriptConfig
    ): Script<*> {
        return RhinoScript(this, scriptEngine, scriptContext, scriptConfig)
    }

    override fun getVariablePrefix(): String {
        return "$"
    }

    override fun getVariableSuffix(): String {
        return ""
    }
}