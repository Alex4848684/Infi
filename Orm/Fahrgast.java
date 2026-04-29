package Orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Fahrgast-Objekt, das von der DAO persistiert wird.
 * Ein Fahrgast kann nur eine Fahrkarte haben.
 */
@DatabaseTable(tableName = "fahrgaeste")
public class Fahrgast {

    public static final String Fahrgast_name = "name";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = Fahrgast_name, canBeNull = false, unique = true)
    private String name;

    Fahrgast() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public Fahrgast(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Fahrgast otherFahrgast = (Fahrgast) other;
        return name != null ? name.equals(otherFahrgast.name) : otherFahrgast.name == null;
    }

    @Override
    public String toString() {
        return "Fahrgast{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
