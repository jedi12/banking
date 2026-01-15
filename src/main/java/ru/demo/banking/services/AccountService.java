package ru.demo.banking.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.demo.banking.configuration.AccountProperties;
import ru.demo.banking.model.Account;
import ru.demo.banking.model.User;
import ru.demo.banking.utils.MathUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class AccountService {

    private final AccountProperties accountProperties;
    private final RepoService repoService;
    private Long newAccountId;

    @Autowired
    public AccountService(AccountProperties accountProperties, RepoService repoService) {
        this.accountProperties = accountProperties;
        this.repoService = repoService;
    }

    @PostConstruct
    private void init() {
        newAccountId = 1L;
    }

    public Account createAccount(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Не указан id пользователя");
        }

        User user = repoService.getUsers().get(userId);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с указанным id не существует");
        }

        BigDecimal defaultAmount = MathUtils.roundWithScale(BigDecimal.valueOf(accountProperties.getDefaultAmount()), 2);
        Account account = new Account(newAccountId++, userId, defaultAmount);
        repoService.getAccounts().put(account.getId(), account);

        user.getAccountList().add(account);

        return account;
    }

    public void closeAccount(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Не указан id счета");
        }

        Account account = repoService.getAccounts().get(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Счет с указанным id не существует");
        }

        List<Account> userAccounts = getAccountsByUser(account.getUserId());
        if (userAccounts.size() == 1) {
            throw new IllegalArgumentException("Счет с указанным id закрыть нельзя, так как у пользователя всего один счет");
        }

        Account mainAccount = userAccounts.get(0);
        if (mainAccount.equals(account)) {
            mainAccount = userAccounts.get(1);
        }

        BigDecimal moneyAmountSum = MathUtils.sum(mainAccount.getMoneyAmount(), account.getMoneyAmount());
        mainAccount.setMoneyAmount(moneyAmountSum);

        repoService.getAccounts().remove(accountId);

        User user = repoService.getUsers().get(account.getUserId());
        user.getAccountList().remove(account);
    }

    public void depositToAccount(Long accountId, BigDecimal moneyAmount) {
        if (moneyAmount == null) {
            throw new IllegalArgumentException("Не указана сумма для пополнения счета");
        }

        if (moneyAmount.signum() <= 0 ) {
            throw new IllegalArgumentException("Указана неверная сумма для пополнения счета");
        }

        changeAccountMoneyAmount(accountId, moneyAmount);
    }

    public void withdrawFromAccount(Long accountId, BigDecimal moneyAmount) {
        if (moneyAmount == null) {
            throw new IllegalArgumentException("Не указана сумма для снятия со счета");
        }

        if (moneyAmount.signum() <= 0 ) {
            throw new IllegalArgumentException("Указана неверная сумма для снятия со счета");
        }

        changeAccountMoneyAmount(accountId, moneyAmount.negate());
    }

    private void changeAccountMoneyAmount(Long accountId, BigDecimal moneyAmount) {
        if (accountId == null) {
            throw new IllegalArgumentException("Не указан id счета");
        }

        Account account = repoService.getAccounts().get(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Счет с указанным id не существует");
        }

        BigDecimal moneyAmountSum = MathUtils.sum(account.getMoneyAmount(), moneyAmount);
        if (moneyAmountSum != null && moneyAmountSum.signum() < 0 ) {
            throw new IllegalArgumentException("На счету с указанным id недостаточно средств для проведения операции");
        }

        account.setMoneyAmount(moneyAmountSum);
    }

    public void transferBetweenAccounts(Long fromAccountId, Long toAccountId, BigDecimal moneyAmount) {
        if (fromAccountId == null) {
            throw new IllegalArgumentException("Не указан id счета отправителя");
        }

        Account fromAccount = repoService.getAccounts().get(fromAccountId);
        if (fromAccount == null) {
            throw new IllegalArgumentException("Счет отправителя с указанным id не существует");
        }

        if (toAccountId == null) {
            throw new IllegalArgumentException("Не указан id счета получателя");
        }

        Account toAccount = repoService.getAccounts().get(toAccountId);
        if (toAccount == null) {
            throw new IllegalArgumentException("Счет получателя с указанным id не существует");
        }

        if (moneyAmount == null) {
            throw new IllegalArgumentException("Не указана сумма для перевода со счета на счет");
        }

        if (moneyAmount.signum() <= 0 ) {
            throw new IllegalArgumentException("Указана неверная сумма для перевода со счета на счет");
        }

        BigDecimal commission = BigDecimal.valueOf(0);
        if (!fromAccount.getUserId().equals(toAccount.getUserId())) {
            commission = MathUtils.multiply(moneyAmount, accountProperties.getTransferCommission(), 2);
        }

        withdrawFromAccount(fromAccount.getId(), moneyAmount);
        depositToAccount(toAccount.getId(), MathUtils.sum(moneyAmount, commission.negate()));
    }

    public List<Account> getAccountsByUser(Long userId) {
        return repoService.getAccounts().values().stream().filter(account -> account.getUserId().equals(userId)).sorted(Comparator.comparing(Account::getId)).toList();
    }
}
