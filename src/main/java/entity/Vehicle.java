package entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "vehicles")
public class Vehicle implements Comparable<Vehicle>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "reg_number", nullable = false, unique = true)
    private String plateNumber;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", nullable = false)
    private VehicleType type;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private TransportCompany company;

    public Vehicle() {
    }

    public Vehicle(long id) {
        this.id = id;
    }

    public Vehicle(long id, String name, String plateNumber, VehicleType type, TransportCompany company) {
        this.id = id;
        this.name = name;
        this.plateNumber = plateNumber;
        this.type = type;
        this.company = company;
    }

    public long getId() {
        return id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public VehicleType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public TransportCompany getCompany() {
        return company;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public void setCompany(TransportCompany company) {
        this.company = company;
    }

    @Override
    public int compareTo(Vehicle v) {
        return this.getPlateNumber().compareTo(v.getPlateNumber());
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", type=" + type +
                ", company=" + company +
                '}';
    }
}
