package entity;

import javax.crypto.Mac;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "employees")
public class Driver implements Serializable, Comparable<Driver> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "drivers")
    private Set<Qualification> qualifications;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private TransportCompany company;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private Set<Logistics> carriages;

    @Column(name = "salary", nullable = false)
    private BigDecimal salary;

//    @Transient
//    private boolean isAvailable;


    public Driver() {
    }

    public Driver(String name, Set<Qualification> qualifications, TransportCompany company, BigDecimal salary) {
        this.name = name;
        this.qualifications = qualifications;
        this.company = company;
        this.carriages = new TreeSet<>();
        this.salary = salary;
        //this.isAvailable = true;
    }

    public Driver(long id, String name, Set<Qualification> qualifications,
                  TransportCompany company, BigDecimal salary) {
        this.id = id;
        this.name = name;
        this.qualifications = qualifications;
        this.company = company;
        this.salary = salary;
        //this.isAvailable = true;
        this.carriages = new TreeSet<>();;
    }

    //Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Qualification> getQualifications() {
        return qualifications;
    }

    public TransportCompany getCompany() {
        return company;
    }

    public Set<Logistics> getCarriages() {
        return carriages;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    //public boolean isAvailable() {
        //return isAvailable;
    //}

    //Setters
    public void setCarriage(Set<Logistics> carriages) {
        this.carriages = carriages;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setQualifications(Set<Qualification> qualifications) {
        this.qualifications = qualifications;
    }

    public void addQualification(Qualification q) {
        this.qualifications.add(q);
    }


    public void addPackage(Logistics logistics) {
        this.carriages.add(logistics);
    }


    @Override
    public int compareTo(Driver d) {
        return this.getName().compareTo(d.getName());
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", company=" + company +
                ", salary=" + salary +
                "orders " + carriages.size()+
                //", isAvailable=" + isAvailable +
                '}';
    }


}
