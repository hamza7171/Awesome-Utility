package com.ameer.awesome.utility

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import org.apache.commons.io.FileUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object FileUtils {

    /**
     * Copy a file from one path to another
     * send res file path in resPath param and desPath is where you want to copy the file
     * */
    fun copyFile(resPath: String, desPath: String) {
        val resFile = File(resPath)
        val desFile = File(desPath)
        FileUtils.copyFileToDirectory(resFile, desFile)
    }

    /**
     * this method will refresh the storage
     * when you save file in any external directory then need to refresh the media to show it in gallery
     * give list of files you modified or add and this method will refresh you media.
     * */
    private fun refreshStorage(filePath: Array<String>, activity: Activity) {
        MediaScannerConnection.scanFile(activity.applicationContext, filePath, null) { path: String, uri: Uri -> }
    }

    /**
     * This method will create a folder in internal storage of app
     * pass the folderName in param and check in internal storage of your package name, there a folder will created
     * */
    fun getInternalFolder(context: Context, folderName: String): String {
        val folder = context.getDir(folderName, Context.MODE_PRIVATE)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return folder.toString()
    }

    /**
     * get a frame from video
     * pass video path in param and this method will get a bitmap from video and return
     * bitmap will be null if video path not exist or invalid
     * */
    fun getVideoFrame(videoPath: String): Bitmap? {
        val bitmap: Bitmap?

        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(videoPath, HashMap())
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Throwable("Exception in retrive VideoFrameFromVideo(String videoPath)" + e.message)
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    }

    /**
     * This method will flip your bitmap on x and y axis
     * Pass a bitmap in param that you want to flip
     * */
    private fun flipBitmap(source: Bitmap, xFlip: Boolean, yFlip: Boolean): Bitmap? {
        val matrix = Matrix()
        matrix.postScale(
            if (xFlip) -1f else 1f,
            if (yFlip) -1f else 1f,
            source.width / 2f,
            source.height / 2f
        )
        val bmp = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        bmp.density = 500
        return bmp
    }


    /**
     * This method will save your bitmap to storage
     * pass bitmap and context in params
     * */
    private fun saveMediaToStorage(bitmap: Bitmap, baseContext: Context) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()

        bitmap.recycle()
        val dummy = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            baseContext.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            dummy.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }
}