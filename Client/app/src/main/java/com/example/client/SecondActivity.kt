package com.example.client

import android.annotation.SuppressLint
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

class SecondActivity : AppCompatActivity(), View.OnClickListener, SensorEventListener {

	private lateinit var datagrama: DatagramSocket
	private lateinit var ipAddress: InetAddress
	private lateinit var leftButton: AppCompatButton
	private lateinit var rightButton: AppCompatButton
	private lateinit var textoPosicion: TextView
	private var inicial: Boolean = true
	private var inicialX: Float = 0.0F
	private var inicialY: Float = 0.0F
	private var direccion: Int = 0
	private var contadorCeros: Int = 0
	@SuppressLint("ClickableViewAccessibility", "SetTextI18n", "MissingInflatedId")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_second)


		val layout: ConstraintLayout = findViewById(R.id.constraint_layout)
		textoPosicion = findViewById(R.id.textPosicion)

		leftButton = findViewById(R.id.leftButton)
		rightButton = findViewById(R.id.rightButton)
		datagrama = DatagramSocket()
		ipAddress = InetAddress.getByName("192.168.137.1")

		leftButton.setOnClickListener(this)
		rightButton.setOnClickListener(this)
		
		val opcion: String = intent.extras?.getString("opcion")!!
		if (opcion.equals("pulsacion", true)) {
			Log.v("opcion", "pulsacion")
			layout.setOnTouchListener { view, motionEvent ->
				var x = motionEvent.x / 10
				var y = motionEvent.y / 10
				var xMouse = (x - 54).toDouble()
				var yMouse = (y - 151).toDouble()

				textoPosicion.text = "$xMouse : $yMouse"
				val mensaje = "pos:$xMouse:$yMouse"
				val mensajeByte = mensaje.toByteArray()
				AsyncTask.execute{
					val paquete = DatagramPacket(mensajeByte, mensajeByte.size, ipAddress, 6000)

					datagrama.send(paquete)
				}

				true
			}
		}else {
			Log.v("opcion", "acelerometro")
			val sensor: SensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
			sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
		}
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

	override fun onSensorChanged(event: SensorEvent?) {
		val x: Float = event!!.values[0]
		val y: Float = event.values[1]
		var mensaje: String = ""
		if (inicial){
			inicial = false
			inicialX = x
			inicialY = y
			Log.v("inicial", "inicialX -> $inicialX\ninicialY -> $inicialY")
		}
		val posicion: String = String.format("%.2f:%.2f", ((x - inicialX) * 10) * 2, ((y - inicialY) * -10) * 2)
		textoPosicion.text = posicion
		if (x.toInt() == 0 && y.toInt() == 0){
			if (contadorCeros >= 6)
				direccion = 0
			++contadorCeros
			Log.v("posicion", "[*] 0")
		} else if (x.toInt() >= 0 && y.toInt() >= 0 && (direccion == 1 || direccion == 0)){
			direccion = 1
			contadorCeros = 0
			Log.v("posicion", "\n[*] Arriba derecha\n")
			Log.v("posicion", "Pos: $posicion")
			mensaje = "pos:$posicion"
		}else if (x.toInt() <= 0 && y.toInt() >= 0 && (direccion == 2 || direccion == 0)){
			direccion = 2
			contadorCeros = 0
			Log.v("posicion", "\n[*] Arriba izquierda\n")
			Log.v("posicion", "Pos: $posicion")
			mensaje = "pos:$posicion"
		}else if (x.toInt() >= 0 && y.toInt() <= 0 && (direccion == 3 || direccion == 0)){
			direccion = 3
			contadorCeros = 0
			Log.v("posicion", "\n[*] Abajo derecha\n")
			Log.v("posicion", "Pos: $posicion")
			mensaje = "pos:$posicion"
		}else if (x.toInt() <= 0 && y.toInt() <= 0 && (direccion == 4 || direccion == 0)){
			direccion = 4
			contadorCeros = 0
			Log.v("posicion", "\n[*] Abajo izquierda\n")
			Log.v("posicion", "Pos: $posicion")
			mensaje = "pos:$posicion"
		}
		val mensajeByte = mensaje.toByteArray()
		GlobalScope.launch{
			val paquete = DatagramPacket(mensajeByte, mensajeByte.size, ipAddress, 6000)

			datagrama.send(paquete)
		}.start()
	}

	override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

}