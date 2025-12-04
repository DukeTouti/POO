package models;

public class Assistante extends Utilisateur {

	public Assistante(int id, String nom, String prenom, String login, String mdp) {
		super(id, nom, prenom, login, mdp);
		// TODO Auto-generated constructor stub
	}
	
	public Assistante(String nom, String prenom, String login, String mdp) {
		super(nom, prenom, login, mdp);
		// TODO Auto-generated constructor stub
	}

	/* ========== IMPLÉMENTATION DES MÉTHODES ABSTRAITES ========== */
	
	@Override
	public String getRole() {
		// TODO Auto-generated method stub
		return "ASSISTANTE";
	}

	@Override
	public String getLibelle() {
		// TODO Auto-generated method stub
		return getNomComplet() + " - Assistante";
	}

	@Override
	public String toString() {
		return getNomComplet() + " (Assistante)";
	}
}
