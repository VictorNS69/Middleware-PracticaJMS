import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Interfaz
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nNombre de Usuario\n");
        String nombre = sc.nextLine();
	/*login*/
	System.out.println("\nTipo de Suscripcion?\n");//?
        String suscripcion = sc.nextLine();
        System.out.println("\nComo desea filtrar sus noticias?\n\t[1]Por Tema\n\t[2]Por Palabra Clave\n\t[3]Por Fecha\n");
        int filtro = sc.nextInt(); 
        switch(filtro)
	{
            case 1:
                System.out.println("\nTema?\n\t[1]Politica\n\t[2]Economia\n\t[3]Deportes\n");
		int tema = sc.nextInt();
			switch(tema)
			{
				case 1:
					/*Politica*/
				break;
				case 2:
					/*Economia*/
				break;
				case 3:
					/*Deportes*/
				break;
				default:
					System.out.println("Entrada no valida... Saliendo...\n");
					/*logout*/
				break;
			}
                break;
            case 2:
		System.out.println("\nIntroduzca Palabra Clave\n");
		String key = sc.nextLine();
		/*llamada con key*/
                break;
            case 3:
		boolean res=true;
		String cadena = "NO";
		System.out.println("\nIntroduzca Fecha: Formato DD/MM/YYYY, si no desea filtrar en algun caso, escriba NO");
                System.out.println("\nFecha Inicio? Si se especifica, se serviran noticias posteriores a esta fecha\n");
		String fechaini = sc.next();
		System.out.println("\nFecha Final? Si se especifica, se serviran noticias anteriores a esta fecha\n");
		String fechafin = sc.next();
		if((fechaini.equals("NO"))&&(fechafin.equals("NO")))
		{
			System.out.println("\nNo hay fecha especifica, salen todas las noticias\n");
			/*llamada para todas las noticias*/
			break;
		} else if(fechaini.equals("NO"))
		{
			res=validarFecha(fechafin);
			if(res==false)
			{
				System.out.println("\nFormato de fecha invalido... Saliendo...");
				/*logout*/
				break;
			} else {
				System.out.println("\nNoticias anteriores:\n");
				/*llamada con fechafin*/
			}
		} else if(fechafin.equals("NO"))
		{
			res=validarFecha(fechaini);
			if(res==false)
			{
				System.out.println("\nFormato de fecha invalido... Saliendo...");
				/*logout*/
				break;
			} else {
				System.out.println("\nNoticias posteriores:\n");
				/*llamada con fechaini*/
			}
		} else
		{
			res=validarFecha(fechafin);
			if(res==false)
			{
				System.out.println("\nFormato de fecha invalido... Saliendo...");
				/*logout*/
				break;
			}
			res=validarFecha(fechaini);
			if(res==false)
			{
				System.out.println("\nFormato de fecha invalido... Saliendo...");
				/*logout*/
				break;
			} else {
				System.out.println("\nNoticias entre "+fechaini+" y "+fechafin+"\n");
				/*llamada con fechaini y fechafin*/
			}
			
		}
                break;
            default:
                System.out.println("\nta lue\n");
		/*logout*/
        }
    }


	public static boolean validarFecha(String fecha) {
        	try 
		{
			SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
		        formatoFecha.setLenient(false);
            		formatoFecha.parse(fecha);
		} catch (ParseException e) 
		{
            		return false;
		}
        	return true;
    	}
}
