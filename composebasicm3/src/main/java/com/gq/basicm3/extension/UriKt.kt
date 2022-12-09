package com.gq.basicm3.extension

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Size
import androidx.documentfile.provider.DocumentFile
import com.gq.basicm3.AppContext
import com.gq.basicm3.common.DirCommon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*


/**
 * 获取视频第一帧
 */
fun Uri.getVideoFirstFrame(): Bitmap? {
    val mediaMetadataRetriever = MediaMetadataRetriever()
    mediaMetadataRetriever.setDataSource(AppContext.application, this)
    return mediaMetadataRetriever.frameAtTime
}

/**
 * 获取文件缩略图
 */
fun Uri.loadVideoThumbnail(): Bitmap? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        return AppContext.application.contentResolver.loadThumbnail(this, Size(640, 480), null)
    } else {
        return getVideoFirstFrame()
    }
}

/**
 * 获取Uri文件扩展名
 */
fun Uri.getFileExtensionName(): String {
    val fileName = DocumentFile.fromSingleUri(AppContext.application, this)?.name
    return fileName?.substringAfterLast(".") ?: ""
}

/**
 * 获取Uri文件名
 */
fun Uri.getFileName(): String {
    val fileName = DocumentFile.fromSingleUri(AppContext.application, this)?.name
    return fileName?.substring(0, fileName.lastIndexOf(".")) ?: ""
}

suspend fun Uri.saveUriFileToAppLocalStorage(): Triple<Boolean, String, File?>  = withContext(Dispatchers.IO) {
    val uri = this@saveUriFileToAppLocalStorage
    val fileDescriptor = AppContext.application.contentResolver.openFileDescriptor(uri, "r")
    fileDescriptor?.let {
        saveFileToLocalStorage(uri.getFileExtensionName(), fileDescriptor)
    } ?: Triple(false, "FileDescriptor is null", null)

}

private suspend fun saveFileToLocalStorage(
    extensionName: String,
    fileDescriptor: ParcelFileDescriptor
): Triple<Boolean, String, File?> {
    var fos: BufferedOutputStream? = null
    var fis: BufferedInputStream? = null
    try {
        val storePath = DirCommon.getCacheDirFile("save_local_file").absolutePath ?: ""
        val appDir = File(storePath)
        if (!appDir.exists()) {
            appDir.mkdir()
        }

        val fileName = "${System.currentTimeMillis()}.${extensionName}"
        val file = File(appDir, fileName)

        fos = BufferedOutputStream(FileOutputStream(file))
        fis = BufferedInputStream(FileInputStream(fileDescriptor.fileDescriptor))
        var byteRead: Int
        while (fis.read().apply { byteRead = this } != -1) {
            fos.write(byteRead)
        }
        fos.flush()
        return Triple(true, "", file)
    } catch (e: IOException) {
        e.printStackTrace()
        return Triple(false, "${e.message}", null)
    } finally {
        fis?.close()
        fos?.close()
    }
}

@Deprecated("use Uri.saveUriFileToAppLocalStorage()")
fun List<Uri>.saveUriFileToAppLocalStorage(
    callback: (MutableList<File>) -> Unit,
    error: (Exception) -> Unit = {}
) {
    val list = mutableListOf<File>()
    this.forEach { i: Uri ->
        AppContext.application.contentResolver.openFileDescriptor(i, "r")?.let {
            saveFileToLocalStorage(i.getFileExtensionName(),it, { f ->
                list.add(f)
                if (list.size == this.size) {
                    callback(list)
                }
            }, error)
        }
    }
}

@Deprecated("use Uri.saveUriFileToAppLocalStorage()")
private fun saveFileToLocalStorage(
    extensionName: String,
    fileDescriptor: ParcelFileDescriptor,
    callback: (File) -> Unit,
    error: (Exception) -> Unit = {}
) {
    var fos: BufferedOutputStream? = null
    var fis: BufferedInputStream? = null
    try {
        val storePath = DirCommon.getCacheDirFile("save_local_file").absolutePath ?: ""
        val appDir = File(storePath)
        if (!appDir.exists()) {
            appDir.mkdir()
        }

        val fileName = "${System.currentTimeMillis()}.${extensionName}"
        val file = File(appDir, fileName)

        fos = BufferedOutputStream(FileOutputStream(file))
        fis = BufferedInputStream(FileInputStream(fileDescriptor.fileDescriptor))
        var byteRead: Int
        while (fis.read().apply { byteRead = this } != -1) {
            fos.write(byteRead)
        }
        fos.flush()
        callback(file)
    } catch (e: IOException) {
        e.printStackTrace()
        error(e)
    } finally {
        fis?.close()
        fos?.close()
    }
}