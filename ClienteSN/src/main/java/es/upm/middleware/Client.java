package es.upm.middleware;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


public class Client {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		System.out.println("CLIENTE ");
		try {
			Scanner scN = new Scanner(System.in);
			System.out.println("\nNombre de Usuario\n");
			@SuppressWarnings("unused")
			String nombre = scN.nextLine();
			System.out.println("\nSuscripción Free o Premium?\n");
			Scanner scS = new Scanner(System.in);
			String suscripcion = scS.nextLine();
			while(true) {
				System.out.println("\nComo desea filtrar sus noticias?\n\t[1]Por Tematica\n\t[2]Por Palabra Clave\n\t[3]Por Fecha\n\t[4]Salir");
				Scanner sc = new Scanner(System.in);
				int filtro = sc.nextInt(); 
				switch(filtro){
				case 1:
					System.out.println("\nTematica?\n\t[1]Politica\n\t[2]Economia\n\t[3]Deportes\n");
					Scanner scF = new Scanner(System.in);
					int tema = scF.nextInt();
					switch(tema){
						case 1:
							request("FiltrarTematica", "POLITICA", suscripcion);
							break;
						case 2:
							request("FiltrarTematica", "ECONOMIA", suscripcion);
							break;
						case 3:
							request("FiltrarTematica", "DEPORTES", suscripcion);
							break;
						default:
							System.out.println("Entrada no valida.\n");
							break;
					}
					break;
				case 2:
					System.out.println("\nIntroduzca Palabra Clave\n");
					Scanner sck = new Scanner(System.in);
					String keyk = sck.nextLine();
					request("FiltrarPalabraClave", keyk, suscripcion);
					break;
				case 3:
					boolean res = true;
					System.out.println("\nIntroduzca Fecha: Formato DD-MM-YYYY.\nSi no desea filtrar en algun caso, escriba NO");
					System.out.println("\nFecha Inicio? \nSi se especifica, se serviran noticias posteriores a esta fecha\n");
					Scanner scfi = new Scanner(System.in);
					String fechaini = scfi.next();
					System.out.println("\nFecha Final? \nSi se especifica, se serviran noticias anteriores a esta fecha\n");
					Scanner scff = new Scanner(System.in);
					String fechafin = scff.next();
					if((fechaini.toUpperCase().equals("NO"))&&(fechafin.toUpperCase().equals("NO"))){
						System.out.println("\nNo hay fecha especifica, no se realiza ninguna busqueda.\n");
						break;
					} 
					else if(fechaini.toUpperCase().equals("NO")){
						res=validarFecha(fechafin);
						if(res==false){
							System.out.println("\nFormato de fecha invalido.");
							break;
						} 
						else {
							System.out.println("\nNoticias anteriores:\n");
							request("FiltrarFechaFin", "XX"+fechafin.replace("-", "")+"XX", suscripcion);
						}
					} 
					else if(fechafin.toUpperCase().equals("NO")){
						res=validarFecha(fechaini);
						if(res==false){
							System.out.println("\nFormato de fecha invalido.");
							break;
						} 
						else {
							System.out.println("\nNoticias posteriores:\n");
							request("FiltrarFechaInicio", "XX"+fechaini.replace("-", "")+"XX", suscripcion);
						}
					} 
					else{
						res=validarFecha(fechafin);
						if(res==false){
							System.out.println("\nFormato de fecha invalido.");
							break;
						}
						res=validarFecha(fechaini);
						if(res==false){
							System.out.println("\nFormato de fecha invalido.");
							break;
						} 
						else {
							System.out.println("\nNoticias entre " + fechaini + " y " + fechafin + "\n");
							request("FiltrarFecha", "XX"+fechaini.replace("-", "")+ "YY" 
									+ fechafin.replace("-", "") +"XX", suscripcion);
						}

					}
					break;
				case 4:
					System.out.println("\nSaliendo del sistema.");
					System.exit(1);
				default:
					System.out.println("\nOpcion no valida\n");
					break;
				}
			}
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

	/** Hace una solicitud de operación y espera la respuesta
	 * @param operation: operaciones permitidas -> FiltrarFecha, FiltrarPalabraClave o FiltrarTematica
	 * @param mySess: sesión
	 * @param myConn: conexión
	 * @throws JMSException
	 */
	private static void request(String operation, String arguments, String subscription) throws JMSException {
		ConnectionFactory myConnFactory;
		// Conexion
		myConnFactory = new com.sun.messaging.ConnectionFactory();
		Connection myConn = myConnFactory.createConnection();
		// Sesion
		Session mySess = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// Cola "usuarios"
		Queue usuarios = crearCola("usuarios");
		// Nombre unico
		String qname = uniqueName(operation, arguments);
		// Enviamos mensaje
		enviarMensaje(mySess, usuarios, qname);
		// Cola Servidor-Cliente
		Queue colaSC = crearCola(qname);
		// Bucle de espera
		boolean received = false;
		while(!received){
			Message msg = recibirMensaje(mySess, colaSC, myConn);
			// si recibimos mensaje
			if (msg instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) msg;
				readJson(txtMsg, subscription);
				received = true;
				myConn.close();
				mySess.close();
				break;
			}
		}
	}

	private static void readJson(TextMessage msg, String subscription) throws JsonSyntaxException, JMSException {
		if (msg.getText() == null || msg.getText().isEmpty() || msg.getText().equals("[]")) {
			System.out.println("\tNo hay noticias");
			return;
		}
		JsonParser jp = new JsonParser();
		JsonArray ofs = jp.parse(msg.getText()).getAsJsonArray();
		for (int i = 0; i < ofs.size(); i++) {
			String replace =  ofs.get(i).toString().replace("\\", "").replace("\"", "")
					.replace("{", "").replace("}", "");
			String [] contenido = replace.split(",contenido:");
			String [] palabras = contenido[0].split(",palabras_clave:");
			String [] categoria = palabras[0].split(",categoria:");
			String [] fecha = categoria[0].split(",fecha:");
			String [] nombre = fecha[0].split("nombre:");
			System.out.println("\n\tTitulo: " + nombre[1] + "\n\tFecha: " + fecha[1]
					+ "\n\tCategoria: "+ categoria[1] + "\n\tPalabras Clave: " + 
					palabras[1].replace("[", "").replace("]", "").replace(",", ", "));
			if (subscription.toLowerCase().equals("premium")) {
				System.out.println("\tContenido: " + contenido[1]);
			}
		}
	}
	
	public static boolean validarFecha(String fecha) {
		try {
			SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
			formatoFecha.setLenient(false);
			formatoFecha.parse(fecha);
		} 
		catch (ParseException e){
			return false;
		}
		return true;
	}
}
