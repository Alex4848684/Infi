package Test_Uebung_2Semester.Unternehmen_BSP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Daten_Einfügen {

    static final String URL = "jdbc:mysql://localhost:3306/test_uebung";
    static final String USER = "root";
    static final String PASSWORD = "RySj3b481";

    public static void main(String[] args) {

        try {

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            // Kunden
            //stmt.executeUpdate("INSERT INTO kunde (name, stadt) VALUES ('Anna','Wien')");
           //stmt.executeUpdate("INSERT INTO kunde (name, stadt) VALUES ('Max','Graz')");
            //stmt.executeUpdate("INSERT INTO kunde (name, stadt) VALUES ('Lisa','Linz')");
            //stmt.executeUpdate("INSERT INTO kunde (name, stadt) VALUES ('Tomas','Berlin')");
            stmt.executeUpdate("INSERT INTO kunde (name, stadt) VALUES ('Tom','Wien')");


            // Produkte
            //stmt.executeUpdate("INSERT INTO produkt (produkt_name, preis) VALUES ('Laptop',1200)");
            //stmt.executeUpdate("INSERT INTO produkt (produkt_name, preis) VALUES ('Maus',25)");
            //stmt.executeUpdate("INSERT INTO produkt (produkt_name, preis) VALUES ('Tastatur',80)");

            // Bestellungen
            //stmt.executeUpdate("INSERT INTO bestellung (kunden_id, produkt_id) VALUES (1,1)");
            //stmt.executeUpdate("INSERT INTO bestellung (kunden_id, produkt_id) VALUES (2,2)");
            //stmt.executeUpdate("INSERT INTO bestellung (kunden_id, produkt_id) VALUES (1,3)");

            // Mitarbeiter
            //stmt.executeUpdate("INSERT INTO mitarbeiter (name, abteilung, gehalt, chef_id) VALUES ('Chef','Management',5000,NULL)");
            //stmt.executeUpdate("INSERT INTO mitarbeiter (name, abteilung, gehalt, chef_id) VALUES ('Tom','IT',3000,1)");
            //stmt.executeUpdate("INSERT INTO mitarbeiter (name, abteilung, gehalt, chef_id) VALUES ('Julia','IT',3200,1)");
            //stmt.executeUpdate("INSERT INTO mitarbeiter (name, abteilung, gehalt, chef_id) VALUES ('Alex','IT',2800,2)");
            //stmt.executeUpdate("INSERT INTO mitarbeiter (name, abteilung, gehalt, chef_id) VALUES ('Sara','Verkauf',2500,1)");
            //stmt.executeUpdate("INSERT INTO mitarbeiter (name, abteilung, gehalt, chef_id) VALUES ('Lukas','Verkauf',2400,1)");

            System.out.println("Testdaten erfolgreich eingefügt!");

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}