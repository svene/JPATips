package com.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQuery(name="Dog.FindByDateOfBirth", query="select d from Dog d where d.dateOfBirth = :dateOfBirth")
public class Dog {

	public static final String FIND_BY_DATE_OF_BIRTH = "Dog.FindByDateOfBirth";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String name;
	private double weight;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfBirth;

	public Dog() {

	}

	public Dog(String name, double weight, Date dateOfBirth) {
		this.name = name;
		this.weight = weight;
		this.dateOfBirth = dateOfBirth;
	}
	
	public static void main(String[] args) {
		
		
		System.out.println(new Date());
	}

	@ManyToOne
	private Person person;

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

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public int hashCode() {
		return getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Dog) {
			Dog dog = (Dog) obj;
			return dog.getId() == getId();
		}

		return false;
	}
}
