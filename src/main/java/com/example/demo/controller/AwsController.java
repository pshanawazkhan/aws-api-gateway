package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.database.Database;
import com.example.demo.entity.Student;

@RestController
@RequestMapping("/main")
public class AwsController {

	@Autowired
	Database  db;
	
	@GetMapping("/get")
	public String getMessage() {
		return "application running in the aws.....";
	}
	
	@GetMapping("/data")
	public 	List<Student>  getDetails(){
		
		
		return db.getData();
	}
	
	@PostMapping("/save")
	public String saveStudent(@RequestBody Student student) {
		
		db.setStudentList(student);
		
		return "Student details has been added";
	}
	
	@DeleteMapping("/delete/{id}")
    public String DeleteData(@PathVariable("id") int id) {
		
		
		return "Student details with id " + id + " has been deleted";
	}
	
}
