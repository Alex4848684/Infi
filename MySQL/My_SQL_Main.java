package MySQL;

public class My_SQL_Main
{

	public static void main(String[] args)
	{

		MySQL_1Aufgabe.TabellenErstellen();

		Kunde.kundeAnlegen("Max Mustermann", "max@mail.at");
		Artikel.artikelAnlegen("Laptop", 999.99);
		Artikel.artikelAnlegen("Maus", 19.90);

		Bestellung.bestellen(1, 1, 1);
		Bestellung.bestellen(1, 2, 2);

		Bestellung.bestellungenAnzeigen(1);

		Kunden_CSV_Einlesen_Jason_Schreiben.kundenImportierenUndJson("CSV_Einlese_Datei_Kunden.csv", "Jason_Rausschreiben.json");
		Bestellungen_CSV_Einlesen_Jason_Schreiben.bestellungenImportierenUndJson("CSV_Einlesen_Bestellungen.csv","Jason_Bestellungen_Rausschreiben.json");
		Artikel_CSV_Einlesen_Jason_Schreiben.artikelImportierenUndJson("CSV_Einlesen_Artikel.csv", "Jason_Artikel_Rausschreiben.json");
		
	}
}
