import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskB {

    private static AtomicBoolean programFinished = new AtomicBoolean(false);

    private static String[] lines = new String[4];

    private static final CyclicBarrier barrier = new CyclicBarrier(4, new BarrierResultRunnable());

    public static void main(String[] args) {
        lines[0] = "ABCD";
        lines[1] = "BAAD";
        lines[2] = "DDAA";
        lines[3] = "ABAB";
        for(int i = 0; i < 4; ++i) {
            new Thread(new LineRunnable(i)).start();
        }
    }

    private record LineRunnable(int num) implements Runnable {

        @Override
            public void run() {
                while (!programFinished.get()) {
                    barrier.reset();
                    String line = lines[num];
                    SecureRandom secureRandom = new SecureRandom(
                            ByteBuffer.allocate(Long.BYTES).putLong(System.currentTimeMillis()).array()
                    );
                    StringBuilder sb = new StringBuilder();
                    for (char ch : line.toCharArray()) {
                        boolean shouldReplace = secureRandom.nextBoolean();
                        sb.append(convertChar(ch, shouldReplace));
                    }
                    lines[num] = sb.toString();
                    try {
                        barrier.await();
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        return;
                    } catch (BrokenBarrierException ignored) {}
                }
            }

            private char convertChar(char ch, boolean shouldReplace) {
                if (!shouldReplace) {
                    return ch;
                }
                return switch (ch) {
                    case 'A' -> 'C';
                    case 'C' -> 'A';
                    case 'B' -> 'D';
                    case 'D' -> 'B';
                    default -> throw new RuntimeException();
                };
            }
        }

    private static class BarrierResultRunnable implements Runnable {
        @Override
        public void run() {
            System.out.print("Checking barrier result: ");
            System.out.println(Arrays.toString(lines));
            Map<Integer, Integer> aMap = new HashMap<>();
            Map<Integer, Integer> bMap = new HashMap<>();
            Arrays.stream(lines).forEach(line -> {
                        int aCount = (int) line.chars().filter(character -> character == 'A').count();
                        int bCount = (int) line.chars().filter(character -> character == 'B').count();
                        aMap.put(aCount, aMap.getOrDefault(aCount, 0) + 1);
                        bMap.put(bCount, bMap.getOrDefault(bCount, 0) + 1);
                    }
            );
            if(aMap.values().stream().anyMatch(val -> val >= 3) && aMap.values().stream().anyMatch(val -> val >= 3)) {
                System.out.println("Result is true");
                programFinished.set(true);
            } else {
                System.out.println("Result is false");
            }
        }
    }
}
