package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private List<Food> cibi;
	
	public List<Food> getFoodsByPortions(int nPorzioni){
		this.dao = new FoodDao();
		if (cibi == null)
			cibi = this.dao.getFoodsByPortions(nPorzioni);
		return cibi;
	}
	
	private List<Collegamento> getCollegamenti(int nPorzioni){
		this.dao = new FoodDao();
		return this.dao.getCollegamenti(nPorzioni);
	}
	
	public String creaGrafo(int nPorzioni) {
		grafo = new SimpleWeightedGraph<Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//AGGIUNGO I VERTICI
		
		Graphs.addAllVertices(grafo, this.getFoodsByPortions(nPorzioni));
		
		//AGGIUNGO GLI ARCHI
		List<Collegamento> collegamenti = this.getCollegamenti(nPorzioni);
		System.out.println(collegamenti.size());	//DEBUGGING
		for(Collegamento c : collegamenti)
			Graphs.addEdgeWithVertices(grafo, c.getF1(), c.getF2(), c.getAverage());
		
		
		String result="Grafo creato\n#VERTICI: " + grafo.vertexSet().size() + "\n#ARCHI: " + grafo.edgeSet().size();
		return result;
		
		
		
	}
	
	public String getMaxCal(Food f) {
		Set<DefaultWeightedEdge> archi = grafo.outgoingEdgesOf(f);
		List<DefaultWeightedEdge> soluzione = new ArrayList<DefaultWeightedEdge>();
		String result = "Cibo selezionato: " + f + "\n";
		
		for(int i = 0;i<5;i++) {
			double maxWeight=0;
			DefaultWeightedEdge edge = null;
			for(DefaultWeightedEdge e : archi) {
				if(grafo.getEdgeWeight(e)>maxWeight && !soluzione.contains(e)) {
					maxWeight= grafo.getEdgeWeight(e);
					edge = e;
				}
			}
			
			
			soluzione.add(edge);
		}
		
		for(DefaultWeightedEdge e : soluzione) {
			result += Graphs.getOppositeVertex(grafo, e, f) + " calorie condivise: " + grafo.getEdgeWeight(e) + "\n";
		}
		
		
		
		return result;
	}
	/**
	 * ritorna una lista di tutti i collegamenti del cibo, ordinati in ordine crescente di calorie in comune
	 * @param f
	 * @return
	 */
	public List<Collegamento> foodByCal(Food f){
		Set<DefaultWeightedEdge> archi = grafo.outgoingEdgesOf(f);
		List<DefaultWeightedEdge> soluzione = new ArrayList<DefaultWeightedEdge>();
		List<Collegamento> result = new ArrayList<Collegamento>();
		
		for(int i = 0;i<archi.size();i++) {
			double maxWeight=0;
			DefaultWeightedEdge edge = null;
			for(DefaultWeightedEdge e : archi) {
				if(grafo.getEdgeWeight(e)>maxWeight && !soluzione.contains(e)) {
					maxWeight= grafo.getEdgeWeight(e);
					edge = e;
				}
			}
			
			
			soluzione.add(edge);
		}
		
		for(DefaultWeightedEdge e : soluzione) {
			result.add(new Collegamento (f, Graphs.getOppositeVertex(grafo, e, f), grafo.getEdgeWeight(e)));
		}
		
		
		
		return result;
	}
	
	public String effettuaSimulazione(Food f, int nTavoli, Model m) {
		Simulatore s = new Simulatore();
		s.init(f, nTavoli, m);
		s.run();
		return s.getResult();
	}

}
