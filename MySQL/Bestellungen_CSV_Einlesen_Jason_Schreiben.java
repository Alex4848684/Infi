package MySQL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bestellungen_CSV_Einlesen_Jason_Schreiben {

    public static class BestellungData {
        int kundenID;
        int artikelID;
        int anzahl;

        public BestellungData(int kundenID, int artikelID, int anzahl) {
            this.kundenID = kundenID;
            this.artikelID = artikelID;
            this.anzahl = anzahl;
        }
    }

    public static void bestellungenImportierenUndJson(String csvDatei, String jsonDatei) {
        List<BestellungData> bestellungen = new ArrayList<>();

        // CSV einlesen
        try (BufferedReader br = new BufferedReader(new FileReader(csvDatei))) {
            br.readLine(); // Header überspringen

            String zeile;
            while ((zeile = br.readLine()) != null) {
                String[] daten = zeile.split(",");
                if (daten.length >= 3) {
                    int kundenID  = Integer.parseInt(daten[0].trim());
                    int artikelID = Integer.parseInt(daten[1].trim());
                    int anzahl    = Integer.parseInt(daten[2].trim());

                    // in DB
                    Bestellung.bestellen(kundenID, artikelID, anzahl);

                    // für JSON
                    bestellungen.add(new BestellungData(kundenID, artikelID, anzahl));
                }
            }
        } catch (Exception e) {
            System.out.println("CSV Bestellungen: " + e.getMessage());
            return;
        }

        // JSON anhängen
        schreibeBestellungenAlsJson(bestellungen, jsonDatei);
    }

    private static void schreibeBestellungenAlsJson(List<BestellungData> bestellungen, String datei) {
        try (FileWriter writer = new FileWriter(datei, true)) { // append
            for (BestellungData b : bestellungen) {
                String timestamp = LocalDateTime.now().toString(); // 2026-01-13T18:30:15.123
                String jsonLine =
                    "{\"kundenID\":" + b.kundenID +
                    ", \"artikelID\":" + b.artikelID +
                    ", \"anzahl\":" + b.anzahl +
                    ", \"bestellt_am\":\"" + timestamp + "\"}\n";
                writer.write(jsonLine);
            }
        } catch (IOException e) {
            System.out.println("JSON Bestellungen: " + e.getMessage());
        }
    }
}
