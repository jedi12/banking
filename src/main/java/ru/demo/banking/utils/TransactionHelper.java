package ru.demo.banking.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TransactionHelper {

    private final SessionFactory sessionFactory;

    public TransactionHelper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T> T executeInTransaction(Function<Session, T> function) {
        var session = sessionFactory.getCurrentSession();
        Transaction transaction = session.getTransaction();

        if (!transaction.getStatus().equals(TransactionStatus.NOT_ACTIVE)) {
            return function.apply(session);
        }

        try {
            session.beginTransaction();
            T returnValue = function.apply(session);
            transaction.commit();
            return returnValue;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
