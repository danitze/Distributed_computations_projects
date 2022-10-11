package task_a;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskA {
    private static final int RECRUITS_AMOUNT = 150;
    private static final int PARTS_COUNT = 3;
    private static final Thread[] threads = new Thread[PARTS_COUNT];
    private static final boolean[] partsFinishStatus = new boolean[PARTS_COUNT];
    private static final int [] recruits = new int[RECRUITS_AMOUNT];
    private static final Barrier barrier = new Barrier(PARTS_COUNT);

    public static void main(String[] args) {
        initRecruits();
        initThreads();
        System.out.println("Result: " + Arrays.toString(recruits));
    }

    private static void initRecruits() {
        SecureRandom random = new SecureRandom(
                ByteBuffer.allocate(Long.BYTES).putLong(System.currentTimeMillis()).array()
        );
        for(int i = 0; i < recruits.length; ++i) {
            if(random.nextBoolean()) recruits[i] = 1;
            else recruits[i] = 0;

        }
    }

    private static void initThreads() {
        for (int i = 0; i < threads.length; ++i) {
            int leftIndex;
            int rightIndex;
            if (i == 0) {
                leftIndex = 0;
                rightIndex = RECRUITS_AMOUNT / PARTS_COUNT + 1;
            } else if (i == threads.length - 1) {
                leftIndex = i * RECRUITS_AMOUNT / PARTS_COUNT - 1;
                rightIndex = (i + 1) * RECRUITS_AMOUNT / PARTS_COUNT;
            } else {
                leftIndex = i * (RECRUITS_AMOUNT / PARTS_COUNT) - 1;
                rightIndex = (i + 1) * RECRUITS_AMOUNT / PARTS_COUNT + 1;
            }
            threads[i] = new Thread(new RecruitsRunnable(recruits, barrier, i, leftIndex, rightIndex));
            threads[i].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private record RecruitsRunnable(
            int[] recruits,
            Barrier barrier,
            int partIndex,
            int leftIndex,
            int rightIndex
    ) implements Runnable {
            private static final AtomicBoolean isFinished = new AtomicBoolean(false);
        public void run() {
                while (!isFinished.get()) {
                    if (!partsFinishStatus[partIndex]) {
                        if (partIndex == 0) {
                            System.out.println("Current: " + Arrays.toString(recruits));
                        }
                        boolean turnsPerformed = false;
                        for (int i = leftIndex; i < rightIndex - 1; ++i) {
                            if (recruits[i] != recruits[i + 1]) {
                                recruits[i] = 1 - recruits[i];
                                turnsPerformed = true;
                            }
                        }
                        if (!turnsPerformed) {
                            finish();
                        }
                    }
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }

            private void finish() {
                partsFinishStatus[partIndex] = true;
                for (boolean isPartFinished : partsFinishStatus)
                    if (!isPartFinished) {
                        return;
                    }
                isFinished.set(true);
            }
        }
}
