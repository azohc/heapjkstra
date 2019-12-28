import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
		
		while (!Q.vacio() && Q.minValor() != indefinido) {
			Par<Integer, Integer> pmin = Q.min(); 
			Q.borrarMin();
			j = pmin.fst; 				// j = clave del nodo siendo visitado actualmente
			int costeMin = pmin.snd;	// costeMin = coste minimo a j
			
			List<Par<Integer, Integer>> js = g.ady.get(j);
			for (k = 0; k < js.size(); k++) {
				int vk = js.get(k).fst;		// vk = clave de nodo adyacente a j
				// calcular coste nuevo de vk (pasando por j)
				int nuevoCoste = costeMin == indefinido ? coste(g, j, vk) : costeMin + coste(g, j, vk); 
				if (nuevoCoste < d[vk]) {	// actualizar variables si es mejor
					d[vk] = nuevoCoste;
					p[vk] = j;
					try {
						Q.decrecerClave(vk, nuevoCoste);
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
	static Grafo generaGrafo(int n, boolean dirigido, long semilla, int umbral) {
		int[][] matriz = new int[n][n];
		List<Grafo.Arista> aristas = new ArrayList<>();
		
		Random r = new Random(semilla);
		
		/*
		 * Para grafos dirigidos, hay que recorrer n*n vertices para crear las aristas
		 * Para grafos normales, solo hay que recorrer la mitad inferior a la diagonal (accn)
		 */
		int accn = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < (dirigido ? n : accn); j++) {
				matriz[i][j] = r.nextInt(100);
				if (matriz[i][j] < umbral) {	// para reducir el numero de aristas	
					matriz[i][j] = 0;		
				}
			}
			accn++;
		}
		accn = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < (dirigido ? n : accn); j++) {
				if (matriz[i][j] != 0) {
					aristas.add(new Grafo.Arista(i, j, matriz[i][j]));
				}
			}
			accn++;
		}
			
		return new Grafo(dirigido, n, aristas.toArray(new Grafo.Arista[aristas.size()]));
	}

	
	/*
	 * algoritmo de dijkstra para caminos minimos 
	 * implementado con monticulo sesgado (con operacion decrecerClave)
	 * 
	 * paraemtros: -d 
	 * grafo dirigido o no
	 */
	public static void main(String[] args) throws IOException {
		Options options = new Options();
		HelpFormatter formatter = new HelpFormatter();
		options.addOption("d", false, "generar un grafo dirigido");
		options.addOption("r", false, "imprimir resultados del algoritmo");
		options.addOption("i", true, "numero de veces que se ejecuta el algoritmo");
		options.addOption("n", true, "cantidad de vértices en el grafo");
		options.addOption("s", true, "semilla para objeto Random");
		options.addOption("u", true, "porcentage de aristas generadas que se eliminarán");
		options.addOption("h", false, "imprimir este mensaje");

		
		String aux;
		boolean dirigido = false;
		int V = -1;
		long semilla = 11;
		int umbral = 30;
		int iteraciones = 1;
		
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("h") || cmd.hasOption("help")) {
				formatter.printHelp("dijkstra -n VS [OPTION...] \n" + 
						"algoritmo de dijkstra de caminos mínimos\n" + 
						"genera un grafo de VS vértices forma aleatoria. imprime el tiempo transcurrido en ejecutar el algoritmo de dijkstra\n" + 
						"\n" + 
						"Ejemplos:\n" + 
						"  dijkstra -n 100 -d  			# genera un grafo dirigido de 100 vértices.\n" + 
						"  dijkstra -n 10 -r			# genera un grafo de 10 vértices e imprime los resultados.\n" + 
						"  dijkstra -n 10 -s 50			# genera un grafo de 10 vertices. usa 50 como semilla para el objeto Random.\n" +
						"  dijkstra -n 10 -u 60			# genera un grafo de 10 vertices. el 60% de las aristas generadas se borrarán.\n" +
						"\n", options);
				return;
			}
			if (!cmd.hasOption("n") || cmd.getOptionValue("n") == null) {
				System.out.println("error: no se ha especificado el número de vértices. (dijkstra -h para mostrar ayuda)");
				return;
			}
			V = Integer.parseInt(cmd.getOptionValue("n"));
			dirigido = cmd.hasOption("d"); 
			if (cmd.hasOption("s")) {
				aux = cmd.getOptionValue("s");			
				if (aux != null) {
					semilla = Long.parseLong(cmd.getOptionValue("s"));			
				}
			}
			if (cmd.hasOption("u")) {
				aux = cmd.getOptionValue("u");			
				if (aux != null) {
					umbral = Integer.parseInt(aux); 
				}
			}
			if (cmd.hasOption("i")) {
				aux = cmd.getOptionValue("i");			
				if (aux != null) {
					iteraciones = Integer.parseInt(aux); 
				}
			}
			Grafo g = generaGrafo(V, dirigido, semilla, umbral);
			for (int i = 0; i < iteraciones; i++) {				
				long t1 = System.nanoTime();
				Par<int[], int[]> resultados = dijkstra(g, 0);
				long t2 = System.nanoTime();
				System.out.println((t2 - t1) / 1000.0f); // milisegundos
				if (cmd.hasOption("r")) {
					print(resultados);				
				}
			}
		} catch (ParseException e) {
	        System.err.println("error de parseo: " + e.getMessage());
		}
	}

}
	
	
	
