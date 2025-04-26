package com.m8test.language.rhino

import com.m8test.language.rhino.executor.RhinoFileScriptExecutor
import com.m8test.language.rhino.executor.RhinoProjectScriptExecutor
import com.m8test.language.rhino.executor.RhinoStringScriptExecutor
import com.m8test.script.core.api.config.ScriptConfig
import com.m8test.script.core.api.config.ScriptFileConfig
import com.m8test.script.core.api.config.ScriptProjectConfig
import com.m8test.script.core.api.config.ScriptStringConfig
import com.m8test.script.core.api.engine.ScriptContext
import com.m8test.script.core.api.engine.ScriptEngine
import com.m8test.script.core.api.language.Language
import com.m8test.script.core.impl.engine.AbstractScript
import com.m8test.script.core.impl.engine.ScriptExecutor

/**
 * Description TODO
 *
 * @date 2025/02/08 15:22:59
 * @author M8Test, contact@m8test.com, https://m8test.com
 */
class RhinoScript(
    language: Language,
    scriptEngine: ScriptEngine,
    scriptContext: ScriptContext,
    scriptConfig: ScriptConfig
) : AbstractScript<ScriptConfig>(
    context = scriptContext,
    language = language,
    scriptEngine = scriptEngine,
    scriptConfig = scriptConfig
) {
    override fun createStringExecutor(scriptConfig: ScriptStringConfig): ScriptExecutor<ScriptStringConfig> {
        return RhinoStringScriptExecutor(this, scriptConfig)
    }

    override fun createFileExecutor(scriptConfig: ScriptFileConfig): ScriptExecutor<ScriptFileConfig> {
        return RhinoFileScriptExecutor(this, scriptConfig)
    }

    override fun createProjectExecutor(scriptConfig: ScriptProjectConfig): ScriptExecutor<ScriptProjectConfig> {
        return RhinoProjectScriptExecutor(this, scriptConfig)
    }
}