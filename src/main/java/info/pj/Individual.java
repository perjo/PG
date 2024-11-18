package info.pj;

import com.google.common.base.Preconditions;

import java.util.Objects;
import java.util.UUID;

public class Individual {

    private final String id;

    private final String name;

    private final int year;
    private final Individual sire;
    private final Individual dam;

    public static Individual founder(String name, int year) {
        return new Individual(name, year,null, null);
    }

    private Individual(String name, int year, Individual sire, Individual dam) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
        this.year = year;
        this.sire = sire;
        this.dam = dam;
    }

    public static Individual descendant(String name, int year, Individual sire, Individual dam) {
        Preconditions.checkArgument(sire == null || sire.year < year);
        Preconditions.checkArgument(dam == null || dam.year < year);
        return new Individual(name, year, sire, dam);
    }

    public Individual getDam() {
        return dam;
    }

    public Individual getSire() {
        return sire;
    }

    public int getYear() {
        return year;
    }

    public boolean founder() {
        return sire == null && dam == null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Individual that = (Individual) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getName() {
        return name;
    }
}
