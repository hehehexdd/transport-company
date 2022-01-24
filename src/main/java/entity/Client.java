package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "clients")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<TransportCompany> companies;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Logistics> orders;

    public Client() {
    }

    public Client(String name, String email) {
        this.name = name;
        this.email = email;
        this.companies = new TreeSet<>();
        this.orders = new TreeSet<>();
    }

    public Client(long id, String name, String email, Set<TransportCompany> companies, Set<Logistics>  orders) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.companies = companies;
        this.orders = orders;
    }

    public void addOrders(Logistics logistics) {
        this.orders.add(logistics);
    }

    public void addCompany(TransportCompany company) {
        this.companies.add(company);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
