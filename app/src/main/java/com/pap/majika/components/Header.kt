package com.pap.majika.components

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.pap.majika.viewModel.MainViewModel
import com.pap.majika.R


/**
 * A simple [Fragment] subclass.
 * Use the [Header.newInstance] factory method to
 * create an instance of this fragment.
 */
class Header : Fragment(), SensorEventListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var sensorManager: SensorManager
    private var sensorAvailable = false
    private var temperature: Sensor? = null
    private var headerTitle: TextView? = null
    private var temperatureText : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        sensorManager = requireActivity().getSystemService(SensorManager::class.java)
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_header, container, false)
        headerTitle = view.findViewById(R.id.header_title)
        temperatureText = view.findViewById(R.id.header_temp)
        mainViewModel.currentMenu.observe(viewLifecycleOwner) {
            headerTitle?.text = it
            if (it == "menu") {
                if (temperature != null) {
                    sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL)
                    temperatureText?.visibility = View.VISIBLE
                }
            } else {
                sensorManager.unregisterListener(this)
            }
        }

        return view
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0?.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            temperatureText?.text = p0.values[0].toString() + "°C"
        }
    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}