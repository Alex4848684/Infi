package Test_Uebung_2Semester.Uni_BSP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Tabelle_Erstellen2 {

    static final String URL = "jdbc:mysql://localhost:3306/test_uebung";
    static final String USER = "root";
    static final String PASSWORD = "RySj3b481";

    public static void main(String[] args) {

        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            // ------------------------
            // Tabellen löschen
            // ------------------------
            stmt.executeUpdate("DROP TABLE IF EXISTS enrollment");
            stmt.executeUpdate("DROP TABLE IF EXISTS course");
            stmt.executeUpdate("DROP TABLE IF EXISTS student");
            stmt.executeUpdate("DROP TABLE IF EXISTS professor");

            // ------------------------
            // Tabellen erstellen
            // ------------------------

            // Studenten
            stmt.executeUpdate(
                    "CREATE TABLE student (" +
                            "student_id INT PRIMARY KEY AUTO_INCREMENT," +
                            "name VARCHAR(100)," +
                            "semester INT" +
                            ")"
            );

            // Professoren (Self Join möglich)
            stmt.executeUpdate(
                    "CREATE TABLE professor (" +
                            "professor_id INT PRIMARY KEY AUTO_INCREMENT," +
                            "name VARCHAR(100)," +
                            "chef_id INT NULL," +
                            "FOREIGN KEY (chef_id) REFERENCES professor(professor_id) ON DELETE SET NULL" +
                            ")"
            );

            // Kurse
            stmt.executeUpdate(
                    "CREATE TABLE course (" +
                            "course_id INT PRIMARY KEY AUTO_INCREMENT," +
                            "course_name VARCHAR(100)," +
                            "professor_id INT," +
                            "FOREIGN KEY (professor_id) REFERENCES professor(professor_id)" +
                            ")"
            );

            // Einschreibungen (Join Tabelle)
            stmt.executeUpdate(
                    "CREATE TABLE enrollment (" +
                            "student_id INT," +
                            "course_id INT," +
                            "grade DOUBLE," +
                            "FOREIGN KEY (student_id) REFERENCES student(student_id)," +
                            "FOREIGN KEY (course_id) REFERENCES course(course_id)" +
                            ")"
            );

            System.out.println("Tabellen erstellt!");

            // ------------------------
            // Daten einfügen
            // ------------------------

            // Studenten
            stmt.executeUpdate("INSERT INTO student (name, semester) VALUES ('Anna',2)");
            stmt.executeUpdate("INSERT INTO student (name, semester) VALUES ('Ben',4)");
            stmt.executeUpdate("INSERT INTO student (name, semester) VALUES ('Clara',3)");
            stmt.executeUpdate("INSERT INTO student (name, semester) VALUES ('David',5)");
            stmt.executeUpdate("INSERT INTO student (name, semester) VALUES ('Eva',1)");

            // Professoren
            stmt.executeUpdate("INSERT INTO professor (name, chef_id) VALUES ('Prof. Müller',NULL)");
            stmt.executeUpdate("INSERT INTO professor (name, chef_id) VALUES ('Dr. Schmidt',1)");
            stmt.executeUpdate("INSERT INTO professor (name, chef_id) VALUES ('Dr. Weber',1)");
            stmt.executeUpdate("INSERT INTO professor (name, chef_id) VALUES ('Dr. Klein',2)");
            stmt.executeUpdate("INSERT INTO professor (name, chef_id) VALUES ('Dr. Fischer',2)");

            // Kurse
            stmt.executeUpdate("INSERT INTO course (course_name, professor_id) VALUES ('Datenbanken',2)");
            stmt.executeUpdate("INSERT INTO course (course_name, professor_id) VALUES ('Algorithmen',3)");
            stmt.executeUpdate("INSERT INTO course (course_name, professor_id) VALUES ('Mathematik',4)");
            stmt.executeUpdate("INSERT INTO course (course_name, professor_id) VALUES ('Physik',5)");
            stmt.executeUpdate("INSERT INTO course (course_name, professor_id) VALUES ('Programmierung',2)");

            // Einschreibungen
            stmt.executeUpdate("INSERT INTO enrollment VALUES (1,1,1.0)");
            stmt.executeUpdate("INSERT INTO enrollment VALUES (1,2,2.0)");
            stmt.executeUpdate("INSERT INTO enrollment VALUES (2,1,3.0)");
            stmt.executeUpdate("INSERT INTO enrollment VALUES (3,3,2.0)");
            stmt.executeUpdate("INSERT INTO enrollment VALUES (4,4,1.0)");
            stmt.executeUpdate("INSERT INTO enrollment VALUES (5,5,2.0)");
            stmt.executeUpdate("INSERT INTO enrollment VALUES (2,2,1.0)");

            System.out.println("Daten eingefügt!");

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}