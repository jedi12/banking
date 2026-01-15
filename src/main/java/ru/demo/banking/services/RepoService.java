package ru.demo.banking.services;

import org.springframework.stereotype.Service;
import ru.demo.banking.model.Account;
import ru.demo.banking.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class RepoService {
    private Map<Long, User> users = new TreeMap<>();
    private Map<Long, Account> accounts = new HashMap<>();

    public Map<Long, User> getUsers() {
        return users;
    }

    public Map<Long, Account> getAccounts() {
        return accounts;
    }
}
