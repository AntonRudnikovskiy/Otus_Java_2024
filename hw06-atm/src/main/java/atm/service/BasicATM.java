package atm.service;

import atm.exeptions.InsufficientFundsException;
import atm.model.BanknoteStorage;
import atm.model.Currency;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BasicATM implements ATM {
    private final BanknoteStorage banknoteStorage = new BanknoteStorage();

    @Override
    public int getBalance() {
        return banknoteStorage.getTotalBalance();
    }

    @Override
    public String putCache(int amount) throws InsufficientFundsException {
        int money = amount;
        for (int denomination : Currency.getDenominations()) {
            if (amount >= denomination) {
                int count = amount / denomination;
                banknoteStorage.addBanknotes(denomination, count);
                amount -= count * denomination;
            }
        }

        if (amount > 0) {
            throw new InsufficientFundsException("Сумма " + money + " не может быть разменяна на доступные номиналы");
        }
        return "Сумма " + money + " внесена";
    }

    @Override
    public String withdraw(int amount) throws InsufficientFundsException {
        int money = amount;
        Map<Integer, Integer> toWithdraw = new HashMap<>();
        int[] denominations = Currency.getDenominations();
        Arrays.sort(denominations);

        for (int i = denominations.length - 1; i >= 0; i--) {
            int denomination = denominations[i];
            while (amount >= denomination && banknoteStorage.getBalance().getOrDefault(denomination, 0) > 0) {
                toWithdraw.put(denomination, toWithdraw.getOrDefault(denomination, 0) + 1);
                banknoteStorage.removeBanknotes(denomination, 1);
                amount -= denomination;
            }
        }

        if (amount > 0) {
            toWithdraw.forEach((denomination, count) -> {
                banknoteStorage.addBanknotes(denomination, count);
            });
            throw new InsufficientFundsException("Недостаточно средств для снятия суммы: " + money);
        }

        return "Сумма " + money + " изъята";
    }
}
