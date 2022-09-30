package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TaskB {

    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static int[][] garden;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter garden height: ");
        int height = scanner.nextInt();
        if(height < 1) {
            throw new IllegalArgumentException("Garden height should be positive");
        }
        System.out.println("Enter garden width: ");
        int width = scanner.nextInt();
        if(width < 1) {
            throw new IllegalArgumentException("Garden width should be positive");
        }
        garden = new int[height][width];
        for (int[] line : garden) {
            Arrays.fill(line, 1);
        }
        new Thread(new GardenerRunnable()).start();
        new Thread(new NatureRunnable()).start();
        new Thread(new FileWriterRunnable()).start();
        new Thread(new ConsoleWriterRunnable()).start();
    }


    private static class GardenerRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                readWriteLock.writeLock().lock();
                for(int i = 0; i < garden.length; ++i) {
                    for(int j = 0; j < garden[i].length; ++j) {
                        if(garden[i][j] == 0) {
                            garden[i][j] = 1;
                        }
                    }
                }
                readWriteLock.writeLock().unlock();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private static class NatureRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                readWriteLock.writeLock().lock();
                SecureRandom secureRandom = new SecureRandom(
                        ByteBuffer.allocate(8).putLong(System.currentTimeMillis()).array()
                );
                for(int i = 0; i < garden.length; ++i) {
                    for(int j = 0; j < garden[i].length; ++j) {
                        if(garden[i][j] == 0) {
                            continue;
                        }
                        if(secureRandom.nextInt(2) == 0) {
                            garden[i][j] = 0;
                        }
                    }
                }
                readWriteLock.writeLock().unlock();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private static class FileWriterRunnable implements Runnable {
        @Override
        public void run() {
            for(int counter = 1; ; ++counter) {
                readWriteLock.readLock().lock();
                try(BufferedWriter br = new BufferedWriter(new FileWriter("garden.txt", true))) {
                    br.write("Snapshot " + counter);
                    br.newLine();
                    for(int i = 0; i < garden.length; ++i) {
                        for(int j = 0; j < garden.length; ++j) {
                            br.write(garden[i][j] + " ");
                        }
                        br.newLine();
                    }
                    br.newLine();
                    br.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                readWriteLock.readLock().unlock();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private static class ConsoleWriterRunnable implements Runnable {
        @Override
        public void run() {
            for(int counter = 1; ; ++counter) {
                readWriteLock.readLock().lock();
                System.out.println("Snapshot " + counter);
                for(int i = 0; i < garden.length; ++i) {
                    for(int j = 0; j < garden.length; ++j) {
                        System.out.print(garden[i][j] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
                readWriteLock.readLock().unlock();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

}
