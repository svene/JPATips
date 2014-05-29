package com.main;

import javax.persistence.EntityManager;

import com.model.Address;
import com.model.Person;

public class Page03 {
	public static void main(String[] args) {
		CodeGenerator.startConnection();

		CodeGenerator.generateData();

		EntityManager em = CodeGenerator.getEntityManager();

		Person person = em.find(Person.class, 1);

		int addressId = 2;

		// usually we send an id or a detached object from the view
		setAddressToOtherPerson(em, person, addressId);

		int personId = 4;

		// usually we send an id or a detached object from the view
		deletePerson(em, personId);

		CodeGenerator.closeConnection();
	}

	private static void setAddressToOtherPerson(EntityManager em, Person person, int addressId) {
		Address address = em.getReference(Address.class, addressId);
		person.setAddress(address);
		em.merge(person);
		em.flush();
		System.out.println("Merged");
	}

	private static void deletePerson(EntityManager em, int personId) {
		Person savedPerson = em.getReference(Person.class, personId);
		em.remove(savedPerson);
		em.flush();
		System.out.println("Deleted");
	}
}