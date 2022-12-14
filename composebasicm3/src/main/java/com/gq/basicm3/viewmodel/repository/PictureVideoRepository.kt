package com.gq.basicm3.viewmodel.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.gq.basicm3.AppContext
import com.gq.basicm3.data.PVUrisData
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@ViewModelScoped
class PictureVideoRepository @Inject constructor() {


    suspend fun queryVideoAndPicUriList(page: Int, limit: Int): List<PVUrisData> = withContext(Dispatchers.IO) {
        // 组装查询
        val queryArgs = Bundle()
        // 偏移量，也就是从第几条开始查询 page是页码，limit是每页数量，根据逻辑自行修改
        val start = page * limit
        queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? or ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?")
        queryArgs.putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, arrayOf("${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}", "${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}"))
        queryArgs.putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)
        queryArgs.putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(MediaStore.Files.FileColumns.DATE_MODIFIED))
        queryArgs.putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
        queryArgs.putInt(ContentResolver.QUERY_ARG_OFFSET, start)

        val mediaStoreFilesUri = MediaStore.Files.getContentUri("external")
        var cursor: Cursor? = null
        val uris = mutableListOf<PVUrisData>()
        try {
            cursor = AppContext.application.contentResolver
                .query(
                    mediaStoreFilesUri,
                    arrayOf(MediaStore.Files.FileColumns._ID,
                        MediaStore.Files.FileColumns.TITLE,
                        MediaStore.Files.FileColumns.SIZE,
                        MediaStore.Files.FileColumns.MEDIA_TYPE),
                    queryArgs, null
                )
            cursor?.let {
                val mediaType =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
                //val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val size = cursor.getInt(sizeColumn)
                    val mt = cursor.getInt(mediaType)
                    //val duration = cursor.getInt(durationColumn)
                    val contentUri: Uri = ContentUris.withAppendedId(mediaStoreFilesUri, id)
                    Timber.i(contentUri.path)
                    uris.add(PVUrisData(
                        uri = contentUri,
                        name = name,
                        size = size,
                        duration = 0,
                        type = if (mt == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) PVUrisData.TYPE_VIDEO else PVUrisData.TYPE_PICTURE
                    ))
                }
            }

        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            cursor?.close()
        }
        return@withContext uris
    }
}