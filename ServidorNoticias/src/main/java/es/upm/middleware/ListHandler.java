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
		String[] keywords = noticia.getPalabras_Clave();
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

	public List<Noticia> get_from_date(String start,String end){
		//---- (fecha1_c < fecha2_c) implica que fecha1 es MAS ANTIGUA
		//---- START 	--> Fecha mas antigua
		//---- END 		--> Fecha mas reciente
		//---- Devolvemos lo que esté entre medias.
		int start_c = date_to_int(start);
		int end_c = date_to_int(end);
		List<Noticia> resultado = new ArrayList();
		int fecha_noticia = 0;
		for(int i=total_noticias.size()-1; i>=0; i--) {
			fecha_noticia = total_noticias.get(i).getFecha_c();
			if(fecha_noticia > start_c && fecha_noticia < end_c) {
				resultado.add(total_noticias.get((i)));
			}
		}
		return resultado;
	}
	public List<Noticia> get_older_than(String start){
		//---- (fecha1_c < fecha2_c) implica que fecha1 es MAS ANTIGUA
		//---- Devolvemos las noticias mas antiguas que la fecha indicada
		int start_c = date_to_int(start);
		List<Noticia> resultado = new ArrayList();
		int fecha_noticia = 0;
		for(int i=total_noticias.size()-1;i>=0;i--) {
			fecha_noticia = total_noticias.get(i).getFecha_c();
			if(fecha_noticia < start_c ) {
				resultado.add(total_noticias.get((i)));
			}
		}
		return resultado;
	}
	public List<Noticia> get_newer_than(String date){
		//---- (fecha1_c < fecha2_c) implica que fecha1 es MAS ANTIGUA
		//---- Devolvemos las noticias mas recientes que la indicada
		int date_c = date_to_int(date);
		List<Noticia> resultado = new ArrayList();
		int fecha_noticia = 0;
		for(int i=total_noticias.size()-1;i>=0;i--) {
			fecha_noticia = total_noticias.get(i).getFecha_c();
			if(fecha_noticia > date_c) {
				resultado.add(total_noticias.get((i)));
			}
		}
		return resultado;
	}

	public String keywords_on_map(){
		String resultado;
		resultado = keyWords_map.keySet().toString();
		return resultado;
	}

	public List<Noticia> get_news_with_keyword(String keyword) {
		List<Noticia> resultado = new ArrayList();
		if(keyWords_map.containsKey(keyword)) {
			resultado = keyWords_map.get(keyword);
		}
		return resultado;
	}

	private int date_to_int (String s_fecha) {
		String[] a_fecha = s_fecha.split("-",3);
		int resultado = 0;
		resultado += Integer.parseInt(a_fecha[2])*10000;
		resultado += Integer.parseInt(a_fecha[1])*100;
		resultado += Integer.parseInt(a_fecha[0]);
		return resultado;
	}
}
