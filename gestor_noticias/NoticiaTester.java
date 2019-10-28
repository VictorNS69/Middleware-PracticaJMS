package gestor_noticias;
import java.io.File;
import java.util.List;


public class NoticiaTester {
	final static String news_dir = "/home/eduardo/Escritorio/Archivador/UNI/MIDDLEWARE/JMS/practicajms/noticias_test";

	public static void pruebas1() {
		File dir = new File(news_dir);
		File[] news_list = dir.listFiles();
		Noticia noticia;
		ListHandler Handler = new ListHandler();
		int i = 0;
		for(File news_file: news_list) {
			try {
				noticia = new Noticia(news_file.getAbsolutePath());
				Handler.importar_noticia(noticia);
				i++;
			}
			catch(Exception e){
				System.out.println("ERROR @ MAIN: no se pudo acceder al archivo de la noticia");
			}
		}
		System.out.println("Numero de noticias importadas = "+i);
		//---- TODAS LAS NOTICIAS HAN SIDO IMPORTADAS ----//
		
		//---- PRUEBAS DE FILTRAR NOTICIAS ----//
		//---- FILTRAR POR FECHA: TOTAL NOTICIAS ----/
		List<Noticia> resultado=Handler.get_total_noticas();
		for(i=0;i<Handler.get_total_noticas().size();i++) {
			System.out.println(resultado.get(i).toString() +" "+resultado.get(i).getFecha());		
		}
		System.out.println("//---- FILTRAR POR FECHA: TOTAL NOTICIAS TERMINADO ----//");
		//---- FILTRAR POR FECHA: DESDE FECHA INDICADA ---//	
		resultado = Handler.get_older_than("29-12-2002");
		for(i=0;i<resultado.size();i++) {
			System.out.println(resultado.get(i).toString()+" "+resultado.get(i).getFecha());
		}
		System.out.println("//---- FILTRAR POR FECHA: MAS ANTIGUAS QUE 29-12-2002 TERMINADO ----//");
		//---- FILTRAR POR FECHA: HASTA FECHA INDICADA ---//
		resultado = Handler.get_newer_than("1-9-2005");
		for(i=0;i<resultado.size();i++) {
			System.out.println(resultado.get(i).toString()+" "+resultado.get(i).getFecha());
		}
		System.out.println("//---- FILTRAR POR FECHA: MAS NUEVAS QUE 1-9-2005 TERMINADO----//");
		//---- FILTRAR POR PALABRA CLAVE: OBTENER PALABRAS CLAVE ----//
		System.out.println(Handler.keywords_on_map());
		//---- FILTRAR POR PALABRA CLAVE: "very" ----//
		resultado = Handler.get_news_with_keyword("very");
		for(i=0;i<resultado.size();i++) {
			System.out.println(resultado.get(i).toString());
			for(String palabra:resultado.get(i).getPalabras_Clave()) {
				System.out.println(palabra);
			}
		}
		System.out.println("//---- FILTRAR POR FECHA: MAS NUEVAS QUE 1-9-2005 TERMINADO----//");
		resultado = Handler.get_cat_deporte();
		for(i=0;i<resultado.size();i++) {
			System.out.println(resultado.get(i).toString()+" "+resultado.get(i).getCategorie());
		}
		System.out.println("//---- FILTRAR POR CATEGORIA: TERMINADO----//");
		System.out.println("//---- TERMINADO ----//");
				
		
		//System.out.println(Handler.get_total_noticas());
		//System.out.println(Handler.get_cat_deporte().toArray().toString()); 
		
	}

	public static void main(String[] args)throws Exception {
		
		pruebas1();
		/*
		 * 		// TODO Auto-generated method stub
		String[] new1_keywords = {"Noticia","Deporte","Dembelé","RAUL"};
		Noticia new1 = new Noticia("Dembele y Raul hacen un baúl","22-04-1999","DEPORTE",new1_keywords,"Esto es el cuerpo de la primera noticia");
		System.out.println("primera noticia creada");
		Noticia new2 = new Noticia("/home/eduardo/Escritorio/Archivador/UNI/MIDDLEWARE/JMS/noticias_test/noticia_1.txt");
		Noticia new3 = new Noticia("/home/eduardo/Escritorio/Archivador/UNI/MIDDLEWARE/JMS/noticias_test/noticia_2");
		Noticia new4 = new Noticia("/home/eduardo/Escritorio/Archivador/UNI/MIDDLEWARE/JMS/noticias_test/noticia_3");
		ListHandler Handler = new ListHandler();
		Handler.importar_noticia(new1);
		Handler.importar_noticia(new2);
		Handler.importar_noticia(new3);
		Handler.importar_noticia(new4);
		System.out.println(Handler.get_cat_deporte());
		 */
		
	}

}
