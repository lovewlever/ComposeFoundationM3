package com.gq.basicm3.data

import android.net.Uri
import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class PVUrisData(
    var uri: Uri,
    var name: String,
    var size: Int,
    var type: Int,
    var duration: Int = 0,
) : Parcelable {
    companion object {
        const val TYPE_PICTURE = 0
        const val TYPE_VIDEO = 1
        const val TYPE_PV_ALL = 2

        // 图片多选
        const val CM_PICTURE_MULTIPLE = 5

        // 视频多选
        const val CM_VIDEO_MULTIPLE = 6

        // 全多选
        const val CM_ALL_MULTIPLE = 7

        // 全单选
        const val CM_ALL_SINGLE = 8
    }

    var selected: Boolean by mutableStateOf(false)
}