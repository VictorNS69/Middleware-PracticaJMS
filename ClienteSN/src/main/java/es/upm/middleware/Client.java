package es.upm.middleware;

import javax.jms.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class Client {
	public static void main(String[] args) {
		System.out.println("EL CLIENTE ");

        try {
            ConnectionFactory myConnFactory;
            // Cola de usuarios
 			Queue usuarios = crearCola("usuarios");
 	        // Conexion
            myConnFactory = new com.sun.messaging.ConnectionFactory();
            Connection myConn = myConnFactory.createConnection();
            
            // Sesion
            Session mySess = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            // Nombre unico
            String qname = uniqueName();
            // Enviamos mensaje
            enviarMensaje(mySess, usuarios, qname);
            
			// Cola Cliente-Servidor
			Queue colaCS = crearCola("CS" + qname);
			// Cola Servidor-Cliente
			Queue colaSC = crearCola("SC" + qname);
            // Bucle de espera
 			while(true){
				Message msg = recibirMensaje(mySess, colaSC, myConn);

 				// si recibimos mensaje
 				if (msg instanceof TextMessage) {
 					TextMessage txtMsg = (TextMessage) msg;
 					System.out.println("Read Message: " + txtMsg.getText() + "\tfrom queue: " + colaSC.getQueueName());
		            TextMessage myTextMsg = mySess.createTextMessage();
					myTextMsg.setText("Recibido!");

 					break;
 				}
 			}

            System.out.println("FIN!");
            // Cerramos sesion y conexion
            mySess.close();
            myConn.close();

        } catch (Exception jmse) {
            System.out.println("Exception occurred : " + jmse.toString());
            jmse.printStackTrace();
        }
    }
	private static Queue crearCola(String name) throws JMSException {
		return new com.sun.messaging.Queue(name);
	}
	private static void enviarMensaje(Session mySess, Queue cola, String msg) throws JMSException {
		// Productor
		MessageProducer myMsgProducer = mySess.createProducer(cola);
		// Enviamos mensaje
        TextMessage myTextMsg = mySess.createTextMessage();
        myTextMsg.setText(msg);
        System.out.println("Sending Message: " + myTextMsg.getText() + "\tto queue: " + cola.getQueueName());
        myMsgProducer.send(myTextMsg);
	}
	private static Message recibirMensaje(Session mySess, Queue cola, Connection myConn) throws JMSException {
		System.out.println("Waiting for Message... ");
		// Consumidor
		MessageConsumer myMsgConsumer = mySess.createConsumer(cola);
		myConn.start();
		return myMsgConsumer.receive();
	}
	private static String uniqueName() {
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		return bean.getName().split("@")[1] + bean.getName().replace("@", "");
	}
}
