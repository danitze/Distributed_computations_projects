package main;

import main.data.PhoneBookRecord;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class TaskA {
    private static final String FILE_NAME = "telephones.txt";
    private static final CustomReadWriteLock readWriteLock = new CustomReadWriteLock();

    public static void main(String[] args) throws InterruptedException, IOException {
        File file = new File(FILE_NAME);
        file.delete();
        file.createNewFile();
        String[] phonesToSeek = new String[] {"1", "2", "3"};
        PhoneBookRecord[] addRecords = new PhoneBookRecord[] {
                new PhoneBookRecord("A", "1"),
                new PhoneBookRecord("B", "2"),
                new PhoneBookRecord("C", "3")
        };

        new Thread(new RecordAdderRunnable(new PhoneBookRecord[] {
                new PhoneBookRecord("A", "1"),
                new PhoneBookRecord("B", "2")
        })).start();
        new Thread(new RecordDeleterRunnable(new PhoneBookRecord[]{new PhoneBookRecord("A", "1")}));
        new Thread(new NameSeekerRunnable(new String[] {"1", "2"})).start();
        new Thread(new PhoneSeekerRunnable(new String[] {"A", "B"})).start();
    }

    private static class PhoneSeekerRunnable implements Runnable {
        private final String[] names;

        public PhoneSeekerRunnable(String[] names) {
            this.names = names;
        }

        @Override
        public void run() {
            for(String name: names) {
                try {
                    readWriteLock.lockRead();
                    boolean recordFound = false;
                    List<String> lines = Files.readAllLines(Path.of(FILE_NAME));
                    for(String line: lines) {
                        PhoneBookRecord record = PhoneBookRecord.deserialize(line);
                        if(record.getName().equals(name)) {
                            recordFound = true;
                            System.out.println("Found record by name " + name + ". Phone number is " + record.getPhoneNumber());
                        }
                    }
                    if(!recordFound) {
                        System.out.println("Could not find record with name " + name);
                    }
                    readWriteLock.unlockRead();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {}
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static class NameSeekerRunnable implements Runnable {

        private final String[] phoneNumbers;

        public NameSeekerRunnable(String[] phoneNumbers) {
            this.phoneNumbers = phoneNumbers;
        }

        @Override
        public void run() {
            for(String phoneNumber: phoneNumbers) {
                try {
                    readWriteLock.lockRead();
                    boolean recordFound = false;
                    List<String> lines = Files.readAllLines(Path.of(FILE_NAME));
                    for(String line: lines) {
                        PhoneBookRecord record = PhoneBookRecord.deserialize(line);
                        if(record.getPhoneNumber().equals(phoneNumber)) {
                            recordFound = true;
                            System.out.println("Found record by number " + phoneNumber + ". Name is " + record.getName());
                        }
                    }
                    if(!recordFound) {
                        System.out.println("Could not find record with phone " + phoneNumber);
                    }
                    readWriteLock.unlockRead();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {}
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static class RecordAdderRunnable implements Runnable {

        private final PhoneBookRecord[] records;

        public RecordAdderRunnable(PhoneBookRecord[] records) {
            this.records = records;
        }

        @Override
        public void run() {
            for(PhoneBookRecord record: records) {
                try {
                    readWriteLock.lockWrite();
                    Files.writeString(Path.of(FILE_NAME), record.serialize() + '\n', StandardOpenOption.APPEND);
                    readWriteLock.unlockWrite();
                    Thread.sleep(2000);
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static class RecordDeleterRunnable implements Runnable {

        private final PhoneBookRecord[] records;

        public RecordDeleterRunnable(PhoneBookRecord[] records) {
            this.records = records;
        }

        @Override
        public void run() {
            for(PhoneBookRecord record: records) {
                try {
                    readWriteLock.lockWrite();
                    String tempFileName = "temp.txt";
                    File tempFile = new File(tempFileName);
                    if(!tempFile.createNewFile()) {
                        throw new RuntimeException("Could not create temp file");
                    }
                    List<String> lines = Files.readAllLines(Path.of(FILE_NAME));
                    for(String line: lines) {
                        if(!PhoneBookRecord.deserialize(line).equals(record)) {
                            Files.writeString(Path.of(tempFileName), line, StandardOpenOption.APPEND);
                        }
                    }
                    File file = new File(FILE_NAME);
                    if(!(file.delete() && tempFile.renameTo(file))) {
                        throw new RuntimeException("Could not rename temp file to file");
                    }
                    readWriteLock.unlockWrite();
                    Thread.sleep(2000);
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}
