package ru.demo.banking.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.demo.banking.model.User;

import java.util.*;

@Service
public class UserService {

    private final AccountService accountService;
    private final RepoService repoService;
    private Long newUserId;

    @Autowired
    public UserService(AccountService accountService, RepoService repoService) {
        this.accountService = accountService;
        this.repoService = repoService;
    }

    @PostConstruct
    private void init() {
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
        repoService.getUsers().put(user.getId(), user);

        accountService.createAccount(user.getId());

        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(repoService.getUsers().values());
    }

    public boolean isUserExists(Long userId) {
        return repoService.getUsers().containsKey(userId);
    }

    private User getUserByLogin(String login) {
        return repoService.getUsers().values().stream().filter(user -> user.getLogin().equals(login)).findFirst().orElse(null);
    }
}
