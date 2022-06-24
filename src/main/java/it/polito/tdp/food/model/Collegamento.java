package it.polito.tdp.food.model;

public class Collegamento {
	private Food f1;
	private Food f2;
	private double average;
	
	public Collegamento(Food f1, Food f2, double average) {
		super();
		this.f1 = f1;
		this.f2 = f2;
		this.average = average;
	}
	
	public Food getF1() {
		return f1;
	}
	public Food getF2() {
		return f2;
	}
	public double getAverage() {
		return average;
	}
	
	

}
