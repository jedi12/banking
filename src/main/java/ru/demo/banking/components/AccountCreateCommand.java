package ru.demo.banking.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.demo.banking.configuration.AnsiEscapeCode;
import ru.demo.banking.model.Account;
import ru.demo.banking.services.AccountService;
import ru.demo.banking.services.UserService;
import ru.demo.banking.utils.ConvertUtils;

import java.util.Scanner;

@Component
public class AccountCreateCommand implements OperationCommand {
    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public AccountCreateCommand(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Укажите id пользователя, для которого создается счет:");
        Long userId = ConvertUtils.getLong(scanner.nextLine());

        if (!userService.isUserExists(userId)) {
            throw new IllegalArgumentException("Пользователь с таким id не существует. Счет для него создать невозможно.");
        }

        Account account = accountService.createAccount(userId);

        System.out.printf("%n%sСоздан новый счет: %s%s%n", AnsiEscapeCode.GREEN, account, AnsiEscapeCode.RESET);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }
}
