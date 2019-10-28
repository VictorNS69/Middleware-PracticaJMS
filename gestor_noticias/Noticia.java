package gestor_noticias;

import java.time.*;
import java.io.*;

public class Noticia {
	private String nombre;// #1
	private int fecha_c; // #2 FORMATO YYYYMMDD (Formato para organizar de manera rápida)
	private String fecha;
	private String categoria; // #3 POLITICA/ECONOMIA/DEPORTES
	private String[] palabras_clave; //#4 - SEPARADAS POR ## 
	private String contenido;
	
	public Noticia (String fileAdress) throws Exception{
		//Metodo para crear una notica desde un archivo
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fileAdress));
			this.nombre = reader.readLine();
			//System.out.println(nombre);
			this.fecha = reader.readLine();
			this.fecha_c = date_to_int(fecha);
			this.categoria = reader.readLine();
			this.palabras_clave = reader.readLine().split("##");		// -.- Revisar funcionamiento
			
			if(!reader.readLine().equals("##"))
				throw new Exception("file format not matching");
			this.contenido = reader.readLine();
			
		}
		catch(Exception e) {
			System.out.println("Error @ crear Noticia, no se puede leer el archivo o no tiene el formato correcto");
		}
	}
	
	public Noticia(String name, String date, String categorie, String[] keyWords, String content ) {
		//Metodo para crear una notica directamente.
		this.nombre = name;
		this.fecha = date;
		this.fecha_c = date_to_int(fecha);
		this.categoria = categorie;
		this.palabras_clave = keyWords.clone();
		this.contenido = content;
	}
	
	
	private int date_to_int (String s_fecha) {
		String[] a_fecha = s_fecha.split("-",3);
		int resultado = 0;
		resultado += Integer.parseInt(a_fecha[2])*10000;
		resultado += Integer.parseInt(a_fecha[1])*100;
		resultado += Integer.parseInt(a_fecha[0]);
		return resultado;
		
	}
	
	
	
	
	public String toString () {
		return this.nombre;
	}
	
	public void setFecha(String date) {
		this.fecha = date;
		this.fecha_c = date_to_int(fecha);
	}
	public void setCategoria(String categorie) {
		this.categoria = categorie;
	}
	public void setPalabrasClave (String[] keyWords) {
		this.palabras_clave = keyWords.clone();
	}
	public void setContenido (String content) {
		this.contenido = content;
	}

	public String getNombre () {
		return nombre;
	}
	public String getFecha() {
		return fecha;
	}
	public int getFecha_c() {
		return fecha_c;
	}
	public String getCategorie() {
		return categoria;
	}
	public String[] getPalabras_Clave() {
		return palabras_clave;
	}
	public String getContenido() {
		return contenido;
	}
	
	

}
