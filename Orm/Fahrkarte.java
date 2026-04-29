package Orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "fahrkarten")
public class Fahrkarte {

    public static final String Fahrkarten_fahrgast = "fahrgast_id";
    public static final String Fahrkarten_fahrt = "fahrt_id";
    public static final String Fahrkarten_kontrolliert = "kontrolliert";
    public static final String Fahrkarten_wagon = "wagon";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = Fahrkarten_fahrgast, canBeNull = false, foreign = true, unique = true)
    private Fahrgast fahrgast;

    @DatabaseField(columnName = Fahrkarten_fahrt, canBeNull = false, foreign = true)
    private Fahrt fahrt;

    @DatabaseField(columnName = Fahrkarten_kontrolliert)
    private boolean kontrolliert = false;

    @DatabaseField(columnName = Fahrkarten_wagon, canBeNull = false)
    private int wagon;

    @DatabaseField
    private double preis;

    Fahrkarte() {
    }

    public Fahrkarte(Fahrgast fahrgast, Fahrt fahrt) {
        this.fahrgast = fahrgast;
        this.fahrt = fahrt;
        this.wagon = 1;
        berechnePreis();
    }

    public Fahrkarte(Fahrgast fahrgast, Fahrt fahrt, int wagon) {
        this.fahrgast = fahrgast;
        this.fahrt = fahrt;
        this.wagon = wagon;
        berechnePreis();
    }

    public Fahrkarte(Fahrgast fahrgast, Fahrt fahrt, boolean kontrolliert, int wagon) {
        this.fahrgast = fahrgast;
        this.fahrt = fahrt;
        this.kontrolliert = kontrolliert;
        this.wagon = wagon;
        berechnePreis();
    }

    private void berechnePreis() {
        if (this.fahrt != null) {
            // Beispiel: 0,15 Euro pro Minute Fahrtzeit
            this.preis = this.fahrt.getDauerMinuten() * 0.15;
        }
    }

    public int getId() {
        return id;
    }

    public Fahrgast getFahrgast() {
        return fahrgast;
    }

    public Fahrt getFahrt() {
        return fahrt;
    }

    public int getFahrgastId() {
        return (fahrgast != null) ? fahrgast.getId() : 0;
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

    public double getPreis() {
        return preis;
    }

    @Override
    public int hashCode() {
        return (fahrgast != null) ? fahrgast.getId() : 0;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Fahrkarte otherCard = (Fahrkarte) other;
        return getFahrgastId() == otherCard.getFahrgastId();
    }

    @Override
    public String toString() {
        return String.format("Fahrkarte{id=%d, fahrgast=%s, fahrt=%s, preis=%.2f€, kontrolliert=%b, wagon=%d}",
                id, 
                (fahrgast != null ? fahrgast.getName() : "null"), 
                (fahrt != null ? fahrt.getName() : "null"), 
                preis, 
                kontrolliert, 
                wagon);
    }
}
