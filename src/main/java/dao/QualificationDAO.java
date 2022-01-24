package dao;

import config.SessionFactoryUtil;

import entity.Qualification;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Set;

public class QualificationDAO {

    public static void saveQualification(Qualification qualification) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(qualification);
            transaction.commit();
        }
    }

    public static void saveOrUpdateQualification(Qualification qualification) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(qualification);
            transaction.commit();
        }
    }

    public static Qualification getQualification(long id) {
        Qualification qualification;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            qualification = session.get(Qualification.class, id);
            transaction.commit();
        }
        return qualification;
    }

    public static void deleteQualification(Qualification qualification) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(qualification);
            transaction.commit();
        }
    }

    public static void saveQualifications(Set<Qualification> qualificationSet) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            qualificationSet.stream().forEach((com) -> session.save(com));
            transaction.commit();
        }
    }

    public static List<Qualification> readQualifications() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT a FROM Qualification a", Qualification.class).getResultList();
        }
    }
}
