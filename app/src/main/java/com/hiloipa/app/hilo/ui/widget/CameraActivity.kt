/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.ui.widget

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.utils.showLoading
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraUtils
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File

class CameraActivity : AppCompatActivity() {

    companion object {
        const val pathKey = "co.fabity.software.joodworld.NEW_IMAGE_PATH"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        toolbar.setNavigationOnClickListener { finish() }

        takePictureBtn.setOnClickListener {
            cameraView.capturePicture()
        }

        cameraView.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray?) {
                val dialog = showLoading()
                CameraUtils.decodeBitmap(jpeg) { bitmap: Bitmap ->
                    val file = File(cacheDir, "${System.currentTimeMillis()}.jpg")
                    file.outputStream().use {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                        val resultIntent = Intent()
                        val extras = Bundle()
                        extras.putString(pathKey, file.absolutePath)
                        resultIntent.putExtras(extras)
                        dialog.dismiss()
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraView.destroy()
    }
}
