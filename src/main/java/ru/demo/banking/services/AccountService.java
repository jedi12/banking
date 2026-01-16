package ru.demo.banking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.demo.banking.configuration.AccountProperties;
import ru.demo.banking.model.Account;
import ru.demo.banking.model.User;
import ru.demo.banking.utils.MathUtils;
import ru.demo.banking.utils.TransactionHelper;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountProperties accountProperties;
    private final RepoService repoService;
    private final TransactionHelper transactionHelper;

    @Autowired
    public AccountService(AccountProperties accountProperties, RepoService repoService, TransactionHelper transactionHelper) {
        this.accountProperties = accountProperties;
        this.repoService = repoService;
        this.transactionHelper = transactionHelper;
    }

    public Account createAccount(User user) {
        return transactionHelper.executeInTransaction(session -> {
            BigDecimal defaultAmount = MathUtils.roundWithScale(BigDecimal.valueOf(accountProperties.getDefaultAmount()), 2);
            Account account = new Account(user, defaultAmount);
            repoService.saveNewAccount(account);

            return account;
        });
    }

    public void closeAccount(Long accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Не указан id счета");
        }

        transactionHelper.executeInTransaction(session -> {
            Account account = repoService.getAccountId(accountId);
            if (account == null) {
                throw new IllegalArgumentException("Счет с указанным id не существует");
            }

            List<Account> userAccounts = repoService.getAccountsByUser(account.getUser());
            if (userAccounts.size() == 1) {
                throw new IllegalArgumentException("Счет с указанным id закрыть нельзя, так как у пользователя всего один счет");
            }

            Account mainAccount = userAccounts.get(0);
            if (mainAccount.equals(account)) {
                mainAccount = userAccounts.get(1);
            }

            BigDecimal moneyAmountSum = MathUtils.sum(mainAccount.getMoneyAmount(), account.getMoneyAmount());
            mainAccount.setMoneyAmount(moneyAmountSum);

            repoService.removeAccount(account);

            return null;
        });
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

        transactionHelper.executeInTransaction(session -> {
            Account account = repoService.getAccountId(accountId);
            if (account == null) {
                throw new IllegalArgumentException("Счет с указанным id не существует");
            }

            BigDecimal moneyAmountSum = MathUtils.sum(account.getMoneyAmount(), moneyAmount);
            if (moneyAmountSum != null && moneyAmountSum.signum() < 0 ) {
                throw new IllegalArgumentException("На счету с указанным id недостаточно средств для проведения операции");
            }

            account.setMoneyAmount(moneyAmountSum);
            return null;
        });
    }

    public void transferBetweenAccounts(Long fromAccountId, Long toAccountId, BigDecimal moneyAmount) {
        if (fromAccountId == null) {
            throw new IllegalArgumentException("Не указан id счета отправителя");
        }

        if (toAccountId == null) {
            throw new IllegalArgumentException("Не указан id счета получателя");
        }

        if (moneyAmount == null) {
            throw new IllegalArgumentException("Не указана сумма для перевода со счета на счет");
        }

        if (moneyAmount.signum() <= 0 ) {
            throw new IllegalArgumentException("Указана неверная сумма для перевода со счета на счет");
        }

        transactionHelper.executeInTransaction(session -> {
            Account fromAccount = repoService.getAccountId(fromAccountId);
            if (fromAccount == null) {
                throw new IllegalArgumentException("Счет отправителя с указанным id не существует");
            }

            Account toAccount = repoService.getAccountId(toAccountId);
            if (toAccount == null) {
                throw new IllegalArgumentException("Счет получателя с указанным id не существует");
            }

            BigDecimal commission = BigDecimal.valueOf(0);
            if (!fromAccount.getUser().equals(toAccount.getUser())) {
                commission = MathUtils.multiply(moneyAmount, accountProperties.getTransferCommission(), 2);
            }

            withdrawFromAccount(fromAccount.getId(), moneyAmount);
            depositToAccount(toAccount.getId(), MathUtils.sum(moneyAmount, commission.negate()));
            return null;
        });
    }
}
