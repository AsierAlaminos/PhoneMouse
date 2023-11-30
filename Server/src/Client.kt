import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

fun main() {
	val datagrama = DatagramSocket()
	val mensaje = "Server hola"
	val mensajeByte = mensaje.toByteArray()
	val ipAddress = InetAddress.getByName("192.168.1.171")

	val paquete = DatagramPacket(mensajeByte, mensajeByte.size, ipAddress, 6000)

	datagrama.send(paquete)
}