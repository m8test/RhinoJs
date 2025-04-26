package com.m8test.language.rhino.executor

import com.m8test.language.rhino.RhinoScript
import com.m8test.script.core.api.config.ScriptStringConfig
import org.mozilla.javascript.Context
import org.mozilla.javascript.ScriptableObject

/**
 * Description TODO
 *
 * @date 2025/02/08 15:57:59
 * @author M8Test, contact@m8test.com, https://m8test.com
 */
class RhinoStringScriptExecutor(script: RhinoScript, private val scriptConfig: ScriptStringConfig) :
    RhinoScriptExecutor<ScriptStringConfig>(script = script, scriptConfig = scriptConfig) {
    override fun execute(context: Context, scope: ScriptableObject): Any? {
        return context.evaluateString(
            scope,
            scriptConfig.getContent(),
            scriptConfig.getName(),
            1,
            null
        )
    }
}