package com.main;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.Address;
import com.model.Person;

public class Page09 {
	public static void main(String[] args) {
		CodeGenerator.startConnection();

		CodeGenerator.generateData();

		EntityManager em = CodeGenerator.getEntityManager();

		Query query = em.createNativeQuery("select id, name, age, a.id as ADDRESS_ID, houseNumber, streetName " +
										   "from person p join address a on a.id = p.address_id where p.id = 1",
										   Person.MAPPING_PERSON_AND_ADDRESS);

		Object[] result = (Object[]) query.getSingleResult();
		
		Person personWithAdress = (Person) result[0];
		Address address = (Address) result[1];

		System.out.println(personWithAdress.getName() + " lives at " + address.getStreetName());
		
		query = em.createNativeQuery("select p.id, p.name, count(0) as dogAmount " +
		   						     "from person p join dog d on p.id = d.person_id where name = 'Mark' " +
		   						     "group by p.id, p.name",
				   					 Person.MAPPING_DOG_AMOUNT);
		
		result = (Object[]) query.getSingleResult();

		Person person = (Person) result[0];
		BigInteger total = (BigInteger) result[1];

		System.out.println(person.getName() + " has " + total + " dogs");

		CodeGenerator.closeConnection();
	}
}