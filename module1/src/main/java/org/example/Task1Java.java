package org.example;

public class Task1Java {

    private static final int PORT_CAPACITY = 50;

    public static void main(String[] args) {
        Port port = new Port(PORT_CAPACITY);
        for (int i = 0; i < 5; ++i) {
            new Thread(new Dock(port), "Dock " + (i + 1)).start();
        }
    }
}