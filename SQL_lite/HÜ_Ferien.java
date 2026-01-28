package SQL_lite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HÜ_Ferien
{

	private static Connection c = null;
	private static Statement stmt = null;

	public static void TabeleErstellen()
	{
		try
		{
			c = DriverManager.getConnection("jdbc:sqlite:ferien.db");
			stmt = c.createStatement();

			String sqlHÜ = "CREATE TABLE IF NOT EXISTS HÜ (" + "ID INTEGER PRIMARY KEY Autoincrement, " + "valu1 INT, "
					+ "valu2 INT)";
			stmt.executeUpdate(sqlHÜ);

			System.out.println("Tabelle 'HÜ' erstellt");
		} catch (SQLException e)
		{
			System.err.println("Fehler beim Erstellen der Tabelle: " + e.getMessage());
		}
	}

	public static void WerteEinfügen()
	{
		try
		{
			stmt.executeUpdate("DELETE FROM HÜ;");

			int k = 0;

			for (int x = 1; x <= 25; x++)
			{
				int valu1 = (int) (Math.random() * 100);
				int valu2 = valu1 % 2;

				String insertHÜ = "INSERT INTO HÜ (ID, valu1, valu2) VALUES (" + x + ", " + valu1 + ", " + valu2 + ");";
				stmt.executeUpdate(insertHÜ);

				if (valu2 == 1)
				{
					if (x == 25)
					{
						System.out.println("\nes sind " + k + " Zahlen 1");
					}
					k++;
				} else
				{
					if (x == 25)
					{
						System.out.println("\nes sind " + k + " Zahlen 1");
					}
				}

			}

		} catch (SQLException e)
		{
			System.err.println("Fehler beim Einfügen der Werte: " + e.getMessage());
		}
	}

	public static void Datanbankauslesen()
	{
		try
		{
			ResultSet rs = stmt.executeQuery("SELECT * FROM HÜ;");

			System.out.println("\nTabelleninhalt:");
			while (rs.next())
			{
				System.out.println("ID: " + rs.getInt("ID") + ", valu1: " + rs.getInt("valu1") + ", valu2: " + rs.getInt("valu2"));
			}
			rs.close();
		} catch (SQLException e)
		{
			System.err.println("Fehler beim Lesen der Datenbank: " + e.getMessage());
		}
	}
	
	
	public static void DatenLöschen()
	{
		try
		{
		stmt.executeUpdate("DROP TABLE IF EXISTS HÜ");
		System.out.println("Tabelle Erfolgreich Gelöscht");
		}catch(SQLException e) 
		{
			System.out.println("Fehler Beim Löschen" + e.getMessage());
		}
	}

	public static void main(String[] args)
	{
		TabeleErstellen();
		Datanbankauslesen();
		WerteEinfügen();
		DatenLöschen();
	}
}
