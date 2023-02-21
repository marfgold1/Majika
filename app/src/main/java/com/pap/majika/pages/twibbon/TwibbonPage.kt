package com.pap.majika.pages.twibbon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.pap.majika.databinding.FragmentTwibbonPageBinding
import com.pap.majika.utils.CameraSetup


class TwibbonPage : Fragment() {
    private lateinit var binding: FragmentTwibbonPageBinding
    private lateinit var cameraSetup: CameraSetup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        binding = FragmentTwibbonPageBinding.inflate(inflater, container, false)
        with (binding) {
            cameraView.previewStreamState.observe(viewLifecycleOwner) {
                if (it == PreviewView.StreamState.STREAMING)
                    cameraView.overlay.add(twibbonView)
            }
            CameraSetup(cameraView).setup(this@TwibbonPage) { cam ->
                cameraSetup = cam
                captureBtn.isVisible = true
                twibbonChangeCameraBtn.isVisible = true
                twibbonChangeCameraBtn.setOnClickListener(changeCamera)
                cam.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                setCaptureButton(cam)
            }
            return root
        }
    }

    private val changeCamera: (View) -> Unit = {
        cameraSetup.stopCamera()
        when (cameraSetup.cameraSelector) {
            CameraSelector.DEFAULT_FRONT_CAMERA -> {
                cameraSetup.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            }
            CameraSelector.DEFAULT_BACK_CAMERA -> {
                cameraSetup.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            }
        }
        binding.captureBtn.text = getString(com.pap.majika.R.string.capture_btn_take)
        cameraSetup.startCamera()
    }

    private fun setCaptureButton(cam: CameraSetup) {
        with (binding) {
            captureBtn.text = getString(com.pap.majika.R.string.capture_btn_take)
            cam.startCamera()
            captureBtn.setOnClickListener {
                cam.stopCamera()
                captureBtn.text = getString(com.pap.majika.R.string.capture_btn_retake)
                captureBtn.setOnClickListener {
                    setCaptureButton(cam)
                }
            }
        }
    }
}