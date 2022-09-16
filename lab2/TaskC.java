import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class TaskC {
    public static void main(String[] args) {
        int monksAmount;
        System.out.print("Enter monks amount (should be a power of 2): ");
        monksAmount = new Scanner(System.in).nextInt();
        int[] monastery1Monks = new int[monksAmount];
        int[] monastery2Monks = new int[monksAmount];
        fillMonksPowers(monastery1Monks, monastery2Monks);
        MonkFightTask monkFightTask = new MonkFightTask(monastery1Monks, monastery2Monks);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Winner winner = forkJoinPool.invoke(monkFightTask);
        System.out.println("Winner from monastery " + winner.monasteryNum + ", with power " + winner.powerAmount);
    }

    private static void fillMonksPowers(int[] monastery1Monks, int[] monastery2Monks) {
        SecureRandom random = new SecureRandom();
        for(int i = 0; i < monastery1Monks.length; ++i) {
            monastery1Monks[i] = random.nextInt(10 * monastery1Monks.length);
        }
        for(int i = 0; i < monastery2Monks.length; ++i) {
            monastery2Monks[i] = random.nextInt(10 * monastery1Monks.length);
        }
        System.out.println("Monastery 1:");
        System.out.println(
                String.join(", ", Arrays.stream(monastery1Monks).mapToObj(String::valueOf).toArray(String[]::new))
        );
        System.out.println("Monastery 2:");
        System.out.println(
                String.join(", ", Arrays.stream(monastery2Monks).mapToObj(String::valueOf).toArray(String[]::new))
        );
    }

    private static class MonkFightTask extends RecursiveTask<Winner> {

        private final int[] monastery1Monks;
        private final int[] monastery2Monks;

        public MonkFightTask(int[] monastery1Monks, int[] monastery2Monks) {
            this.monastery1Monks = monastery1Monks;
            this.monastery2Monks = monastery2Monks;
        }

        @Override
        protected Winner compute() {
            if(monastery1Monks.length == 1 && monastery2Monks.length == 1) {
                if(monastery1Monks[0] >= monastery2Monks[0]) {
                    return new Winner(1, monastery1Monks[0]);
                } else {
                    return new Winner(2, monastery2Monks[0]);
                }
            }
            MonkFightTask startMonksFightTask = new MonkFightTask(
                    Arrays.copyOfRange(monastery1Monks, 0, monastery1Monks.length / 2),
                    Arrays.copyOfRange(monastery2Monks, 0, monastery2Monks.length / 2)
            );
            MonkFightTask trailingMonksFightTask = new MonkFightTask(
                    Arrays.copyOfRange(monastery1Monks, monastery1Monks.length / 2, monastery1Monks.length),
                    Arrays.copyOfRange(monastery2Monks, monastery2Monks.length / 2, monastery2Monks.length)
            );
            startMonksFightTask.fork();
            trailingMonksFightTask.fork();
            Winner startWinner = startMonksFightTask.join();
            Winner trailingWinner = trailingMonksFightTask.join();
            if(startWinner.powerAmount >= trailingWinner.powerAmount) {
                return startWinner;
            } else {
                return trailingWinner;
            }
        }
    }

    private static class Winner {
        int monasteryNum;
        int powerAmount;

        public Winner(int monasteryNum, int powerAmount) {
            this.monasteryNum = monasteryNum;
            this.powerAmount = powerAmount;
        }
    }
}
