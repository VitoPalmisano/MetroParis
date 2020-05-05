package it.polito.tdp.metroparis.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private Graph<Fermata, DefaultEdge> graph;
	private List<Fermata> fermate;
	private Map<Integer, Fermata> fermateIdMap;
	
	public Model() {
		this.graph = new SimpleDirectedGraph<Fermata, DefaultEdge>(DefaultEdge.class);
		
		MetroDAO dao = new MetroDAO();
		
		// Creazione dei vertici
		this.fermate = dao.getAllFermate();
		
		this.fermateIdMap = new HashMap<>();
		for(Fermata f : this.fermate) {
			fermateIdMap.put(f.getIdFermata(), f);
		}
		
		Graphs.addAllVertices(this.graph, this.fermate);
		
		System.out.println(this.graph);
		
		// Creazione degli archi -- metodo 1 (coppie di vertici)
		/*
		for(Fermata fp : this.fermate) {
			for(Fermata fa : this.fermate) {
				if(dao.fermateConnesse(fp, fa)) {
					graph.addEdge(fp, fa);
				}
			}
		}
		*/
		
		// Creazione degli archi -- metodo 2 ( da un vertice, trova tutti i connessi)
		/*
		for(Fermata fp : this.fermate) {
			List<Fermata> connesse = dao.fermateSuccessive(fp, fermateIdMap);
					
			for(Fermata fa : connesse) {
				this.graph.addEdge(fp, fa);
			}
		}
		*/
		
//		Creazione degli archi -- metodo 3 (Chiedo al DB l'elenco degli archi)
		
		List<CoppiaFermate> coppie = dao.coppieFermate(fermateIdMap);
		
		for(CoppiaFermate c : coppie) {
			this.graph.addEdge(c.getFp(), c.getFa());
		}
		
	//	System.out.println(this.graph);
		System.out.format("Grafo caricato con %d vertici %d archi", this.graph.vertexSet().size(), this.graph.edgeSet().size());
	}
	
	public static void main(String args[]) {
		Model m = new Model();
	}
}
