package com.tdg.vroom.utils

import android.util.Log
import com.tdg.vroom.BuildConfig
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

class MyLog {
    companion object {
        private var sTag = "MyLog-"
        private var sShowLogs = BuildConfig.DEBUG
        private var sIsPackageNameVisible = false
        private var sIsThreadIdVisible = false
        private var sIsTimeVisible = true
        private var sIsRemoveOverride = true
        private var sPackageName: String? = null

        fun v(msg: String) {
            if (sShowLogs) {
                logIt(Log.VERBOSE, msg, null)
            }
        }

        fun d(msg: String) {
            if (sShowLogs) {
                logIt(Log.DEBUG, msg, null)
            }
        }

        fun i(msg: String) {
            if (sShowLogs) {
                logIt(Log.INFO, msg, null)
            }
        }

        fun w(msg: String) {
            if (sShowLogs) {
                logIt(Log.WARN, msg, null)
            }
        }

        fun e(msg: String) {
            if (sShowLogs) {
                logIt(Log.ERROR, msg, null)
            }
        }

        fun a(msg: String) {
            if (sShowLogs) {
                logIt(Log.ASSERT, msg, null)
            }
        }

        fun e(msg: String, t: Throwable) {
            if (sShowLogs) {
                logIt(Log.ERROR, msg, t)
            }
        }

        private fun logIt(level: Int, msg: String, t: Throwable?) {
            val stackTrace = Thread.currentThread().stackTrace
            if (stackTrace.size > 4) {
                val element = stackTrace[4]

                val result = StringBuilder()
                if (sIsTimeVisible) {
                    result.append(getTime()).append(" - ")
                }

                if (sIsThreadIdVisible) {
                    result.append("T:").append(getThreadId()).append(" | ")
                }

                // Class
                val simpleClassName = StringBuilder()
                val fullClassName = element.className


                if (sIsPackageNameVisible) {
                    simpleClassName.append(fullClassName.replace(sPackageName!!, ""))
                } else {
                    simpleClassName.append(fullClassName.substring(fullClassName.lastIndexOf('.')))
                }

                if (sIsRemoveOverride && simpleClassName.indexOf("\$override") != -1) {
                    simpleClassName.replace(
                        simpleClassName.indexOf("\$override"),
                        simpleClassName.length,
                        "*"
                    )
                }

                while (simpleClassName.length < if (sIsPackageNameVisible) 35 else 15) {
                    simpleClassName.append(" ")
                }

                result.append(simpleClassName).append(" # ")

                // Method
                val methodName = StringBuilder(element.methodName)
                methodName.append("()")
                while (methodName.length < 25) methodName.append(" ")
                result.append(methodName).append(" => ")

                // Message
                result.append(msg)

                if (t != null) {
                    val sw = StringWriter()
                    val pw = PrintWriter(sw)
                    t.printStackTrace(pw)
                    pw.flush()
                    result.append("\n Throwable: ")
                    result.append(sw.toString())
                }

                Log.println(level, sTag, result.toString())
            }
        }

        private fun getThreadId(): StringBuilder {
            val threadId = StringBuilder(Thread.currentThread().id.toString())
            while (threadId.length < 6) threadId.append(" ")
            return threadId
        }

        private fun getTime(): String {
            val df = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            return df.format(Calendar.getInstance().time)
        }

    }
}
