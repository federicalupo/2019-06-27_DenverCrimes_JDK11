package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDAO;

public class Model {
	
	private EventsDAO dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<Arco> archi;
	private List<String> camminoBest;
	private Integer pesoMin;
	
	public Model() {
		dao = new EventsDAO();
		
	}
	
	public List<String> getCategorie(){
		return dao.listaCategorie();
	}

	public List<Integer> listaAnni(){
		return dao.listaAnni();
	}
	
	public void creaGrafo(String categoria, Integer anno) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.archi = new ArrayList<>();
		
		Graphs.addAllVertices(this.grafo, dao.vertici(categoria, anno));
		
		//archi => approccio 1 
		for(String s1 : grafo.vertexSet()) { //per ogni tipo
			for(String s2 : grafo.vertexSet()) {
				if(s1.compareTo(s2)<0) {
					Integer peso = dao.getPeso(s1, s2);
					if(peso>0) {
						archi.add(new Arco(s1, s2, peso));
						Graphs.addEdge(this.grafo, s1, s2, peso);
					}
				}
			}
		}
	
	}
	
	public Integer nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Integer nVertici() {
		return this.grafo.vertexSet().size();
	}
	public List<Arco> archiFiltro(){
		Collections.sort(archi); //ordine decrescente di peso
		Integer pesoMax = archi.get(0).getPeso();
		
		List<Arco> listaFiltrata = new ArrayList<>();
		
		for(Arco a : archi) {
			if(a.getPeso().compareTo(pesoMax)==0) {
				listaFiltrata.add(a);
			}
		}
		
		return listaFiltrata;
		
		
	}
	/**
	 * 
	 * parto da sorgente, 
	 * inserisco in parziale
	 * 
	 * 
	 * mi faccio dare i vicini
	 * per ogni vicino, se non è in parziale, lo inserisco
	 * continuo 
	 * 
	 * backtracking
	 * 
	 * quando l'ultimo inserito = s2
	 * se il numero di vertici > numero di vertici fino ad allora inseriti
	 * riaggiornare
	 * 
	 * variabile nVertici = parziale.size()
	 * 
	 * se il numero di vertici = numero di vertici fino ad allora inseriti
	 * controllo se il peso è <
	 * variabile pesoMin = MAx_value
	 * 
	 * 
	 * 
	 * 
	 * @param s1
	 * @param s2
	 */
	public List<String> cammino(String s1, String s2) {
		this.camminoBest = new ArrayList<>();
		this.pesoMin = Integer.MAX_VALUE;
		
		List<String> parziale = new ArrayList<>();
		parziale.add(s1);
		
		Integer peso = 0;
		ricorsiva(s1, s2, peso,  parziale);
		
		return this.camminoBest;
		
	}
	
	private void ricorsiva(String s1, String s2, Integer peso,  List<String> parziale) {
		
		if(s1.equals(s2)) {
			if(parziale.size() > this.camminoBest.size()) {
				this.camminoBest = new ArrayList<>(parziale);
				this.pesoMin = peso;
				
				return;
			}
			if(parziale.size() == this.camminoBest.size()) {
				if(peso < pesoMin) {
					this.camminoBest = new ArrayList<>(parziale);
					pesoMin =peso;
				}
				
				return;
			}
		}
		
		
		for(String s : Graphs.neighborListOf(this.grafo, s1)) {
			if(!parziale.contains(s)) {
				parziale.add(s);
				peso+=(int)this.grafo.getEdgeWeight(this.grafo.getEdge(s1, s));
				ricorsiva(s, s2, peso, parziale);
				parziale.remove(s);
			}
		}
		
	}
	
	public Integer pesoMin() {
		return this.pesoMin;
	}
}
