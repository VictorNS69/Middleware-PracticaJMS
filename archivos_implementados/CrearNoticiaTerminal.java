import java.io.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CrearNoticiaTerminal {
    public static void main(String ars[])
	{	
		FileWriter fichero = null;
		PrintWriter pw = null;
		boolean p = false;
		String lineaclave = "";
		try{
			Scanner sc = new Scanner(System.in);
	       		System.out.println("Creador de noticias: Nombre del archivo:");
			String namefile = sc.nextLine();		
			namefile=namefile+".txt";
			File file = new File(namefile);
			fichero = new FileWriter(namefile);
            		pw = new PrintWriter(fichero);
			
			// Si el archivo no existe es creado
			if (!file.exists()) {
				file.createNewFile();
			}
			
			System.out.println("Titulo:");
			String titulo = sc.nextLine();
			pw.println(titulo);
			
			System.out.println("Fecha: Formato: DD-MM-YYYY (Sin control de errores)");
			String fecha = sc.nextLine();
			pw.println(fecha);
			
			System.out.println("Tema: POLITICA, DEPORTE ó ECONOMIA (en mayusculas)");
			while(p==false){
				String tema = sc.nextLine();
				if(tema.equals("POLITICA")||tema.equals("DEPORTE")||tema.equals("ECONOMIA")){
					p = true;
					pw.println(tema);
				} else {
					System.out.println("Tema incorrecto, vuelva a intentarlo.");				
				}
			}

			System.out.println("Palabras Clave:");
			
			while(p==true){
				String pclave = sc.nextLine();
				lineaclave = lineaclave+pclave+"##";
				System.out.println("Quiere escribir otra palabra clave?\n\tSI\tNO\t(en mayusculas)");
				String q = sc.nextLine();
				if(q.equals("NO")){
					pw.println(lineaclave);
					pw.println("##");
					p=false;
				} else {
					System.out.println("Siguiente Palabra Clave:");				
				}		
			}
			
			System.out.println("Cuerpo de la noticia: (Sin saltos de linea)");
			String cuerpo = sc.nextLine();
			pw.println(cuerpo);

			pw.close();

		} catch (Exception e) {
            		e.printStackTrace();
        	}		
	}
}








