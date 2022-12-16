package org.example.rmi.client;


import org.example.data.Student;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Manager extends Remote {

    void addStudent(Student student) throws RemoteException;

    void removeStudent(int id) throws RemoteException;

    Optional<Student> getStudent(int id) throws RemoteException;

    List<Student> getFacultyStudents(String faculty) throws RemoteException;

    Map<String, List<Student>> getFacultyYearStudents() throws RemoteException;

    List<Student> getStudentsBornAfterYear(int year) throws RemoteException;

    List<Student> getStudentsByGroup(String group) throws RemoteException;
}
