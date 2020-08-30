package com.example.gallerypicker.utils

import android.content.Context
import org.jetbrains.anko.runOnUiThread

class RunOnUiThread(var context: Context?) {
    fun safely(dothis: () -> Unit) {
        context?.runOnUiThread {
            try {
                dothis.invoke()
            } catch (e: Exception) {
                MLog.e("runonui", e.toString())
                e.printStackTrace()
            }
        }
    }
}