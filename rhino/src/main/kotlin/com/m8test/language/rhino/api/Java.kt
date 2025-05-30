package com.m8test.language.rhino.api

import com.m8test.dokka.annotation.Keep
import com.m8test.script.core.api.component.Variable
import org.mozilla.javascript.NativeJavaClass

/**
 * 用于和java互操作的接口.
 *
 * @date 2025/05/30 13:36:31
 * @author M8Test, contact@m8test.com, https://m8test.com
 */
@Keep
interface Java : Variable {
    /**
     * 加载java类
     *
     * @param clazz java类名
     * @return 加载的java类
     */
    @Keep
    fun loadClass(clazz: String): NativeJavaClass
}