package Test_Uebung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Tabellen_Anlegen
{
	private static final String URL = "jdbc:mysql://localhost:3306/firma?serverTimezone=UTC";
	private static final String USER = "root";
	private static final String PASSWORD = "RySj3b481";

	public static Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
		public static void Tabellen_Erstellen()
		{
		try (Connection c = getConnection(); Statement stmt = c.createStatement())
		{
		    // Foreign Keys aktivieren!
		    stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=1");
		    
		    stmt.executeUpdate(
		        "CREATE TABLE IF NOT EXISTS kunde (" +
		        "kunden_id INT AUTO_INCREMENT PRIMARY KEY," +
		        "name VARCHAR(100)," +
		        "email VARCHAR(100)" +
		        ")"
		    );

		    stmt.executeUpdate(
		        "CREATE TABLE IF NOT EXISTS artikel (" +
		        "artikel_id INT AUTO_INCREMENT PRIMARY KEY," +
		        "bezeichnung VARCHAR(100)," +
		        "preis DOUBLE" +
		        ")"
		    );

		    stmt.executeUpdate(
		        "CREATE TABLE IF NOT EXISTS bestellungen (" +
		        "bestellung_id INT AUTO_INCREMENT PRIMARY KEY," +
		        "kunden_id INT," +
		        "artikel_id INT," +
		        "anzahl INT," +
		        "FOREIGN KEY (kunden_id) REFERENCES kunde(kunden_id)," +
		        "FOREIGN KEY (artikel_id) REFERENCES artikel(artikel_id)" +
		        ")"
		    );

		} catch (SQLException e) {
		    System.out.println("Fehler: " + e.getMessage());
		}
		}
	}
