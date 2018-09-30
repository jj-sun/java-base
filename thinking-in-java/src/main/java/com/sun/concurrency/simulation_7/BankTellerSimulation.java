package com.sun.concurrency.simulation_7;

import java.util.concurrent.ArrayBlockingQueue;

class Customer {
    private final int serviceTime;
    public Customer(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "serviceTime=" + serviceTime +
                '}';
    }
}

class CustomerLine extends ArrayBlockingQueue<Customer> {
    public CustomerLine(int maxLineSize) {
        super(maxLineSize);
    }

    @Override
    public String toString() {
        if(this.size() == 0) {
            return "[Empty]";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(Customer customer : this) {
            stringBuilder.append(customer);
        }
        return stringBuilder.toString();
    }
}

public class BankTellerSimulation {
}
