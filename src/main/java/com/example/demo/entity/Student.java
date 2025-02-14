package com.example.demo.entity;

import org.springframework.stereotype.Component;

@Component
public class Student {

	
	
	private int id;
	
	private String name;
	
	private double salary;
	
	public Student() {
	}

	public Student(int id, String name, double salary) {
	
		this.id = id;
		this.name = name;
		this.salary = salary;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}
	
}
