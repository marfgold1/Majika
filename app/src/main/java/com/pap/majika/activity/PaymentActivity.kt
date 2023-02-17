package com.pap.majika.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Size
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.pap.majika.R
import com.pap.majika.databinding.ActivityPaymentBinding
import com.pap.majika.utils.CameraSetup
import com.pap.majika.viewModel.PaymentStatusViewModel
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private val trIdRegex = """^[a-z]{32}$""".toRegex()
    private lateinit var handler: Handler
    private lateinit var cameraSetup: CameraSetup
    private var isInit = false
    private val analysisUseCase = ImageAnalysis.Builder()
        .setTargetResolution(Size(1280, 720))
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
    // getClient() creates a new instance of the MLKit barcode scanner with the specified options
    private val scanner = BarcodeScanning.getClient(BarcodeScannerOptions.Builder(
    ).setBarcodeFormats(
        Barcode.FORMAT_QR_CODE,
    ).build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        with (binding) {
            statusLayout.isVisible = false
            setContentView(root)
            handler = Handler(Looper.getMainLooper())
            val total = intent.extras?.getString("total_price")!!
            priceTotalText.text = getString(R.string.total_payment_text, total)
            cameraSetup = CameraSetup(previewView)
            cameraSetup.setup(this@PaymentActivity) { cam ->
                startAnalysis()
                cam.startCamera(analysisUseCase)
            }
        }
    }

    private fun startAnalysis() {
        stopAnalysis()
        analysisUseCase.setAnalyzer(
            // newSingleThreadExecutor() will let us perform analysis on a single worker thread
            Executors.newSingleThreadExecutor()
        ) { imageProxy ->
            processImageProxy(scanner, imageProxy)
        }
    }

    private fun stopAnalysis() {
        analysisUseCase.clearAnalyzer()
    }

    private val backToMain = {
        finish()
    }

    private fun refreshTimer(x: Int) {
        if (x == 0) return
        binding.statusTitleText.text = getString(
            R.string.status_title_pay_done,
            x
        )
        handler.postDelayed({
            refreshTimer(x-1)
        }, 1000)
    }

    private fun changeStatus(status: String) {
        with (binding) {
            when (status) {
                "SUCCESS" -> {
                    cameraSetup.stopCamera()
                    statusImg.setColorFilter(Color.argb(255, 0, 255, 0))
                    statusImg.setImageResource(R.drawable.ic_status_success)
                    statusLayout.isVisible = true
                    statusBodyText.text = getText(
                        R.string.status_body_pay_success,
                    )
                    statusDescText.text = getText(
                        R.string.status_desc_pay_success
                    )
                    handler.postDelayed(backToMain, 5000)
                    refreshTimer(5)
                }
                "FAILED" -> {
                    statusImg.setColorFilter(Color.argb(255, 255, 0, 0))
                    statusImg.setImageResource(R.drawable.ic_status_fail)
                    statusLayout.isVisible = true
                    tryAgainBtn.isVisible = true
                    statusTitleText.text = getText(
                        R.string.status_title_pay_failed,
                    )
                    statusBodyText.text = getText(
                        R.string.status_body_pay_fail,
                    )
                    statusDescText.text = getText(
                        R.string.status_desc_pay_fail
                    )
                }
                else -> {
                    startAnalysis()
                    statusTitleText.text = getText(
                        R.string.status_title_pay_scanning,
                    )
                    statusLayout.isVisible = false
                    tryAgainBtn.isVisible = false
                }
            }
        }
    }

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    ) {
        val paymentStatusViewModel: PaymentStatusViewModel by viewModels()
        val inputImage =
            InputImage.fromMediaImage(
                imageProxy.image!!,
                imageProxy.imageInfo.rotationDegrees,
            )
        if (!isInit) {
            binding.tryAgainBtn.setOnClickListener {
                paymentStatusViewModel.reset()
                startAnalysis()
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    paymentStatusViewModel.uiState.collect {
                        changeStatus(it.status)
                    }
                }
            }
            isInit = true
        }
        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodeList ->
                val barcode = barcodeList.getOrNull(0)
                // `rawValue` is the decoded value of the barcode
                barcode?.rawValue?.let {
                    if (trIdRegex.matches(it)) {
                        stopAnalysis()
                        Log.d("PaymentBarcode", "Barcode detected: $it")
                        binding.statusTitleText.text = getText(
                            R.string.status_title_pay_checking,
                        )
                        paymentStatusViewModel.postTransaction(it)
                    }
                }
            }
            .addOnFailureListener {
                // This failure will happen if the barcode scanning model
                // fails to download from Google Play Services
                Log.e("PaymentBarcode", it.message.orEmpty())
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}