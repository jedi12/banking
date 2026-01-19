package ru.demo.banking.services;

import org.springframework.stereotype.Service;
import ru.demo.banking.model.Account;
import ru.demo.banking.model.User;
import ru.demo.banking.utils.TransactionHelper;

import java.util.List;

@Service
public class RepoService {

    private final TransactionHelper transactionHelper;

    public RepoService(TransactionHelper transactionHelper) {
        this.transactionHelper = transactionHelper;
    }

    public List<User> getUsers() {
        return transactionHelper.executeInTransaction(session -> {
            return session.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.accountList ORDER BY u.id ASC", User.class).getResultList();
        });
    }

    public User getUserById(Long userId) {
        return transactionHelper.executeInTransaction(session -> {
            List<User> users =  session.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.accountList WHERE u.id = :userId", User.class)
                    .setParameter("userId", userId)
                    .getResultList();

            return users.isEmpty() ? null : users.get(0);
        });
    }

    public User getUserByLogin(String login) {
        return transactionHelper.executeInTransaction(session -> {
            List<User> users = session.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .getResultList();

            return users.isEmpty() ? null : users.get(0);
        });
    }

    public void saveNewUser(User user) {
        transactionHelper.executeInTransaction(session -> {
            session.persist(user);
            return null;
        });
    }

    public Account getAccountId(Long accountId) {
        return transactionHelper.executeInTransaction(session -> {
            return session.get(Account.class, accountId);
        });
    }

    public List<Account> getAccountsByUser(User user) {
        return transactionHelper.executeInTransaction(session -> {
            return session.createQuery("SELECT a FROM Account a WHERE a.user = :user ORDER BY a.id ASC", Account.class)
                    .setParameter("user", user)
                    .getResultList();
        });
    }

    public void removeAccount(Account account) {
        transactionHelper.executeInTransaction(session -> {
            session.remove(account);
            return null;
        });
    }

    public void saveNewAccount(Account account) {
        transactionHelper.executeInTransaction(session -> {
            session.persist(account);
            return null;
        });
    }
}
