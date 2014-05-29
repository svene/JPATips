package com.main;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import com.model.Person;

public class Page15 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		CodeGenerator.startConnection();

		CodeGenerator.generateData();

		EntityManager em = CodeGenerator.getEntityManager();

		CriteriaQuery criteriaQuery = em.getCriteriaBuilder().createQuery();
		criteriaQuery.select(criteriaQuery.from(Person.class));

		List<Person> result = em.createQuery(criteriaQuery).getResultList();

		System.out.println("Found " + result.size() + " persons.");

		CodeGenerator.closeConnection();
	}
}