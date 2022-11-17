package com.gq.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * userInfo.getString("appId")
req.partnerId = userInfo.getString("partnerId")
req.prepayId = userInfo.getString("prepayId")
req.packageValue = userInfo.getString("packageValue")
req.nonceStr = userInfo.getString("nonceStr")
req.timeStamp = userInfo.getString("timeStamp")
req.sign = userInfo.getString("sign")
 */
@Parcelize
data class WXPayData(
    val appId: String,
    val partnerId: String,
    val prepayId: String,
    val packageValue: String,
    val nonceStr: String,
    val timeStamp: String,
    val sign: String,
    val extData: String = "",
) : Parcelable
