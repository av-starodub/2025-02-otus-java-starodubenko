package ru.otus.homework;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class CustomerReverseOrder {
    private final Deque<Customer> customerDeque;

    public CustomerReverseOrder() {
        customerDeque = new ArrayDeque<>();
    }

    public void add(Customer customer) {
        customerDeque.add(Objects.requireNonNull(customer, " customer must not be null."));
    }

    public Customer take() {
        return customerDeque.pollLast();
    }
}
