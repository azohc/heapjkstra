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
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	public Nodo(Nodo n) {
		_clave = n._clave; _valor = n._valor;
		if (n.izq != null) 		izq = new Nodo(n.izq); 
		if (n.der != null) 		der = new Nodo(n.der);
	}
	
	
	public Nodo(int clave, int valor) { 
		_clave = clave; 
		_valor = valor; 
	}
	
	
	public Nodo(Nodo i, int clave, int valor, Nodo d) { 
		_clave = clave; 
		_valor = valor; 
		izq = i; 
		der = d; 
	}
	
	@Override
	public String toString() {
		if (izq == null && der == null)
			return "[hoja] c: " + _clave + ", v: " + _valor;
		return "c: " + _clave + ", v: " + _valor;
	}
	
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
			insertar(tam++, v);
		}
	}
	
	
	public void insertar(int clave, int valor) {
		if (tabla.containsKey(clave)) { 
			throw new IllegalArgumentException("ya existe la clave " + clave + " en el monticulo");
		}
		Nodo nodo = new Nodo(clave, valor);
		
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
		if (tam != 0) {
			raiz.padre = null;
		}
	}

	
	public Par<Integer, Integer> min() {
		return new Par<Integer, Integer>(raiz._clave, raiz._valor); 
	}
	
	
	public int minValor() {
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
	

	public void decrecerClave(int clave, int valor) throws IllegalAccessException {
		Nodo nodo = tabla.get(clave);
		if (nodo == null) {
			throw new IllegalAccessException("no hay nodo almacenado con clave " + clave);
		}
		
		if (valor >= nodo._valor) return;

		if (nodo._clave == raiz._clave) {
			raiz._valor = valor;
			return;
		}
		nodo._valor = valor;
		
		while (nodo.padre != null && nodo._valor < nodo.padre._valor) {
			Nodo izq = nodo.izq;
			Nodo der = nodo.der;
			Nodo padre = nodo.padre;
			
			if (nodo == padre.izq) {
				nodo.izq = padre;
				nodo.der = padre.der;
				nodo.padre = padre.padre;
				if (nodo.der != null) {
					nodo.der.padre = nodo;
				}
			} else {
				nodo.der = nodo.padre;
				nodo.izq = nodo.padre.izq;
				nodo.padre = nodo.padre.padre;
				if (nodo.izq != null) {
					nodo.izq.padre = nodo;
				}
			}
			
			if (padre.padre.izq == padre) {
				padre.padre.izq = nodo;
			} else {
				padre.padre.der = nodo;
			}
			padre.padre = nodo;
			padre.izq = izq;
			padre.der = der;
			if(raiz == padre) {
				raiz = padre.padre;
			}
		}
	}
	
	
	public int tam() {
		return tam;
	}
	
	
	public boolean vacio() {
		return tam == 0;
	}
	
	
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
