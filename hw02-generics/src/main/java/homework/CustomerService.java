package homework;

import java.util.*;

public class CustomerService {
    private final TreeMap<Customer, String> customerMap;

    public CustomerService() {
        this.customerMap = new TreeMap<>();
    }

    public Map.Entry<Customer, String> getSmallest() {
        Customer customer = customerMap.firstKey();
        return new AbstractMap.SimpleEntry<>(new Customer(customer.getId(), customer.getName(), customer.getScores()),
                customerMap.get(customer));
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextEntry = customerMap.higherEntry(customer);
        if (nextEntry != null) {
            return new AbstractMap.SimpleEntry<>(new Customer(nextEntry.getKey().getId(), nextEntry.getKey().getName(),
                    nextEntry.getKey().getScores()), nextEntry.getValue());
        }
        return null;
    }

    public void add(Customer customer, String data) {
        customerMap.put(customer, data);
    }
}
