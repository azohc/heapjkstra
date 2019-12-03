import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

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
						e.printStackTrace();
					}
				}
			}
		}
		
		return new Par<int[], int[]>(d, p);
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
	
	static void print(Par<int[], int[]> rs) {
		System.out.println("vertice\tcoste\tvertice anterior");
		for(int i = 0; i < rs.fst.length; i++) {
			if (rs.fst[i] == Integer.MAX_VALUE) 
				System.out.println(i + "\tinf\t" + rs.snd[i]);
			else if (rs.snd[i] == Integer.MAX_VALUE)
				System.out.println(i + "\t" +rs.fst[i]);
			else 
				System.out.println(i  + "\t" + rs.fst[i] + "\t" + rs.snd[i]);
		}
	}

	/*
	 * Genera un grafo de n vertices creando aristas con costes aleatorios
	 */
	static Grafo generaGrafo(int n, boolean dirigido, long semilla) {
		int[][] matriz = new int[n][n];
		List<Grafo.Arista> aristas = new ArrayList<>();
		
		Random r = new Random(semilla);

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matriz[i][j] = r.nextInt(100);
				if (matriz[i][j] <= 10) {
					matriz[i][j] = 0;
				}
			}
		}

		/*
		 * Para grafos dirigidos, hay que recorrer n*n vertices para crear las aristas
		 * Para grafos normales, solo hay que recorrer la mitad inferior a la diagonal (accn)
		 */
		int accn = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < (dirigido ? n : accn); j++) {
				if (matriz[i][j] != 0) {
					aristas.add(new Grafo.Arista(i, j, matriz[i][j]));
				}
			}
			accn++;
		}
			
		return new Grafo(dirigido, aristas.toArray(new Grafo.Arista[aristas.size()]));
	}
	
	public static void main(String[] args) {
		boolean dirigido = false;
		Grafo g = generaGrafo(125, false, 11);

		Par<int[], int[]> resultados = dijkstra(g, 0);
		print(resultados);	
	}

}
	
	
	
