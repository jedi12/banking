package ru.demo.banking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.demo.banking.model.Account;
import ru.demo.banking.model.User;
import ru.demo.banking.utils.TransactionHelper;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final AccountService accountService;
    private final RepoService repoService;
    private final TransactionHelper transactionHelper;

    @Autowired
    public UserService(AccountService accountService, RepoService repoService, TransactionHelper transactionHelper) {
        this.accountService = accountService;
        this.repoService = repoService;
        this.transactionHelper = transactionHelper;
    }

    public User createUser(String login) {
        login = login == null ? null : login.trim();

        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("Не указан логин пользователя");
        }

        if (getUserByLogin(login) != null) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует в системе");
        }

        User user = new User(login, new ArrayList<>());

        return transactionHelper.executeInTransaction(session -> {
            repoService.saveNewUser(user);
            Account account = accountService.createAccount(user);
            user.getAccountList().add(account);
            return user;
        });
    }

    public List<User> getAllUsers() {
        return repoService.getUsers();
    }

    public User getUserById(Long userId) {
        return repoService.getUserById(userId);
    }

    private User getUserByLogin(String login) {
        return repoService.getUserByLogin(login);
    }
}
