package it.polito.tdp.food.model;

public class Event implements Comparable<Event> {
	
	double tempoCompletamento;
	Food cibo;

	public Event(double tempoCompletamento, Food cibo) {
		super();
		this.tempoCompletamento = tempoCompletamento;
		this.cibo = cibo;
	}

	public double getTempoCompletamento() {
		return tempoCompletamento;
	}
	
	public Food getCibo() {
		return cibo;
	}

	@Override
	public int compareTo(Event o) {
		if(this.tempoCompletamento>o.getTempoCompletamento())
			return 1;
		else if (this.tempoCompletamento==o.getTempoCompletamento())
			return 0;
		else return -1;
	}
	
	

}
