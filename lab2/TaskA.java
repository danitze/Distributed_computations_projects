import java.security.SecureRandom;

public class TaskA {
    private static final SynchronizedInteger bearFound = new SynchronizedInteger(0);
    private static final SynchronizedInteger currentRow = new SynchronizedInteger(0);

    private static final int[][] forest = new int[100][100];


    public static void main(String[] args) {
        putBearIntoForest();
        for (int i = 0; i < 4; ++i) {
            new Thread(new BeeRunnable(), "Bees " + i).start();
        }
    }

    private static void putBearIntoForest() {
        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                forest[i][j] = 0;
            }
        }
        SecureRandom random = new SecureRandom();
        int xCord = random.nextInt(100);
        int yCord = random.nextInt(100);
        System.out.println("Bear is in [" + xCord + "][" + yCord + "]");
        forest[xCord][yCord] = 1;
    }

    private static class BeeRunnable implements Runnable {
        @Override
        public void run() {
            int row;
            while ((row = currentRow.getAndIncrement()) < 100) {
                if (bearFound.getValue() == 1) {
                    break;
                }
                System.out.println(Thread.currentThread().getName() + " begins scanning row " + row);
                for (int i = 0; i < 100; ++i) {
                    if (forest[row][i] == 1) {
                        System.out.println(Thread.currentThread().getName() + " found the bear!");
                        bearFound.setValue(1);
                        break;
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    private static class SynchronizedInteger {
        private int value;

        public SynchronizedInteger(int value) {
            this.value = value;
        }

        public synchronized int getValue() {
            return value;
        }

        public synchronized void setValue(int value) {
            this.value = value;
        }

        public synchronized int getAndIncrement() {
            return value++;
        }
    }
}
