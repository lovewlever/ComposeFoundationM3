package com.gq.basicm3.extension

import android.os.Parcelable
import com.google.gson.Gson
import com.gq.basicm3.common.GsonCommon

inline fun Parcelable.toGsonStr(gson: Gson = GsonCommon.gson): String =
    gson.toJson(this)