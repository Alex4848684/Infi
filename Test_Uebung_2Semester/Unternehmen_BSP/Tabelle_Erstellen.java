<<<<<<< HEAD
package Test_Uebung_2Semester.Unternehmen_BSP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Tabelle_Erstellen {

    static final String URL = "jdbc:mysql://localhost:3306/test_uebung";
    static final String USER = "root";
    static final String PASSWORD = "RySj3b481";

    public static void main(String[] args) {

        try {

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            // Alte Tabellen löschen
            stmt.executeUpdate("DROP TABLE IF EXISTS bestellung");
            stmt.executeUpdate("DROP TABLE IF EXISTS produkt");
            stmt.executeUpdate("DROP TABLE IF EXISTS kunde");
            stmt.executeUpdate("DROP TABLE IF EXISTS mitarbeiter");

            // Kunde Tabelle
            stmt.executeUpdate(
                    "CREATE TABLE kunde (" +
                            "kunden_id INT PRIMARY KEY AUTO_INCREMENT," +
                            "name VARCHAR(100)," +
                            "stadt VARCHAR(100)" +
                            ")"
            );

            // Produkt Tabelle
            stmt.executeUpdate(
                    "CREATE TABLE produkt (" +
                            "produkt_id INT PRIMARY KEY AUTO_INCREMENT," +
                            "produkt_name VARCHAR(100)," +
                            "preis DOUBLE" +
                            ")"
            );

            // Bestellung Tabelle
            stmt.executeUpdate(
                    "CREATE TABLE bestellung (" +
                            "bestellung_id INT PRIMARY KEY AUTO_INCREMENT," +
                            "kunden_id INT," +
                            "produkt_id INT," +
                            "FOREIGN KEY (kunden_id) REFERENCES kunde(kunden_id)," +
                            "FOREIGN KEY (produkt_id) REFERENCES produkt(produkt_id)" +
                            ")"
            );

            // Mitarbeiter Tabelle (erweitert für GROUP BY)
            stmt.executeUpdate(
                    "CREATE TABLE mitarbeiter (\n" +
                            "    mitarbeiter_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "    name VARCHAR(100),\n" +
                            "    abteilung VARCHAR(100),\n" +
                            "    gehalt DOUBLE,\n" +
                            "    chef_id INT NULL,\n" +
                            "    FOREIGN KEY (chef_id) REFERENCES mitarbeiter(mitarbeiter_id) ON DELETE SET NULL" +
                            ")"
            );

            System.out.println("Tabellen erfolgreich erstellt!");

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
=======
package Test_Uebung_2Semester.Unternehmen_BSP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Tabelle_Erstellen {

    static final String URL = "jdbc:mysql://localhost:3306/test_uebung";
    static final String USER = "root";
    static final String PASSWORD = "RySj3b481";

    public static void main(String[] args) {

        try {

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            // Alte Tabellen löschen
            stmt.executeUpdate("DROP TABLE IF EXISTS bestellung");
            stmt.executeUpdate("DROP TABLE IF EXISTS produkt");
            stmt.executeUpdate("DROP TABLE IF EXISTS kunde");
            stmt.executeUpdate("DROP TABLE IF EXISTS mitarbeiter");

            // Kunde Tabelle
            stmt.executeUpdate(
                    "CREATE TABLE kunde (" +
                            "kunden_id INT PRIMARY KEY AUTO_INCREMENT," +
                            "name VARCHAR(100)," +
                            "stadt VARCHAR(100)" +
                            ")"
            );

            // Produkt Tabelle
            stmt.executeUpdate(
                    "CREATE TABLE produkt (" +
                            "produkt_id INT PRIMARY KEY AUTO_INCREMENT," +
                            "produkt_name VARCHAR(100)," +
                            "preis DOUBLE" +
                            ")"
            );

            // Bestellung Tabelle
            stmt.executeUpdate(
                    "CREATE TABLE bestellung (" +
                            "bestellung_id INT PRIMARY KEY AUTO_INCREMENT," +
                            "kunden_id INT," +
                            "produkt_id INT," +
                            "FOREIGN KEY (kunden_id) REFERENCES kunde(kunden_id)," +
                            "FOREIGN KEY (produkt_id) REFERENCES produkt(produkt_id)" +
                            ")"
            );

            // Mitarbeiter Tabelle (erweitert für GROUP BY)
            stmt.executeUpdate(
                    "CREATE TABLE mitarbeiter (\n" +
                            "    mitarbeiter_id INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "    name VARCHAR(100),\n" +
                            "    abteilung VARCHAR(100),\n" +
                            "    gehalt DOUBLE,\n" +
                            "    chef_id INT NULL,\n" +
                            "    FOREIGN KEY (chef_id) REFERENCES mitarbeiter(mitarbeiter_id) ON DELETE SET NULL" +
                            ")"
            );

            System.out.println("Tabellen erfolgreich erstellt!");

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
>>>>>>> origin/main
}