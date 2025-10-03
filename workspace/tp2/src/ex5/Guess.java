package ex5;

public class Guess {
	private int nombreADeviner;
	private int tentativesMax;
	
	public Guess(int nombreADeviner, int tentativesMax) {
		// TODO Auto-generated constructor stub
		this.nombreADeviner = nombreADeviner;
		this.tentativesMax = tentativesMax;
	}
	
	public int getNombreADeviner() {
		return this.nombreADeviner;
	}
	
	public int getTentativesMax() {
		return this.tentativesMax;
	}

}
