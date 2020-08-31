package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao ;
	private Graph<String, DefaultWeightedEdge> grafo ;
	private List<String> vertici;
	private List<Adiacenza> adiacenze;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public List<String> getReati(){
		return this.dao.getReati();
	}
	
	public List<Integer> getMesi(){
		return this.dao.getMesi();
	}
	
	public void creaGrafo(String categoria, Integer mese) {
		this.grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.vertici = this.dao.getVertici(categoria, mese);
		Graphs.addAllVertices(this.grafo, vertici);
		this.adiacenze = this.dao.getAdiacenze(categoria, mese);
		for(Adiacenza a : this.adiacenze) {
			if(this.grafo.containsVertex(a.getS1()) && this.grafo.containsVertex(a.getS2()) && a.getPeso() != null) {
				Graphs.addEdgeWithVertices(this.grafo, a.getS1(), a.getS2(), a.getPeso());
			}
		}
	}
	
	public int numVertici() {
		return this.grafo.vertexSet().size(); 
	}
	
	public int numArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Double getMedia() {
		int somma = 0;
		for(DefaultWeightedEdge edge : this.grafo.edgeSet()) {
			somma += this.grafo.getEdgeWeight(edge);
		}
		return (double) (somma/this.numArchi());
		
	}
	
	public List<Adiacenza> getRisultato(){
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		for(DefaultWeightedEdge edge : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(edge) > this.getMedia())
				result.add(new Adiacenza(this.grafo.getEdgeSource(edge), this.grafo.getEdgeTarget(edge), this.grafo.getEdgeWeight(edge)));
		}
		return result;
	}
	
	
}
