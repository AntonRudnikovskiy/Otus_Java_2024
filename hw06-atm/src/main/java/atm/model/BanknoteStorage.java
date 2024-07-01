package atm.model;

import java.util.HashMap;
import java.util.Map;

public class BanknoteStorage {
    private final Map<Integer, Integer> balance = new HashMap<>();

    public Map<Integer, Integer> getBalance() {
        return balance;
    }
}
