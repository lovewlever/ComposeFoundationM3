package com.gq.basicm3.common

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonCommon {
    val gson by lazy { Gson() }
    val gsonExpose by lazy { GsonBuilder().excludeFieldsWithoutExposeAnnotation().create() }
}