import java.util.Scanner;

public class Interfaz
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nombre de Usuario");
        String nombre = sc.nextLine();
	System.out.println("Tipo de Suscripcion?");
        String suscripcion = sc.nextLine();
        System.out.println("Como desea filtrar sus noticias?\n\t[1]Por Tema\n\t[2]Por Palabra Clave\n\t[3]Por Fecha");
        int filtro = sc.nextInt(); 
        switch(filtro)
	{
            case 1:
                System.out.println("Tema?\n\t[1]Politica\n\t[2]Economia\n\t[3]Deportes");
		int tema = sc.nextInt();
			switch(tema)
			{
				case 1:
				break;
				case 2:
				break;
				case 3:
				break;
			}
                break;
            case 2:
                System.out.println("Palabra Clave?");
		String key = sc.nextLine();
                break;
            case 3:
                System.out.println("Fecha Inicio?");
		String fechaini = sc.nextLine();
		System.out.println("Fecha Final?");
		String fechafin = sc.nextLine(); 
                break;
            default:
                System.out.println("ta lue");
        }
    }
}
