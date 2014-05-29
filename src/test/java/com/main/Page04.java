package com.main;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.Dog;
import com.model.Person;

public class Page04 {
	public static void main(String[] args) {
		CodeGenerator.startConnection();

		CodeGenerator.generateData();

		EntityManager em = CodeGenerator.getEntityManager();

		List<Dog> dogs = listAllDogs(em);

		for (Dog dog : dogs) {
			System.out.println(dog.getName());
		}

		Person person03 = findPersonByName(em, CodeGenerator.PERSON03_NAME);
		System.out.println(person03.getName());

		Person person01 = new Person();
		person01.setId(1);

		Person savedPerson = findPersonByPersonObject(em, person01);
		System.out.println(savedPerson.getName());

		List<Dog> dogsByWeight = listAllDogsOrderingByWeight(em);

		System.out.print("Weights found: ");
		for (Dog dog : dogsByWeight) {
			System.out.print(dog.getWeight() + " / ");
		}System.out.println();

		String addressName = findAddressNameOfPerson(em, CodeGenerator.PERSON04_NAME);
		System.out.println("Person 04 address is: " + addressName);

		Person person02 = findPersonByNameWithAllDogs(em, CodeGenerator.PERSON02_NAME);

		for (Dog dog : person02.getDogs()) {
			System.out.println("Person 02 Dog: " + dog.getName());
		}

		Person person05 = findPersonByNameThatMayNotHaveDogs(em, CodeGenerator.PERSON06_NAME);
		System.out.println("Is the list of the Dogs from the Person 05 empty? " + person05.getDogs().size());

		CodeGenerator.closeConnection();
	}

	/**
	 * Easiest way to do a query
	 */
	@SuppressWarnings("unchecked")
	private static List<Dog> listAllDogs(EntityManager em) {
		Query query = em.createQuery("select d from Dog d", Dog.class);

		return query.getResultList();
	}

	/**
	 * Easiest way to do a query with parameters
	 */
	private static Person findPersonByName(EntityManager em, String name) {
		Query query = em.createQuery("select p from Person p where name = :name", Person.class);
		query.setParameter("name", name);
		return (Person) query.getSingleResult();
	}

	/**
	 * Executes a query that has as parameter an object
	 */
	private static Person findPersonByPersonObject(EntityManager em, Person person) {
		Query query = em.createQuery("select p from Person p where p = :person");
		query.setParameter("person", person);
		return (Person) query.getSingleResult();
	}

	/**
	 * Query that will list all dogs with an order
	 */
	@SuppressWarnings("unchecked")
	private static List<Dog> listAllDogsOrderingByWeight(EntityManager em) {
		Query query = em.createQuery("select d from Dog d order by d.weight desc", Dog.class);

		return query.getResultList();
	}

	/**
	 * Query that get only a field instead a complete class object
	 */
	private static String findAddressNameOfPerson(EntityManager em, String name) {
		Query query = em.createQuery("select p.address.streetName from Person p where p.name = :name");
		query.setParameter("name", name);
		return (String) query.getSingleResult();
	}

	/**
	 * Query that will fetch a lazy relationship Be carefull, with this kind of
	 * query only those who have the relationship will come in the result
	 */
	private static Person findPersonByNameWithAllDogs(EntityManager em, String name) {
		Query query = em.createQuery("select p from Person p join fetch p.dogs where p.name = :name", Person.class);
		query.setParameter("name", name);
		return (Person) query.getSingleResult();
	}

	/**
	 * With this query will will bring results that may not have arelationship
	 */
	private static Person findPersonByNameThatMayNotHaveDogs(EntityManager em, String name) {
		Query query = em.createQuery("select p from Person p left join fetch p.dogs where p.name = :name", Person.class);
		query.setParameter("name", name);
		return (Person) query.getSingleResult();
	}
}