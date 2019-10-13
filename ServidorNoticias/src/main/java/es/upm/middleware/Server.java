package es.upm.middleware;

import javax.jms.*;

public class Server {
	public static void main( String[] args ){
		                System.out.println("EL MAIIIIN! ");
		try {

            ConnectionFactory myConnFactory;
            Queue usuarios;

            myConnFactory = new com.sun.messaging.ConnectionFactory();

            Connection myConn = myConnFactory.createConnection();

            Session mySess = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            usuarios = new com.sun.messaging.Queue("usuarios");


            while(true){
                System.out.println("Waiting for Message... ");
                MessageConsumer myMsgConsumer = mySess.createConsumer(usuarios);
                myConn.start();
                Message msg = myMsgConsumer.receive();

                if (msg instanceof TextMessage) {
                    TextMessage txtMsg = (TextMessage) msg;
                    System.out.println("Read Message: " + txtMsg.getText());
                    break;
                }
            }
            System.out.println("FIN!!!!!!!!!!!! server");
            mySess.close();
            myConn.close();

        } catch (Exception jmse) {
            System.out.println("Exception occurred : " + jmse.toString());
            jmse.printStackTrace();
        }
		
	}
}
