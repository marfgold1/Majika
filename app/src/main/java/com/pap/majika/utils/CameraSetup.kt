package com.pap.majika.utils

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraSetup(
    private val fragment: Fragment,
    private val view: PreviewView,
    afterSetup: ((CameraSetup)->Unit)? = null,
) {
    private val context = fragment.requireContext()
    private var executor: ExecutorService? = null
    private lateinit var cameraProvider: ProcessCameraProvider

    init {
        var isGranted = true
        if (!allPermissionsGranted(fragment)) {
            val reqPerm = fragment.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {
                if(!it) {
                    isGranted = false
                    Toast.makeText(
                        fragment.requireContext(),
                        "Can't show twibbon because camera is not permitted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            reqPerm.launch(REQUIRED_PERMISSIONS[0])
        }
        if (isGranted) {
            executor = Executors.newSingleThreadExecutor()
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                cameraProvider = cameraProviderFuture.get()
                afterSetup?.invoke(this)
            }, ContextCompat.getMainExecutor(context))
        }
    }

    fun startCamera() {
        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(view.surfaceProvider)
            }
        // Select back camera as a default
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()
            // Bind use cases to camera
            cameraProvider.bindToLifecycle(fragment, cameraSelector, preview)
        } catch(exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    fun stopCamera() {
        cameraProvider.unbindAll()
    }

    companion object {
        const val TAG = "MajikaApp"
        val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).toTypedArray()
        private fun allPermissionsGranted(frag: Fragment) = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                frag.requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }
    }
}