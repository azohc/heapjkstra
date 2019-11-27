/**
 *  
 * @author Juan Chozas Sumbera
 * juanchozass@gmail.com
 * 
 */
	
public class Dijkstra {

	/**
	 * El algoritmo recibe un grafo G implementado con listas de adyacencia, y un vertice origen s.
	 * Devuelve un vector d de distancias minimas desde s a cada vertice, y un vector p de predecesores que 
	 * indica, para cada vertice k, cual es el vertice anterior a k en el camino minimo desde s hasta k. 
	 */
	void dijkstra(Dijkstra g, int origen) {
		
	}
	
	public static void main(String[] args) {
		Grafo g = new Grafo();
		g = new Grafo(new Grafo.Arista(0, 1, 2), new Grafo.Arista(1, 0, 1));
		g.print();

		MonticuloSesgado m = new MonticuloSesgado(9, 2, 1, 3, 4, 5, 6);
		m.print();
		System.out.println("min antes de borrar: " + m.min());
		m.borrarMin();
		System.out.println("min despues de borrar: " + m.min());
	}

}
	
	
	
