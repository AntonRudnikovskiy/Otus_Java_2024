package atm.service;

import atm.exeptions.InsufficientFundsException;

import java.util.HashMap;
import java.util.Map;

public class WithdrawalServiceImpl implements WithdrawalService {
    private final Map<Integer, Integer> stash;

    public WithdrawalServiceImpl(Map<Integer, Integer> stash) {
        this.stash = stash;
    }

    @Override
    public String withdraw(int amount) throws InsufficientFundsException {
        int money = amount;
        Map<Integer, Integer> toWithdraw = new HashMap<>();

        for (int denomination : new int[]{5000, 2000, 1000, 500}) {
            while (amount >= denomination && stash.getOrDefault(denomination, 0) > 0) {
                stash.put(denomination, stash.get(denomination) - 1);
                amount -= denomination;
                toWithdraw.put(denomination, toWithdraw.getOrDefault(denomination, 0) + 1);
            }
        }

        if (amount > 0) {
            toWithdraw.forEach((denomination, count) -> {
                stash.put(denomination, stash.getOrDefault(denomination, 0) + count);
            });
        }
        return "Сумма " + money + " изъята";
    }

}
