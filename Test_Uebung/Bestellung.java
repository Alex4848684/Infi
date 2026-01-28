package Test_Uebung;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Bestellung
{
	public static void Bestellunge_einfügen(int kunden_id,int artikel_id,  int anzahl) {
		
		String sql = "INSERT INTO bestellungen (kunden_id, artikel_id, anzahl) VALUES(?,?,?)";
		
		try (Connection c = Tabellen_Anlegen.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
			
			ps.setInt(1, kunden_id);
			ps.setInt(2, artikel_id);
			ps.setInt(3, anzahl);
			ps.executeUpdate();
			
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			
		}
	}
}
