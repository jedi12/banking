package ru.demo.banking.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.demo.banking.configuration.AnsiEscapeCode;
import ru.demo.banking.services.AccountService;
import ru.demo.banking.utils.ConvertUtils;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class AccountTransferCommand implements OperationCommand {
    private final AccountService accountService;

    @Autowired
    public AccountTransferCommand(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Укажите id счета отправителя:");
        Long fromAccountId = ConvertUtils.getLong(scanner.nextLine());

        System.out.println("Укажите id счета получателя:");
        Long toAccountId = ConvertUtils.getLong(scanner.nextLine());

        System.out.println("Укажите сумму перевода:");
        BigDecimal moneyAmount = ConvertUtils.getBigDecimal(scanner.nextLine());

        accountService.transferBetweenAccounts(fromAccountId, toAccountId, moneyAmount);

        System.out.printf("%n%sСумма %s переведена со счета с id=%s на счет с id=%s (с учетом комиссии)%s%n", AnsiEscapeCode.GREEN, moneyAmount, fromAccountId, toAccountId, AnsiEscapeCode.RESET);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
