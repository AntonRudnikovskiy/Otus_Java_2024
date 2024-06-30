package atm.service;

import java.util.Map;

public class BalanceInquiryServiceImpl implements BalanceInquiryService {
    private final Map<Integer, Integer> stash;

    public BalanceInquiryServiceImpl(Map<Integer, Integer> stash) {
        this.stash = stash;
    }

    @Override
    public int getBalance() {
        return stash.entrySet().stream()
                .mapToInt(value -> value.getKey() * value.getValue())
                .sum();
    }
}
