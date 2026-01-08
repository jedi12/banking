package ru.demo.banking.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.demo.banking.model.User;

import java.util.*;

@Service
public class UserService {

    private final AccountService accountService;
    private Map<Long, User> users;
    private Long newUserId;

    @Autowired
    public UserService(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostConstruct
    private void init() {
        users = new TreeMap<>();
        newUserId = 1L;
    }

    public User createUser(String login) {
        login = login == null ? null : login.trim();

        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("Не указан логин пользователя");
        }

        if (getUserByLogin(login) != null) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует в системе");
        }

        User user = new User(newUserId++, login, new ArrayList<>());
        user.getAccountList().add(accountService.createAccount(user.getId()));

        users.put(user.getId(), user);

        return user;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        for (User user: users.values()) {
            user.getAccountList().clear();
            user.getAccountList().addAll(accountService.getAccountsByUser(user.getId()));
            userList.add(user);
        }

        return userList;
    }

    public boolean isUserExists(Long userId) {
        return users.containsKey(userId);
    }

    private User getUserByLogin(String login) {
        return users.values().stream().filter(user -> user.getLogin().equals(login)).findFirst().orElse(null);
    }
}
