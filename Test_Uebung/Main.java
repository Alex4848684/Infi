package Test_Uebung;

public class Main
{
	public static void main(String[] args)
	{
		Tabellen_Anlegen.Tabellen_Erstellen();
	    System.out.println("Tabellen wurden erstellt (oder existieren bereits).");
	    
	    Kunden_Anlegen.Kunden_Erstellen("Max", "MaxMusterman@gmail.com");
	    Artikel.Artikel_Anlegen("Maus", 9.99);
	    Bestellung.Bestellunge_einfügen(1,13,1);
	    
	}
}
