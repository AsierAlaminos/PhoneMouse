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
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MainActivity : AppCompatActivity(), View.OnClickListener, SensorEventListener{

	private lateinit var datagrama: DatagramSocket
	private lateinit var ipAddress: InetAddress
	private lateinit var leftButton: AppCompatButton
	private lateinit var rightButton: AppCompatButton
	private lateinit var textoPosicion: TextView
	private var inicial: Boolean = true
	private var inicialX: Float = 0.0F
	private var inicialY: Float = 0.0F
	@SuppressLint("ClickableViewAccessibility", "SetTextI18n", "MissingInflatedId")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)


		val layout: ConstraintLayout = findViewById(R.id.constraint_layout)
		textoPosicion = findViewById(R.id.textPosicion)

		leftButton = findViewById(R.id.leftButton)
		rightButton = findViewById(R.id.rightButton)
		datagrama = DatagramSocket()
		ipAddress = InetAddress.getByName("192.168.137.1")
/*
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
		}*/

		//leftButton.setOnClickListener(this)
		//rightButton.setOnClickListener(this)

		val sensor: SensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

		sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
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
		var x: Float = event!!.values[0]
		var y: Float = event!!.values[1]
		if (inicial){
			inicial = false
			inicialX = x
			inicialY = y
		}
		var posicion: String = String.format("%.2f:%.2f", ((x - inicialX) * 10) * -1, (y - inicialY) * 10)
		Log.v("inicial", "${inicialX}:${inicialY}")
		textoPosicion.text = posicion
		Log.v("posicion", "Pos: $posicion")
		val mensaje = "pos:$posicion"
		val mensajeByte = mensaje.toByteArray()
		AsyncTask.execute{
			val paquete = DatagramPacket(mensajeByte, mensajeByte.size, ipAddress, 6000)

			datagrama.send(paquete)
		}
	}

	override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
	}

	/*
public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];

        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - lastUpdateTime;

        if (timeDifference > 100) {  // Limita la frecuencia de actualización
            float deltaX = x - lastX;
            float deltaY = y - lastY;

            float acceleration = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if (acceleration > MOVEMENT_THRESHOLD) {
                // Calcula la nueva posición del ratón
                float newX = targetView.getX() + deltaX * MOUSE_SPEED;
                float newY = targetView.getY() - deltaY * MOUSE_SPEED; // Invierte el eje y

                // Limita la posición a los límites de la pantalla si es necesario
                newX = Math.max(0, Math.min(newX, targetView.getWidth() - targetView.getWidth()));
                newY = Math.max(0, Math.min(newY, targetView.getHeight() - targetView.getHeight()));

                // Actualiza la posición del ratón en el hilo principal
                handler.post(() -> {
                    targetView.setX(newX);
                    targetView.setY(newY);
                });

                lastX = x;
                lastY = y;
                lastUpdateTime = currentTime;
            }
        }
    }

	*/
}