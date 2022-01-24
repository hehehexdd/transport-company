package entity;

import com.mysql.cj.log.Log;

import javax.persistence.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

@Entity
@Table(name = "logistics")
public class Logistics implements Serializable, Comparable<Logistics>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LogisticsType type;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private TransportCompany company;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "weight", nullable = false)
    private String weight;

    @Column(name = "from_dest", nullable = false)
    private String from;

    @Column(name = "to_dest", nullable = false)
    private String destination;

    @Column(name = "date_sent", nullable = false)
    private LocalDate date;

    @Column(name = "eta", nullable = false)
    private LocalDate eta;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "isPaid", nullable = false)
    private boolean isPaid;


    public Logistics() {
    }

    public Logistics(long id,
                     LogisticsType type,
                     TransportCompany company,
                     Client client,
                     String weight,
                     String from,
                     String to,
                     LocalDate date,
                     LocalDate eta,
                     BigDecimal price,
                     boolean isPaid) {
        this.id = id;
        this.type = type;
        this.company = company;
        //this.setDriver();
        this.client = client;
        this.weight = weight;
        this.from = from;
        this.destination = to;
        this.date = date;
        this.eta = eta;
        this.price = price;
        this.isPaid = isPaid;
        this.name = "Package #" + this.getCompany().getPackageID();
        this.company.incrPackageID();
    }

    public Logistics(LogisticsType type,
                     TransportCompany company,
                     Driver driver,
                     Client client,
                     String weight,
                     String from,
                     String destination,
                     LocalDate date,
                     LocalDate eta,
                     BigDecimal price,
                     boolean isPaid) {
        this.type = type;
        this.company = company;
        this.driver = driver;
        this.client = client;
        this.weight = weight;
        this.from = from;
        this.destination = destination;
        this.date = date;
        this.eta = eta;
        this.price = price;
        this.isPaid = isPaid;
        this.name = "Package #" + this.getCompany().getPackageID();
        this.company.incrPackageID();
    }

    //Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LogisticsType getType() {
        return type;
    }

    public Driver getDriver() {
        return driver;
    }

    public TransportCompany getCompany() {
        return company;
    }

    public Client getClient() {
        return client;
    }

    public String getWeight() {
        return weight;
    }

    public String getFrom() {
        return from;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDate getEta() {
        return eta;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public BigDecimal getPrice() {
        return price;
    }

    //Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setType(LogisticsType type) {
        this.type = type;
    }

//    public void setDriver() {
//        ArrayList<String> tmp = new ArrayList<>();
//        Set<Driver> availableDrivers = this.company.getAvailableDrivers();
//        for (Driver driver : availableDrivers) {
//            driver.getQualifications().forEach((q) -> tmp.add(q.getType().toString()));
//            if(tmp.contains(this.getType().toString())) {
//                this.driver = driver;
//                System.out.println(driver);
//                break;
//            }
//        }
//    }

    public void setCompany(TransportCompany company) {
        this.company = company;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setEta(LocalDate eta) {
        this.eta = eta;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public void serialize() {
        String name = this.getName() + ".ser";

        File path = new File("./Carriages/" + name);
        try (FileOutputStream fos = new FileOutputStream(path);

             ObjectOutputStream outputStream = new ObjectOutputStream(fos)) {

            outputStream.writeObject(this);
        }
        catch (IOException e) {
            System.out.println("ERROR WHILE SERIALIZING!");
            e.printStackTrace();
        }
    }

    public static void deserialize(Logistics logistics) {
        Logistics tmp;
        String name = logistics.getName() + ".ser";
        try(FileInputStream file = new FileInputStream("./Carriages/"+name)) {
            ObjectInputStream in = new ObjectInputStream(file);
            tmp = (Logistics)in.readObject();
            System.out.println(tmp);
            in.close();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ERROR READING FROM FILE!");
        }
    }

    @Override
    public int compareTo(Logistics l) {
        return Long.compare(this.getId(), l.getId());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Logistics{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", driver=" + driver +
                ", company=" + company +
                ", client=" + client +
                ", weight=" + weight +
                ", from='" + from + '\'' +
                ", destination='" + destination + '\'' +
                ", date=" + date +
                ", eta=" + eta +
                ", price=" + price +
                ", isPaid=" + isPaid +
                '}';
    }
}
