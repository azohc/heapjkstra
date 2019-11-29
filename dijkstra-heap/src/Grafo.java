/**
 *  
 * @author Juan Chozas Sumbera
 * juanchozass@gmail.com
 * 
 */

import java.util.List;
import java.util.ArrayList;

/*
 * Grafo con representacion interna mediante listas de adyacencia
 */
public class Grafo {
	/*
	 * Representacion de una arista: vertice origen, vertice destino, coste de la arista
	 */
	static class Arista {
		int orig, dest, cost;
		
		public Arista(int o, int d) {
			orig = o; dest = d;
		}
		
		public Arista(int o, int d, int c) {
			orig = o; dest = d; cost = c;
		}
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	/*
	 * Lista de listas de adyacencia: cada vertice v se asocia a una lista de pares
	 * los pares contienen 
	 * el vertice v' adyacente a v
	 * el coste de la arista v --> v'
	 */
	public List<List<Par<Integer, Integer>>> ady;
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	/*
	 * Dado un booleano y una o mas aristas, construye un grafo normal o dirigido representado por listas de adyacencia
	 * Las aristas deben contener indices menores que el numero total de vertices
	 */
	public Grafo(boolean dirigido, Arista... aristas) {
		final int n = aristas.length;
		ady = new ArrayList<>(n);
		
		for (int i = 0; i < n; i++) {
			ady.add(i, new ArrayList<Par<Integer, Integer>>());
		}
		
		for (Arista a : aristas) {
			if (a.orig >= n || a.dest >= n) 
				throw new IndexOutOfBoundsException(
						"No se admiten aristas que conecten vertices con indice mayor o igual el numero total de vertices");
			
			ady.get(a.orig).add(new Par<Integer, Integer>(a.dest, a.cost));
			
			if (!dirigido) {
				ady.get(a.dest).add(new Par<Integer, Integer>(a.orig, a.cost));
			}
		}
	}
	
	/*
	 * Para cada vertice v, imprime v -> [(v', c) ... (v', c)]
	 * donde cada par (v', c) contiene
	 * el vertice v' adyacente a v 
	 * el coste c de la arista v -> v'
	 */
	public void print() {
		for (int i = 0; i < ady.size(); i++) {
			String s = i + " -> [";
			boolean hay_ady = false;
			
			for (Par<Integer, Integer> par : ady.get(i)) {
				hay_ady = true;
				s += "(" + par.fst + ", " + par.snd + "), ";
			}
			
			if (hay_ady) 
				s = s.substring(0, s.length() - 2);
		
			System.out.println(s + "]");
		}
	}
	
	
	public int size() {
		return ady.size();
	}
}