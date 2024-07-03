package atm.model;

import java.util.Arrays;

public enum Currency {
    FIVE_THOUSAND(5000),
    TWO_THOUSAND(2000),
    ONE_THOUSAND(1000),
    FIVE_HUNDRED(500);

    private final int value;

    Currency(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static int[] getDenominations() {
        return Arrays.stream(values()).mapToInt(Currency::getValue).toArray();
    }
}
