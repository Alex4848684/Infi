//package SQL_lite;
//
//import java.sql.*;
//
//public class Main
//{
//
//	public static void main(String args[])
//	{
//		Connection c = null;
//		Statement stmt = null;
//
//		try
//		{
//			Class.forName("org.sqlite.JDBC");
//			c = DriverManager.getConnection("jdbc:sqlite:test.db");
//			c.setAutoCommit(false);
//			System.out.println("Opened database successfully");
//
//			stmt = c.createStatement();
//
//			String sqlCompany = "CREATE TABLE IF NOT EXISTS COMPANY " + "(ID INT PRIMARY KEY NOT NULL, "
//					+ " NAME TEXT NOT NULL, " + " AGE INT NOT NULL, " + " ADDRESS CHAR(50), " + " SALARY REAL)";
//			stmt.executeUpdate(sqlCompany);
//
//			String sqlDepartment = "CREATE TABLE IF NOT EXISTS DEPARTMENT " + "(ID INT PRIMARY KEY NOT NULL, "
//					+ " NAME TEXT NOT NULL, " + " AGE INT NOT NULL, " + " ADDRESS CHAR(50), " + " SALARY REAL)";
//			stmt.executeUpdate(sqlDepartment);
//
//			String sqlEmployee = "CREATE TABLE IF NOT EXISTS EMPLOYEE " + "(ID INT PRIMARY KEY NOT NULL, "
//					+ " NAME TEXT NOT NULL, " + " AGE INT NOT NULL, " + " ADDRESS CHAR(50), " + " SALARY REAL)";
//			stmt.executeUpdate(sqlEmployee);
//			
//			
//			stmt.executeUpdate("DELETE FROM COMPANY;");
//			stmt.executeUpdate("DELETE FROM DEPARTMENT;");
//			stmt.executeUpdate("DELETE FROM EMPLOYEE;");
//			
//			
//			String insertCompany = "INSERT INTO COMPANY (ID, NAME, AGE, ADDRESS, SALARY) "
//					+ "VALUES (1, 'Müller GmbH', 10, 'Berlin', 50000.00), "
//					+ "       (2, 'Tech AG', 5, 'Hamburg', 75000.00);";
//			
//			stmt.executeUpdate(insertCompany);
//
//			String insertDepartment = "INSERT INTO DEPARTMENT (ID, NAME, AGE, ADDRESS, SALARY) "
//					+ "VALUES (1, 'Vertrieb', 3, 'München', 40000.00), "
//					+ "       (2, 'Entwicklung', 6, 'Frankfurt', 60000.00);";
//			stmt.executeUpdate(insertDepartment);
//
//			String insertEmployee = "INSERT INTO EMPLOYEE (ID, NAME, AGE, ADDRESS, SALARY) "
//					+ "VALUES (1, 'Hans Meier', 35, 'Köln', 55000.00), "
//					+ "       (2, 'Sabine Schmidt', 29, 'Düsseldorf', 48000.00), "
//					+ "       (3, 'Thomas Weber', 41, 'Berlin', 65000.00);";
//			stmt.executeUpdate(insertEmployee);
//
//			stmt.close();
//			c.commit();
//			c.close();
//
//		} catch (Exception e)
//		{
//			System.err.println("Fehler: " + e.getMessage());
//			System.exit(1);
//		}
//
//		System.out.println("Tables created and values inserted successfully");
//	}
//}
package SQL_lite;

import java.sql.*;

public class Main
{

	private static Connection c = null;
	private static Statement stmt = null;

	public static void openDatabase()
	{
		try
		{
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			stmt = c.createStatement();
			System.out.println("Opened database successfully");
		} catch (Exception e)
		{
			System.err.println("Fehler beim Öffnen der Datenbank: " + e.getMessage());
			System.exit(1);
		}
	}

	public static void createTables()
	{
		try
		{
			String sqlCompany = "CREATE TABLE IF NOT EXISTS COMPANY " + "(ID INT PRIMARY KEY NOT NULL, "
					+ "NAME TEXT NOT NULL, " + "AGE INT NOT NULL, " + "ADDRESS CHAR(50), " + "SALARY REAL)";
			stmt.executeUpdate(sqlCompany);

			String sqlDepartment = "CREATE TABLE IF NOT EXISTS DEPARTMENT " + "(ID INT PRIMARY KEY NOT NULL, "
					+ "NAME TEXT NOT NULL, " + "AGE INT NOT NULL, " + "ADDRESS CHAR(50), " + "SALARY REAL)";
			stmt.executeUpdate(sqlDepartment);

			String sqlEmployee = "CREATE TABLE IF NOT EXISTS EMPLOYEE " + "(ID INT PRIMARY KEY NOT NULL, "
					+ "NAME TEXT NOT NULL, " + "AGE INT NOT NULL, " + "ADDRESS CHAR(50), " + "SALARY REAL)";
			stmt.executeUpdate(sqlEmployee);

			System.out.println("Tables created successfully");
		} catch (SQLException e)
		{
			System.err.println("Fehler beim Erstellen der Tabellen: " + e.getMessage());
		}
	}

	public static void insertValues()
	{
		try
		{
			// Optional: Vorher alte Werte löschen
			stmt.executeUpdate("DELETE FROM COMPANY;");
			stmt.executeUpdate("DELETE FROM DEPARTMENT;");
			stmt.executeUpdate("DELETE FROM EMPLOYEE;");

			String insertCompany = "INSERT INTO COMPANY (ID, NAME, AGE, ADDRESS, SALARY) "
					+ "VALUES (1, 'Müller GmbH', 10, 'Berlin', 50000.00), "
					+ "       (2, 'Tech AG', 5, 'Hamburg', 75000.00);";
			stmt.executeUpdate(insertCompany);

			String insertDepartment = "INSERT INTO DEPARTMENT (ID, NAME, AGE, ADDRESS, SALARY) "
					+ "VALUES (1, 'Vertrieb', 3, 'München', 40000.00), "
					+ "       (2, 'Entwicklung', 6, 'Frankfurt', 60000.00);";
			stmt.executeUpdate(insertDepartment);

			String insertEmployee = "INSERT INTO EMPLOYEE (ID, NAME, AGE, ADDRESS, SALARY) "
					+ "VALUES (1, 'Hans Meier', 35, 'Köln', 55000.00), "
					+ "       (2, 'Sabine Schmidt', 29, 'Düsseldorf', 48000.00), "
					+ "       (3, 'Thomas Weber', 41, 'Berlin', 65000.00);";
			stmt.executeUpdate(insertEmployee);

			System.out.println("Values inserted successfully");
		} catch (SQLException e)
		{
			System.err.println("Fehler beim Einfügen der Werte: " + e.getMessage());
		}
	}

	public static void closeDatabase()
	{
		try
		{
			if (stmt != null)
				stmt.close();
			if (c != null)
			{
				c.commit();
				c.close();
			}
			System.out.println("Database closed successfully");
		} catch (SQLException e)
		{
			System.err.println("Fehler beim Schließen der Datenbank: " + e.getMessage());
		}
	}

	public static void deletDatabase()
	{
		try
		{
			stmt.executeUpdate("DROP TABLE IF EXISTS COMPANY;");
			stmt.executeUpdate("DROP TABLE IF EXISTS DEPARTMENT;");
			stmt.executeUpdate("DROP TABLE IF EXISTS EMPLOYEE;");
			System.out.println("Database tables deleted successfully");
		} catch (SQLException e)
		{
			System.err.println("Fehler beim Löschen der Datenbank: " + e.getMessage());
		}
	}

	public static void readDatabase()
	{
		try {

	        ResultSet rsCompany = stmt.executeQuery("SELECT * FROM COMPANY WHERE ID = 1;");
	        while (rsCompany.next()) {
	            System.out.println(
	                    "ID: " + rsCompany.getInt("ID") +
	                    ", Name: " + rsCompany.getString("NAME") +
	                    ", Age: " + rsCompany.getInt("AGE") +
	                    ", Address: " + rsCompany.getString("ADDRESS") +
	                    ", Salary: " + rsCompany.getDouble("SALARY")
	            );
	        }
	        rsCompany.close();

	        ResultSet rsDepartment = stmt.executeQuery("SELECT * FROM DEPARTMENT WHERE ID = 2;");
	        while (rsDepartment.next()) {
	            System.out.println(
	                    "ID: " + rsDepartment.getInt("ID") +
	                    ", Name: " + rsDepartment.getString("NAME") +
	                    ", Age: " + rsDepartment.getInt("AGE") +
	                    ", Address: " + rsDepartment.getString("ADDRESS") +
	                    ", Salary: " + rsDepartment.getDouble("SALARY")
	            );
	        }
	        rsDepartment.close();

	        ResultSet rsEmployee = stmt.executeQuery("SELECT * FROM EMPLOYEE WHERE Age Like '42';");
	        while (rsEmployee.next()) {
	            System.out.println(
	                    "ID: " + rsEmployee.getInt("ID") +
	                    ", Name: " + rsEmployee.getString("NAME") +
	                    ", Age: " + rsEmployee.getInt("AGE") +
	                    ", Address: " + rsEmployee.getString("ADDRESS") +
	                    ", Salary: " + rsEmployee.getDouble("SALARY")
	            );
	        }
	        rsEmployee.close();	
		} catch (SQLException e)
		{
			System.err.println("Fehler beim Lesen der Datenbank: " + e.getMessage());
		}
	}
	
	public static void updateDatabase() {
	    try {
	        String updateCompany = "UPDATE COMPANY SET NAME = 'Müller & Co GmbH', SALARY = 52000.00 WHERE ID = 1;";
	        stmt.executeUpdate(updateCompany);

	        String updateDepartment = "UPDATE DEPARTMENT SET NAME = 'Marketing', AGE = 4 WHERE ID = 1;";
	        stmt.executeUpdate(updateDepartment);

	        String updateEmployee = "UPDATE EMPLOYEE SET SALARY = 57000.00 WHERE ID = 1;";
	        stmt.executeUpdate(updateEmployee);

	        System.out.println("Database updated successfully");
	    } catch (SQLException e) {
	        System.err.println("Fehler beim Aktualisieren der Datenbank: " + e.getMessage());
	    }
	}
	
	public static void updateDatabaseWithCondition() {
	    try {
	        String updateCompany = "UPDATE COMPANY SET SALARY = SALARY + 5000 WHERE ID = 1 OR NAME = 'Tech AG';";
	        stmt.executeUpdate(updateCompany);
 
	        String updateDepartment = "UPDATE DEPARTMENT SET AGE = 5 WHERE ID = 1 AND NAME = 'Marketing';";
	        stmt.executeUpdate(updateDepartment);

	        String updateEmployee = "UPDATE EMPLOYEE SET SALARY = SALARY + 2000 WHERE ID = 1 OR SALARY < 50000;";
	        stmt.executeUpdate(updateEmployee);

	        System.out.println("Database updated successfully with AND/OR conditions");
	    } catch (SQLException e) {
	        System.err.println("Fehler beim Aktualisieren mit Bedingungen: " + e.getMessage());
	    }
	}
	
	public static void main(String[] args)
	{
		openDatabase();
		readDatabase();
		createTables();
		insertValues();
		deletDatabase();
	}
}
