package ru.demo.banking.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.demo.banking.configuration.AnsiEscapeCode;
import ru.demo.banking.services.AccountService;
import ru.demo.banking.utils.ConvertUtils;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class AccountWithdrawCommand implements OperationCommand {
    private final AccountService accountService;

    @Autowired
    public AccountWithdrawCommand(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Укажите id счета:");
        Long accountId = ConvertUtils.getLong(scanner.nextLine());

        System.out.println("Укажите сумму для списания со счета:");
        BigDecimal moneyAmount = ConvertUtils.getBigDecimal(scanner.nextLine());

        accountService.withdrawFromAccount(accountId, moneyAmount);

        System.out.printf("%n%sСумма %s списана со счета с d=%s%s%n", AnsiEscapeCode.GREEN, moneyAmount, accountId, AnsiEscapeCode.RESET);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_WITHDRAW;
    }
}
