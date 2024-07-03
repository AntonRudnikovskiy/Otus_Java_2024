package atm.model;

import atm.exeptions.InsufficientFundsException;

import java.util.HashMap;
import java.util.Map;

public class BanknoteStorage {
    private final Map<Integer, Integer> balance = new HashMap<>();

    public void addBanknotes(int denomination, int count) {
        balance.put(denomination, balance.getOrDefault(denomination, 0) + count);
    }

    public void removeBanknotes(int denomination, int count) {
        int currentCount = balance.getOrDefault(denomination, 0);
        if (currentCount < count) {
            throw new InsufficientFundsException("Недостаточно купюр номиналом " + denomination);
        }
        balance.put(denomination, currentCount - count);
    }

    public Map<Integer, Integer> getBalance() {
        return new HashMap<>(balance);
    }

    public int getTotalBalance() {
        return balance.entrySet().stream()
                .mapToInt(entry -> entry.getKey() * entry.getValue())
                .sum();
    }
}
