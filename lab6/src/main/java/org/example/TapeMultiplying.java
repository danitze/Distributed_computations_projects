package org.example;

public class TapeMultiplying {

    private static int[][] a;
    private static int[][] b;
    private static int process_amount;
    private static int[][] res;

    public static int[][] multiply(int[][] a, int[][] b, int process_amount){
        TapeMultiplying.a = a;
        TapeMultiplying.b = b;
        TapeMultiplying.process_amount = process_amount;

        res = new int[a.length][a.length];

        for(int i = 0; i < a.length; i++){
            for(int j = 0; j < a.length; j++){
                res[i][j] = 0;
            }
        }

        Thread[] tapes = new Thread[process_amount];
        for(int i = 0; i < tapes.length; i++){
            tapes[i] = new Thread(new Tape(i));
        }

        for(int i = 0; i < tapes.length; i++){
            tapes[i].start();
        }

        for(int i = 0; i < tapes.length; i++){
            try {
                tapes[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    private static class Tape implements Runnable{

        private final int part_index;
        public Tape(int part_index){
            this.part_index = part_index;
        }

        @Override
        public void run() {

            int pivot = (int) Math.ceil(a.length / (double) process_amount);
            for (int row = part_index * pivot; row < (part_index + 1) * pivot && row < a.length; row++) {
                int index = row;
                for (int counter = 0; counter < a.length; counter++){
                    for (int j = 0; j < a.length; j++) {
                        res[row][index] += a[row][j] * b[j][index];
                    }
                    index = (index + 1) % a.length;
                }
            }
        }
    }


}