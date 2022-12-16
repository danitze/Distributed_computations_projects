package org.example.rmi.server;

import org.example.dao.Dao;
import org.example.data.Student;
import org.example.rmi.client.Manager;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ManagerServer implements Manager {

    private static final Dao dao;

    static {
        try {
            dao = new Dao();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addStudent(Student student) throws RemoteException {
        dao.addStudent(student);
    }

    @Override
    public void removeStudent(int id) throws RemoteException {
        dao.removeStudent(id);
    }

    @Override
    public Optional<Student> getStudent(int id) throws RemoteException {
        return dao.getStudent(id);
    }

    @Override
    public List<Student> getFacultyStudents(String faculty) throws RemoteException {
        return dao.getFacultyStudents(faculty);
    }

    @Override
    public Map<String, List<Student>> getFacultyYearStudents() throws RemoteException {
        return dao.getFacultyYearStudents();
    }

    @Override
    public List<Student> getStudentsBornAfterYear(int year) throws RemoteException {
        return dao.getStudentsBornAfterYear(year);
    }

    @Override
    public List<Student> getStudentsByGroup(String group) throws RemoteException {
        return dao.getStudentsByGroup(group);
    }
}
