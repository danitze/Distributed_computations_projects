import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class TaskB {
    private static final Semaphore hairdresserSemaphore = new Semaphore(1);
    private static final Semaphore clientsSemaphore = new Semaphore(1, true);

    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    public static void main(String[] args) {
        new Thread(new BarbershopRunnable()).start();
    }

    private static class HairdresserRunnable implements Runnable {
        @Override
        public void run() {
            try {
                hairdresserSemaphore.acquire();
            } catch (InterruptedException ignored) {}
            while (!Thread.interrupted()) {
                try {
                    hairdresserSemaphore.acquire();
                    cyclicBarrier.reset();

                    System.out.println(Thread.currentThread().getName() + " is cutting hair");
                    Thread.sleep(2000);
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    return;
                }
            }
        }
    }

    private static class ClientRunnable implements Runnable {
        @Override
        public void run() {
            try {
                clientsSemaphore.acquire();
                hairdresserSemaphore.release();
                System.out.println(Thread.currentThread().getName() + "'s hair is being cut");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + " goes away");
                clientsSemaphore.release();
            } catch (InterruptedException | BrokenBarrierException ignored) {}
        }
    }

    private static class BarbershopRunnable implements Runnable {
        @Override
        public void run() {
            new Thread(new HairdresserRunnable(), "Hairdresser thread").start();
            for(int i = 0; i < 5; ++i) {
                new Thread(new ClientRunnable(), "Client " + (i + 1) + " thread").start();
            }
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            new Thread(new ClientRunnable(), "Client 6 thread").start();
        }
    }
}
