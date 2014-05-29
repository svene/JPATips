package com.main;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.Dog;

public class Page08 {
	public static void main(String[] args) {
		CodeGenerator.startConnection();

		CodeGenerator.generateData();

		EntityManager em = CodeGenerator.getEntityManager();

		String nameOfFirstPerson = getFirstPersonName(em);
		System.out.println(nameOfFirstPerson);

		Dog dog = getTopDogDescending(em);
		System.out.println(dog.getName());

		CodeGenerator.closeConnection();
	}

	/**
	 * Returns the name of the first person using a native sql
	 */
	private static String getFirstPersonName(EntityManager em) {
		Query query = em.createNativeQuery("select top 1 name from person");
		return (String) query.getSingleResult();
	}

	/**
	 * Return an object using a native sql
	 */
	private static Dog getTopDogDescending(EntityManager em) {
		Query query = em.createNativeQuery("select top 1 id, name, weight from dog order by id desc", Dog.class);
		return (Dog) query.getSingleResult();
	}
}