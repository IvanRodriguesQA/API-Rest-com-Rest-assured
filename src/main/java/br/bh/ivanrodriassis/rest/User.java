package br.bh.ivanrodriassis.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)

public class User {
// 3 atributos que são enviados quando salva
	@XmlAttribute
	private Long id;
	private String name;
	private Integer age;
	private Double salary;
	
	// Construtor sem argumentos
	public User() {		
	}
	
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
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", salary=" + salary + "]";
	}

}
