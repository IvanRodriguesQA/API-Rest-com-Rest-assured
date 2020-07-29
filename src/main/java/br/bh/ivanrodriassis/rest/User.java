package br.bh.ivanrodriassis.rest;

public class User {
// 3 atributos que são enviados quando salva
	private String name;
	private Integer age;
//	private Double salary;
	
	// Construtor
	public User(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;	
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
		
}
