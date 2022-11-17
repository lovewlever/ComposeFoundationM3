package com.gq.lsp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.webkit.WebView
import com.alipay.sdk.app.AuthTask
import com.alipay.sdk.app.PayTask
import com.gq.basicm3.AppContext
import com.gq.basicm3.common.ToastCommon
import com.gq.data.AuthResult
import com.gq.data.PayResult
import timber.log.Timber

object AliCommon {

    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    const val APPID = ""

    /**
     * 用于支付宝账户登录授权业务的入参 pid。
     */
    const val PID = ""

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。
     */
    const val TARGET_ID = ""

    /**
     * pkcs8 格式的商户私钥。
     *
     * 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * RSA2_PRIVATE。
     *
     * 建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    const val RSA2_PRIVATE = ""
    const val RSA_PRIVATE = ""
    private const val SDK_PAY_FLAG = 1
    private const val SDK_AUTH_FLAG = 2

    var onPayStatusCallback: (bool: Boolean, result: PayResult) -> Unit = { _,_ -> }
    var onAuthStatusCallback: (bool: Boolean, msg: String) -> Unit = { _,_ -> }

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String?, String?>)

                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo: String = payResult.result // 同步返回需要验证的信息
                    val resultStatus: String = payResult.resultStatus
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        //ToastCommon.showCenterToast("${AppContext.application.getString(R.string.pay_success)}${resultInfo}")
                        onPayStatusCallback(true, payResult)
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        //ToastCommon.showCenterToast("${AppContext.application.getString(R.string.pay_failed)}${payResult}")
                        onPayStatusCallback(false, payResult)
                    }
                }
                SDK_AUTH_FLAG -> {
                    val authResult = AuthResult(msg.obj as Map<String?, String?>, true)
                    val resultStatus: String = authResult.resultStatus

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus,
                            "9000") && TextUtils.equals(authResult.getResultCode(), "200")
                    ) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        ToastCommon.showCenterToast("${AppContext.application.getString(R.string.auth_success)}${authResult}")
                        onAuthStatusCallback(true, "Success")
                    } else {
                        // 其他状态值则为授权失败
                        ToastCommon.showCenterToast("${AppContext.application.getString(R.string.auth_failed)}${resultStatus}")
                        onAuthStatusCallback(false, resultStatus)
                    }
                }
                else -> {}
            }
        }
    }

    /**
     * 支付宝支付业务示例
     */
    fun payV2(activity: Activity, orderInfo: String) {
        /*if (TextUtils.isEmpty(APPID) || TextUtils.isEmpty(
                RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE)
        ) {
            ToastCommon.showCenterToast(AppContext.application.getString(R.string.error_missing_appid_rsa_private))
            return
        }

        *//*
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
		 *
		 * orderInfo 的获取必须来自服务端；
		 *//*
        val rsa2: Boolean = RSA2_PRIVATE.length > 0
        val params: Map<String, String> =
            OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2)
        val orderParam: String = OrderInfoUtil2_0.buildOrderParam(params)
        val privateKey: String =
            if (rsa2) RSA2_PRIVATE else RSA_PRIVATE
        val sign: String = OrderInfoUtil2_0.getSign(params, privateKey, rsa2)
        val orderInfo = "$orderParam&$sign"*/
        val payRunnable = Runnable {
            val alipay = PayTask(activity)
            val result = alipay.payV2(orderInfo, true)
            Timber.i(result.toString())
            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            mHandler.sendMessage(msg)
        }

        // 必须异步调用
        val payThread = Thread(payRunnable)
        payThread.start()
    }

    /**
     * 支付宝账户授权业务示例
     */
    fun authV2(activity: Activity, authInfo: String) {
        /*if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
            || TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE)
            || TextUtils.isEmpty(TARGET_ID)
        ) {
            ToastCommon.showCenterToast("错误: 需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
            return
        }

        *//*
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
		 *
		 * authInfo 的获取必须来自服务端；
		 *//*
        val rsa2: Boolean = RSA2_PRIVATE.length > 0
        val authInfoMap: Map<String, String> =
            OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID,TARGET_ID, rsa2)
        val info: String = OrderInfoUtil2_0.buildOrderParam(authInfoMap)
        val privateKey: String =
            if (rsa2) RSA2_PRIVATE else RSA_PRIVATE
        val sign: String = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2)
        val authInfo = "$info&$sign"*/
        val authRunnable = Runnable { // 构造AuthTask 对象
            val authTask = AuthTask(activity)
            // 调用授权接口，获取授权结果
            val result = authTask.authV2(authInfo, true)
            val msg = Message()
            msg.what = SDK_AUTH_FLAG
            msg.obj = result
            mHandler.sendMessage(msg)
        }

        // 必须异步调用
        val authThread = Thread(authRunnable)
        authThread.start()
    }

    /**
     * 将 H5 网页版支付转换成支付宝 App 支付的示例
     */
    fun h5Pay(context: Context, url: String, extras: Bundle = Bundle()) {
        WebView.setWebContentsDebuggingEnabled(true)
        val intent = Intent(context, AliPayActivity::class.java)
        /*
		 * URL 是要测试的网站，在 Demo App 中会使用 H5PayDemoActivity 内的 WebView 打开。
		 *
		 * 可以填写任一支持支付宝支付的网站（如淘宝或一号店），在网站中下订单并唤起支付宝；
		 * 或者直接填写由支付宝文档提供的“网站 Demo”生成的订单地址
		 * （如 https://mclient.alipay.com/h5Continue.htm?h5_route_token=303ff0894cd4dccf591b089761dexxxx）
		 * 进行测试。
		 *
		 * H5PayDemoActivity 中的 MyWebViewClient.shouldOverrideUrlLoading() 实现了拦截 URL 唤起支付宝，
		 * 可以参考它实现自定义的 URL 拦截逻辑。
		 *
		 * 注意：WebView 的 shouldOverrideUrlLoading(url) 无法拦截直接调用 open(url) 打开的第一个 url，
		 * 所以直接设置 url = "https://mclient.alipay.com/cashier/mobilepay.htm......" 是无法完成网页转 Native 的。
		 * 如果需要拦截直接打开的支付宝网页支付 URL，可改为使用 shouldInterceptRequest(view, request) 。
		 */
        extras.putString("url", url)
        intent.putExtras(extras)
        context.startActivity(intent)
    }
}