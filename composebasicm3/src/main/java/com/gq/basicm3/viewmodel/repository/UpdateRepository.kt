package com.gq.basicm3.viewmodel.repository


import com.gq.basicm3.AppContext
import com.gq.basicm3.R
import com.gq.basicm3.common.DirCommon
import com.gq.basicm3.data.DownloadApkResult
import com.gq.basicm3.viewmodel.api.UpdateApi
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.*
import javax.inject.Inject

@ViewModelScoped
class UpdateRepository @Inject constructor(
    private val updateApi: UpdateApi,
) {

    /**
     * 下载APK
     */
    suspend fun startUpdateApk(url: String): DownloadApkResult<String> =
        withContext(Dispatchers.IO) {
            updateApi.downloadUpdateApk(url).execute().body()?.byteStream()
                ?.use { stream: InputStream ->
                    val path = File(getPath(), "update.apk")
                    val bis = BufferedInputStream(stream)
                    val bos = BufferedOutputStream(FileOutputStream(path))
                    var byteRead: Int
                    while (bis.read().apply { byteRead = this } != -1) {
                        bos.write(byteRead)
                    }
                    bos.flush()

                    return@withContext DownloadApkResult(
                        code = 200,
                        msg = "",
                        data = mutableListOf(path.absolutePath)
                    )
                } ?: let {
                return@withContext DownloadApkResult(
                    code = 400,
                    msg = AppContext.application.getString(R.string.cb_download_fail)
                )
            }
        }


    private fun getPath(): String {
        return DirCommon.getCacheDirFile("apks").absolutePath
    }

    /**
     * 查询Ipv4
     */
    suspend fun queryQueryExternalNetworkIpv4() = withContext(Dispatchers.IO) {
        return@withContext try {
            updateApi.queryQueryExternalNetworkIpv4("http://pv.sohu.com/cityjson?ie=utf-8").execute().body()?.string()?.let { str ->
                Timber.d(str)
            }
            ""
        } catch (e: Exception) {
            Timber.e(e)
            ""
        }
    }

    /**
     * 查询Ipv4
     */
    suspend fun queryQueryExternalNetworkIpv6() = withContext(Dispatchers.IO) {
        return@withContext try {
            updateApi.queryQueryExternalNetworkIpv4("https://ipv6.ipw.cn/").execute().body()?.string()?.let { str ->
                Timber.d(str)
            }
        } catch (e: Exception) {
            Timber.e(e)
            ""
        }
    }
}