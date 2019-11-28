import java.util.HashMap;

/**
 *  
 * @author Juan Chozas Sumbera
 * juanchozass@gmail.com
 * 
 */

class Nodo <V> {
	int clave;		// clave unica en el arbol 
	V valor;		// valor asociado a clave
	Nodo<V> izq;	// hijo izquierdo
	Nodo<V> der;	// hijo derecho
	
	public Nodo(int c, V v) { clave = c; valor = v; }
	public Nodo(Nodo<V> i, int c, V v, Nodo<V> d) { clave = c; valor = v; izq = i; der = d; }
	
	public void claveValor(boolean mostrarClaves) {
		if (mostrarClaves) {
			System.out.print("(" + clave + ", " + valor + ") ");
		} else {			
			System.out.print(valor + " ");
		}
	}
	public void preorden(boolean claves) {
		claveValor(claves);
		if (izq != null) izq.preorden(claves);	
		if (der != null) der.preorden(claves);
	}
	public void inorden(boolean claves) {
		if (izq != null) izq.inorden(claves);	
		claveValor(claves);
		if (der != null) der.inorden(claves);
	}
	public void postorden(boolean claves) {
		if (izq != null) izq.postorden(claves);	
		if (der != null) der.postorden(claves);
		claveValor(claves);
	}
}

/*
 * Monticulo sesgado con la operacion decrecerClave
 */
public class MonticuloSesgado <V extends Comparable<V>> {
	
	private int tam;
	private Nodo<V> raiz;
	private HashMap<Integer, Nodo<V>> tabla;
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	public MonticuloSesgado() {
		tam = 0;
		raiz = null;
		tabla = new HashMap<>();
	}
	
	public MonticuloSesgado(V... vs) {
		tabla = new HashMap<>();
		for (V v : vs) {
			insertar(v);
		}
	}
	
	public void insertar(V v) {
		int clave = tam;
		if (tabla.containsKey(clave)) { //TODO change if -> while
			clave++; 
		}
		Nodo<V> nodo = new Nodo<>(clave, v);
		
		if (vacio()) {
			raiz = nodo;
		} else {
			raiz = join(raiz, nodo);
		}		
		
		tabla.put(clave, nodo);
		tam++;
	}
	
	public void borrarMin() {
		tam--;
		raiz = join(izq(), der());
	}
	
	public int min() {
		return raiz.clave;
	}
	
	public Nodo<V> join(Nodo<V> n1, Nodo<V> n2) { 
	      
        if (n1 == null)  
            return n2; 
        if (n2 == null) 
            return n1; 
        
        return (n1.valor.compareTo(n2.valor) > 0) ?
        		new Nodo<V>(join(n1, n2.der), n2.clave, n2.valor, n2.izq) :
        		new Nodo<V>(join(n1.der, n2), n1.clave, n1.valor, n1.izq); 
    } 

	public void decrecerClave(int c, V v) {
		
	}
	
	public int tam() {
		return tam;
	}
	
	public boolean vacio() {
		return tam == 0;
	}
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	private Nodo<V> izq() {
		return raiz.izq;
	}
	
	private Nodo<V> der() {
		return raiz.der;
	}

	public void print() {
		if (raiz == null) {
			return;
		}
		raiz.preorden(true);
		System.out.println();
	}

	public void printSinClaves() {
		if (raiz == null) {
			return;
		}
		raiz.preorden(false);
		System.out.println();		
	}
		
}
