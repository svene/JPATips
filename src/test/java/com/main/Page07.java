package com.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.model.Dog;
import com.model.Person;

public class Page07 {
	public static void main(String[] args) {
		CodeGenerator.startConnection();

		CodeGenerator.generateData();

		EntityManager em = CodeGenerator.getEntityManager();

		int age = 70;
		List<Person> personByAge = getPersonByAge(em, 70);
		System.out.println("Found " + personByAge.size() + " person(s) with the age of: " + age);

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dateOfBirth = null;
		try {
			dateOfBirth = formatter.parse("10/1/1995");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<Dog> dogsByDayOfBirth = getDogsByDayOfBirth(em, dateOfBirth);
		System.out.println("Found " + dogsByDayOfBirth.size() + " dog with birth date of " + formatter.format(dateOfBirth));

		/*
		 * This queries will raise Runtime Exceptions
		 * 
		 * em.createQuery("select p from Person p").getSingleResult(); // NonUniqueResultException
		 * 
		 * em.createQuery("select p from Person p where p.name = 'JJJJ'").getSingleResult(); //NoResultException
		 */

		CodeGenerator.closeConnection();
	}

	@SuppressWarnings("unchecked")
	private static List<Dog> getDogsByDayOfBirth(EntityManager em, Date dateOfBirth) {
		Query query = em.createNamedQuery(Dog.FIND_BY_DATE_OF_BIRTH);
		query.setParameter("dateOfBirth", dateOfBirth, TemporalType.DATE);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	private static List<Person> getPersonByAge(EntityManager em, int age) {
		Query query = em.createNamedQuery(Person.FIND_BY_AGE);
		query.setParameter("age", age);

		return query.getResultList();
	}
}