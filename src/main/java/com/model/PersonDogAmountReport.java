package com.model;

public class PersonDogAmountReport {
	private int dogAmount;
	private Person person;

	public PersonDogAmountReport(Person person, int dogAmount) {
		this.person = person;
		this.dogAmount = dogAmount;
	}

	public int getDogAmount() {
		return dogAmount;
	}

	public void setDogAmount(int dogAmount) {
		this.dogAmount = dogAmount;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
