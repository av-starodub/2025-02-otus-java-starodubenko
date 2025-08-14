package ru.otus.homework;

import java.util.*;

public class CustomerService {

    private final NavigableMap<Customer, String> sortedByScoreCustomers;

    public CustomerService() {
        sortedByScoreCustomers = new TreeMap<>();
    }

    public Map.Entry<Customer, String> getSmallest() {
        var smallest = sortedByScoreCustomers.firstEntry();
        if (smallest == null) {
            return null;
        }
        return doCopy(smallest);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var next = sortedByScoreCustomers.higherEntry(customer);
        if (next == null) {
            return null;
        }
        return doCopy(next);
    }

    public void add(Customer customer, String data) {
        sortedByScoreCustomers.put(customer, data);
    }

    private Map.Entry<Customer, String> doCopy(Map.Entry<Customer, String> customerDataEntry) {
        var customerSource = customerDataEntry.getKey();
        var customerCopy = new Customer(customerSource.getId(), customerSource.getName(), customerSource.getScores());
        return new AbstractMap.SimpleEntry<>(customerCopy, customerDataEntry.getValue());
    }
}
