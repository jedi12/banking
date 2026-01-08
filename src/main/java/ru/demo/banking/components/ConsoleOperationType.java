package ru.demo.banking.components;

public enum ConsoleOperationType {

    USER_CREATE,
    SHOW_ALL_USERS,
    ACCOUNT_CREATE,
    ACCOUNT_CLOSE,
    ACCOUNT_DEPOSIT,
    ACCOUNT_TRANSFER,
    ACCOUNT_WITHDRAW;

    public static ConsoleOperationType getByName(String name) {
        for (ConsoleOperationType type: values()) {
            if (type.name().equals(name)) return type;
        }
        return null;
    }
}
