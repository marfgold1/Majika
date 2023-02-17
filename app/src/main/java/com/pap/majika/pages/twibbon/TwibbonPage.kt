package com.pap.majika.pages.twibbon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.pap.majika.R
import com.pap.majika.databinding.FragmentTwibbonPageBinding
import com.pap.majika.utils.CameraSetup

/**
 * A simple [Fragment] subclass.
 * Use the [TwibbonPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class TwibbonPage : Fragment() {
    private var _binding: FragmentTwibbonPageBinding? = null
    private val binding get() = _binding!!

    private var camera: CameraSetup? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        _binding = FragmentTwibbonPageBinding.inflate(inflater, container, false)
        binding.captureBtn.isVisible = false
        CameraSetup(binding.cameraView).setup(this) { cam ->
            binding.captureBtn.isVisible = true
            binding.cameraView.overlay.add(binding.twibbonView)
            cam.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            setCaptureButton(cam)
        }
        return binding.root
    }

    private fun setCaptureButton(cam: CameraSetup) {
        binding.captureBtn.text = getString(R.string.capture_btn_take)
        cam.startCamera()
        binding.captureBtn.setOnClickListener {
            cam.stopCamera()
            binding.captureBtn.text = getString(R.string.capture_btn_retake)
            binding.captureBtn.setOnClickListener {
                setCaptureButton(cam)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TwibbonPage.
         */
        @JvmStatic
        fun newInstance() =
            TwibbonPage()
    }
}