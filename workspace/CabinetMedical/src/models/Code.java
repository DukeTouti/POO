package models;

/* ************************************************ *
 * Enum représentant les codes NGAP                 *
 * (Nomenclature Générale des Actes Professionnels) *
 * Chaque code a un libellé et un coût de base      *
 * ************************************************ */

public enum Code {
	
	/* Valeurs de l'enum avec leurs attributs */
	CS("Consultation au cabinet", 23.0),
	CSC("Consultation cardiologie", 45.73),
	FP("Forfait pédiatrique", 5.0),
	KC("Actes de chirurgie et de spécialité", 2.09),
	KE("Actes d'échographie, de doppler", 1.89),
	K("Autres actes de spécialité", 1.92),
	KFA("Forfait A", 30.49),
	KFB("Forfait B", 60.98),
	ORT("Orthodontie", 2.15),
	PRO("Prothèse dentaire", 2.15);
	
	/* Attributs de chaque code */
	private String libelle;
	private double cout;
	
	/* Constructeur */
	Code(String libelle, double cout) {
		this.libelle = libelle;
		this.cout = cout;
	}
	
	/* ========== GETTERS ========== */
	
	public String getLibelle() {
		return libelle;
	}
	
	public double getCout() {
		return cout;
	}
	
	/* ========== MÉTHODES ========== */
	
	/* Calcule le coût pour un coefficient donné */
	public double calculerCout(int coefficient) {
		return coefficient * cout;
	}
	
	@Override
	public String toString() {
		return name() + " : " + libelle + " (" + cout + "€)";
	}
}