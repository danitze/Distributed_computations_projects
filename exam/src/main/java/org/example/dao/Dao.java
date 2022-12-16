package org.example.dao;

import org.example.data.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Dao {
    private final List<Student> students;

    public Dao() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        students = new LinkedList<>();
        students.add(new Student(
                1,
                "Andrieiev",
                "Danylo",
                "Dmytrovich",
                simpleDateFormat.parse("13.05.2003"),
                "Kyiv, Solomjanska str.",
                "+380991112233",
                "FKNK",
                3,
                "IPS-31"
        ));
        students.add(new Student(
                2,
                "Bukhanets",
                "Yehor",
                "Dimitrievich",
                simpleDateFormat.parse("07.06.2003"),
                "Chernihiv",
                "+380992223344",
                "FKNK",
                3,
                "IPS-31"
        ));
        students.add(new Student(
                3,
                "Ivanov",
                "Ivan",
                "Ivanovych",
                simpleDateFormat.parse("01.01.2004"),
                "Lviv",
                "+380993334455",
                "FRECS",
                2,
                "FT-2"
        ));
    }

    public void addStudent(Student student) {
        if(students.stream().noneMatch(stud -> stud.getId() == student.getId())) {
            students.add(student);
        }
    }

    public void removeStudent(int id) {
        students.removeIf(student -> student.getId() == id);
    }

    public Optional<Student> getStudent(int id) {
        return students.stream().filter(student -> student.getId() == id).findAny();
    }

    public List<Student> getFacultyStudents(String faculty) {
        return students.stream()
                .filter(student -> student.getFaculty().equalsIgnoreCase(faculty))
                .collect(Collectors.toList());
    }

    public Map<String, List<Student>> getFacultyYearStudents() {
        return students.stream()
                .collect(groupingBy(student -> student.getFaculty().toLowerCase() + " " + student.getYear()));
    }

    public List<Student> getStudentsBornAfterYear(int year) {
        Calendar calendar = Calendar.getInstance();
        return students.stream().filter(student -> {
            calendar.setTime(student.getDateOfBirth());
            return calendar.get(Calendar.YEAR) > year;
        }).collect(Collectors.toList());
    }

    public List<Student> getStudentsByGroup(String group) {
        return students.stream()
                .filter(student -> student.getGroup().equalsIgnoreCase(group))
                .collect(Collectors.toList());
    }
}
