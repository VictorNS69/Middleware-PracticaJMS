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

import com.google.gson.JsonArray;

import java.util.InputMismatchException;
import java.util.Scanner;
import es.upm.middleware.ListHandler;
import es.upm.middleware.Noticia;


public class Server {
	// public ConnectionFactory myConnFactory;
    public static  Connection myConn;
    public static Session mySess;
    private static ListHandler LH;

    static class TextListenerUsuarios implements MessageListener {
    	public void onMessage(Message message) {
    		if (message instanceof TextMessage) {
    	        TextMessage requestMessage = (TextMessage) message;
                try {
				    System.out.println("Read Message: " + requestMessage.getText() + "\tfrom queue: usuarios" );
					// Cola de la petición
				    String[] split = requestMessage.getText().split("\\d+");
				    
				    String operation = split[0];
				    String arguments = split[1];
				    
				    System.out.println("operación: " + operation + " Arguments: " + arguments);
				    
				    // Cola Cliente-Servidor
				    Queue colaSC = new com.sun.messaging.Queue(requestMessage.getText());
				    System.out.println("Se enviará a la cola " + colaSC.getQueueName());
				    
					MessageProducer myMsgProducer = mySess.createProducer(colaSC);
			        TextMessage myTextMsg = mySess.createTextMessage();

				    if (operation.equals("FiltrarFecha")) {
				    	System.out.println("Solicitud de Filtrar por Fecha");
				    	// TODO: procesar solicitud y cambiar myTextMsg.setText(solucion)
				    	myTextMsg.setText("Solicitud de Filtrar por Fecha");
				    }
				    else if (operation.equals("FiltrarPalabraClave")) {
				    	System.out.println("Solicitud de Filtrar por Palabra Clave");
						JsonArray jsonArr = new JsonArray();
				    	for (Noticia n: LH.get_news_with_keyword(arguments)) {
							jsonArr.add(n.toString());
						}
						myTextMsg.setText(jsonArr.toString());
				    }
				    else if (operation.equals("FiltrarTematica")) {
				    	System.out.println("Solicitud de Filtrar por Temática");
						JsonArray jsonArr = new JsonArray();

				    	switch (arguments) {
							case "POLITICA":
								for (Noticia n: LH.get_cat_politica()) {
									jsonArr.add(n.toString());
								}
								myTextMsg.setText(jsonArr.toString());
								break;
							case "ECONOMIA":
								for (Noticia n: LH.get_cat_economia()) {
									jsonArr.add(n.toString());
								}
								myTextMsg.setText(jsonArr.toString());			
								break;
							case "DEPORTES":
								for (Noticia n: LH.get_cat_deporte()) {
									jsonArr.add(n.toString());
								}
								myTextMsg.setText(jsonArr.toString());
								break;
							default:
								System.out.println("No se tienen datos de la categoria: " + arguments);
								break;
						}
				    }
				    else {
				    	System.out.println("No se ha entendido la petición: " + operation);
				        myTextMsg.setText("No se ha entendido la solicitud: " + operation);
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
			// Gestor de noticias
        	LH = new ListHandler();
        	
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
            	System.out.println("\t2 Añadir noticia (fichero)");
            	System.out.println("\t3 Salir");
            	
                Scanner sc = new Scanner(System.in);
                Integer operation = -1;
                
        		try {
        			operation = sc.nextInt();
        		}
        		catch (InputMismatchException e) {
            		System.out.println("No has introducido un númerno.\nSaliendo de la aplicación");
            		System.exit(1);
        		}
                switch(operation) {
	                case 1:
	                	System.out.println("Añadir noticia terminal \n#TODO");
	                	//TODO: Quitar también TODO del print
	                	break;
	                case 2:
	                	System.out.println("Añadir noticia desde un fichero \nEscribe la ruta GLOBAL");
	                    Scanner scF = new Scanner(System.in);
	                	String path = scF.nextLine();
	                	try {
		                	Noticia noticia = new Noticia(path);
		                	LH.importar_noticia(noticia);
						} catch (Exception e) {
							System.out.println(e);
						}
	                	break;
	                case 3:
	                	System.out.println("\tSalir");
	                	// TODO: BORRAR PRINT
	                	System.out.println("Todas las noticias: "+ LH.get_total_noticas());
	                    mySess.close();
	                    myConn.close();
	                    sc.close();
	                    System.exit(1);
	                	break;
	            	default:
	            		System.out.println("operación no válida");
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
