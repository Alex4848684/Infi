package MySQL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Kunden_CSV_Einlesen_Jason_Schreiben {

    public static void kundenImportierenUndJson(String csvDatei, String jsonDatei) {
        List<String[]> kunden = new ArrayList<>();

        // CSV einlesen
        try (BufferedReader br = new BufferedReader(new FileReader(csvDatei))) {
            br.readLine(); // Header überspringen

            String zeile;
            while ((zeile = br.readLine()) != null) {
                String[] daten = zeile.split(",");
                if (daten.length >= 2) {
                    String name  = daten[0].trim();
                    String email = daten[1].trim();

                    // in DB
                    Kunde.kundeAnlegen(name, email);

                    // für JSON
                    kunden.add(new String[]{name, email});
                }
            }
        } catch (Exception e) {
            System.out.println("CSV Kunden: " + e.getMessage());
            return;
        }

        // JSON anhängen
        schreibeKundenAlsJson(kunden, jsonDatei);
    }

    private static void schreibeKundenAlsJson(List<String[]> kunden, String datei) {
        String heute = LocalDate.now().toString(); // z.B. 2026-01-13

        try (FileWriter writer = new FileWriter(datei, true)) { // append = true
            for (String[] kunde : kunden) {
                String name  = kunde[0].replace("\"", "\\\"");
                String email = kunde[1].replace("\"", "\\\"");
                String jsonLine =
                    "{\"name\":\"" + name +
                    "\", \"email\":\"" + email +
                    "\", \"angelegt_am\":\"" + heute + "\"}\n";
                writer.write(jsonLine);
            }
        } catch (IOException e) {
            System.out.println("JSON Kunden: " + e.getMessage());
        }
    }

    // optional: Fallback
    public static void kundenImportieren(String csvDatei) {
        kundenImportierenUndJson(csvDatei, "kunden.jsonl");
    }
}
