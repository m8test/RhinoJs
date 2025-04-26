/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * 本源代码受Mozilla公共许可证v. 2.0条款的约束。如果没有随此文件分发MPL的副本，
 * 您可以在 http://mozilla.org/MPL/2.0/ 获取一份。 */

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/** 控制使用多个范围和线程的JavaScript的示例。 */
public class DynamicScopes {

    static boolean useDynamicScope = true;

    static class MyFactory extends ContextFactory {
        @Override
        protected boolean hasFeature(Context cx, int featureIndex) {
            if (featureIndex == Context.FEATURE_DYNAMIC_SCOPE) {
                return useDynamicScope;
            }
            return super.hasFeature(cx, featureIndex);
        }
    }

    static {
        ContextFactory.initGlobal(new MyFactory());
    }

    /**
     * 主入口点。
     *
     *
     设置共享范围，然后生成新线程，这些线程相对于该共享范围执行。
     * 尝试在有和没有动态范围的情况下运行函数以观察效果。
     *
     *

     预期输出是
     *
     *

     * sharedScope
     * nested:sharedScope
     * sharedScope
     * nested:sharedScope
     * sharedScope
     * nested:sharedScope
     * thread0
     * nested:thread0
     * thread1
     * nested:thread1
     * thread2
     * nested:thread2
     *

     *
     * 最后三行可能会根据线程调度以任何顺序排列。
     */
    public static void main(String[] args) {
        Context cx = Context.enter();
        try {
            // 只预编译源码一次
            String source =
                    ""
                            + "var x = 'sharedScope';\n"
                            + "function f() { return x; }\n"
                            // 动态范围也适用于嵌套函数
                            + "function initClosure(prefix) {\n"
                            + "    return function test() { return prefix+x; }\n"
                            + "}\n"
                            + "var closure = initClosure('nested:');\n"
                            + "";
            Script script = cx.compileString(source, "sharedScript", 1, null);

            useDynamicScope = false;
            runScripts(cx, script);
            useDynamicScope = true;
            runScripts(cx, script);
        } finally {
            Context.exit();
        }
    }

    static void runScripts(Context cx, Script script) {
        // 初始化标准对象（Object, Function等）
        // 这必须在执行脚本之前完成。该调用返回我们将共享的新范围。
        ScriptableObject sharedScope = cx.initStandardObjects(null, true);

        // 现在我们可以针对范围执行预编译的脚本以在共享范围内定义x变量和f函数。
        script.exec(cx, sharedScope);

        // 现在我们生成一些线程，这些线程执行一个调用函数'f'的脚本。范围链如下：
        //
        //            ------------------                ------------------
        //           | per-thread scope | -prototype-> |   shared scope   |
        //            ------------------                ------------------
        //                    ^
        //                    |
        //               parentScope
        //                    |
        //            ------------------
        //           | f's activation   |
        //            ------------------
        //

        // 共享范围和每个线程的范围都定义了变量'x'。如果'f'编译时启用了动态范围，
        // 则会使用每个线程范围中的'x'。否则，将使用共享范围中的'x'。在'g'中定义的'x'（调用'f'）不应被'f'看到。
        final int threadCount = 3;
        Thread[] t = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            String source2 =
                    ""
                            + "function g() { var x = 'local'; return f(); }\n"
                            + "java.lang.System.out.println(g());\n"
                            + "function g2() { var x = 'local'; return closure(); }\n"
                            + "java.lang.System.out.println(g2());\n"
                            + "";
            t[i] = new Thread(new PerThread(sharedScope, source2, "thread" + i));
        }
        for (int i = 0; i < threadCount; i++) t[i].start();
        // 在所有生成的线程完成之前，此线程不返回。
        for (int i = 0; i < threadCount; i++) {
            try {
                t[i].join();
            } catch (InterruptedException e) {
            }
        }
    }

    static class PerThread implements Runnable {

        PerThread(Scriptable sharedScope, String source, String x) {
            this.sharedScope = sharedScope;
            this.source = source;
            this.x = x;
        }

        @Override
        public void run() {
            // 这个线程需要一个新的Context。
            Context cx = Context.enter();
            try {
                // 我们可以共享这个范围。
                Scriptable threadScope = cx.newObject(sharedScope);
                threadScope.setPrototype(sharedScope);

                // 我们希望 "threadScope" 成为一个新的顶级范围，因此将其父范围设置为null。这意味着
                // 任何通过赋值创建的变量都将是 "threadScope" 的属性。
                threadScope.setParentScope(null);

                // 为线程范围创建一个名为'x'的JavaScript属性，并为其保存一个值。
                threadScope.put("x", threadScope, x);
                cx.evaluateString(threadScope, source, "threadScript", 1, null);
            } finally {
                Context.exit();
            }
        }

        private Scriptable sharedScope;
        private String source;
        private String x;
    }
}
