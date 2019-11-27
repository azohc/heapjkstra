/**
 *  
 * @author Juan Chozas Sumbera
 * juanchozass@gmail.com
 * 
 */

/*
 * Monticulo sesgado con la operacion decrecerClave
 */
public class MonticuloSesgado {
	/*
	 * Nodo del arbol binario para el monticulo
	 */
	class Nodo implements Cloneable {
		int clave;
		Nodo izq;
		Nodo der;
		
		public Nodo(int c) { clave = c; }
		public Nodo(Nodo i, int c, Nodo d) { clave = c; izq = i; der = d; }
		public void preorder() {
			System.out.print(clave + " ");
			if (izq != null) izq.preorder();	
			if (der != null) der.preorder();
		}
		public void inorder() {
			if (izq != null) izq.inorder();	
			System.out.print(clave + " ");
			if (der != null) der.inorder();
			
		}
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	
	private int tam;
	private Nodo raiz;
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	public MonticuloSesgado() {
		tam = 0;
		raiz = null;
	}
	
	public MonticuloSesgado(int... cs) {
		for (int c : cs) {
			insertar(c);
		}
	}
	
	public void insertar(int c) {
		if (vacio()) {
			raiz = new Nodo(c);
			tam = 1;
		} else {
			raiz = join(raiz, new Nodo(c));
			tam++;
		}		
	}
	
	public void borrarMin() {
		tam--;
		raiz = join(izq(), der());
	}
	
	public int min() {
		return raiz.clave;
	}
	
	public Nodo join(Nodo n1, Nodo n2) { 
	      
        if (n1 == null)  
            return n2; 
        if (n2 == null) 
            return n1; 
         
        return (n1.clave <= n2.clave) ? 
        		new Nodo(join(n1.der, n2), n1.clave, n1.izq) : 
        		new Nodo(join(n1, n2.der), n2.clave, n2.izq); 
    } 

	public void decrecerClave(int c) {
		
	}
	
	public int tamanio() {
		return tam;
	}
	
	public boolean vacio() {
		return tam == 0;
	}
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	private Nodo izq() {
		return raiz.izq;
	}
	
	private Nodo der() {
		return raiz.der;
	}

	public void print() {
		if (raiz == null) {
			return;
		}
		raiz.preorder();
		System.out.println();
	}
		
}
