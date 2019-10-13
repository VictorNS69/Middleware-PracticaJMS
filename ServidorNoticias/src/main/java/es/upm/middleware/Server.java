package es.upm.middleware;

import javax.jms.*;

public class Server {
	public static void main( String[] args ){
		System.out.println("EL SERVIDOR ");
		try {
			ConnectionFactory myConnFactory;
			Queue usuarios;
			
        	// Conexion
			myConnFactory = new com.sun.messaging.ConnectionFactory();
			Connection myConn = myConnFactory.createConnection();
            // Sesion
			Session mySess = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // Cola de usuarios
			usuarios = new com.sun.messaging.Queue("usuarios");

			// Bucle de espera
			while(true){
				System.out.println("Waiting for Message... ");
				// Consumidor
				MessageConsumer myMsgConsumer = mySess.createConsumer(usuarios);
				myConn.start();
				Message msg = myMsgConsumer.receive();
				// si recibimos mensaje
				if (msg instanceof TextMessage) {
					TextMessage txtMsg = (TextMessage) msg;
					System.out.println("Read Message: " + txtMsg.getText() + " Queue:" + usuarios.getQueueName());
					
					// Cola Cliente-Servidor
					Queue colaCS = crearCola("CS");
					// Cola Servidor-Cliente
					Queue colaSC = crearCola("SC");
					
					// Productor
					MessageProducer myMsgProducer = mySess.createProducer(colaSC);

		            // Enviamos mensaje
		            TextMessage myTextMsg = mySess.createTextMessage();
		            myTextMsg.setText("Recibido!");
		            System.out.println("Sending Message: " + myTextMsg.getText() + " Queue:" + colaSC.getQueueName());
		            myMsgProducer.send(myTextMsg);
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
}
