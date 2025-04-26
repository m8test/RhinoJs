package com.m8test.language.rhino.executor

import com.m8test.language.rhino.RhinoScript
import com.m8test.script.core.api.config.ScriptFileConfig
import org.mozilla.javascript.Context
import org.mozilla.javascript.ScriptableObject
import java.io.FileReader

/**
 * Description TODO
 *
 * @date 2025/02/08 15:59:35
 * @author M8Test, contact@m8test.com, https://m8test.com
 */
class RhinoFileScriptExecutor(script: RhinoScript, private val scriptConfig: ScriptFileConfig) :
    RhinoScriptExecutor<ScriptFileConfig>(script = script, scriptConfig = scriptConfig) {
    override fun execute(context: Context, scope: ScriptableObject): Any? {
        val file = scriptConfig.getFile()
        return FileReader(file.getFile()).use {
            context.evaluateReader(scope, it, file.getCanonicalPath(), 1, null)
        }
    }
}