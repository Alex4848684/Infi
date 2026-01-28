package MySQL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Artikel_CSV_Einlesen_Jason_Schreiben {

    public static class ArtikelData {
        String bezeichnung;
        double preis;

        public ArtikelData(String bezeichnung, double preis) {
            this.bezeichnung = bezeichnung;
            this.preis = preis;
        }
    }

    public static void artikelImportierenUndJson(String csvDatei, String jsonDatei) {
        List<ArtikelData> artikel = new ArrayList<>();

        // CSV einlesen
        try (BufferedReader br = new BufferedReader(new FileReader(csvDatei))) {
            br.readLine(); // Header überspringen

            String zeile;
            while ((zeile = br.readLine()) != null) {
                String[] daten = zeile.split(",");
                if (daten.length >= 2) {
                    String bezeichnung = daten[0].trim();
                    double preis       = Double.parseDouble(daten[1].trim());

                    // in DB
                    Artikel.artikelAnlegen(bezeichnung, preis);

                    // für JSON
                    artikel.add(new ArtikelData(bezeichnung, preis));
                }
            }
        } catch (Exception e) {
            System.out.println("CSV Artikel: " + e.getMessage());
            return;
        }

        // JSON anhängen
        schreibeArtikelAlsJson(artikel, jsonDatei);
    }

    private static void schreibeArtikelAlsJson(List<ArtikelData> artikel, String datei) {
        String heute = LocalDate.now().toString();

        try (FileWriter writer = new FileWriter(datei, true)) { // append
            for (ArtikelData a : artikel) {
                String bez   = a.bezeichnung.replace("\"", "\\\"");
                String preis = String.format("%.2f", a.preis).replace(",", ".");
                String jsonLine =
                    "{\"bezeichnung\":\"" + bez +
                    "\", \"preis\":" + preis +
                    ", \"angelegt_am\":\"" + heute + "\"}\n";
                writer.write(jsonLine);
            }
        } catch (IOException e) {
            System.out.println("JSON Artikel: " + e.getMessage());
        }
    }
}
