package Test_Uebung;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Artikel
{

	public static void Artikel_Anlegen(String bezeichnung, double preis)
	{

		String sql = "INSERT INTO artikel (bezeichnung, preis) VALUES(?,?)";

		try (Connection c = Tabellen_Anlegen.getConnection(); PreparedStatement ps = c.prepareStatement(sql))
		{
			ps.setString(1, bezeichnung);
			ps.setDouble(2, preis);
			ps.executeUpdate();
			System.out.println("Eingefügt, Artikel");
			System.out.println(bezeichnung);
			System.out.print(preis);

		} catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}