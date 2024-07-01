package atm.service;

import atm.exeptions.InsufficientFundsException;
import atm.model.BanknoteStorage;
import atm.model.Currency;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BasicATM implements ATM {
    private static final BanknoteStorage banknoteStorage = new BanknoteStorage();

    public static void main(String[] args) {
        BasicATM basicATM = new BasicATM();
        Scanner scanner = new Scanner(System.in);

        System.out.println(basicATM.putCache(scanner.nextInt()));
        System.out.println(basicATM.withdraw(scanner.nextInt()));
        System.out.println(basicATM.getBalance());
    }

    @Override
    public int getBalance() {
        return banknoteStorage.getBalance().entrySet().stream()
                .mapToInt(value -> value.getKey() * value.getValue())
                .sum();
    }

    @Override
    public String putCache(int amount) throws InsufficientFundsException {
        int money = amount;
        Map<Integer, Integer> balance = banknoteStorage.getBalance();

        while (amount > 0) {
            if (amount >= Currency.FIVE_THOUSAND.getValue()) {
                int count = amount / Currency.FIVE_THOUSAND.getValue();
                balance.put(Currency.FIVE_THOUSAND.getValue(), balance.getOrDefault(Currency.FIVE_THOUSAND.getValue(), 0) + count);
                amount -= count * Currency.FIVE_THOUSAND.getValue();
            } else if (amount >= Currency.TWO_THOUSAND.getValue()) {
                int count = amount / Currency.TWO_THOUSAND.getValue();
                balance.put(Currency.TWO_THOUSAND.getValue(), balance.getOrDefault(Currency.TWO_THOUSAND.getValue(), 0) + count);
                amount -= count * Currency.TWO_THOUSAND.getValue();
            } else if (amount >= Currency.ONE_THOUSAND.getValue()) {
                int count = amount / Currency.ONE_THOUSAND.getValue();
                balance.put(Currency.ONE_THOUSAND.getValue(), balance.getOrDefault(Currency.ONE_THOUSAND.getValue(), 0) + count);
                amount -= count * Currency.ONE_THOUSAND.getValue();
            } else if (amount >= Currency.FIVE_HUNDRED.getValue()) {
                int count = amount / Currency.FIVE_HUNDRED.getValue();
                balance.put(Currency.FIVE_HUNDRED.getValue(), balance.getOrDefault(Currency.FIVE_HUNDRED.getValue(), 0) + count);
                amount -= count * Currency.FIVE_HUNDRED.getValue();
            } else {
                throw new InsufficientFundsException("Amount cannot be split into 500, 1000, 2000, or 5000 parts");
            }
        }
        return "Сумма " + money + " внесена";
    }

    @Override
    public String withdraw(int amount) throws InsufficientFundsException {
        int money = amount;
        Map<Integer, Integer> balance = banknoteStorage.getBalance();
        Map<Integer, Integer> toWithdraw = new HashMap<>();

        for (int denomination : new int[]{Currency.FIVE_THOUSAND.getValue(), Currency.TWO_THOUSAND.getValue(),
                Currency.ONE_THOUSAND.getValue(), Currency.FIVE_HUNDRED.getValue()}) {
            while (amount >= denomination && balance.getOrDefault(denomination, 0) > 0) {
                balance.put(denomination, balance.get(denomination) - 1);
                toWithdraw.put(denomination, toWithdraw.getOrDefault(denomination, 0) + 1);
                amount -= denomination;
            }
        }

        if (amount > 0) {
            toWithdraw.forEach((denomination, count) -> {
                balance.put(denomination, balance.getOrDefault(denomination, 0) + count);
            });
            throw new InsufficientFundsException("Недостаточно средств для снятия суммы: " + money);
        }

        return "Сумма " + money + " изъята";
    }
}
