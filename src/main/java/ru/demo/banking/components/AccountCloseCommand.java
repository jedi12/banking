package ru.demo.banking.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.demo.banking.configuration.AnsiEscapeCode;
import ru.demo.banking.services.AccountService;
import ru.demo.banking.utils.ConvertUtils;

import java.util.Scanner;

@Component
public class AccountCloseCommand implements OperationCommand {
    private final AccountService accountService;

    @Autowired
    public AccountCloseCommand(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Укажите id счета:");
        Long accountId = ConvertUtils.getLong(scanner.nextLine());
        accountService.closeAccount(accountId);

        System.out.printf("%n%sСчет с id=%s закрыт%s%n", AnsiEscapeCode.GREEN, accountId, AnsiEscapeCode.RESET);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}
