package com.gq.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.gq.lsp.WeChatCommon
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import timber.log.Timber


class WXPayEntryActivity: Activity(), IWXAPIEventHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val intent = intent
            WeChatCommon.api.handleIntent(intent, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        WeChatCommon.api.handleIntent(intent, this)
    }


    override fun onReq(req: BaseReq) {
        Timber.i("${req.type}")
    }

    override fun onResp(baseResp: BaseResp) {
        val resp = baseResp as PayResp
        val orderId = resp.extData
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            when (baseResp.errCode) {
                0 -> { WeChatCommon.onPayStatusCallback(true, "支付成功", orderId) } //支付成功页面返回或点击返回商家所做的事情
                -1 -> {
                    Toast.makeText(this, "支付错误", Toast.LENGTH_SHORT).show()
                    WeChatCommon.onPayStatusCallback(false, "支付错误", "")
                }
                -2 -> {
                    Toast.makeText(this, "用户取消了订单", Toast.LENGTH_SHORT).show()
                    WeChatCommon.onPayStatusCallback(false, "用户取消了订单", "")
                }
            }
            finish() //这里需要关闭该页面
        }

        //finish()
    }

}