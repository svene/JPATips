package com.main;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.Dog;

public class Page12 {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		CodeGenerator.startConnection();

		CodeGenerator.generateData();

		EntityManager em = CodeGenerator.getEntityManager();

		Query query = em.createQuery("select d from Dog d");
		query.setHint("org.hibernate.timeout", 1000);
		
		List<Dog> dogs = query.getResultList();
		System.out.println("Found " + dogs.size() + " dogs");
		
		CodeGenerator.closeConnection();
	}
}