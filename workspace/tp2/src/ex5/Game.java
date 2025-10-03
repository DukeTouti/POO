package ex5;

public class Game {
	private Guess guess;
	private int tentativesRestantes;

	public Game(Guess guess) {
		// TODO Auto-generated constructor stub
		this.guess = guess;
		this.tentativesRestantes = guess.getTentativesMax();
	}
	
	public String proposition(int proposition) {
		tentativesRestantes--;
		
		if (proposition == guess.getNombreADeviner()) {
			return "Félicitation ! vous venez de trouver le nombre";
		} else if (proposition < guess.getNombreADeviner()) {
			return "La valeur recherchée est plus grande";
		} else {
			return "La valeur recherchée est plus petite";
		}
	}
	
	public boolean encoreDesTentatives() {
		return tentativesRestantes > 0;
	}
	
	public int getTentativesRestantes() {
		return tentativesRestantes;
	}

}
