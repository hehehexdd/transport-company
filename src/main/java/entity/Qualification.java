package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "qualification")
public class Qualification implements Comparable<Qualification>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private QualificationType type;

    @ManyToMany
    private Set<Driver> drivers;


    public Qualification(QualificationType type) {
        this.type = type;
        this.drivers = new TreeSet<>();
    }

    public Qualification(QualificationType type, Set<Driver> drivers) {
        this.type = type;
        this.drivers = drivers;
    }

    public long getId() {
        return id;
    }

    public QualificationType getType() {
        return type;
    }

    public void addDriver(Driver driver) {
        this.drivers.add(driver);
    }

    @Override
    public int compareTo(Qualification q) {
        return this.getType().compareTo(q.getType());
    }

    @Override
    public String toString() {
        return "Qualification{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }
}
