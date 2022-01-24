package dao;

import config.SessionFactoryUtil;
import entity.Driver;
import entity.Logistics;
import entity.QualificationType;
import entity.TransportCompany;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.*;

public class DriverDAO {

    public static void saveDriver(Driver driver /*, long companyId*/) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(driver);
            transaction.commit();
        }
    }

    public static void saveOrUpdateDriver(Driver driver /*, long companyId*/) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(driver);
            transaction.commit();
        }
    }

    public static Driver getDriver(long id) {
        Driver driver;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            driver = session.get(Driver.class, id);
            transaction.commit();
        }
        return driver;
    }

    public static void deleteDriver(Driver driver) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(driver);
            transaction.commit();
            //}
        }
    }

    public static void saveDrivers(Set<Driver> drivers /*, long companyId*/) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            drivers.stream().forEach((v) -> session.save(v));
            transaction.commit();
        }
    }

    public static List<Driver> readDrivers(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT a FROM entities.Driver a", Driver.class).getResultList();
        }
    }

    public static List<Driver> getDriversByCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Driver a WHERE company_id = :companyId", Driver.class)
                    .setParameter("companyId", companyId)
                    .getResultList();
        }
    }


    public static List<Driver> driversSortedByQualificationAndSalary2(long companyId) {
        // SELECT * FROM employees WHERE company_id = companyId ORDER BY qualification ASC, salary DESC;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Driver> cr = criteriaBuilder.createQuery(Driver.class);
            Root<Driver> root = cr.from(Driver.class);
            Path<Long> cId = root.join("company").get("id");
            Predicate predicate = criteriaBuilder.equal(root.get("company_id"), companyId);
            //cr.select(root).where(predicate);
            cr.select(root);

            ArrayList<Order> orderList = new ArrayList<>();

            orderList.add(criteriaBuilder.asc(root.get("qualification")));
            orderList.add(criteriaBuilder.desc(root.get("salary")));

            cr.orderBy(orderList);

            Query<Driver> query = session.createQuery(cr);
            ArrayList<Driver> companies = (ArrayList<Driver>) query.getResultList();
            return companies;
        }
    }

    public static List<Driver> driversSortedBySalary(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT d FROM Driver d " +
                                    "join d.company c " +
                                    "WHERE c.id = :companyId " +
                                    "ORDER BY salary DESC", Driver.class)
                    .setParameter("companyId", companyId)
                    .getResultList();
        }
    }

    public static List<Driver> driversSortedByQualification(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT d FROM Driver d " +
                                    "join fetch d.qualifications " +
                                    "join d.company c " +
                                    "WHERE c.id = :companyId " +
                                    "ORDER BY size(d.qualifications)", Driver.class)
                    .setParameter("companyId", companyId)
                    .getResultList();
        }
    }

    public static Map<Long, Integer> totalCarriagesByEveryDriverInCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            List<Driver> drivers = session.createQuery(
                            "SELECT d FROM Driver d " +
                            "WHERE company_id = :companyId", Driver.class)
                    .setParameter("companyId", companyId)
                    .getResultList();

            Map<Long, Integer> result = new LinkedHashMap<>();

            for(Driver driver : drivers) {
                result.put(driver.getId(), driver.getCarriages().size());
            }
            return result;
        }
    }

    public static Map<Long, BigDecimal> totalProfitByEveryDriverInCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            List<Driver> drivers = session.createQuery(
                            "SELECT d FROM Driver d " +
                                    "WHERE company_id = :companyId", Driver.class)
                    .setParameter("companyId", companyId)
                    .getResultList();

            Map<Long, BigDecimal> result = new LinkedHashMap<>();

            for(Driver driver : drivers) {
                BigDecimal driverProfit = driver.getCarriages()
                        .stream()
                        .map(Logistics::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                result.put(driver.getId(), driverProfit);
            }
            return result;
        }
    }

}



