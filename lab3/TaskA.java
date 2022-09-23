import java.util.Scanner;

public class TaskA {

    private static Pot pot;

    private static final AtomicInteger bearSemaphore = new AtomicInteger();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter capacity (> 0): ");
        int capacity = scanner.nextInt();
        if(capacity <= 0) {
            throw new RuntimeException("Capacity of pot cannot be less than or equal zero");
        }
        pot = new Pot(capacity);
        System.out.print("Enter bees amount (> 0): ");
        int beesAmount = scanner.nextInt();
        if(beesAmount <= 0) {
            throw new RuntimeException("Bees amount cannot be less than or equal zero");
        }
        for(int i = 0; i < beesAmount; ++i) {
            new Thread(new BeeRunnable(), "Bee " + (i + 1)).start();
        }
        new Thread(new BearRunnable(), "Bear").start();
    }

    private static class Pot {
        private final int capacity;
        private int currentLevel;
        public Pot(int capacity) {
            this.capacity = capacity;
            this.currentLevel = 0;
        }

        public synchronized void addHoney() {
            ++currentLevel;
            System.out.println(Thread.currentThread().getName() + " add honey to pot (" + currentLevel + "/" + capacity + ")");
            if(currentLevel == capacity) {
                bearSemaphore.set(1);
            }
        }

        public synchronized void clearPot() {
            System.out.println(Thread.currentThread().getName() + " clears pot");
            currentLevel = 0;
            bearSemaphore.set(0);
        }
    }

    private static class BeeRunnable implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                while (bearSemaphore.equals(1)) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " sleeping");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                pot.addHoney();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private static class BearRunnable implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                while (bearSemaphore.equals(0)) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " sleeping");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                System.out.println(Thread.currentThread().getName() + " wakes up");
                pot.clearPot();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private static class AtomicInteger {
        int value;

        public AtomicInteger(int value) {
            this.value = value;
        }

        public AtomicInteger() {
            this(0);
        }

        public synchronized void set(int value) {
            this.value = value;
        }

        public synchronized boolean equals(int value) {
            return this.value == value;
        }
    }
}
