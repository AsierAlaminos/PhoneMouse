package com.example.client

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MainActivity : AppCompatActivity(), View.OnClickListener {

	private lateinit var datagrama: DatagramSocket
	private lateinit var ipAddress: InetAddress
	private lateinit var leftButton: AppCompatButton
	private lateinit var rightButton: AppCompatButton
	@SuppressLint("ClickableViewAccessibility", "SetTextI18n", "MissingInflatedId")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)


		val layout: ConstraintLayout = findViewById(R.id.constraint_layout)
		val textView: TextView = findViewById(R.id.textPosicion)

		leftButton = findViewById(R.id.leftButton)
		rightButton = findViewById(R.id.rightButton)
		datagrama = DatagramSocket()
		ipAddress = InetAddress.getByName("192.168.137.1")

		layout.setOnTouchListener { view, motionEvent ->
			var x = motionEvent.x / 10
			var y = motionEvent.y / 10
			var xMouse = (x - 54).toDouble()
			var yMouse = (y - 76).toDouble()

			textView.text = "$xMouse : $yMouse"
			val mensaje = "pos:$xMouse:$yMouse"
			val mensajeByte = mensaje.toByteArray()
			AsyncTask.execute{
				val paquete = DatagramPacket(mensajeByte, mensajeByte.size, ipAddress, 6000)

				datagrama.send(paquete)
			}

			true
		}

		leftButton.setOnClickListener(this)
		rightButton.setOnClickListener(this)
	}

	override fun onClick(v: View?) {
		var mensaje = "press:"
		if (v!!.id == leftButton.id){
			mensaje += "1"
		}else if (v.id == rightButton.id){
			mensaje += "2"
			Log.v("test", "right")
		}
		val mensajeByte = mensaje.toByteArray()

		AsyncTask.execute {
			val paquete = DatagramPacket(mensajeByte, mensajeByte.size, ipAddress, 6000)

			datagrama.send(paquete)
		}
	}
}