package com.gq.basicm3.extension

import com.gq.basicm3.common.GsonCommon

fun <K, V> Map<K,V>.toGsonStr() =
    GsonCommon.gson.toJson(this)
