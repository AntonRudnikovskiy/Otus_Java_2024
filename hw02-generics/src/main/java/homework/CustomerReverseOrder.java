package homework;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {
    private Deque<Customer> customers;

    public void add(Customer customer) {
        if (customers == null) {
            customers = new ArrayDeque<>();
        }
        customers.add(customer);
    }

    public Customer take() {
        return customers.pollLast();
    }
}
