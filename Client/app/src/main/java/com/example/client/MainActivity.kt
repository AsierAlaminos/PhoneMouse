package com.example.client

import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MainActivity : AppCompatActivity(), View.OnClickListener{

	private lateinit var botonPulsacion: AppCompatButton
	private lateinit var botonAcelerometro: AppCompatButton
	@SuppressLint("MissingInflatedId")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		botonPulsacion = findViewById(R.id.pulsacion)
		botonAcelerometro = findViewById(R.id.acelerometro)

		botonPulsacion.setOnClickListener(this)
		botonAcelerometro.setOnClickListener(this)

	}

	override fun onClick(v: View?) {
		val intent = Intent(applicationContext, SecondActivity::class.java)
		if (v!!.id == R.id.pulsacion){
			intent.putExtra("opcion", "pulsacion")
		}else{
			intent.putExtra("opcion", "acelerometro")
		}
		startActivity(intent)
	}


}