package com.example.schedule.util

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import com.example.schedule.interfaces.Receiver

class ServiceResultReceiver(handler: Handler?) : ResultReceiver(handler) {
    private var mReceiver: Receiver? = null

    fun setReceiver(receiver: Receiver) {
        mReceiver = receiver
    }

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        if (resultData != null) {
            mReceiver?.onReceiveResult(resultCode, resultData)
        }
    }
}