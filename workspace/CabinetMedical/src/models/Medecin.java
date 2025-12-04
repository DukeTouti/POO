package models;

public class Medecin extends Utilisateur{
	
	private String specialite;

	/* Constructeur complet (utilisé lors de la récupération depuis la base de donnée) */
	public Medecin(int id, String nom, String prenom, String login, String mdp, String specialite) {
		super(id, nom, prenom, login, mdp);
		// TODO Auto-generated constructor stub
		this.specialite = specialite;
	}
	
	/* Constructeur sans ID (utilisé lors de la création d'un nouveau compte) */
	public Medecin(String nom, String prenom, String login, String mdp, String specialite) {
		super(nom, prenom, login, mdp);
		this.specialite = specialite;
	}
	
	/* ========== GETTERS & SETTERS ========== */
	
	public String getSpecialite() {
		return specialite;
	}

	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}

	/* ========== IMPLÉMENTATION DES MÉTHODES ABSTRAITES ========== */

	@Override
	public String getRole() {
		// TODO Auto-generated method stub
		return "MEDECIN";
	}

	@Override
	public String getLibelle() {
		// TODO Auto-generated method stub
		return "Dr " + getNomComplet() + " - " + specialite;
	}

	@Override
	public String toString() {
		return "Dr " + getNomComplet() + " (" + specialite + ")";
	}
}
