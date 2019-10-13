package es.upm.middleware;

import javax.jms.*;


public class Client {
	public static void main(String[] args) {

        try {

            ConnectionFactory myConnFactory;
            Queue usuarios;


            myConnFactory = new com.sun.messaging.ConnectionFactory();

            Connection myConn = myConnFactory.createConnection();

            Session mySess = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            usuarios = new com.sun.messaging.Queue("usuarios");

            MessageProducer myMsgProducer = mySess.createProducer(usuarios);

            TextMessage myTextMsg = mySess.createTextMessage();
            myTextMsg.setText("Ey! soy un usuario");
            System.out.println("Sending Message: " + myTextMsg.getText());
            myMsgProducer.send(myTextMsg);

            System.out.println("FIN!!!!!!!!!!!! client");
            mySess.close();
            myConn.close();

        } catch (Exception jmse) {
            System.out.println("Exception occurred : " + jmse.toString());
            jmse.printStackTrace();
        }
    }
}
