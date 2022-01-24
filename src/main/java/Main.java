import config.SessionFactoryUtil;
import dao.*;
import entity.*;
import jdk.swing.interop.SwingInterOpUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;


public class Main {

    public static void main(String[] args) {
        TransportCompany company = new TransportCompany("company_1");
        TransportCompany company2 = new TransportCompany("company_2");
        TransportCompany company3 = new TransportCompany("company_3");
        TransportCompany company4 = new TransportCompany("company_4");
        TransportCompany company5 = new TransportCompany("company_5");
        TransportCompany company6 = new TransportCompany("company_5");
        TransportCompany company7 = new TransportCompany("company_7");

        Set<TransportCompany> companies = new TreeSet<>();
        companies.add(company);
        companies.add(company2);
        companies.add(company3);
        companies.add(company4);
        companies.add(company5);
        companies.add(company6);

        company.setProfit(BigDecimal.valueOf(5000));
        company2.setProfit(BigDecimal.valueOf(100));
        company3.setProfit(BigDecimal.valueOf(500));
        company4.setProfit(BigDecimal.valueOf(100));
        company5.setProfit(BigDecimal.valueOf(10));
        company6.setProfit(BigDecimal.valueOf(77777));
        company7.setProfit(BigDecimal.valueOf(23232323));


        TransportCompanyDAO.saveCompanies(companies);
        TransportCompanyDAO.saveCompany(company7);


        Set<Vehicle> vehicles = new TreeSet<>();
        Vehicle v = new Vehicle(1, "BMW", "CO7678BH", VehicleType.BUS, company);
        vehicles.add(v);
        vehicles.add(new Vehicle(1, "Audi", "T9090XT", VehicleType.TANK_TRUCK, company));
        vehicles.add(new Vehicle(1, "Mercedes", "CA1993CT", VehicleType.TRUCK, company));
        vehicles.add(new Vehicle(1, "Toyota", "B9000PP", VehicleType.BUS, company));

        VehicleDAO.saveVehicles(vehicles);
        company.setVehicles(vehicles);
        System.out.println(vehicles);
        System.out.println("----------------");
        System.out.println(company.getVehicles());

        //TransportCompany c = TransportCompanyDAO.getCompany(1);
        //System.out.println(c.getVehicles());
        //TransportCompanyDAO.deleteVehicle(v);
        //System.out.println(TransportCompanyDAO.getCompany(1).getVehicles());

        Qualification q1 = new Qualification(QualificationType.PASSENGER);
        Qualification q2 = new Qualification(QualificationType.SPECIAL);
        Qualification q3 = new Qualification(QualificationType.COMMERCIAL);

        QualificationDAO.saveQualifications(new TreeSet<>(Set.of(q1, q2, q3)));

        Driver driver = new Driver(
                1,
                "Ivan",
                new TreeSet<>(Set.of(q1, q2)),
                company,
                BigDecimal.valueOf(1200));

//        List<TransportCompany> c = TransportCompanyDAO.companiesSortedByNameAndProfit();
//        c.forEach((com) -> {
//            System.out.println(com.getName() + ": " + com.getProfit());
//        });

        Driver driver2 = new Driver(
                2,
                "Jankos",
                new TreeSet<>(Set.of(q1)),
                company,
                BigDecimal.valueOf(500));

        Set<Qualification> qualifications = new TreeSet<>(Set.of(q2, q3));

        Driver driver3 = new Driver(
                3,
                "Halos",
                qualifications,
                company,
                BigDecimal.valueOf(5000));

        DriverDAO.saveDriver(driver);
        DriverDAO.saveDriver(driver2);
        DriverDAO.saveDriver(driver3);

        driver3.addQualification(q1);
        q1.addDriver(driver);
        q1.addDriver(driver2);
        q1.addDriver(driver3);

        q2.addDriver(driver);

        q3.addDriver(driver3);

        DriverDAO.saveOrUpdateDriver(driver3);
        QualificationDAO.saveOrUpdateQualification(q1);
        QualificationDAO.saveOrUpdateQualification(q2);
        QualificationDAO.saveOrUpdateQualification(q3);

        company.setDrivers(Set.of(driver, driver2, driver3));
        TransportCompanyDAO.saveOrUpdateCompany(company);

        //DriverDAO.driversSortedBySalary(company.getId()).stream().forEach((System.out::println));
        //System.out.println(DriverDAO.getDriversByCompany(company.getId()));
        //DriverDAO.driversSortedByQualification(company.getId()).stream().forEach(System.out::println);

        Client cl1 = new Client("Ivan", "email@domain.com");
        Logistics l1 = new Logistics(LogisticsType.GOODS, company, driver, cl1, "118.2", "Sofia",
                "Varna", LocalDate.now(), LocalDate.now(), BigDecimal.valueOf(100), true);
        Logistics l2 = new Logistics(LogisticsType.PEOPLE, company, driver2, cl1, "-", "Sofia",
                "Plovdiv", LocalDate.now(), LocalDate.now(), BigDecimal.valueOf(100), true);
        Logistics l3 = new Logistics(LogisticsType.SPECIAL, company, driver3, cl1, "50000", "Sofia",
                "Burgas", LocalDate.now(), LocalDate.now(), BigDecimal.valueOf(100), true);


        //l1.setDriver();
        ClientDAO.saveClient(cl1);
        cl1.addCompany(company);
        cl1.addOrders(l1);
        driver.addPackage(l1);
        driver2.addPackage(l2);
        driver3.addPackage(l3);
        DriverDAO.saveOrUpdateDriver(driver);
        DriverDAO.saveOrUpdateDriver(driver2);
        DriverDAO.saveOrUpdateDriver(driver3);

        LogisticsDAO.saveLogistics(l1);
        LogisticsDAO.saveLogistics(l2);
        LogisticsDAO.saveLogistics(l3);

        //System.out.println(company.getAvailableDrivers());
        //System.out.println(l1);
        ClientDAO.saveOrUpdateClient(cl1);
        LogisticsDAO.saveLogistics(l1);

        System.out.println("-------------------------------ORDERS---------------------------------------");
        //LogisticsDAO.logisticsSortedByDestination().stream().forEach(System.out::println);
        LogisticsDAO.allPaidOrders().stream().forEach(System.out::println);
        //System.out.println("by company ID:\n");
        //LogisticsDAO.allPaidOrdersInCompany(company.getId()).stream().forEach(System.out::println);
        System.out.println("-------TOTAL PROFIT: " + TransportCompanyDAO.totalProfitForCompany(company.getId()));
        //LogisticsDAO.allOrdersForCompanyForTimePeriod(company.getId(), LocalDate.now(), LocalDate.now())
                //.stream()
                //.forEach(System.out::println);
        System.out.println("-------------------------------------------------------------------------------------");

        System.out.println("--------------------------------SERIALIZE----------------------------------");
        ArrayList<Logistics> serLogistics = new ArrayList<>(List.of(l1, l2, l3));

        serLogistics.forEach(Logistics::serialize);
        serLogistics.forEach(Logistics::deserialize);

        System.out.println("-------------------------------------------------------------------------------------");
        TransportCompany tmp = TransportCompanyDAO.getCompany(1);
        System.out.println(tmp);

        System.out.println(TransportCompanyDAO.getDriversFromCompany(1));

        System.out.println("--------------------------FETCHING DATA AS A SET-------------------------------------");
        System.out.println(TransportCompanyDAO.getCompanyEmployees(1));
        System.out.println(TransportCompanyDAO.getCompanyVehicles(1));
        System.out.println(TransportCompanyDAO.getCompanyClients(1));
        System.out.println(TransportCompanyDAO.getCompanyLogistics(1));
    }

}
