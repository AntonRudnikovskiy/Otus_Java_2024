package atm;

import atm.service.BasicATM;

public class Main {
    public static void main(String[] args) {
        BasicATM atm = new BasicATM();

        System.out.println(atm.putCache(13000));
        System.out.println("Баланс: " + atm.getBalance());
        System.out.println(atm.withdraw(7000));
        System.out.println("Баланс: " + atm.getBalance());
    }
}
