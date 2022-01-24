package dao;

import config.SessionFactoryUtil;
import entity.Logistics;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class LogisticsDAO {

    public static void saveLogistics(Logistics logistics) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(logistics);
            transaction.commit();
        }
    }

    public static void saveOrUpdateLogistics(Logistics logistics) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(logistics);
            transaction.commit();
        }
    }

    public static Logistics getLogistics(long id) {
        Logistics logistics;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            logistics = session.get(Logistics.class, id);
            transaction.commit();
        }
        return logistics;
    }

    public static void deleteLogistics(Logistics logistics) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(logistics);
            transaction.commit();
        }
    }

    public static void saveLogistics(Set<Logistics> logisticsSet) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            logisticsSet.stream().forEach((com) -> session.save(com));
            transaction.commit();
        }
    }

    public static List<Logistics> readLogistics() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT l FROM Logistics l", Logistics.class).getResultList();
        }
    }

    public static List<Logistics> logisticsOfCompanySortedByDestination(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT l FROM Logistics l " +
                                    "join l.company c " +
                                    "WHERE c.id = :companyId " +
                                    "ORDER BY destination ASC", Logistics.class)
                    .setParameter("companyId", companyId)
                    .getResultList();
        }
    }

    public static List<Logistics> logisticsSortedByDestination() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT l FROM Logistics l " +
                                    "ORDER BY destination ASC", Logistics.class)
                    .getResultList();
        }
    }

    //returns the paid(completed) orders in all companies
    public static List<Logistics> allPaidOrders() {
       try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
           return session.createQuery(
                           "SELECT l FROM Logistics l " +
                                   "WHERE isPaid = :isPaid", Logistics.class)
                   .setParameter("isPaid", true)
                   .getResultList();
       }
    }

    //returns the paid(completed) orders in a specific company
    public static List<Logistics> allPaidOrdersInCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            "SELECT l FROM Logistics l " +
                                    "join l.company c " +
                                    "WHERE c.id = :companyId " +
                                    "AND isPaid = :isPaid", Logistics.class)
                    .setParameter("companyId", companyId)
                    .setParameter("isPaid", true)
                    .getResultList();
        }
    }

    public static Integer numberOfPaidOrdersInCompany(long companyId) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            List<Logistics> lg = session.createQuery(
                            "SELECT l FROM Logistics l " +
                                    "join l.company c " +
                                    "WHERE c.id = :companyId " +
                                    "AND isPaid = :isPaid", Logistics.class)
                    .setParameter("companyId", companyId)
                    .setParameter("isPaid", true)
                    .getResultList();

            Integer count = lg.size();
            return count;
        }
    }

    //returns a list of all orders in a company for a time period
    public static List<Logistics> allOrdersForCompanyForTimePeriod(
            long companyId,
            LocalDate start,
            LocalDate end) {

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            return session.createQuery(
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
        }
    }

}
