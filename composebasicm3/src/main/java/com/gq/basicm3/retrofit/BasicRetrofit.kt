package com.gq.basicm3.retrofit

import com.gq.basicm3.AppContext
import okhttp3.*
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.net.Proxy
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 * @Description:
 * @Author: GQ
 * @Date: 2021/4/2 16:42
 */
@Singleton
class BasicRetrofit @Inject constructor() {

    private val cookieStore = HashMap<String, List<Cookie>>()
    private lateinit var retrofitInstance: Retrofit

    fun <T : Any> retrofit(service: KClass<T>): T {
        return retrofitInstance.create(service.java)
    }

    fun initialization(
        baseUrl: String,
        cache: Boolean = true,
        okHttpClientBuilder: OkHttpClient.Builder.() -> OkHttpClient.Builder = { this },
        retrofitBuilder: Retrofit.Builder.() -> Retrofit.Builder = { this }
    ) {
        val okHttpClient: OkHttpClient =
            OkHttpClient.Builder()
                .cookieJar(saveCookie())
                .apply {
                    if (cache) {
                        val cacheDir = AppContext.application.cacheDir
                        cache(Cache(cacheDir, (10 * 1024 * 1024).toLong()))
                    }
                    okHttpClientBuilder()
                }
                .retryOnConnectionFailure(true)//允许重试
                .build()

        retrofitInstance = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .apply {
                retrofitBuilder()
            }
            .build()
    }

    @Deprecated("")
    fun initialization(
        baseUrl: String,
        cache: Boolean = false,
        interceptor: MutableList<Interceptor>? = null,
        interceptorNetwork: MutableList<Interceptor>? = null,
        factoryCallAdapter: MutableList<CallAdapter.Factory>? = null,
        factoryConverter: MutableList<Converter.Factory>? = null
    ) {
        val okHttpClient: OkHttpClient =
            OkHttpClient.Builder()
                .proxy(Proxy.NO_PROXY)
                .cookieJar(saveCookie())
                /*.callTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)*/
                .apply {
                    interceptor?.forEach {
                        addInterceptor(it)
                    }
                    interceptorNetwork?.forEach {
                        addNetworkInterceptor(it)
                    }
                    if (cache) {
                        val cacheDir = AppContext.application.cacheDir
                        cache(Cache(cacheDir, (10 * 1024 * 1024).toLong()))
                    }
                }
                .retryOnConnectionFailure(true)//允许重试
                .build()

        retrofitInstance = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .apply {
                factoryCallAdapter?.forEach {
                    addCallAdapterFactory(it)
                }
                factoryConverter?.forEach {
                    addConverterFactory(it)
                }
            }
            .build()
    }
    /**
     * 保存Cookie
     * @return
     */
    private fun saveCookie(): CookieJar =
        object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookieStore[url.host] = cookies
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                val cookies = cookieStore[url.host]
                return cookies ?: ArrayList()
            }
        }

}