package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;



public class Simulatore {
	
	//PARAMETRI DI OUTPUT
	double tTot;
	int nCibi;
	
	//PARAMETRI DELLO STATO DEL MONDO
	int stazioni;	//NUMERO COMPRESO TRA 1 E 10
	
	//CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;
	
	//PARAMETRI DELLA SIMULAZIONE
	Food ciboIniziale;
	int tavoli;
	int tLiberi;	
	List<Food> cibiPreparati;
	Model model;
	
	public void init(Food f, int tavoli, Model m) {
		
		this.queue = new PriorityQueue<Event>();
		cibiPreparati = new ArrayList<Food>();
		ciboIniziale = f;
		this.tTot = tavoli;
		this.tLiberi = tavoli;	//INUTILE (MANCO FOSSE NA DONNA)
		this.tavoli = tavoli;
		this.model = m;
		
		
		//INIZIALIZZO LA LISTA DEGLI EVENTI
		
		List<Collegamento> collegamenti = this.model.foodByCal(this.ciboIniziale);
		int max = Math.min(collegamenti.size(), this.tavoli);
		
		this.tLiberi -= max;
		for(int i = 0;i<max;i++) {
			this.queue.add(new Event(collegamenti.get(i).getAverage(), collegamenti.get(i).getF2()));
			this.cibiPreparati.add(collegamenti.get(i).getF2());
		}
		
		
		
		
	}
	
	public void run() {
		while(!queue.isEmpty()) {
			Event e = queue.poll();
			processaEvento(e);
		}
		this.nCibi = this.cibiPreparati.size();
	}

	private void processaEvento(Event e) {
		//AGGIORNARE TEMPO
		//CREARE UN NUOVO EVENTO (SE POSSIBILE)
		//AGGIORNARE LA LISTA DI CIBI PREPARATI
		
		this.tTot = e.getTempoCompletamento();
		List<Collegamento> collegamenti = this.model.foodByCal(e.getCibo());
		
		for(Collegamento c : collegamenti) {
			if(!this.cibiPreparati.contains(c.getF2()))
			{
				//HO TROVATO IL CIBO DA AGGIUNGERE
				this.cibiPreparati.add(c.getF2());
				this.queue.add(new Event(this.tTot+c.getAverage(), c.getF2()));
				break;
			}
		}
		
		
		
	}
	
	public String getResult() {
		return "Tempo totale impiegato: " + this.tTot + "\nCibi preparati: " + this.nCibi;
	}

}
