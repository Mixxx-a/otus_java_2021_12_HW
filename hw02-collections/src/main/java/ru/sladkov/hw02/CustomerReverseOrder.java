package ru.sladkov.hw02;

import java.util.Stack;

public class CustomerReverseOrder {

    private final Stack<Customer> customerList = new Stack<>();

    public void add(Customer customer) {
        customerList.push(customer);
    }

    public Customer take() {
        return customerList.pop();
    }
}
