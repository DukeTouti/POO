package models;

/* ********************************************* *
 * Classe représentant un acte médical           *
 * Un acte est composé d'un code NGAP            *
 * et d'un coefficient multiplicateur            *
 * ********************************************* */

public class Acte {
	/* Attributs */
	private Code code; /* Code NGAP (CS, KC, etc.) */
	private int coefficient; /* Coefficiant multiplicateur */
	
	/* Constructeur */
	public Acte(Code code, int coefficient) {
		this.code = code;
		this.coefficient = coefficient;
	}
	
	/* ========== GETTERS & SETTERS ========== */

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public int getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(int coefficient) {
		this.coefficient = coefficient;
	}
	
	/* ========== MÉTHODES ========== */
	public double cout() {
		return code.calculerCout(this.coefficient);
	}
	
	@Override
	public String toString() {
		return code.name() + " x" + coefficient + " -> " + String.format("%.2f", cout()) + "€";
	}
}
