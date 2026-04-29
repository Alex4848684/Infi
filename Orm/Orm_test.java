package Orm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Testklasse für das ORM-System der Fahrkartenverwaltung mit Fahrten.
 */
public class Orm_test {

    private final static String DATABASE_URL = "jdbc:mysql://localhost:3306/fahrkarten?serverTimezone=UTC";
    private final static String DB_USER = "root";
    private final static String DB_PASSWORD = "RySj3b481";

    private Dao<Fahrgast, Integer> fahrgastDao;
    private Dao<Fahrkarte, Integer> fahrkarteDao;
    private Dao<Fahrt, Integer> fahrtDao;
    
    private List<Fahrt> verfuegbareFahrten = new ArrayList<>();

    public static void main(String[] args) {
        try {
            new Orm_test().doMain();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doMain() throws Exception {
        ConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcConnectionSource(DATABASE_URL, DB_USER, DB_PASSWORD);
            setupDatabase(connectionSource);
            
            // 5 Fahrten erstellen
            erzeugeFahrten();
            
            // Test: Ein Fahrgast darf nur eine Fahrkarte haben
            testEinTicketProFahrgast();
            
            readWriteData();
            displayFreeSeatsInWagon(1);
            readWriteBunch();
            fillWagonToCapacity(1);
            displayWagonStatistics();
            
            System.out.println("\n\nAlle Tests erfolgreich abgeschlossen!\n\n");
        } finally {
            if (connectionSource != null) {
                connectionSource.close();
            }
        }
    }

    private void setupDatabase(ConnectionSource connectionSource) throws Exception {
        fahrgastDao = DaoManager.createDao(connectionSource, Fahrgast.class);
        fahrkarteDao = DaoManager.createDao(connectionSource, Fahrkarte.class);
        fahrtDao = DaoManager.createDao(connectionSource, Fahrt.class);

        TableUtils.dropTable(connectionSource, Fahrkarte.class, true);
        TableUtils.dropTable(connectionSource, Fahrgast.class, true);
        TableUtils.dropTable(connectionSource, Fahrt.class, true);
        
        TableUtils.createTable(connectionSource, Fahrt.class);
        TableUtils.createTable(connectionSource, Fahrgast.class);
        TableUtils.createTable(connectionSource, Fahrkarte.class);
        System.out.println("Datenbank-Tabellen wurden neu erstellt.");
    }

    private void erzeugeFahrten() throws SQLException {
        String[] namen = {"Wien - Salzburg", "Graz - Linz", "Innsbruck - Bregenz", "Villach - Klagenfurt", "Wien - München"};
        int[] dauern = {150, 120, 90, 45, 240};

        System.out.println("\n--- Erzeuge 5 verfügbare Fahrten ---");
        for (int i = 0; i < 5; i++) {
            Fahrt fahrt = new Fahrt(namen[i], dauern[i]);
            fahrtDao.create(fahrt);
            verfuegbareFahrten.add(fahrt);
            System.out.println("Erstellt: " + fahrt);
        }
    }

    private Fahrt getRandomFahrt() {
        return verfuegbareFahrten.get(new Random().nextInt(verfuegbareFahrten.size()));
    }

    private void testEinTicketProFahrgast() throws Exception {
        System.out.println("\n--- Test: Ein Ticket pro Fahrgast ---");
        Fahrgast fahrgast = new Fahrgast("Max Mustermann");
        fahrgastDao.create(fahrgast);

        Fahrt fahrt = getRandomFahrt();
        Fahrkarte k1 = new Fahrkarte(fahrgast, fahrt, 1);
        fahrkarteDao.create(k1);
        System.out.println("Erste Fahrkarte für " + fahrgast.getName() + " (Fahrt: " + fahrt.getName() + ") erstellt.");

        Fahrkarte k2 = new Fahrkarte(fahrgast, fahrt, 2);
        try {
            fahrkarteDao.create(k2);
            System.err.println("!!! FEHLER: Ein Fahrgast konnte eine zweite Fahrkarte kaufen!");
        } catch (SQLException e) {
            System.out.println("ERFOLG: Zweiter Kaufversuch wurde wie erwartet blockiert.");
        }
    }

    private void readWriteData() throws Exception {
        Fahrgast fahrgast = new Fahrgast("Jim Coakley");
        fahrgastDao.create(fahrgast);

        Fahrt fahrt = getRandomFahrt();
        Fahrkarte fahrkarte = new Fahrkarte(fahrgast, fahrt, 1);
        fahrkarteDao.create(fahrkarte);
        
        fahrkarte.setKontrolliert(true);
        fahrkarteDao.update(fahrkarte);

        List<Fahrkarte> fahrkarten = fahrkarteDao.queryForAll();
        System.out.println("Basis-Test: Fahrkarte für " + fahrgast.getName() + " auf Fahrt " + fahrt.getName() + " erstellt.");
    }

    private void readWriteBunch() throws Exception {
        Random random = new Random();
        int count = 20;
        System.out.println("\nErstelle " + count + " weitere Fahrgäste mit zufälligen Fahrten...");
        for (int i = 1; i <= count; i++) {
            Fahrgast fahrgast = new Fahrgast("Fahrgast " + i);
            fahrgastDao.create(fahrgast);

            Fahrt fahrt = getRandomFahrt();
            int wagon = (i % 7) + 1;
            Fahrkarte fahrkarte = new Fahrkarte(fahrgast, fahrt, random.nextBoolean(), wagon);
            fahrkarteDao.create(fahrkarte);
        }
    }

    private void displayWagonStatistics() throws SQLException {
        System.out.println("\n=== WAGON STATISTICS ===");
        for (int wagon = 1; wagon <= 7; wagon++) {
            QueryBuilder<Fahrkarte, Integer> qb = fahrkarteDao.queryBuilder();
            qb.setCountOf(true);
            qb.where().eq(Fahrkarte.Fahrkarten_wagon, wagon);
            long count = fahrkarteDao.countOf(qb.prepare());
            System.out.printf("Wagon %d: %d Plätze belegt%n", wagon, count);
        }
    }

    private void displayFreeSeatsInWagon(int wagonId) throws SQLException {
        QueryBuilder<Fahrkarte, Integer> qb = fahrkarteDao.queryBuilder();
        qb.setCountOf(true);
        qb.where().eq(Fahrkarte.Fahrkarten_wagon, wagonId);
        long count = fahrkarteDao.countOf(qb.prepare());
        System.out.println("\nWagon " + wagonId + " hat noch " + (40 - (int) count) + " freie Plätze.");
    }

    private void fillWagonToCapacity(int wagonId) throws Exception {
        QueryBuilder<Fahrkarte, Integer> qb = fahrkarteDao.queryBuilder();
        qb.setCountOf(true);
        qb.where().eq(Fahrkarte.Fahrkarten_wagon, wagonId);
        long currentCount = fahrkarteDao.countOf(qb.prepare());
        int seatsToAdd = 40 - (int) currentCount;

        if (seatsToAdd > 0) {
            System.out.println("\nFülle Wagon " + wagonId + " auf 40 Plätze auf...");
            for (int i = 1; i <= seatsToAdd; i++) {
                Fahrgast fahrgast = new Fahrgast("Zusatz-Fahrgast " + i + " (W" + wagonId + ")");
                fahrgastDao.create(fahrgast);
                fahrkarteDao.create(new Fahrkarte(fahrgast, getRandomFahrt(), wagonId));
            }
        }
    }
}
