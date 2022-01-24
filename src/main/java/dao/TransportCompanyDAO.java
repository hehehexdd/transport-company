package dao;

import config.SessionFactoryUtil;
import dto.DriverDTO;
import entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TransportCompanyDAO {

    public static void saveCompany(TransportCompany company) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(company);
            transaction.commit();
        }
    }

    public static void saveOrUpdateCompany(TransportCompany company) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(company);
            transaction.commit();
        }
    }

    public static TransportCompany getCompany(long id) {
        TransportCompany company;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            company = session.get(TransportCompany.class, id);
            transaction.commit();
        }
        return company;
    }

    public static void deleteCompany(TransportCompany company) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(company);
            transaction.commit();
        }
    }

    public static void saveCompanies(Set<TransportCompany> companyList) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            companyList.stream().forEach((com) -> session.save(com));
            transaction.commit();
        }
    }

    public static List<TransportCompany> readCompanies() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT a FROM TransportCompany a", TransportCompany.class)
                    .getResultList();
        }
    }

    public static Set<Driver> getCompanyEmployees(long id) {
        TransportCompany company;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            company = session.createQuery(
                            "select c from TransportCompany c" +
                                    " join fetch c.drivers" +
                                    " where c.id = :id",
                            TransportCompany.class)
                    .setParameter("id", id)
                    .getSingleResult();
            transaction.commit();
        }
        return company.getDrivers();
    }
    public static Set<Vehicle> getCompanyVehicles(long companyId) {
        TransportCompany company;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            company = session.createQuery(
                            "select c from TransportCompany c" +
                                    " join fetch c.vehicles" +
                                    " where c.id = :id",
                            TransportCompany.class)
                    .setParameter("id", companyId)
                    .getSingleResult();
            transaction.commit();
        }
        return company.getVehicles();
    }

    public static Set<Client> getCompanyClients(long companyId) {
        TransportCompany company;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            company = session.createQuery(
                            "select c from TransportCompany c" +
                                    " join fetch c.clients" +
                                    " where c.id = :id",
                            TransportCompany.class)
                    .setParameter("id", companyId)
                    .getSingleResult();
            transaction.commit();
        }
        return company.getClients();
    }

    public static Set<Logistics> getCompanyLogistics(long companyId) {
        TransportCompany company;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            company = session.createQuery(
                            "select c from TransportCompany c" +
                                    " join fetch c.logistics" +
                                    " where c.id = :id",
                            TransportCompany.class)
                    .setParameter("id", companyId)
                    .getSingleResult();
            transaction.commit();
        }
        return company.getLogistics();
    }

    public static List<DriverDTO> getDriversFromCompany(long companyId) {
        List<DriverDTO> employees;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            employees = session.createQuery(
                            "SELECT new dto.DriverDTO(d.id, d.name, d.salary) FROM Driver d" +
                                    " join d.company c " +
                                    "WHERE c.id = :id",
                            DriverDTO.class)
                    .setParameter("id", companyId)
                    .getResultList();
            transaction.commit();
        }
        return employees;
    }

    public static List<TransportCompany> companiesSortedByNameAndProfit() {
        // SELECT * FROM companies ORDER BY name ASC, profit DESC;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<TransportCompany> cr = criteriaBuilder.createQuery(TransportCompany.class);
            Root<TransportCompany> root = cr.from(TransportCompany.class);
            cr.select(root);

            ArrayList<Order> orderList = new ArrayList<>();

            orderList.add(criteriaBuilder.asc(root.get("name")));
            orderList.add(criteriaBuilder.desc(root.get("profit")));

            cr.orderBy(orderList);

            Query<TransportCompany> query = session.createQuery(cr);
            ArrayList<TransportCompany> companies = (ArrayList<TransportCompany>) query.getResultList();
            return companies;
        }
    }

    public static BigDecimal totalProfitForCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            List<Logistics> lg =session.createQuery(
                            "SELECT l FROM Logistics l " +
                                    "join l.company c " +
                                    "WHERE c.id = :companyId " +
                                    "AND isPaid = :isPaid", Logistics.class)
                    .setParameter("companyId", companyId)
                    .setParameter("isPaid", true)
                    .getResultList();
            transaction.commit();

            BigDecimal profit = lg.stream()
                    .map(Logistics::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
            return profit;
        }
    }

    public static BigDecimal totalProfitForCompanyForTimePeriod(
            long companyId,
            LocalDate start,
            LocalDate end) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            List<Logistics> lg = session.createQuery(
                            "SELECT l FROM Logistics l " +
                                    "WHERE l.company.id = :id " +
                                    "AND isPaid = :isPaid " +
                                    "AND eta >= :startDate " +
                                    "AND eta <= :endDate", Logistics.class)
                    .setParameter("id", companyId)
                    .setParameter("isPaid", true)
                    .setParameter("startDate", start)
                    .setParameter("endDate", end)
                    .getResultList();
            transaction.commit();

            BigDecimal profit = lg.stream()
                    .map(Logistics::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return profit;
        }
    }

}
