package com.main;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.Dog;
import com.model.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class Page04 {

	private EntityManager em;

	@Before
	public void setUp() throws Exception {
		CodeGenerator.startConnection();
		CodeGenerator.generateData();


		em = CodeGenerator.getEntityManager();
	}

	@After
	public void tearDown() throws Exception {
		CodeGenerator.rollback();
	}

	@Test
	public void all_dogs() throws Exception {
		List<Dog> dogs = listAllDogs(em);
		List<String> dogNames = dogs.stream().map(Dog::getName).collect(Collectors.toList());
		List<String> expectedDogNames = CodeGenerator.ALL_DOGS.stream().map(Dog::getName).collect(Collectors.toList());
		assertThat(dogNames).containsAll(expectedDogNames);
	}

	@Test
	public void person_by_name() throws Exception {
		Person person03 = findPersonByName(em, CodeGenerator.PERSON03_NAME);
		assertThat(person03.getName()).isEqualTo(CodeGenerator.PERSON03_NAME);
	}

	@Test
	public void person_by_person_object() throws Exception {
		Person person01 = new Person();
		person01.setId(1);

		Person savedPerson = findPersonByPersonObject(em, person01);
		assertThat(savedPerson.getName()).isEqualTo(CodeGenerator.PERSON01_NAME);
	}

	@Test
	public void all_dogs_ordered_by_weight() throws Exception {
		List<Dog> dogs = listAllDogsOrderingByWeight(em);
		List<String> dogNames = dogs.stream().map(Dog::getName).collect(Collectors.toList());
		List<String> expectedDogNames = dogs.stream().sorted(Comparator.comparingDouble(Dog::getWeight).reversed()).map(Dog::getName).collect(Collectors.toList());
		assertThat(dogNames).isEqualTo(expectedDogNames);
	}

	@Test
	public void findAddressNameOfPerson() throws Exception {
		String addressName = findAddressNameOfPerson(em, CodeGenerator.PERSON04_NAME);
		assertThat(addressName).isEqualTo("Street C");
	}

	@Test
	public void findPersonByNameWithAllDogs() throws Exception {
		Person person02 = findPersonByNameWithAllDogs(em, CodeGenerator.PERSON02_NAME);
		List<String> dogNames = person02.getDogs().stream().map(Dog::getName).collect(Collectors.toList());
		List<String> expectedDogNames = CodeGenerator.ALL_DOGS.subList(3, 6).stream().map(Dog::getName).collect(Collectors.toList());
		assertThat(person02.getName()).isEqualTo(CodeGenerator.PERSON02_NAME);
		assertThat(dogNames).isEqualTo(expectedDogNames);
	}
	@Test
	public void findPersonByNameThatMayNotHaveDogs() throws Exception {
		Person person06 = findPersonByNameThatMayNotHaveDogs(em, CodeGenerator.PERSON06_NAME);
		List<String> dogNames = person06.getDogs().stream().map(Dog::getName).collect(Collectors.toList());
		assertThat(person06.getName()).isEqualTo(CodeGenerator.PERSON06_NAME);
		assertThat(dogNames).isEqualTo(Collections.emptyList());
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