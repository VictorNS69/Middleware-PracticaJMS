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
				    //System.out.println("Read Message: " + requestMessage.getText() + "\tfrom queue: usuarios" );
					// Cola de la petición
				    String[] split = requestMessage.getText().split("\\d+");
				    String[] auxSPlit = requestMessage.getText().split("XX");
				    
				    String operation = split[0];
				    String arguments = split[1];
				    				    				    
				    // Cola Cliente-Servidor
				    Queue colaSC = new com.sun.messaging.Queue(requestMessage.getText());
				    //System.out.println("Se enviará a la cola " + colaSC.getQueueName());
				    
					MessageProducer myMsgProducer = mySess.createProducer(colaSC);
			        TextMessage myTextMsg = mySess.createTextMessage();

				    if (operation.equals("FiltrarFechaFin")) {
				    	String auxDate = auxSPlit[1].substring(auxSPlit[1].length() - 8);
				    	String finalDate = auxDate.substring(0,2) + "-" + auxDate.substring(2,4) 
				    		+ "-" + auxDate.substring(auxDate.length() - 4);
				    	JsonArray jsonArr = new JsonArray();
				    	for (Noticia n: LH.get_older_than(finalDate)) {
							jsonArr.add(n.toString());
						}
						myTextMsg.setText(jsonArr.toString());
				    }
				    else if (operation.equals("FiltrarFechaInicio")) {
				    	String auxDate = auxSPlit[1].substring(auxSPlit[1].length() - 8);
				    	String finalDate = auxDate.substring(0,2) + "-" + auxDate.substring(2,4) 
				    		+ "-" + auxDate.substring(auxDate.length() - 4);
				    	JsonArray jsonArr = new JsonArray();
				    	for (Noticia n: LH.get_newer_than(finalDate)) {
							jsonArr.add(n.toString());
						}
						myTextMsg.setText(jsonArr.toString());
				    }
				    else if (operation.equals("FiltrarFecha")) {
				    	String date1 = auxSPlit[1].substring(0, 8);
				    	String date2 = auxSPlit[1].substring(auxSPlit[1].length() - 8);
				    	String finalDate1 = date1.substring(0,2) + "-" + date1.substring(2,4) 
			    		+ "-" + date1.substring(date1.length() - 4);
				    	String finalDate2 = date2.substring(0,2) + "-" + date2.substring(2,4) 
				    		+ "-" + date2.substring(date2.length() - 4);
				    	JsonArray jsonArr = new JsonArray();
				    	for (Noticia n: LH.get_from_date(finalDate1, finalDate2)) {
							jsonArr.add(n.toString());
						}
						myTextMsg.setText(jsonArr.toString());
				    }
				    else if (operation.equals("FiltrarPalabraClave")) {
						JsonArray jsonArr = new JsonArray();
				    	for (Noticia n: LH.get_news_with_keyword(arguments)) {
							jsonArr.add(n.toString());
						}
						myTextMsg.setText(jsonArr.toString());
				    }
				    else if (operation.equals("FiltrarTematica")) {
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
								myTextMsg.setText("No se tienen datos de esa categoria");
								break;
						}
				    }
				    else {
				        myTextMsg.setText("No se ha entendido la solicitud: " + operation);
				    }
			        //System.out.println("Sending Message: " + myTextMsg.getText() + "\tto queue: " +  colaSC.getQueueName());
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
	                	//TODO: 
	                	break;
	                case 2:
	                	System.out.println("Añadir noticia desde un fichero \nEscribe la ruta GLOBAL");
	                    Scanner scF = new Scanner(System.in);
	                	String path = scF.nextLine();
	                	try {
		                	Noticia noticia = new Noticia(path);
		                	LH.importar_noticia(noticia);
		                	System.out.println("La noticia se ha importado correctamente");
						} catch (Exception e) {
							System.out.println(e);
						}
	                	break;
	                case 3:
	                	System.out.println("\tSalir");
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
