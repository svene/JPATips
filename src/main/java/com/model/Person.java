package com.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.QueryHint;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

@Entity
@NamedQueries({
		@NamedQuery(name="Person.findByName", query="select p from Person p where p.name = :name"),
		@NamedQuery(name="Person.findByAge", query="select p from Person p where p.age = :age", hints={@QueryHint(name="org.hibernate.timeout", value="1000")})
})
@SqlResultSetMappings({
	@SqlResultSetMapping(name="personAndAdress", 
			entities={
				@EntityResult(entityClass=Person.class), 
				@EntityResult(entityClass=Address.class, 
					fields={
						@FieldResult(name="id", column="ADDRESS_ID")
					}
				)
		}),
	@SqlResultSetMapping(name="personWithDogAmount", 
		entities={@EntityResult(entityClass=Person.class)},
		columns={@ColumnResult(name="dogAmount")}
	)
})
public class Person {
	
	public static final String FIND_BY_NAME = "Person.findByName";
	public static final String FIND_BY_AGE = "Person.findByAge";
	public static final String MAPPING_PERSON_AND_ADDRESS = "personAndAdress";
	public static final String MAPPING_DOG_AMOUNT = "personWithDogAmount";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String name;
	private int age;

	public Person() {

	}

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
	private List<Dog> dogs;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="address_id")
	private Address address;

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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<Dog> getDogs() {
		if (dogs == null) {
			dogs = new ArrayList<Dog>();
		}

		return dogs;
	}

	public void setDogs(List<Dog> dogs) {
		this.dogs = dogs;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	@Override
	public int hashCode() {
		return getId();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Person) {
			Person person = (Person) obj;
			return person.getId() == getId();
		}
		
		return false;
	}
}