package org.example;

public class CannonMultiplying {
    private static int[][] a;
    private static int[][] b;
    private static int process_amount;
    private static int[][] res;

    public static int[][] multiply(int[][] a, int[][] b, int process_amount){
        CannonMultiplying.a = a;
        CannonMultiplying.b = b;
        CannonMultiplying.process_amount = process_amount;

        res = new int[a.length][a.length];

        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < a.length; j++){
                res[i][j] = 0;
            }
        }

        Thread[] tasks = new Thread[process_amount];
        for(int i = 0; i < tasks.length; i++){
            tasks[i] = new Thread(new Task(i));
        }

        for (Thread task : tasks) {
            task.start();
        }

        for (Thread task : tasks) {
            try {
                task.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    private static class Task implements Runnable{

        private final int part_index;
        public Task(int part_index){
            this.part_index = part_index;
        }

        @Override
        public void run() {
            int pivot = (int) Math.ceil(a.length / (double) process_amount);
            for (int row = part_index * pivot; row < (part_index + 1) * pivot && row < a.length; row++) {
                int a_j = row;
                int b_i = row;
                for (int counter = 0; counter < a.length; counter++) {
                    for (int i = 0; i < a.length; i++) {
                        res[row][i] += a[row][a_j] * b[b_i][i];
                    }

                    a_j = (a.length + a_j - 1) % a.length;
                    b_i = (a.length + b_i - 1) % a.length;
                }
            }
        }
    }
}