package atm.service;

public interface ATM {
    int getBalance();

    String putCache(int amount);

    String withdraw(int amount);
}
