package ru.otus.homework;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class CustomerReverseOrder {

    private final Deque<Customer> customerStack;

    public CustomerReverseOrder() {
        customerStack = new ArrayDeque<>();
    }

    public void add(Customer customer) {
        customerStack.add(Objects.requireNonNull(customer, "Parameter customer must not be null."));
    }

    public Customer take() {
        return customerStack.pollLast();
    }
}
