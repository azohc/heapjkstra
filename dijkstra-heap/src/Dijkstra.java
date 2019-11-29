import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 *  
 * @author Juan Chozas Sumbera
 * juanchozass@gmail.com
 * 
 */
	
public class Dijkstra {
	
	private final static int indefinido = Integer.MAX_VALUE;
	
	/**
	 * El algoritmo recibe un grafo G implementado con listas de adyacencia, y un vertice origen s.
	 * Devuelve un vector d de distancias minimas desde s a cada vertice, y un vector p de predecesores que 
	 * indica, para cada vertice k, cual es el vertice anterior a k en el camino minimo desde s hasta k. 
	 */
	public static Par<int[], int[]> dijkstra(Grafo g, int s) {
		int n = g.size();
		MonticuloSesgado Q = new MonticuloSesgado();
		int j, k;
		int[] d = new int[n], p = new int[n];
		d[s] = 0; p[s] = indefinido;
		
		for (k = 0; k < n; k++) {
			if (k != s) {
				d[k] = coste(g, s, k);
				p[k] = s;
				Q.insertar(k, d[k]);
			}
		}
		
		while (!Q.vacio()) {
			Par<Integer, Integer> pmin = Q.min(); 
			Q.borrarMin();
			j = pmin.fst; 
			int distMin = pmin.snd;
			
			List<Par<Integer, Integer>> js = g.ady.get(j);
			for (k = 0; k < js.size(); k++) {
				int vk = js.get(k).fst;
				int ncoste = distMin + coste(g, j, vk); 
				if (ncoste < d[vk]) {
					d[vk] = ncoste;
					p[vk] = j;
					try {
						Q.decrecerClave(vk, ncoste);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		return new Par<int[], int[]>(p,d);
	}
	
	private static int coste(Grafo g, int orig, int dest) {
		if (orig >= g.size() || dest >= g.size()) 
			throw new IndexOutOfBoundsException(
					"No se admiten aristas que conecten vertices con indice mayor o igual el numero total de vertices");
		
		// para cada vertice <v', c> adyacente a orig
		for (Par<Integer, Integer> par : g.ady.get(orig)) {
			if (dest == par.fst) {	// si dest = v', existe arista orig -> dest
				return par.snd;		// devolvemos el coste de la arista
			}
		}
			
		return indefinido;			// si no existe arista
	}
	
	static void print(int[] vs) {
		for(int v : vs) {
			System.out.print(v + ", ");
		}
		System.out.println();
	}
	public static void main(String[] args) {
		boolean dirigido = false;
		Grafo g = new Grafo(dirigido,
							new Grafo.Arista(0, 1, 7),
							new Grafo.Arista(0, 2, 9),
							new Grafo.Arista(0, 5, 14),
							new Grafo.Arista(1, 2, 10),
							new Grafo.Arista(1, 3, 15),
							new Grafo.Arista(2, 3, 11),
							new Grafo.Arista(2, 5, 2),
							new Grafo.Arista(3, 4, 6),
							new Grafo.Arista(4, 5, 9));
		
		
		Par<int[], int[]> resultados = dijkstra(g, 0);
		print(resultados.fst); print(resultados.snd);
		
	}

}
	
	
	
