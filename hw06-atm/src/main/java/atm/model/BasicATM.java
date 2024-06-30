package atm.model;

import atm.service.BalanceInquiryServiceImpl;
import atm.service.DepositServiceImpl;
import atm.service.WithdrawalServiceImpl;

import java.util.Map;
import java.util.Scanner;

public class BasicATM extends ATM {
    public BasicATM(int id, String location) {
        super(id, location);
    }

    public static void main(String[] args) {
        BasicATM basicATM = new BasicATM(1, "Moscow");

        Scanner scanner = new Scanner(System.in);
        Map<Integer, Integer> balance = basicATM.getBalance();

        DepositServiceImpl depositService = new DepositServiceImpl(balance);
        System.out.println(depositService.putCache(scanner.nextInt()));

        WithdrawalServiceImpl withdrawalService = new WithdrawalServiceImpl(balance);
        System.out.println(withdrawalService.withdraw(scanner.nextInt()));

        BalanceInquiryServiceImpl balanceInquiryService = new BalanceInquiryServiceImpl(balance);
        System.out.println(balanceInquiryService.getBalance());
    }
}
