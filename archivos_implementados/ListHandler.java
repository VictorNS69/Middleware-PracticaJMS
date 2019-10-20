package news_handler;

import java.util.*;
import noticia.Noticia;;

public class ListHandler {
	private static List total_noticias  = new ArrayList(); //Lista de noticias
	private static List palabras_clave= new ArrayList();
	private static List cat_politica= new ArrayList();
	private static List cat_economia = new ArrayList();
	private static List cat_deporte = new ArrayList();
	private HashMap<String,List> keyWords_map = new HashMap();
	
	public boolean importar_noticia (Noticia noticia) {
		//------ importamos la noticia en la lista de la categoría correspondiente ----//
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
		//---- Importamos la noticia en la lista general ----//
//TO DO: Ordenar las noticias de más reciente a mas antigua 
//	ahora se ordenan según llegan.
		
		total_noticias.add(noticia);
		String[] keywords =noticia.getPalabras_Clave();
		//---- Importamos la noticia en las listas de palabras clave correspondiente ----//
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
	public List get_total_noticas () {
		return total_noticias;
	}
	public List get_cat_politica() {
		return cat_politica;
	}
	public List get_cat_deporte() {
		return cat_deporte;
	}
	public List get_cat_economia() {
		return cat_economia;
	}
	private ArrayList all_news_with_keyword(String keyword) {
		return null;
	}
	
	
	private void gestion_total_noticias (Noticia noticia) {
		//TO DO: Implementar un metodo que coloque la noticia en su lugar
	}



}
