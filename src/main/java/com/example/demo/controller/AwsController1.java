package com.example.demo.controller;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.database.Database;
import com.example.demo.entity.Student;

@RestController
@RequestMapping("/aws")
@Configuration
public class AwsController1 {

	
	@Autowired
	Database db;
	
	@Bean
	public Supplier<String> getMessage(){
		
		
		return ()->"app deployed in aws services-----";
	}
	
	@Bean
	public Function<Student, String> student1(){
		
		Function<Student, String> f1= (Stu) ->{db.setStudentList(Stu);
		return "Student Data has been saved sucessfully-----------";
		}; 
		
		return f1 ;
	}
	
	@Bean
	public Supplier<List<Student>>   getAllData(){
		
		
		return ()-> db.getData();
	}
	
}
