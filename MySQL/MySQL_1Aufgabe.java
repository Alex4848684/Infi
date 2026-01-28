package MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL_1Aufgabe {

    private static final String URL =
            "jdbc:mysql://localhost:3306/shopdb?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "RySj3b481";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void TabellenErstellen() {

        try (Connection c = getConnection();
             Statement stmt = c.createStatement()) {

            // Kunden
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS kunden (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100),
                    email VARCHAR(100)
                )
            """);

            // Artikel mit DATE
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS artikel (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    bezeichnung VARCHAR(100),
                    preis DOUBLE,
                    angelegt_am DATE
                )
            """);

            // Bestellungen mit DATETIME
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS bestellungen (
                    kundenID INT,
                    artikelID INT,
                    anzahl INT,
                    bestellt_am DATETIME,
                    FOREIGN KEY (kundenID) REFERENCES kunden(id),
                    FOREIGN KEY (artikelID) REFERENCES artikel(id)
                )
            """);

            System.out.println("Tabellen erfolgreich erstellt!");

        } catch (SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }
}
