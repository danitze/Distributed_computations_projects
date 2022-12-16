package org.example.socket.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.data.Student;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.*;

public class ClientSocketTask1 {

    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {
            try {
                clientSocket = new Socket("localhost", 4004); // этой строкой мы запрашиваем
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                Queue<String> commandsQueue = new LinkedList<>();
                commandsQueue.offer("getFacultyYearStudents");
                commandsQueue.offer("getFacultyStudents FKNK");
                commandsQueue.offer("stop");
                while (!commandsQueue.isEmpty()) {
                    String message = commandsQueue.poll();
                    writeMessage(out, message);
                    parseMessage(in.readLine());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    private static void parseMessage(String message) {
        if(message == null) {
            return;
        }
        if(message.contains("students")) {
            int index = message.indexOf(' ');
            if(message.toLowerCase().contains("map")) {
                Type mapType = new TypeToken<Map<String,List<Student>>>(){}.getType();
                Map<String, List<Student>> studentsMap = new Gson().fromJson(message.substring(index + 1), mapType);
                System.out.println(studentsMap);
            } else {
                Type listType = new TypeToken<List<Student>>(){}.getType();
                List<Student> students = new Gson().fromJson(message.substring(index + 1), listType);
                System.out.println(students);
            }
        } else if(message.contains("student")) {
            int index = message.indexOf(' ');
            System.out.println(new Gson().fromJson(message.substring(index + 1), Student.class));
        } else {
            System.out.println(message);
        }
    }

    private static void writeMessage(BufferedWriter bufferedWriter, String message) throws IOException {
        bufferedWriter.write(message + "\n");
        bufferedWriter.flush();
    }
}
