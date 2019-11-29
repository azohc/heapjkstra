import java.util.HashMap;

/**
 *  
 * @author Juan Chozas Sumbera
 * juanchozass@gmail.com
 * 
 */

class Nodo {
	int _clave;		// clave unica en el arbol 
	int _valor;		// valor asociado a clave
	Nodo izq;		// hijo izquierdo
	Nodo der;		// hijo derecho
	Nodo padre;		// nodo padre
	
	public Nodo(Nodo n) {
		_clave = n._clave; _valor = n._valor;
		if (n.izq != null) 		izq = new Nodo(n.izq); 
		if (n.der != null) 		der = new Nodo(n.der);
	}
	public Nodo(int clave, int valor) { _clave = clave; _valor = valor; }
	public Nodo(Nodo i, int clave, int valor, Nodo d) { _clave = clave; _valor = valor; izq = i; der = d; }

	public void claveValor(boolean mostrarClaves) {
		if (mostrarClaves) {
			System.out.print("(" + _clave + ", " + _valor + ") ");
		} else {			
			System.out.print(_valor + " ");
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
public class MonticuloSesgado {
	
	private int tam;
	private Nodo raiz;
	private HashMap<Integer, Nodo> tabla;
		
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	public MonticuloSesgado() {
		tam = 0;
		raiz = null;
		tabla = new HashMap<>();
	}
	
	public MonticuloSesgado(int... vs) {
		tabla = new HashMap<>();
		for (int v : vs) {
			insertar(v);
		}
	}
	
	public void insertar(int v) {
		int clave = tam;
		while (tabla.containsKey(clave)) { 
			clave++; 
		}
		Nodo nodo = new Nodo(clave, v);
		
		if (vacio()) {
			raiz = nodo;
		} else {
			raiz = unir(raiz, nodo);
		}		
		
		tabla.put(clave, nodo);
		tam++;
	}
	
	public void borrarMin() {
		tam--;
		tabla.remove(raiz._clave);
		raiz = unir(raiz.izq, raiz.der);
	}
	
	public int min() {
		return raiz._valor;
	}
	
	public Nodo unir(Nodo n1, Nodo n2) { 
	      
        if (n1 == null)  
            return n2; 
        if (n2 == null) 
            return n1; 
        
        Nodo nodo = null;
        if (n1._valor < n2._valor) {
        	nodo = new Nodo(unir(n1.der, n2), n1._clave, n1._valor, n1.izq);
        	nodo.padre = n1.padre;
        } else {
        	nodo = new Nodo(unir(n2.der, n1), n2._clave, n2._valor, n2.izq);
        	nodo.padre = n2.padre;
        }
        		 
        nodo.izq.padre = nodo; 
        	
        return nodo;
    } 

	public void decrecerClave(int c, int v) {
		Nodo nodo = tabla.get(c);
		nodo._valor = v;
		
		while (nodo.padre != null && nodo._valor < nodo.padre._valor) {
			Nodo izq = nodo.izq;
			Nodo der = nodo.der;
			
			if (nodo == nodo.padre.izq) {
				nodo.izq = nodo.padre;
				nodo.der = nodo.padre.der;
				nodo.padre = nodo.padre.padre;
				nodo.der.padre = nodo;
				
				// viejo nodo.padre ahora en nodo.izq
				nodo.izq.padre = nodo;
				nodo.izq.izq = izq;
				nodo.izq.der = der;
				if(raiz == nodo.izq) {
					raiz = nodo.izq.padre;
				}
			} else {
				nodo.der = nodo.padre;
				nodo.izq = nodo.padre.izq;
				nodo.padre = nodo.padre.padre;
				nodo.izq.padre = nodo;
				
				// viejo nodo.padre ahora en nodo.der
				nodo.der.padre = nodo;
				nodo.der.izq = izq;
				nodo.der.der = der;
				if(raiz == nodo.der) {
					raiz = nodo.der.padre;
				}
			}
		}
	}
	
	public int tam() {
		return tam;
	}
	
	public boolean vacio() {
		return tam == 0;
	}
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	public void print() {
		if (raiz == null) {
			return;
		}
		System.out.print("recorrido preorden: ");
		raiz.preorden(true);
		System.out.println();
	}

	public void printSinClaves() {
		if (raiz == null) {
			return;
		}
		System.out.print("recorrido preorden: ");
		raiz.preorden(false);
		System.out.println();		
	}
		
}
