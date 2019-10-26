package es.upm.middleware;

import java.util.*;
import es.upm.middleware.Noticia;



public class ListHandler {
	private static List<Noticia> total_noticias  = new ArrayList(); //Lista de noticias
	private static List palabras_clave= new ArrayList();
	private static List<Noticia> cat_politica= new ArrayList();
	private static List<Noticia> cat_economia = new ArrayList();
	private static List<Noticia> cat_deporte = new ArrayList();
	private HashMap<String,List> keyWords_map = new HashMap();
		
	public boolean importar_noticia (Noticia noticia) {
		//---- Importamos la noticia según categoría ----//
		if(!add_list_categoria(noticia))
			return false;
		//---- Importamos noticia en orden temporal a total_noticias ----//
		orden_noticias_tiempo(noticia);
		//---- Importamos la noticia en las listas de palabras clave correspondiente ----//
		String[] keywords =noticia.getPalabras_Clave();
		for(int i = 0; i< keywords.length;i++) {
			if(keyWords_map.containsKey(keywords[i])) {
				List noticias_con_keyword = keyWords_map.get(keywords[i]);
				noticias_con_keyword.add(noticia);
			}else{
				List<Noticia> nueva_lista = new ArrayList();
				nueva_lista.add(noticia);
				keyWords_map.put(keywords[i], nueva_lista);
			}
			
		}
		return true;
	}
	private boolean add_list_categoria(Noticia noticia) {
		switch(noticia.getCategorie()){
		case "POLITICA":
			cat_politica.add(noticia);
			break;
		case "ECONOMIA":
			cat_economia.add(noticia);
			break;
		case "DEPORTE":
			cat_deporte.add(noticia);
			break;
		default:
			return false;
		}
		return true;
		
	}
	private void orden_noticias_tiempo (Noticia noticia){
		boolean terminado = false;
		int fecha_actual = noticia.getFecha_c();
		int fecha_consulta;
		for(int i=0; i<total_noticias.size()&&!terminado;i++) {
			fecha_consulta = total_noticias.get(i).getFecha_c();
			if(fecha_actual > fecha_consulta) {
				total_noticias.add(i, noticia);
				terminado = true;
			}
		}
		if(!terminado) {
			total_noticias.add(noticia);
		}
	}
	
	
	public List<Noticia> get_total_noticas () {
		return total_noticias;
	}
	public List<Noticia> get_cat_politica() {
		return cat_politica;
	}
	public List<Noticia> get_cat_deporte() {
		return cat_deporte;
	}
	public List<Noticia> get_cat_economia() {
		return cat_economia;
	}
	public List<Noticia> all_news_with_keyword(String keyword) {
		List<Noticia> resultado = new ArrayList();
		if(keyWords_map.containsKey(keyword)) {
			resultado = keyWords_map.get(keyword);
		}
		return resultado;
	}

}
