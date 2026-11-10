package com.example.myapplication.util

import androidx.annotation.StringRes
import androidx.test.platform.app.InstrumentationRegistry

fun @receiver:StringRes Int.asString(): String =
    InstrumentationRegistry.getInstrumentation().targetContext.getString(this)
