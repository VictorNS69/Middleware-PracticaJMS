package gestor_noticias;

;


public class NoticiaTester {

	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
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
		
		
	}

}