package com.shiryaev.gallerypicker.utils

import android.util.Log

object MLog {
    var canLog = true
    fun e(tag: String, message: String?) {
        if (canLog) message?.let { Log.e(tag, it) }
    }

    fun d(tag: String, message: String?) {
        if (canLog) message?.let { Log.d(tag, it) }
    }

    fun v(tag: String, message: String?) {
        if (canLog) message?.let { Log.v(tag, it) }
    }
}