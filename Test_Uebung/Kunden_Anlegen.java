package Test_Uebung;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Kunden_Anlegen
{
	public static void Kunden_Erstellen(String name, String email)
	{

		String sql = "INSERT INTO kunde (name, email) VALUES (?, ?)";

		try (Connection c = Tabellen_Anlegen.getConnection(); PreparedStatement ps = c.prepareStatement(sql))
		{
			
			ps.setString(1, name);
			ps.setString(2, email);
			ps.executeUpdate();
			System.out.println("Eingefügt, Kunde");
			System.out.println(name);
			System.out.print(email);
			System.out.println("");
			System.out.println("");

		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}

	}

}
