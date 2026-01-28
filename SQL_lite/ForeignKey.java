package SQL_lite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ForeignKey {

    private static Connection c = null;
    private static Statement stmt = null;

    public static void TabellenErstellen() {
        try {
            c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/shopdb?serverTimezone=UTC",
                "root",
                "RySj3b481"
            );
            stmt = c.createStatement();

            stmt.executeUpdate("DROP TABLE IF EXISTS Bestellung;");
            stmt.executeUpdate("DROP TABLE IF EXISTS Artikel;");
            stmt.executeUpdate("DROP TABLE IF EXISTS Kunden;");

            String kunden =
                "CREATE TABLE IF NOT EXISTS Kunden (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "email VARCHAR(255)" +
                ") ENGINE=InnoDB;";

            String artikel =
                "CREATE TABLE IF NOT EXISTS Artikel (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "bezeichnung VARCHAR(255), " +
                "preis DOUBLE" +
                ") ENGINE=InnoDB;";

            String bestellung =
                "CREATE TABLE IF NOT EXISTS Bestellung (" +
                "kundenID INT, " +
                "artikelID INT, " +
                "anzahl INT, " +
                "FOREIGN KEY(kundenID) REFERENCES Kunden(id), " +
                "FOREIGN KEY(artikelID) REFERENCES Artikel(id)" +
                ") ENGINE=InnoDB;";

            stmt.executeUpdate(kunden);
            stmt.executeUpdate(artikel);
            stmt.executeUpdate(bestellung);

        } catch (SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    public static void WerteEinfügen() {
        try {
            stmt.executeUpdate("INSERT INTO Kunden (name, email) VALUES ('Peter Meier', 'peter@mail.com');");
            stmt.executeUpdate("INSERT INTO Kunden (name, email) VALUES ('Anna Schmidt', 'anna@mail.com');");

            stmt.executeUpdate("INSERT INTO Artikel (bezeichnung, preis) VALUES ('Laptop', 999.99);");
            stmt.executeUpdate("INSERT INTO Artikel (bezeichnung, preis) VALUES ('Maus', 19.99);");

            stmt.executeUpdate("INSERT INTO Bestellung (kundenID, artikelID, anzahl) VALUES (1, 1, 1);");
            stmt.executeUpdate("INSERT INTO Bestellung (kundenID, artikelID, anzahl) VALUES (1, 2, 2);");

        } catch (SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    public static void KundenAusgeben() {
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Kunden;");
            System.out.println("Kunden:");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " | " + rs.getString("name") + " | " + rs.getString("email")
                );
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    public static void ArtikelAusgeben() {
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Artikel;");
            System.out.println("Artikel:");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " | " + rs.getString("bezeichnung") + " | " + rs.getDouble("preis")
                );
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    public static void BestellungenAusgeben() {
        try {
            String sql =
                "SELECT k.name, a.bezeichnung, a.preis, b.anzahl " +
                "FROM Bestellung b " +
                "JOIN Kunden k ON k.id = b.kundenID " +
                "JOIN Artikel a ON a.id = b.artikelID;";

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Bestellungen:");
            while (rs.next()) {
                double summe = rs.getDouble("preis") * rs.getInt("anzahl");
                System.out.println(
                    rs.getString("name") + " | " +
                    rs.getString("bezeichnung") + " | " +
                    rs.getInt("anzahl") + " | " +
                    summe
                );
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        TabellenErstellen();
        WerteEinfügen();
        KundenAusgeben();
        ArtikelAusgeben();
        BestellungenAusgeben();
    }
}
