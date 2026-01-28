package MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Kunde {

    public static void kundeAnlegen(String name, String email) {

        String sql =
            "INSERT INTO kunden (name, email) VALUES (?, ?)";

        try (Connection c = MySQL_1Aufgabe.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
