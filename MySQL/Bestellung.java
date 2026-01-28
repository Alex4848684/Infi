package MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Bestellung {

    public static void bestellen(int kundenID, int artikelID, int anzahl) {

        String sql = """
            INSERT INTO bestellungen
            (kundenID, artikelID, anzahl, bestellt_am)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection c = MySQL_1Aufgabe.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, kundenID);
            ps.setInt(2, artikelID);
            ps.setInt(3, anzahl);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); // DATETIME

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void bestellungenAnzeigen(int kundenID) {

        String sql = """
            SELECT a.bezeichnung, b.anzahl, a.preis, b.bestellt_am
            FROM bestellungen b
            JOIN artikel a ON b.artikelID = a.id
            WHERE b.kundenID = ?
        """;

        try (Connection c = MySQL_1Aufgabe.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, kundenID);
            ResultSet rs = ps.executeQuery();

            System.out.println("Bestellungen von Kunde ID " + kundenID + ":");

            while (rs.next()) {
                System.out.println(
                        rs.getString("bezeichnung") + " | " +
                        rs.getInt("anzahl") + " Stk | " +
                        rs.getDouble("preis") + " € | " +
                        rs.getTimestamp("bestellt_am")
                );
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
