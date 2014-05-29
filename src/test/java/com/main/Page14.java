package com.main;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.Person;

public class Page14 {
	
	public static void main(String[] args) {
		CodeGenerator.startConnection();

		CodeGenerator.generateData();

		EntityManager em = CodeGenerator.getEntityManager();

		em.clear();
		Query query = em.createQuery("update Person p set p.name = 'Fluffy, the destroyer of worlds!'");
		query.executeUpdate();
		
		query = em.createQuery("select p from Person p where p.id = 4");
		
		Person person = (Person) query.getSingleResult();
		
		System.out.println("My new name is: " + person.getName());
		
		query = em.createQuery("delete from Person p where p.dogs is empty");
		query.executeUpdate();
		
		query = em.createQuery("select p from Person p");
		
		System.out.println("We had 6, but was found " + query.getResultList().size() + " persons in the database");
		
		CodeGenerator.closeConnection();
	}
}