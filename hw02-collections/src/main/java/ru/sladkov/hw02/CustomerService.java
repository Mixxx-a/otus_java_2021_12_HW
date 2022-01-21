package ru.sladkov.hw02;


import java.util.*;

public class CustomerService {

    private final NavigableMap<Customer, String> treeMap = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> currEntry = treeMap.firstEntry();
        return getImmutableEntry(currEntry);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> currEntry = treeMap.higherEntry(customer);
        return getImmutableEntry(currEntry);
    }

    public void add(Customer customer, String data) {
        treeMap.put(customer, data);
    }

    private Map.Entry<Customer, String> getImmutableEntry(Map.Entry<Customer, String> entry) {
        if (entry == null) {
            return null;
        }
        Customer keyCustomer = entry.getKey();
        String valueString = entry.getValue();
        Customer copyCustomer = new Customer(keyCustomer.getId(), keyCustomer.getName(), keyCustomer.getScores());
        return Map.entry(copyCustomer, valueString);
    }
}
