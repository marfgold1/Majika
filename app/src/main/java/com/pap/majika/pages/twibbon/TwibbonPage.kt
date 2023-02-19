package com.pap.majika.pages.twibbon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.pap.majika.R
import com.pap.majika.databinding.FragmentTwibbonPageBinding
import com.pap.majika.utils.CameraSetup

class TwibbonPage : Fragment() {
    private var _binding: FragmentTwibbonPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        _binding = FragmentTwibbonPageBinding.inflate(inflater, container, false)
        with (binding) {
            cameraView.previewStreamState.observe(viewLifecycleOwner) {
                if (it == PreviewView.StreamState.STREAMING)
                    cameraView.overlay.add(twibbonView)
            }
            captureBtn.isVisible = false
            CameraSetup(cameraView).setup(this@TwibbonPage) { cam ->
                captureBtn.isVisible = true
                cam.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                setCaptureButton(cam)
            }
            return root
        }
    }

    private fun setCaptureButton(cam: CameraSetup) {
        with (binding) {
            captureBtn.text = getString(R.string.capture_btn_take)
            cam.startCamera()
            captureBtn.setOnClickListener {
                cam.stopCamera()
                captureBtn.text = getString(R.string.capture_btn_retake)
                captureBtn.setOnClickListener {
                    setCaptureButton(cam)
                }
            }
        }
    }
}