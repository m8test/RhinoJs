package com.m8test.language.rhino.executor

import com.m8test.language.rhino.RhinoScript
import com.m8test.script.core.api.config.ScriptConfig
import com.m8test.script.core.api.engine.Script
import com.m8test.script.core.impl.engine.AbstractScriptExecutor
import org.mozilla.javascript.Context
import org.mozilla.javascript.ScriptableObject

/**
 * Description TODO
 *
 * @date 2025/02/08 15:47:22
 * @author M8Test, contact@m8test.com, https://m8test.com
 */
abstract class RhinoScriptExecutor<T : ScriptConfig>(
    private val script: RhinoScript,
    private val scriptConfig: T
) : AbstractScriptExecutor<T>(script) {
    protected abstract fun execute(context: Context, scope: ScriptableObject): Any?
    override fun execute(): Any? {
        return runCatching {
            val context = Context.enter()
            val scope = context.initStandardObjects(null, true)
            context.isInterpretedMode = true
            // 添加全局变量到js中
            script.getVariables { v, c -> true }.forEach { v ->
                val jsObj = Context.javaToJS(v, scope)
                ScriptableObject.putProperty(scope, script.getVariableName(v), jsObj)
            }
            execute(context, scope)
        }.let {
            if (it.isFailure) {
                script.getContext().getBindings().getConsole()
                    .error(it.exceptionOrNull()?.message ?: "no error message is null")
                it.exceptionOrNull()?.printStackTrace()
            }
            Context.exit()
            it.getOrNull()
        }
    }

    override fun getScript(): Script<*> {
        return script
    }

    override fun getConfig(): T {
        return scriptConfig
    }
}