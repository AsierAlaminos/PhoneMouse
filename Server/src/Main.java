import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Main {
	public static void main(String[] args) {

		try {
			DatagramSocket socket = new DatagramSocket(6000);
			Robot robot = new Robot();
			while (true) {
				byte[] buffer = new byte[1024];
				DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);

				socket.receive(paquete);

				String paqueteRecibido = new String(paquete.getData(), 0, paquete.getLength());

				if (paqueteRecibido.split(":")[0].equalsIgnoreCase("pos")) {
					String[] posicion = paqueteRecibido.split(":");

					float[] posicionesMouse = new float[]{MouseInfo.getPointerInfo().getLocation().x + Float.parseFloat(posicion[1]), MouseInfo.getPointerInfo().getLocation().y + Float.parseFloat(posicion[2])};

					robot.mouseMove((int) posicionesMouse[0], (int) posicionesMouse[1]);
				}else if (paqueteRecibido.split(":")[0].equalsIgnoreCase("press")){
					String botonPresionado = paqueteRecibido.split(":")[1];
					int mask = 0;
					if (botonPresionado.equalsIgnoreCase("1")) {
						mask = InputEvent.BUTTON1_DOWN_MASK;
					}else if (botonPresionado.equalsIgnoreCase("2")){
						mask = InputEvent.BUTTON3_DOWN_MASK;
					}
					robot.mousePress(mask);
					robot.mouseRelease(mask);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}


	}
}