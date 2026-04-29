package Orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "fahrten")
public class Fahrt {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private int dauerMinuten;

    Fahrt() {
    }

    public Fahrt(String name, int dauerMinuten) {
        this.name = name;
        this.dauerMinuten = dauerMinuten;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDauerMinuten() {
        return dauerMinuten;
    }

    @Override
    public String toString() {
        return "Fahrt{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dauer=" + dauerMinuten + " min" +
                '}';
    }
}
