import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
	
	private static final String HELP = "dijkstra -n VS [OPTION...] \n" + 
			"algoritmo de dijkstra de caminos minimos\n" + 
			"genera un grafo de VS vertices forma aleatoria. imprime el tiempo transcurrido en ejecutar el algoritmo de dijkstra\n" + 
			"\n" + 
			"Ejemplos:\n" + 
			"  dijkstra -t 1                # ejecuta el caso de prueba 1" +		
			"  dijkstra -n 100 -d  			# genera un grafo dirigido de 100 vertices.\n" + 
			"  dijkstra -n 10 -r			# genera un grafo de 10 vertices e imprime los resultados.\n" + 
			"  dijkstra -n 10 -s 50			# genera un grafo de 10 vertices. usa 50 como semilla para el objeto Random.\n" +
			"  dijkstra -n 10 -u 60			# genera un grafo de 10 vertices. el 60% de las aristas generadas se borraran.\n" +
			"\n";
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
	 * devuelve el grafo y el numero de aristas que contiene
	 */
	static Par<Grafo, Integer> generaGrafo(int n, boolean dirigido, long semilla, int umbral) {
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
			
		return new Par<Grafo, Integer>(new Grafo(dirigido, n, aristas.toArray(new Grafo.Arista[aristas.size()])), aristas.size());
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
		crearOpciones(options);
		
		String aux;
		boolean dirigido = false;
		int V = -1;
		long semilla = 11;
		int umbral = 30;
		int iteraciones = 1;
		
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("h")) {
				formatter.printHelp(HELP, options);
				return;
			}
			if (cmd.hasOption("t")) {
				if (cmd.getOptionValue("t") == null) {
					System.out.println("error: no se ha especificado el caso de prueba. (puede ser 1 o 2)");
				} else {
					ejecutarPrueba(Integer.parseInt(cmd.getOptionValue("t")));
				}
			}
			if (!cmd.hasOption("n") || cmd.getOptionValue("n") == null) {
				System.out.println("error: no se ha especificado el numero de vertices. (dijkstra -h para mostrar ayuda)");
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
			Par<Grafo, Integer> grafoGenerado = generaGrafo(V, dirigido, semilla, umbral); 
			Grafo g = grafoGenerado.fst;
			Par<int[], int[]> resultados = new Par<int[], int[]>();
			if (cmd.hasOption("a")) {	// imprimir numero de aristas
				System.out.println(grafoGenerado.snd);
			}
			for (int i = 0; i < iteraciones; i++) {	// solo medir e imprimir tiempo del algoritmo				
				long t1 = System.nanoTime();
				resultados = dijkstra(g, 0);
				long t2 = System.nanoTime();
				System.out.println((t2 - t1) / 1000.0f); // milisegundos
			}
			if (cmd.hasOption("r")) {	// imprimir tabla de resultados
				print(resultados);				
			}
		} catch (ParseException e) {
	        System.err.println("error de parseo: " + e.getMessage());
		}
	}

	private static void ejecutarPrueba(int prueba) {
		if (prueba == 1) {
			Grafo g = new Grafo(true, 6,
                    new Grafo.Arista(0, 1, 7),
                    new Grafo.Arista(0, 2, 9),
                    new Grafo.Arista(0, 5, 14),
                    new Grafo.Arista(1, 2, 10),
                    new Grafo.Arista(1, 3, 15),
                    new Grafo.Arista(2, 3, 11),
                    new Grafo.Arista(2, 5, 2),
                    new Grafo.Arista(3, 4, 6),
                    new Grafo.Arista(4, 5, 9));
	
			Par<int[], int[]> resultados;
			long t1 = System.nanoTime();
			resultados = dijkstra(g, 0);
			long t2 = System.nanoTime();
			System.out.println((t2 - t1) / 1000.0f); // milisegundos
			print(resultados);
		} else if (prueba == 2) {
			Grafo g = new Grafo(false, 6,
                    new Grafo.Arista(0, 1, 3),
                    new Grafo.Arista(0, 2, 5),
                    new Grafo.Arista(0, 3, 9),
                    new Grafo.Arista(1, 3, 4),
                    new Grafo.Arista(1, 2, 3),
                    new Grafo.Arista(1, 4, 7),
                    new Grafo.Arista(2, 3, 2),
                    new Grafo.Arista(2, 4, 6),
                    new Grafo.Arista(2, 5, 8),
                    new Grafo.Arista(2, 4, 6),
                    new Grafo.Arista(3, 4, 2),
                    new Grafo.Arista(3, 5, 2),
                    new Grafo.Arista(4, 5, 5));
	
			Par<int[], int[]> resultados;
			long t1 = System.nanoTime();
			resultados = dijkstra(g, 0);
			long t2 = System.nanoTime();
			System.out.println((t2 - t1) / 1000.0f); // milisegundos
			print(resultados);
		} else {
			System.out.println("error: caso de prueba invalido. (puede ser 1 o 2)");
		}
	}
	
	private static void crearOpciones(Options options) {
		options.addOption("a", false, "imprimir numero de aristas generadas");
		options.addOption("d", false, "generar un grafo dirigido");
		options.addOption("h", false, "imprimir este mensaje");
		options.addOption("i", true, "numero de veces que se ejecuta el algoritmo");
		options.addOption("n", true, "cantidad de v�rtices en el grafo");
		options.addOption("r", false, "imprimir resultados del algoritmo");
		options.addOption("s", true, "semilla para objeto Random");
		options.addOption("t", true, "ejecutar un caso de prueba");
		options.addOption("u", true, "porcentage de aristas generadas que se eliminar�n");
	}
}
	
	
	
