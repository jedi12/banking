package ru.demo.banking.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.demo.banking.configuration.AnsiEscapeCode;
import ru.demo.banking.services.AccountService;
import ru.demo.banking.utils.ConvertUtils;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class AccountDepositCommand implements OperationCommand {
    private final AccountService accountService;

    @Autowired
    public AccountDepositCommand(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Укажите id счета:");
        Long accountId = ConvertUtils.getLong(scanner.nextLine());

        System.out.println("Укажите сумму для внесения на счет:");
        BigDecimal moneyAmount = ConvertUtils.getBigDecimal(scanner.nextLine());

        accountService.depositToAccount(accountId, moneyAmount);

        System.out.printf("%n%sСумма %s внесена на счет с id=%s%s%n", AnsiEscapeCode.GREEN, moneyAmount, accountId, AnsiEscapeCode.RESET);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_DEPOSIT;
    }
}
