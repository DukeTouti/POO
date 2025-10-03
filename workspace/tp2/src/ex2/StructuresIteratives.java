package ex2;

public class StructuresIteratives {
	private int nombre;
	@SuppressWarnings("unused")
	private int taille;
	
	public StructuresIteratives(int nombre) {
		this.nombre = nombre;
		this.taille = 0;
	}
	
	public int[] diviseurs() {
		int taille = 0;
		for (int i = 1 ; i <= nombre ; i++) {
			if (nombre % i == 0) {
				taille ++;
			}
		}
		
		int[] diviseurs = new int[taille];
		int index = 0 ;
		for (int i = 1 ; i <= nombre ; i++) {
			if (nombre % i == 0) {
				diviseurs[index] = i;
				index++ ;
			}
		}
		
		return diviseurs;
	}
	
	public int somme() {
		int somme = 0;
		int i = 1;
		
		while (i < nombre) {
			if (i % 2 == 0) {
				somme += i;
			}
			i++;
		}
		
		return somme;
	}

	public int getNombre() {
		// TODO Auto-generated method stub
		return this.nombre;
	}

	public boolean estPremier() {
		// TODO Auto-generated method stub
		if (diviseurs().length == 2) {
			return true;
		}
		return false;
	}
}
