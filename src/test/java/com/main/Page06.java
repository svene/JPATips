package com.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.Dog;
import com.model.Person;

public class Page06 {

	public static void main(String[] args) {
		CodeGenerator.startConnection();

		CodeGenerator.generateData();

		EntityManager em = CodeGenerator.getEntityManager();

		List<Person> personByLike = getPersonByNameUsingLike(em, "oh");

		for (Person person : personByLike) {
			System.out.println(person.getName());
		}

		List<Person> personsByAdressNumber = getPersonsByAddressNumberHigherThan(em, 90);

		for (Person person : personsByAdressNumber) {
			System.out.println(person.getName());
		}

		List<Person> personsWithoutDogs = getPersonsWithoutDogs(em);

		System.out.println("Total of persons without dogs: " + personsWithoutDogs.size());

		List<Person> personsWithoutAddress = getPersonsWithoutAddress(em);

		System.out.println("Total of persons without address: " + personsWithoutAddress.size());

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			Date startDate = formatter.parse("01/01/1996");
			Date endDate = formatter.parse("01/01/1999");

			List<Dog> dogsByBirth = getDogByBirthDate(em, startDate, endDate);

			for (Dog dog : dogsByBirth) {
				System.out.println(dog.getName() + ": " + formatter.format(dog.getDateOfBirth()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Dog dog = (Dog) em.createQuery("select d from Dog d where d.id = 1", Dog.class).getSingleResult();

		boolean belongsTo = isThisDogBelongingToAperson(em, dog, CodeGenerator.PERSON01_NAME);

		System.out.println("Is this Dog member of Perons01? " + belongsTo);

		Person personByConcatedName = getPersonConcatingName(em, "Ma", "ry");

		System.out.println("Found the person? " + personByConcatedName.getName());

		List<Person> personByLocate = getPersonByLocatingStringInTheName(em, "Mary");

		System.out.println("Amount of persons found by locate: " + personByLocate.size());

		String personNameBySubstring = getPersonNameBySubstring(em, CodeGenerator.PERSON06_NAME, 12, 18);

		System.out.println("Name substring is: " + personNameBySubstring);

		List<Person> personsDogWeight = getPersonByDogWeightOnlyHigherThan(em, 20);

		for (Person person : personsDogWeight) {
			System.out.println(person.getName());
		}

		List<Person> distinctPersons = getDistinctPersonsByDogsWeight(em, 2d);
		System.out.println("With the distinct, the result size is: " + distinctPersons.size());

		List<Person> personsWithDogsAmount = getPersonsWithDougsAmountOf(em, 4);
		System.out.println("Number of persons with 4 dogs: " + personsWithDogsAmount.size());

		Number numberOfDogsByPerson = getDogAmountByPerson(em, CodeGenerator.PERSON04_NAME);
		System.out.println("The dog amount is to " + CodeGenerator.PERSON04_NAME + ": " + numberOfDogsByPerson);

		List<Dog> dogsBornedAfterToday = getDogsBornAfterToday(em);
		System.out.println("The amount of dogs borned after today is: " + dogsBornedAfterToday.size());

		CodeGenerator.closeConnection();
	}

	/**
	 * This methods compares a value with LIKE
	 */
	@SuppressWarnings("unchecked")
	private static List<Person> getPersonByNameUsingLike(EntityManager em, String name) {
		Query query = em.createQuery("select p from Person p where p.name like :name");
		query.setParameter("name", "%" + name + "%");
		return query.getResultList();
	}

	/**
	 * This methods show several ways to do a query that checks if a part of a collection is inside another
	 */
	@SuppressWarnings("unchecked")
	private static List<Person> getPersonsByAddressNumberHigherThan(EntityManager em, int houseNumber) {
		Query query = em.createQuery("select p from Person p where p.address in (select a from Address a where a.houseNumber > :houseNumber)");
		// Query query = em.createQuery("select p from Person p where (select a from Address a where a.houseNumber > :houseNumber and p.address = a) > 0");
		// Query query = em.createQuery("select p from Person p where p.address = any (select a from Address a where a.houseNumber > :houseNumber)");
		// Query query = em.createQuery("select p from Person p where p.address = some (select a from Address a where a.houseNumber > :houseNumber)");
		// Query query = em.createQuery("select p from Person p where exists (select a from p.address a where a.houseNumber > :houseNumber)");
		query.setParameter("houseNumber", houseNumber);
		return query.getResultList();
	}

	/**
	 * This methods show how to check if a collection is empty
	 */
	@SuppressWarnings("unchecked")
	private static List<Person> getPersonsWithoutDogs(EntityManager em) {
		Query query = em.createQuery("select p from Person p where p.dogs is empty");
		return query.getResultList();
	}

	/**
	 * This method shows two ways to check if a relationship @OneToOne is empty
	 */
	@SuppressWarnings("unchecked")
	private static List<Person> getPersonsWithoutAddress(EntityManager em) {
		Query query = em.createQuery("select p from Person p where p.address is null");
		// Query query = em.createQuery("select p from Person p where p.address is empty");
		return query.getResultList();
	}

	/**
	 * Method that uses the between comparation
	 */
	@SuppressWarnings("unchecked")
	private static List<Dog> getDogByBirthDate(EntityManager em, Date startDate, Date endDate) {
		Query query = em.createQuery("select d from Dog d where d.dateOfBirth between :startDate and :endDate");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	/**
	 * Method that uses the member of comparation to check if an object belogs to a collection
	 */
	private static boolean isThisDogBelongingToAperson(EntityManager em, Dog dog, String name) {
		Query query = em.createQuery("select p from Person p where :dog member of p.dogs and p.name = :name");
		query.setParameter("dog", dog);
		query.setParameter("name", name);

		try {
			return query.getSingleResult() != null;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Methods that concats Strings
	 */
	private static Person getPersonConcatingName(EntityManager em, String firstWord, String secondWord) {
		Query query = em.createQuery("select p from Person p where p.name = concat(:firstWord, :secondWord)", Person.class);
		query.setParameter("firstWord", firstWord);
		query.setParameter("secondWord", secondWord);
		return (Person) query.getSingleResult();
	}

	/**
	 * Method that locates a string inside another
	 */
	@SuppressWarnings("unchecked")
	private static List<Person> getPersonByLocatingStringInTheName(EntityManager em, String valueToBeLocated) {
		Query query = em.createQuery("select p from Person p where locate(p.name, :value) > 0", Person.class);
		query.setParameter("value", valueToBeLocated);
		return query.getResultList();
	}

	/**
	 * Methods that uses the ALL comparator
	 */
	@SuppressWarnings("unchecked")
	private static List<Person> getPersonByDogWeightOnlyHigherThan(EntityManager em, double weight) {
		Query query = em.createQuery("select p from Person p where p.dogs is not empty and :weight < all (select d.weight from p.dogs d)");
		query.setParameter("weight", weight);

		return query.getResultList();
	}

	/**
	 * Method that uses the distinct to remove any repetetition  
	 */
	@SuppressWarnings("unchecked")
	private static List<Person> getDistinctPersonsByDogsWeight(EntityManager em, double weight) {
		Query query = em.createQuery("select distinct p from Person p join p.dogs d where d.weight > :weight");
		query.setParameter("weight", weight);
		return query.getResultList();
	}

	/**
	 * Method that uses the substring to get just a position of chars inside the string
	 */
	private static String getPersonNameBySubstring(EntityManager em, String personName, int startPosition, int endPosition) {
		Query query = em.createQuery("select substring(p.name, :startPosition, :endPosition) from Person p where p.name = :personName");
		query.setParameter("personName", personName);
		query.setParameter("startPosition", startPosition);
		query.setParameter("endPosition", endPosition);
		return (String) query.getSingleResult();
	}

	/**
	 * Method that checks the size of a collection
	 */
	@SuppressWarnings("unchecked")
	private static List<Person> getPersonsWithDougsAmountOf(EntityManager em, int dogAmount) {
		Query query = em.createQuery("select p from Person p where size(p.dogs) = :dogAmount");
		query.setParameter("dogAmount", dogAmount);
		return query.getResultList();
	}

	/**
	 * Method that gets the size of a collection
	 */
	private static Number getDogAmountByPerson(EntityManager em, String personName) {
		Query query = em.createQuery("select size(p.dogs) from Person p where p.name = :personName");
		query.setParameter("personName", personName);
		return (Number) query.getSingleResult();
	}

	/**
	 * Methods that uses the current database server date/time
	 */
	@SuppressWarnings("unchecked")
	private static List<Dog> getDogsBornAfterToday(EntityManager em) {
		Query query = em.createQuery("select d from Dog d where d.dateOfBirth > CURRENT_DATE");
		return query.getResultList();
	}
}
