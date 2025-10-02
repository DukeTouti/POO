package ex3;

import java.util.Random;

public class ManipulationTableau {
	public int[] tableau;
	public int taille;
	public int max;
	
	public ManipulationTableau() {
		this.taille = 20;
		this.max = 10000;
		this.tableau = generateArray(taille, max);
	}
	
	public ManipulationTableau(int[] tableau) {
		this.tableau = tableau;
		this.taille = tableau.length;
		this.max = 0;
	}
	
	public ManipulationTableau(int taille) {
		this.taille = taille;
		this.max = 10000;
		this.tableau = generateArray(taille, max);
	}
	

	public ManipulationTableau(int taille, int max) {
		this.taille = taille;
		this.max = max;
		this.tableau = generateArray(taille, max);
	}
	
	private int[] generateArray(int taille, int max) {
		// TODO Auto-generated method stub
		int[] tab = new int[taille];
		Random rand = new Random();
		
		for (int i = 0 ; i < taille ; i++) {
			tab[i] = rand.nextInt(max + 1);
		}
		return tab;
	}
	
	public int sommeValeurTableau() {
		int val = 0;
		
		for (int i = 0 ; i < taille ; i++ ) {
			val += tableau[i];
		}
		
		return val;
	}
	
	public int[] getTableau() {
		return this.tableau;
	}
	
	public int getTaille() {
		return this.taille;
	}
	
	public int getMax() {
		return this.max;
	}

}
