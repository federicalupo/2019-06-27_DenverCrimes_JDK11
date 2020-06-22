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
}
