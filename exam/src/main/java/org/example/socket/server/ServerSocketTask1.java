package org.example.socket.server;

import com.google.gson.Gson;
import org.example.dao.Dao;
import org.example.data.Student;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ServerSocketTask1 {
    private static final Dao dao;
    private static Socket clientSocket; //сокет для общения
    private static ServerSocket server; // серверсокет
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    static {
        try {
            dao = new Dao();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {
            server = new ServerSocket(4004);
            try {
                clientSocket = server.accept();
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                while (true) {
                    String command = in.readLine();
                    if(command.equals("stop")) {
                        break;
                    } else if(command.contains("addStudent")) {
                        Student student = new Gson().fromJson(command.split(" ")[1], Student.class);
                        dao.addStudent(student);
                        out.write("Added\n");
                    } else if(command.contains("removeStudent")) {
                        int id = Integer.parseInt(command.split(" ")[1]);
                        dao.removeStudent(id);
                        out.write("Removed\n");
                    } else if(command.contains("getStudent")) {
                        int id = Integer.parseInt(command.split(" ")[1]);
                        Optional<Student> student = dao.getStudent(id);
                        if(student.isPresent()) {
                            out.write("student " + new Gson().toJson(student.get()) + "\n");
                        } else {
                            out.write("Not found\n");
                        }
                    } else if(command.contains("getFacultyStudents")) {
                        String faculty = command.split(" ")[1];
                        List<Student> students = dao.getFacultyStudents(faculty);
                        out.write("students " + new Gson().toJson(students) + "\n");
                    } else if(command.contains("getFacultyYearStudents")) {
                        Map<String, List<Student>> facultyYearStudents = dao.getFacultyYearStudents();
                        out.write("studentsMap " + new Gson().toJson(facultyYearStudents) + "\n");
                    } else if(command.contains("getStudentsBornAfterYear")) {
                        int year = Integer.parseInt(command.split(" ")[1]);
                        List<Student> students = dao.getStudentsBornAfterYear(year);
                        out.write("students" + new Gson().toJson(students) + "\n");
                    } else if(command.contains("getStudentsByGroup")) {
                        String group = command.split(" ")[1];
                        List<Student> students = dao.getStudentsByGroup(group);
                        out.write("students " + new Gson().toJson(students) + "\n");
                    }
                    out.flush();
                }
            } finally {
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
