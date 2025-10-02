package ex3;

public class ManipulationTableau {
	public static int[] tableau;
	public static int taille;
	public static int max;
	
	public ManipulationTableau() {
		this.taille = 20;
		this.max = 10000;
		this.tableau = generateArray(taille, max);
	}
	
	public ManipulationTableau(int[] tableau) {
		this.tableau = tableau;
		this.taille = tableau.length;
	}
	
	public ManipulationTableau(int taille) {
		this.taille = taille;
		this.max = 10000;
		this.tableau = generateArray(taille, max);
	}
	

	public ManipulationTableau(int taille, int max) {
		this.taille = 20;
		this.max = max;
		this.tableau = generateArray(taille, max);
	}
	
	private int[] generateArray(int taille, int max) {
		// TODO Auto-generated method stub
		for (int i = 0 ; i < taille ; i++) {
			tableau[i] = (int) (Math.random() * (max + 1));
		}
		return tableau;
	}
	
	public int sommeValeurTableau(int[] tableau) {
		int val = 0;
		
		for (int i = 0 ; i < taille ; i++ ) {
			val += tableau[i];
		}
		
		return val;
	}

}
