package es.upm.middleware;

import javax.jms.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class Client {
	public static void main(String[] args) {
		System.out.println("CLIENTE ");
        try {
            // TODO: Hacer el switch aquí y llamar a request
        	// TODO: Controlar tematica Free o Premium
        	request("FiltrarTematica", "Free");

            System.out.println("FIN!");

        } 
        catch (Exception jmse) {
            jmse.printStackTrace();
            System.exit(1);
        }
    }
	
	/**Crea una cola
	 * @param name: nombre de la cola
	 * @return la cola creada
	 * @throws JMSException
	 */
	private static Queue crearCola(String name) throws JMSException {
		return new com.sun.messaging.Queue(name);
	}
	
	/** Envia un mensaje a una cola 
	 * @param mySess: sesión
	 * @param cola: cola
	 * @param msg: mensaje a enviar
	 * @throws JMSException
	 */
	private static void enviarMensaje(Session mySess, Queue cola, String msg) throws JMSException {
		// Productor
		MessageProducer myMsgProducer = mySess.createProducer(cola);
		// Enviamos mensaje
        TextMessage myTextMsg = mySess.createTextMessage();
        myTextMsg.setText(msg);
	    myTextMsg.setJMSReplyTo(cola);
        System.out.println("Sending Message: " + myTextMsg.getText() + "\tto queue: " + cola.getQueueName());
	    myTextMsg.setJMSReplyTo(cola);
        myMsgProducer.send(myTextMsg);
	}
	
	/** Recibe un mensaje de una cola 
	 * @param mySess: sesión
	 * @param cola: cola de escucha
	 * @param myConn: conexión
	 * @return mensaje
	 * @throws JMSException
	 */
	private static Message recibirMensaje(Session mySess, Queue cola, Connection myConn) throws JMSException {
		System.out.println("Waiting for Message... ");
		// Consumidor
		MessageConsumer myMsgConsumer = mySess.createConsumer(cola);
		myConn.start();
		Message msg = myMsgConsumer.receive(); 
		myConn.stop();
		return msg;
	}
	
	/**Se genera un nombre con la siguiente estructura
	 * concatenación de númeroPID y nombre ssoo
	 * @param op: operación de la solicitud
	 * @return String nombre único
	 */
	private static String uniqueName(String op, String suscription) {
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		return op + bean.getName().split("[a-zA-Z]+")[0].replace("@", "").replace("-", "") + suscription;
	}
	
	/**
	 * 
	 * @param operation: operaciones permitidas -> FiltrarFecha, FiltrarPalabraClave o FiltrarTematica
	 * @param mySess: sesión
	 * @param myConn: conexión
	 * @throws JMSException
	 */
	private static void request(String operation, String suscription) throws JMSException {
        ConnectionFactory myConnFactory;
	    // Conexion
        myConnFactory = new com.sun.messaging.ConnectionFactory();
        Connection myConn = myConnFactory.createConnection();
        // Sesion
        Session mySess = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// Cola "usuarios"
		Queue usuarios = crearCola("usuarios");
		// Nombre unico
		String qname = uniqueName(operation, suscription);
		// Enviamos mensaje
		enviarMensaje(mySess, usuarios, qname);
		// Cola Servidor-Cliente
		Queue colaSC = crearCola(qname);
		// Bucle de espera
		while(true){
			Message msg = recibirMensaje(mySess, colaSC, myConn);
			// si recibimos mensaje
			if (msg instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) msg;
				System.out.println("Read Message: " + txtMsg.getText() + "\tfrom queue: " + colaSC.getQueueName());
				myConn.close();
				mySess.close();
				break;
			}
		}
	}
}
