<<<<<<< HEAD
package Orm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Main sample routine to show how to do basic operations with the package.
 *
 * <p>
 * <b>NOTE:</b> We use asserts in a couple of places to verify the results but if this were actual production code, we
 * would have proper error handling.
 * </p>
 */
public class Orm_test {

    // we are using MySQL database
    private final static String DATABASE_URL = "jdbc:mysql://localhost:3306/fahrkarten?serverTimezone=UTC";
    private final static String DB_USER = "root";
    private final static String DB_PASSWORD = "RySj3b481";

    private Dao<Fahrkarte, Integer> fahrkarteDao;

    public static void main(String[] args) throws Exception {
        // turn our static method into an instance of Main
        new Orm_test().doMain(args);
    }

    private void doMain(String[] args) throws Exception {
        ConnectionSource connectionSource = null;
        try {
            // create our data-source for the database
            connectionSource = new JdbcConnectionSource(DATABASE_URL, DB_USER, DB_PASSWORD);
            // setup our database and DAOs
            setupDatabase(connectionSource);
            // read and write some data
            readWriteData();
            // show free seats in wagon 1 after first entry
            displayFreeSeatsInWagon(1);
            // do a bunch of bulk operations
            readWriteBunch();
            // fill wagon 1 to capacity
            fillWagonToCapacity(1);
            // display wagon statistics
            displayWagonStatistics();
            // check if wagons are full
            checkWagonCapacity();
            System.out.println("\n\nIt seems to have worked\n\n");
        } finally {
            // destroy the data source which should close underlying connections
            if (connectionSource != null) {
                connectionSource.close();
            }
        }
    }

    /**
     * Setup our database and DAOs
     */
    private void setupDatabase(ConnectionSource connectionSource) throws Exception {

        fahrkarteDao = DaoManager.createDao(connectionSource, Fahrkarte.class);

        // drop and recreate the table to start fresh
        TableUtils.dropTable(connectionSource, Fahrkarte.class, true);
        TableUtils.createTable(connectionSource, Fahrkarte.class);
    }

    /**
     * Read and write some example data.
     */
    private void readWriteData() throws Exception {
        // create an instance of Fahrkarte
        String kaeufer = "Jim Coakley";
        Fahrkarte fahrkarte = new Fahrkarte(kaeufer, 1); // Wagon 1

        // persist the fahrkarte object to the database
        fahrkarteDao.create(fahrkarte);
        int id = fahrkarte.getId();
        verifyDb(id, fahrkarte);

        // mark as kontrolliert
        fahrkarte.setKontrolliert(true);
        // update the database after changing the object
        fahrkarteDao.update(fahrkarte);
        verifyDb(id, fahrkarte);

        // query for all items in the database
        List<Fahrkarte> fahrkarten = fahrkarteDao.queryForAll();
        assertEquals(1, fahrkarten.size(), "Should have found 1 fahrkarte matching our query");
        verifyFahrkarte(fahrkarte, fahrkarten.get(0));

        // loop through items in the database
        int fahrkarteC = 0;
        for (Fahrkarte fahrkarte2 : fahrkarteDao) {
            verifyFahrkarte(fahrkarte, fahrkarte2);
            fahrkarteC++;
        }
        assertEquals(1, fahrkarteC, "Should have found 1 fahrkarte in for loop");

        // construct a query using the QueryBuilder
        QueryBuilder<Fahrkarte, Integer> statementBuilder = fahrkarteDao.queryBuilder();
        // shouldn't find anything: kaeufer LIKE 'hello" does not match our fahrkarte
        statementBuilder.where().like(Fahrkarte.Fahrkarten_kaeufer, "hello");
        fahrkarten = fahrkarteDao.query(statementBuilder.prepare());
        assertEquals(0, fahrkarten.size(), "Should not have found any fahrkarten matching our query");

        // should find our fahrkarte: kaeufer LIKE 'Jim%' should match our fahrkarte
        statementBuilder.where().like(Fahrkarte.Fahrkarten_kaeufer, kaeufer.substring(0, 3) + "%");
        fahrkarten = fahrkarteDao.query(statementBuilder.prepare());
        assertEquals(1, fahrkarten.size(), "Should have found 1 fahrkarte matching our query");
        verifyFahrkarte(fahrkarte, fahrkarten.get(0));
    }

    /**
     * Example of reading and writing a large(r) number of objects.
     */
    private void readWriteBunch() throws Exception {

        Map<String, Fahrkarte> fahrkarten = new HashMap<String, Fahrkarte>();
        java.util.Random random = new java.util.Random(); // Random-Generator

        for (int i = 1; i <= 100; i++) {
            String kaeufer = "Fahrgast " + i; // Fahrgastnamen
            int wagon = (i % 7) + 1; // Verteile auf Wagons 1-7
            boolean kontrolliert = random.nextBoolean(); // Zufällig true oder false
            Fahrkarte fahrkarte = new Fahrkarte(kaeufer, kontrolliert, wagon);
            // persist the fahrkarte object to the database, it should return 1
            fahrkarteDao.create(fahrkarte);
            fahrkarten.put(kaeufer, fahrkarte);
        }

        // query for all items in the database
        List<Fahrkarte> all = fahrkarteDao.queryForAll();
        // Filter to only include the fahrkarten created in this method (by kaeufer, not Jim Coakley)
        List<Fahrkarte> batchFahrkarten = new java.util.ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            Fahrkarte fahrkarte = all.get(i);
            if (fahrkarten.containsKey(fahrkarte.getKaeufer())) {
                batchFahrkarten.add(fahrkarte);
            }
        }
        assertEquals(fahrkarten.size(), batchFahrkarten.size(), "Should have found same number of fahrkarten in map");
        for (Fahrkarte fahrkarte : batchFahrkarten) {
            assertTrue(fahrkarten.containsValue(fahrkarte), "Should have found fahrkarte in map");
            verifyFahrkarte(fahrkarten.get(fahrkarte.getKaeufer()), fahrkarte);
        }

        // loop through items in the database
        int fahrkarteC = 0;
        for (Fahrkarte fahrkarte : fahrkarteDao) {
            if (fahrkarten.containsKey(fahrkarte.getKaeufer())) {
                assertTrue(fahrkarten.containsValue(fahrkarte), "Should have found fahrkarte in map");
                verifyFahrkarte(fahrkarten.get(fahrkarte.getKaeufer()), fahrkarte);
                fahrkarteC++;
            }
        }
        assertEquals(fahrkarten.size(), fahrkarteC, "Should have found the right number of fahrkarten in for loop");
    }

    /**
     * Check if wagons are full (40 seats per wagon, 7 wagons).
     */
    private void checkWagonCapacity() throws Exception {
        for (int wagon = 1; wagon <= 7; wagon++) {
            boolean isFull = isWagonFull(wagon);
            System.out.println("Wagon " + wagon + " is " + (isFull ? "full" : "not full"));
        }
    }

    /**
     * Check if a wagon is full.
     */
    private boolean isWagonFull(int wagonId) throws SQLException {
        QueryBuilder<Fahrkarte, Integer> qb = fahrkarteDao.queryBuilder();
        qb.setCountOf(true); // Erforderlich für countOf
        qb.where().eq(Fahrkarte.Fahrkarten_wagon, wagonId);
        long count = fahrkarteDao.countOf(qb.prepare());
        return count >= 40;
    }

    /**
     * Verify that the fahrkarte stored in the database was the same as the expected object.
     */
    private void verifyDb(int id, Fahrkarte expected) throws SQLException, Exception {
        // make sure we can read it back
        Fahrkarte fahrkarte2 = fahrkarteDao.queryForId(id);
        if (fahrkarte2 == null) {
            throw new Exception("Should have found id '" + id + "' in the database");
        }
        verifyFahrkarte(expected, fahrkarte2);
    }

    /**
     * Verify that the fahrkarte is the same as expected.
     */
    private static void verifyFahrkarte(Fahrkarte expected, Fahrkarte fahrkarte2) {
        assertEquals(expected, fahrkarte2, "expected fahrkarte does not equal fahrkarte");
        assertEquals(expected.isKontrolliert(), fahrkarte2.isKontrolliert(), "expected kontrolliert does not equal fahrkarte kontrolliert");
        assertEquals(expected.getWagon(), fahrkarte2.getWagon(), "expected wagon does not equal fahrkarte wagon");
    }

    /**
     * Display wagon statistics - show how many passengers are in each wagon
     */
    private void displayWagonStatistics() throws SQLException {
        System.out.println("\n=== WAGON STATISTICS ===\n");

        for (int wagon = 1; wagon <= 7; wagon++) {
            QueryBuilder<Fahrkarte, Integer> qb = fahrkarteDao.queryBuilder();
            qb.setCountOf(true);
            qb.where().eq(Fahrkarte.Fahrkarten_wagon, wagon);
            long count = fahrkarteDao.countOf(qb.prepare());

            int capacity = 40;
            double percentage = (double) count / capacity * 100;
            String status = count >= capacity ? "VOLL" : "NICHT VOLL";

            System.out.printf("Wagon %d: %d / %d Plätze (%.1f%%) - %s%n",
                    wagon, count, capacity, percentage, status);
        }
        System.out.println();
    }

    /**
     * Display how many seats are still free in a specific wagon
     */
    private void displayFreeSeatsInWagon(int wagonId) throws SQLException {
        QueryBuilder<Fahrkarte, Integer> qb = fahrkarteDao.queryBuilder();
        qb.setCountOf(true);
        qb.where().eq(Fahrkarte.Fahrkarten_wagon, wagonId);
        long count = fahrkarteDao.countOf(qb.prepare());

        int capacity = 40;
        int freeSeats = capacity - (int) count;

        System.out.println("Wagon " + wagonId + " hat noch " + freeSeats + " freie Plätze.\n");
    }

    /**
     * Fill a specific wagon to its capacity (40 seats)
     */
    private void fillWagonToCapacity(int wagonId) throws SQLException {
        QueryBuilder<Fahrkarte, Integer> qb = fahrkarteDao.queryBuilder();
        qb.setCountOf(true);
        qb.where().eq(Fahrkarte.Fahrkarten_wagon, wagonId);
        long currentCount = fahrkarteDao.countOf(qb.prepare());

        int capacity = 40;
        int seatsToAdd = capacity - (int) currentCount;

        if (seatsToAdd <= 0) {
            System.out.println("Wagon " + wagonId + " ist bereits voll!\n");
            return;
        }

        System.out.println("Fülle Wagon " + wagonId + " mit " + seatsToAdd + " zusätzlichen Fahrkarten...\n");

        java.util.Random random = new java.util.Random();
        for (int i = 1; i <= seatsToAdd; i++) {
            String kaeufer = "Zusatz-Fahrgast " + i + " (Wagon " + wagonId + ")";
            boolean kontrolliert = random.nextBoolean();
            Fahrkarte fahrkarte = new Fahrkarte(kaeufer, kontrolliert, wagonId);
            fahrkarteDao.create(fahrkarte);
        }

        System.out.println("Wagon " + wagonId + " ist jetzt voll!\n");
    }
=======
package Orm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Main sample routine to show how to do basic operations with the package.
 *
 * <p>
 * <b>NOTE:</b> We use asserts in a couple of places to verify the results but if this were actual production code, we
 * would have proper error handling.
 * </p>
 */
public class Orm_test {

    // we are using MySQL database
    private final static String DATABASE_URL = "jdbc:mysql://localhost:3306/account?serverTimezone=UTC";
    private final static String DB_USER = "root";
    private final static String DB_PASSWORD = "RySj3b481";

    private Dao<Account, Integer> accountDao;

    public static void main(String[] args) throws Exception {
        // turn our static method into an instance of Main
        new Orm_test().doMain(args);
    }

    private void doMain(String[] args) throws Exception {
        ConnectionSource connectionSource = null;
        try {
            // create our data-source for the database
            connectionSource = new JdbcConnectionSource(DATABASE_URL, DB_USER, DB_PASSWORD);
            // setup our database and DAOs
            setupDatabase(connectionSource);
            // read and write some data
            readWriteData();
            // do a bunch of bulk operations
            readWriteBunch();
            // show how to use the SelectArg object
            useSelectArgFeature();
            // show how to use the SelectArg object
            useTransactions(connectionSource);
            System.out.println("\n\nIt seems to have worked\n\n");
        } finally {
            // destroy the data source which should close underlying connections
            if (connectionSource != null) {
                connectionSource.close();
            }
        }
    }

    /**
     * Setup our database and DAOs
     */
    private void setupDatabase(ConnectionSource connectionSource) throws Exception {

        accountDao = DaoManager.createDao(connectionSource, Account.class);

        // drop and recreate the table to start fresh
        TableUtils.dropTable(connectionSource, Account.class, true);
        TableUtils.createTable(connectionSource, Account.class);
    }

    /**
     * Read and write some example data.
     */
    private void readWriteData() throws Exception {
        // create an instance of Account
        String name = "Jim Coakley";
        Account account = new Account(name);

        // persist the account object to the database
        accountDao.create(account);
        int id = account.getId();
        verifyDb(id, account);

        // assign a password
        account.setPassword("_secret");
        // update the database after changing the object
        accountDao.update(account);
        verifyDb(id, account);

        // query for all items in the database
        List<Account> accounts = accountDao.queryForAll();
        assertEquals(1, accounts.size(), "Should have found 1 account matching our query");
        verifyAccount(account, accounts.get(0));

        // loop through items in the database
        int accountC = 0;
        for (Account account2 : accountDao) {
            verifyAccount(account, account2);
            accountC++;
        }
        assertEquals(1, accountC, "Should have found 1 account in for loop");

        // construct a query using the QueryBuilder
        QueryBuilder<Account, Integer> statementBuilder = accountDao.queryBuilder();
        // shouldn't find anything: name LIKE 'hello" does not match our account
        statementBuilder.where().like(Account.NAME_FIELD_NAME, "hello");
        accounts = accountDao.query(statementBuilder.prepare());
        assertEquals(0, accounts.size(), "Should not have found any accounts matching our query");

        // should find our account: name LIKE 'Jim%' should match our account
        statementBuilder.where().like(Account.NAME_FIELD_NAME, name.substring(0, 3) + "%");
        accounts = accountDao.query(statementBuilder.prepare());
        assertEquals(1, accounts.size(), "Should have found 1 account matching our query");
        verifyAccount(account, accounts.get(0));
    }

    /**
     * Example of reading and writing a large(r) number of objects.
     */
    private void readWriteBunch() throws Exception {

        Map<String, Account> accounts = new HashMap<String, Account>();
        for (int i = 1; i <= 100; i++) {
            String name = Integer.toString(i);
            Account account = new Account(name);
            // persist the account object to the database, it should return 1
            accountDao.create(account);
            accounts.put(name, account);
        }

        // query for all items in the database
        List<Account> all = accountDao.queryForAll();
        // Filter to only include the accounts created in this method (by name, not Jim Coakley)
        List<Account> batchAccounts = new java.util.ArrayList<>();
        for (Account account : all) {
            if (accounts.containsKey(account.getName())) {
                batchAccounts.add(account);
            }
        }
        assertEquals(accounts.size(), batchAccounts.size(), "Should have found same number of accounts in map");
        for (Account account : batchAccounts) {
            assertTrue(accounts.containsValue(account), "Should have found account in map");
            verifyAccount(accounts.get(account.getName()), account);
        }

        // loop through items in the database
        int accountC = 0;
        for (Account account : accountDao) {
            if (accounts.containsKey(account.getName())) {
                assertTrue(accounts.containsValue(account), "Should have found account in map");
                verifyAccount(accounts.get(account.getName()), account);
                accountC++;
            }
        }
        assertEquals(accounts.size(), accountC, "Should have found the right number of accounts in for loop");
    }

    /**
     * Example of created a query with a ? argument using the {@link SelectArg} object. You then can set the value of
     * this object at a later time.
     */
    private void useSelectArgFeature() throws Exception {

        String name1 = "foo";
        String name2 = "bar";
        String name3 = "baz";
        assertEquals(1, accountDao.create(new Account(name1)));
        assertEquals(1, accountDao.create(new Account(name2)));
        assertEquals(1, accountDao.create(new Account(name3)));

        QueryBuilder<Account, Integer> statementBuilder = accountDao.queryBuilder();
        SelectArg selectArg = new SelectArg();
        // build a query with the WHERE clause set to 'name = ?'
        statementBuilder.where().like(Account.NAME_FIELD_NAME, selectArg);
        PreparedQuery<Account> preparedQuery = statementBuilder.prepare();

        // now we can set the select arg (?) and run the query
        selectArg.setValue(name1);
        List<Account> results = accountDao.query(preparedQuery);
        assertEquals(1, results.size(), "Should have found 1 account matching our query");
        assertEquals(name1, results.get(0).getName());

        selectArg.setValue(name2);
        results = accountDao.query(preparedQuery);
        assertEquals(1, results.size(), "Should have found 1 account matching our query");
        assertEquals(name2, results.get(0).getName());

        selectArg.setValue(name3);
        results = accountDao.query(preparedQuery);
        assertEquals(1, results.size(), "Should have found 1 account matching our query");
        assertEquals(name3, results.get(0).getName());
    }

    /**
     * Example of created a query with a ? argument using the {@link SelectArg} object. You then can set the value of
     * this object at a later time.
     */
    private void useTransactions(ConnectionSource connectionSource) throws Exception {
        String name = "trans1";
        final Account account = new Account(name);
        assertEquals(1, accountDao.create(account));

        TransactionManager transactionManager = new TransactionManager(connectionSource);
        try {
            // try something in a transaction
            transactionManager.callInTransaction(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    // we do the delete
                    assertEquals(1, accountDao.delete(account));
                    assertNull(accountDao.queryForId(account.getId()));
                    // but then (as an example) we throw an exception which rolls back the delete
                    throw new Exception("We throw to roll back!!");
                }
            });
            fail("This should have thrown");
        } catch (SQLException e) {
            // expected
        }

        assertNotNull(accountDao.queryForId(account.getId()));
    }

    /**
     * Verify that the account stored in the database was the same as the expected object.
     */
    private void verifyDb(int id, Account expected) throws SQLException, Exception {
        // make sure we can read it back
        Account account2 = accountDao.queryForId(id);
        if (account2 == null) {
            throw new Exception("Should have found id '" + id + "' in the database");
        }
        verifyAccount(expected, account2);
    }

    /**
     * Verify that the account is the same as expected.
     */
    private static void verifyAccount(Account expected, Account account2) {
        assertEquals(expected, account2, "expected account does not equal account");
        assertEquals(expected.getPassword(), account2.getPassword(), "expected password does not equal account password");
    }
>>>>>>> origin/main
}