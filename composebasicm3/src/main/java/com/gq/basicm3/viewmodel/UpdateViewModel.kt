package com.gq.basicm3.viewmodel

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gq.basicm3.common.Commons.Version
import com.gq.basicm3.data.DownloadApkResult
import com.gq.basicm3.extension.ifNotNullAndEmpty
import com.gq.basicm3.viewmodel.repository.UpdateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val updateApkRepository: UpdateRepository,
) : ViewModel() {

    private val _downloadApkDoneResult = MutableLiveData<String>()
    val downloadApkDoneResult: LiveData<String> = _downloadApkDoneResult
    private var downloadApkJob: Job? = null

    /**
     * 下载APK
     */
    fun downloadApk(url: String) {
        downloadApkJob = viewModelScope.launch {
            updateApkRepository.startUpdateApk(url).let { result: DownloadApkResult<String> ->
                if (result.code == 200) {
                    result.data.ifNotNullAndEmpty { it[0] }?.let { it: String ->
                        _downloadApkDoneResult.value = it
                    }
                } else {

                }
            }
        }
    }

    /**
     * 取消下载
     */
    fun cancelDownloadApk() {
        downloadApkJob?.cancel()
    }

    /**
     * 安装Apk
     */
    fun installApk(context: Context, file: File) {
        context.startActivity(Intent(Intent.ACTION_VIEW).also { intent ->
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val apkUri = FileProvider.getUriForFile(context, "${Version.getApplicationId()}.fileProvider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        })
    }
}