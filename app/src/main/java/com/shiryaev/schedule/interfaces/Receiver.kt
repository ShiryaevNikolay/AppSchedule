package com.shiryaev.schedule.interfaces

import android.os.Bundle

interface Receiver {
    fun onReceiveResult(resultCode: Int, resultData: Bundle)
}