package com.m8test.language.rhino.executor

import com.m8test.language.rhino.RhinoScript
import com.m8test.script.core.api.config.ScriptProjectConfig
import org.mozilla.javascript.Context
import org.mozilla.javascript.ScriptableObject
import org.mozilla.javascript.commonjs.module.RequireBuilder
import org.mozilla.javascript.commonjs.module.provider.SoftCachingModuleScriptProvider
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider
import java.io.File
import java.io.FileReader

/**
 * Description TODO
 *
 * @date 2025/02/08 19:11:07
 * @author M8Test, contact@m8test.com, https://m8test.com
 */
class RhinoProjectScriptExecutor(
    script: RhinoScript,
    private val scriptConfig: ScriptProjectConfig,
) : RhinoScriptExecutor<ScriptProjectConfig>(script = script, scriptConfig = scriptConfig) {
    private fun installRequire(context: Context, scope: ScriptableObject) {
        val projectRoot = scriptConfig.getRootPath().getFile()
        val moduleScriptProvider = SoftCachingModuleScriptProvider(
            UrlModuleSourceProvider(listOf(projectRoot.toURI()), null)
        )
        RequireBuilder()
            .setModuleScriptProvider(moduleScriptProvider)
            .setSandboxed(true)
            .createRequire(context, scope)
            .install(scope)
    }

    override fun execute(context: Context, scope: ScriptableObject): Any? {
        val projectRoot = scriptConfig.getRootPath().getFile()
        installRequire(context, scope)
        // 执行初始化脚本
        scriptConfig.getInitScripts().forEach { s ->
            val file = File(projectRoot, s)
            FileReader(file).use {
                context.evaluateReader(scope, it, file.canonicalPath, 1, null)
            }
        }
        // 执行入口脚本
        val file = File(projectRoot, scriptConfig.getEntry())
        return FileReader(file).use {
            context.evaluateReader(scope, it, file.canonicalPath, 1, null)
        }
    }

    override fun executeFile(file: File): Any? = enterContext { context, scope ->
        installRequire(context, scope)
        FileReader(file).use {
            context.evaluateReader(scope, it, file.canonicalPath, 1, null)
        }
    }

    override fun executeString(script: String): Any? = enterContext { context, scope ->
        installRequire(context, scope)
        context.evaluateString(
            scope,
            script,
            getScript().getContext().getBindings().getStrings().md5(script),
            1,
            null
        )
    }
}