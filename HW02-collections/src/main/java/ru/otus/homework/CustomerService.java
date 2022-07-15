package ru.otus.homework;


import java.util.*;

public class CustomerService {
    private final NavigableMap<Customer, String> customers;

    public CustomerService() {
        customers = new TreeMap<>();
    }

    public Map.Entry<Customer, String> getSmallest() {
        return doCopy(customers.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return doCopy(customers.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }

    private Map.Entry<Customer, String> doCopy(Map.Entry<Customer, String> customerDataEntry) {
        if (Objects.isNull(customerDataEntry)) {
            return null;
        }
        var customerSource = customerDataEntry.getKey();
        var customerCopy = new Customer(customerSource.getId(), customerSource.getName(), customerSource.getScores());
        return new AbstractMap.SimpleEntry<>(customerCopy, customerDataEntry.getValue());
    }
}
