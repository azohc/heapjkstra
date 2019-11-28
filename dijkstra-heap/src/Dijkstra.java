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
		MonticuloSesgado m = new MonticuloSesgado(1, 2, 3);
		m.print();
		m.decrecerClave(2, 0);
		m.print();
	}

}
	
	
	
