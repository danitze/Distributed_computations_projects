package org.example;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Port {
    private final int capacity;
    private int currentContainers = 0;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public Port(int capacity) {
        this.capacity = capacity;
    }

    public void addContainers(int amount) {
        lock.lock();
        if(amount > capacity) {
            throw new IllegalArgumentException();
        }
        while (currentContainers + amount > capacity) {
            try {
                condition.await();
            } catch (InterruptedException e) {
                return;
            }
        }
        currentContainers += amount;
        System.out.println("Added " + amount + " to port at " + Thread.currentThread().getName() + " . Now amount is " + currentContainers);
        condition.signalAll();
        lock.unlock();
    }

    public void getContainers(int amount) {
        lock.lock();
        if(amount > capacity) {
            throw new IllegalArgumentException();
        }
        while (amount > currentContainers) {
            try {
                condition.await();
            } catch (InterruptedException e) {
                return;
            }
        }
        currentContainers -= amount;
        System.out.println("Took " + amount + " from port at " + Thread.currentThread().getName() + ". Now amount is " + currentContainers);
        condition.signalAll();
        lock.unlock();
    }

    public int getCapacity() {
        return capacity;
    }
}
