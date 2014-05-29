package com.main;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.model.PersonDogAmountReport;

public class Page13 {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		CodeGenerator.startConnection();

		CodeGenerator.generateData();

		EntityManager em = CodeGenerator.getEntityManager();

		Query query = em.createQuery("select new com.model.PersonDogAmountReport(p, size(p.dogs)) from Person p group by p.id");
		
		List<PersonDogAmountReport> persons = query.getResultList();
		for (PersonDogAmountReport personReport : persons) {
			System.out.println(personReport.getPerson().getName() + " has: " + personReport.getDogAmount() + " dogs.");
		}
		
		CodeGenerator.closeConnection();
	}
}