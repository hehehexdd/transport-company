package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name="company"
//        ,uniqueConstraints =
//        @UniqueConstraint(
//                columnNames = {"name"}
//        )
)
public class TransportCompany implements Comparable<TransportCompany>, Serializable {

    @Transient
    private transient long packageID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "company_profit")
    private BigDecimal profit;

    @OneToMany(mappedBy = "company")
    private Set<Vehicle> vehicles;

    @OneToMany(mappedBy = "company")
    private Set<Driver> drivers;

    @ManyToMany(mappedBy = "companies", cascade = CascadeType.ALL)
    private Set<Client> clients;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Logistics> logistics;



    public TransportCompany() {
    }

    public TransportCompany(String name) {
        this.name = name;
        this.profit = BigDecimal.ZERO;
        this.vehicles = new TreeSet<>();
        this.drivers = new TreeSet<>();
        this.clients = new TreeSet<>();
        this.logistics = new TreeSet<>();
    }

    public TransportCompany(long id, String name) {
        this.id = id;
        this.name = name;
        this.profit = BigDecimal.ZERO;
        this.vehicles = new TreeSet<>();
        this.drivers = new TreeSet<>();
        this.clients = new TreeSet<>();
        this.logistics = new TreeSet<>();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public Set<Logistics> getLogistics() {
        return logistics;
    }

    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }

    public Set<Driver> getDrivers() {
        return drivers;
    }

//    public Set<Driver> getAvailableDrivers() {
//        Set<Driver> tmp = new LinkedHashSet<>();
//        for (Driver driver : this.getDrivers()) {
//            if (driver.isAvailable()) {
//                tmp.add(driver);
//            }
//        }
//        return tmp;
//    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public void setLogistics(Set<Logistics> logistics) {
        this.logistics = logistics;
    }

    public void deleteVehicle(Vehicle vehicle) {
        this.vehicles.remove(vehicle);
    }

    public long getPackageID() {
        return packageID;
    }

    public void incrPackageID() {
        this.packageID += 1;
    }

    @Override
    public int compareTo(TransportCompany c) {
        return this.getName().compareTo(c.getName());
    }

    @Override
    public String toString() {
        return "TransportCompany{" +
                "packageID=" + packageID +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", profit=" + profit +
                '}';
    }
}
