package com.main;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.Dog;

public class Page11 {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		CodeGenerator.startConnection();

		CodeGenerator.generateData();

		EntityManager em = CodeGenerator.getEntityManager();

		Query query = em.createQuery("select d from Dog d");
		
		List<Dog> dogs = query.getResultList();
		
		System.out.println("Total of dogs found: " + dogs.size());
		
		query.setMaxResults(5);
		query.setFirstResult(0);
		
		List<Dog> fiveFirstDogs = query.getResultList();
		
		System.out.print("Total of dogs found: " + fiveFirstDogs.size() + " ");
		for (Dog dog : fiveFirstDogs) {
			System.out.print(dog.getName() + " ");
		}System.out.println();
		
		query.setMaxResults(5);
		query.setFirstResult(5);
		
		List<Dog> fiveSecondDogs = query.getResultList();

		System.out.print("Total of dogs found: " + fiveSecondDogs.size() + " ");
		for (Dog dog : fiveSecondDogs) {
			System.out.print(dog.getName() + " ");
		}
		
		CodeGenerator.closeConnection();
	}
}