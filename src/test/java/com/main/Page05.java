package com.main;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.Person;

public class Page05 {
	public static void main(String[] args) {
		CodeGenerator.startConnection();

		CodeGenerator.generateData();

		EntityManager em = CodeGenerator.getEntityManager();

		Number average = getPersonsAgeAverage(em);
		System.out.println(average);

		List<Object[]> personsFilteredByDogsWeight = getPersonsWithDogsWeightHigherThan(em, 4d);

		for (Object[] objects : personsFilteredByDogsWeight) {
			Person person = (Person) objects[0];
			Long count = (Long) objects[1];
			System.out.println("The person : " + person.getName() + " has " + count + " dogs with the weight > 4");
		}

		List<Object[]> dogsMinAndMaxWeightList = getDogMinAndMaxWeight(em);
		Object[] dogMinAndMaxWeightResult = dogsMinAndMaxWeightList.get(0);
		System.out.println("Min: " + dogMinAndMaxWeightResult[0] + " Max: " + dogMinAndMaxWeightResult[1]);

		Number sumOfAllAges = getTheSumOfAllAges(em);
		System.out.println("All summed ages are: " + sumOfAllAges);

		String loweredCaseName = getLoweredCaseNameFromUpperCase(em, CodeGenerator.PERSON03_NAME);
		System.out.println(loweredCaseName);

		Number personAgeMod = getPersonAgeMode(em, CodeGenerator.PERSON05_NAME, 6);
		System.out.println("Person modulus age: " + personAgeMod);

		Number personAgeSqrt = getPersonAgeSqrtUsingTrim(em, "        " + CodeGenerator.PERSON04_NAME + "        ");
		System.out.println("Person modulus age: " + personAgeSqrt);

		CodeGenerator.closeConnection();
	}
	
	/**
	 * Uses the AVG sql database function
	 */
	private static Number getPersonsAgeAverage(EntityManager em) {
		Query query = em.createQuery("select avg(p.age) from Person p");
		return (Number) query.getSingleResult();
	}

	/**
	 * This query will use the count database function
	 * 
	 * @return List<Object[]> where object[0] is a person, object [2] is a Long
	 */
	@SuppressWarnings("unchecked")
	private static List<Object[]> getPersonsWithDogsWeightHigherThan(EntityManager em, double weight) {
		Query query = em.createQuery("select p, count(p) from Person p join p.dogs d where d.weight > :weight group by p");
		query.setParameter("weight", weight);
		return query.getResultList();
	}

	/**
	 * This query will use the min and max sql database function
	 * 
	 * @return List<Object[]> where object[0] is the min, object [2] is the max
	 */
	@SuppressWarnings("unchecked")
	private static List<Object[]> getDogMinAndMaxWeight(EntityManager em) {
		Query query = em.createQuery("select min(weight), max(weight) from Dog");
		return query.getResultList();
	}

	/**
	 * This query will use the sum sql database function
	 */
	private static Number getTheSumOfAllAges(EntityManager em) {
		Query query = em.createQuery("select sum(p.age) from Person p");
		return (Number) query.getSingleResult();
	}

	/**
	 * Method that uses the UPPER and LOWER database functions
	 */
	private static String getLoweredCaseNameFromUpperCase(EntityManager em, String name) {
		Query query = em.createQuery("select lower(p.name) from Person p where UPPER(p.name) = :name");
		query.setParameter("name", name.toUpperCase());
		return (String) query.getSingleResult();
	}

	/**
	 * Method that uses the mod database function
	 */
	private static Number getPersonAgeMode(EntityManager em, String personName, int modBy) {
		Query query = em.createQuery("select mod(p.age, :modBy) from Person p where p.name = :name");
		query.setParameter("modBy", modBy);
		query.setParameter("name", personName);
		return (Number) query.getSingleResult();
	}

	/**
	 * Method that uses the square root of a person age using the trim function in the name
	 */
	private static Number getPersonAgeSqrtUsingTrim(EntityManager em, String name) {
		Query query = em.createQuery("select sqrt(p.age) from Person p where p.name = trim(:name)");
		query.setParameter("name", name);
		return (Number) query.getSingleResult();
	}
}