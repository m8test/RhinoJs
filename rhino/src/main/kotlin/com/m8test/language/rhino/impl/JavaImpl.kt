package com.m8test.language.rhino.impl

import com.m8test.language.rhino.api.Java
import com.m8test.script.core.api.plugin.Plugins
import org.mozilla.javascript.NativeJavaClass
import org.mozilla.javascript.Scriptable
import java.lang.reflect.Type

/**
 * Description TODO
 *
 * @date 2025/05/30 13:41:42
 * @author M8Test, contact@m8test.com, https://m8test.com
 */
class JavaImpl(private val scope: () -> Scriptable, private val plugins: () -> Plugins) : Java {
    override fun loadClass(clazz: String): NativeJavaClass {
        return NativeJavaClass(scope(), plugins().getClassLoader().loadClass(clazz))
    }

    override fun getPublicType(): Type {
        return Java::class.java
    }

    override fun getGlobalName(): String {
        return "java"
    }

    override fun isPrefixRequired(): Boolean = true

    override fun isSuffixRequired(): Boolean = true
}