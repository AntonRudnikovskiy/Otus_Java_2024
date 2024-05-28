package homework;

import java.util.*;

public class CustomerService {
    private final TreeMap<Customer, String> customerMap;

    public CustomerService() {
        this.customerMap = new TreeMap<>();
    }

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> smallestScore = customerMap.entrySet().stream()
                .min(Comparator.comparingLong(c -> c.getKey().getScores()))
                .orElseThrow();
        Customer smallestCustomerCopy = getCustomer(smallestScore);
        return new AbstractMap.SimpleEntry<>(smallestCustomerCopy, smallestScore.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextEntry = customerMap.higherEntry(customer);
        if (nextEntry != null) {
            Customer nextCustomerCopy = getCustomer(nextEntry);
            return new AbstractMap.SimpleEntry<>(nextCustomerCopy, nextEntry.getValue());
        }
        return null;
    }

    private Customer getCustomer(Map.Entry<Customer, String> nextEntry) {
        return new Customer(nextEntry.getKey().getId(), nextEntry.getKey().getName(), nextEntry.getKey().getScores());
    }

    public void add(Customer customer, String data) {
        customerMap.put(customer, data);
    }
}
