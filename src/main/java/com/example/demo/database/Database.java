package com.example.demo.database;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.entity.Student;

import jakarta.annotation.PostConstruct;

@Component
public class Database {

    private List<Student> studentList;

    @PostConstruct
    public void init() {
        // Initializing the list with actual ArrayList implementation
        studentList = new ArrayList<>();

        // Adding students to the list
        studentList.add(new Student(1, "shanawaz", 568879));
        studentList.add(new Student(3, "test-1", 89000));
        studentList.add(new Student(2, "test-2", 98000));
        studentList.add(new Student(4, "vh", 78950));
    }

    public List<Student> getData() {
        return studentList;
    }

	public void setStudentList(Student stu ) {
	
    this.studentList.add(stu);
    
}
}