package atm.service;

import atm.exeptions.InsufficientFundsException;

import java.util.Map;

public class DepositServiceImpl implements DepositService {
    private final Map<Integer, Integer> stash;

    public DepositServiceImpl(Map<Integer, Integer> stash) {
        this.stash = stash;
    }

    @Override
    public String putCache(int amount) {
        int money = amount;
        while (amount > 0) {
            if (amount >= 5000) {
                stash.put(5000, stash.getOrDefault(amount, 0) + 1);
                amount -= 5000;
            } else if (amount >= 2000) {
                stash.put(2000, stash.getOrDefault(amount, 0) + 1);
                amount -= 2000;
            } else if (amount >= 1000) {
                stash.put(1000, stash.getOrDefault(amount, 0) + 1);
                amount -= 1000;
            } else if (amount >= 500) {
                stash.put(500, stash.getOrDefault(amount, 0) + 1);
                amount -= 500;
            }
            else {
                throw new InsufficientFundsException("Amount cannot be split into 500, 1000, 2000, or 5000 parts");
            }
        }
        return "Сумма " + money + " внесена";
    }
}
