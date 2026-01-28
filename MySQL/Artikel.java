package MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.time.LocalDate;

public class Artikel {

    public static void artikelAnlegen(String bezeichnung, double preis) {

        String sql =
            "INSERT INTO artikel (bezeichnung, preis, angelegt_am) VALUES (?, ?, ?)";

        try (Connection c = MySQL_1Aufgabe.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, bezeichnung);
            ps.setDouble(2, preis);
            ps.setDate(3, Date.valueOf(LocalDate.now())); // DATE

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
