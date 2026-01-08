package ru.demo.banking.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccountProperties {

    private final double defaultAmount;
    private final double transferCommission;

    public AccountProperties(@Value("${account.default-amount:0}") double defaultAmount, @Value("${account.transfer-commission:0}") double transferCommission) {
        if (defaultAmount < 0) {
            throw new IllegalArgumentException("Значение 'default-amount' не должно быть отрицательным");
        }

        if (transferCommission < 0 || transferCommission > 1) {
            throw new IllegalArgumentException("Значение 'transfer-commission' должно быть в диапазоне от 0 до 1");
        }

        this.defaultAmount = defaultAmount;
        this.transferCommission = transferCommission;
    }

    public double getDefaultAmount() {
        return defaultAmount;
    }

    public double getTransferCommission() {
        return transferCommission;
    }

    @Override
    public String toString() {
        return "AccountProperties{" +
                "defaultAmount=" + defaultAmount +
                ", transferCommission=" + transferCommission +
                '}';
    }
}
