package es.upm.middleware;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Server {
	// public ConnectionFactory myConnFactory;
    public static  Connection myConn;
    public static Session mySess;

    static class TextListenerUsuarios implements MessageListener {
    	public void onMessage(Message message) {
    		if (message instanceof TextMessage) {
    	        TextMessage requestMessage = (TextMessage) message;
                try {
				    System.out.println("Read Message: " + requestMessage.getText() + "\tfrom queue: usuarios" );
					// Cola de la petición
				    String op = requestMessage.getText().split("\\d+")[0]; 
				    
				    // Cola Cliente-Servidor
				    Queue colaSC = new com.sun.messaging.Queue(requestMessage.getText());
				    System.out.println("Se enviará a la cola " + colaSC.getQueueName());
				    
					MessageProducer myMsgProducer = mySess.createProducer(colaSC);
			        TextMessage myTextMsg = mySess.createTextMessage();

				    if (op.equals("FiltrarFecha")) {
				    	System.out.println("Solicitud de Filtrar por Fecha");
				    	// TODO: procesar solicitud y myTextMsg.setText(solucion)
				    	myTextMsg.setText("Solicitud de Filtrar por Fecha");
				    }
				    else if (op.equals("FiltrarPalabraClave")) {
				    	System.out.println("Solicitud de Filtrar por Palabra Clave");
				    	// TODO: procesar solicitud y myTextMsg.setText(solucion)
				    	myTextMsg.setText("Solicitud de Filtrar por Palabra Clave");

				    }
				    else if (op.equals("FiltrarTematica")) {
				    	System.out.println("Solicitud de Filtrar por Temática");
				    	// TODO: procesar solicitud y myTextMsg.setText(solucion)
				    	myTextMsg.setText("Solicitud de Filtrar por Temática");
				    }
				    else {
				    	System.out.println("No se ha entendido la petición: " + op);
				    	// TODO: procesar solicitud y myTextMsg.setText(solucion)
				        myTextMsg.setText("No se ha entendido la solicitud: " + op);
				    }
			        System.out.println("Sending Message: " + myTextMsg.getText() + "\tto queue: " +  colaSC.getQueueName());
			        myMsgProducer.send(myTextMsg);
				}
                catch (JMSException e) {
					e.printStackTrace();
				}
    		}
    	}
    }
    
	public static void main( String[] args ){
		System.out.println("SERVIDOR ");
		try {
			ConnectionFactory myConnFactory;
			// Cola de usuarios
			Queue usuarios = new com.sun.messaging.Queue("usuarios");
        	// Conexion
			myConnFactory = new com.sun.messaging.ConnectionFactory();
			myConn = myConnFactory.createConnection();
            // Sesion
			mySess = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			// Escuchar en "usuarios"
			MessageConsumer myMsgConsumer = mySess.createConsumer(usuarios);
			TextListenerUsuarios textListener = new TextListenerUsuarios();
            myMsgConsumer.setMessageListener(textListener);
            myConn.start();
            
            while(true){
            	System.out.println("\t1 Añadir noticia (terminal)");
            	System.out.println("\t3 Añadir noticia (fichero)");
            	System.out.println("\t3 Salir");
            	
                Scanner sc = new Scanner(System.in);
                Integer option = -1;
                
        		try {
        			option = sc.nextInt();
        		}
        		catch (InputMismatchException e) {
            		System.out.println("No has introducido un númerno.\nSaliendo de la aplicación");
            		System.exit(1);
        		}
                switch(option) {
	                case 1:
	                	System.out.println("Añadir noticia terminal \n\t#TODO");
	                	break;
	                case 2:
	                	System.out.println("Añadir noticia fichero \n\t#TODO");
	                	break;
	                case 3:
	                	System.out.println("\tSalir");
	                    mySess.close();
	                    myConn.close();
	                    sc.close();
	                    System.exit(1);
	                	break;
	            	default:
	            		System.out.println("Opción no válida");
	            		break;
                }
            }
		}
    	catch (Exception jmse) {
            jmse.printStackTrace();
            System.exit(1);
		}
	}
}
