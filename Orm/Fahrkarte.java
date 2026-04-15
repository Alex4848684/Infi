package Orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Beispiel-Fahrkarte-Objekt, das von der DAO und anderen Beispielklassen auf die Festplatte persistiert wird.
 */
@DatabaseTable(tableName = "fahrkarten")
public class Fahrkarte {

    // Für QueryBuilder, um die Felder finden zu können
    public static final String Fahrkarten_kaeufer = "kaeufer";
    public static final String Fahrkarten_kontrolliert = "kontrolliert";
    public static final String Fahrkarten_wagon = "wagon";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = Fahrkarten_kaeufer, canBeNull = false)
    private String kaeufer;

    @DatabaseField(columnName = Fahrkarten_kontrolliert)
    private boolean kontrolliert = false; // Standard: nicht kontrolliert

    @DatabaseField(columnName = Fahrkarten_wagon, canBeNull = false)
    private int wagon;

    // Alle persistierten Klassen müssen einen no-arg-Konstruktor mit mindestens Paket-Sichtbarkeit definieren
    Fahrkarte() {
    }

    public Fahrkarte(String kaeufer) {
        this.kaeufer = kaeufer;
        this.wagon = 1; // Standard-Wagon
    }

    public Fahrkarte(String kaeufer, boolean kontrolliert) {
        this.kaeufer = kaeufer;
        this.kontrolliert = kontrolliert;
        this.wagon = 1; // Standard-Wagon
    }

    public Fahrkarte(String kaeufer, int wagon) {
        this.kaeufer = kaeufer;
        this.wagon = wagon;
    }

    public Fahrkarte(String kaeufer, boolean kontrolliert, int wagon) {
        this.kaeufer = kaeufer;
        this.kontrolliert = kontrolliert;
        this.wagon = wagon;
    }

    public int getId() {
        return id;
    }

    public String getKaeufer() {
        return kaeufer;
    }

    public void setKaeufer(String kaeufer) {
        this.kaeufer = kaeufer;
    }

    public boolean isKontrolliert() {
        return kontrolliert;
    }

    public void setKontrolliert(boolean kontrolliert) {
        this.kontrolliert = kontrolliert;
    }

    public int getWagon() {
        return wagon;
    }

    public void setWagon(int wagon) {
        this.wagon = wagon;
    }

    @Override
    public int hashCode() {
        return kaeufer.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        return kaeufer.equals(((Fahrkarte) other).kaeufer);
    }
}