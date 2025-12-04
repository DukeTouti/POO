package models;

/* ************************************************ *
 * Classe abstraite représentant un utilisateur     *
 * Sert de base pour Medecin, Assistante et Patient *
 * ************************************************ */

public abstract class Utilisateur {

	/* Attributs communs à tous les utilisateurs */
	protected int id; /* ID sera auto-généré par la base de donnée */
	protected String nom;
	protected String prenom;
	protected String login; /* Identifiant unique de connexion */
	protected String mdp;

	/* Constructeur complet (utilisé lors de la récupération depuis la base de donnée) */
	public Utilisateur(int id, String nom, String prenom, String login, String mdp) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.login = login;
		this.mdp = mdp;
	}

	/* ********************************************************************** *
	 * Constructeur sans ID (utilisé lors de la création d'un nouveau compte) *
	 * L'ID sera généré automatiquement par la BD                             *
	 * ********************************************************************** */
	public Utilisateur(String nom, String prenom, String login, String mdp) {
		this(-1, nom, prenom, login, mdp);
	}

	/* ========== GETTERS & SETTERS ========== */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	/* ========== MÉTHODES ========== */

	/* Vérifie si le login et le mot de passe correspondent */
	public boolean verifierMotDePasse(String login, String mdp) {
		return this.login.equals(login) && this.mdp.equals(mdp);
	}

	/* ******************************************** *
	 * Retourne le nom complet formaté : NOM Prénom *
	 * Exemple : "HATHOUTI Mohammed Taha"           *
	 * ******************************************** */
	public String getNomComplet() {
		if ((nom == null) || (prenom == null)) {
			return "";
		}

		String nomMaj = nom.toUpperCase();
		String prenomMin = formaterPrenom(prenom);

		return nomMaj + " " + prenomMin;
	}

	/* ********************************************************** *
	 * Formate un prénom avec majuscule après chaque espace/tiret *
	 * Exemples :                                                 *
	 * - "mohammed taha" -> "Mohammed Taha"                       *
	 * - "jean-luc" -> "Jean-Luc"                                 *
	 * - "claire marie" -> "Claire Marie"                         *
	 * ********************************************************** */
	private String formaterPrenom(String prenom) {
		if ((prenom == null) || (prenom.isEmpty())) {
			return "";
		}

		StringBuilder resultat = new StringBuilder();
		boolean majusculeSuivante = true; /* Première lettre toujours en majuscule */

		for (int i = 0; i < prenom.length(); i++) {
			char c = prenom.charAt(i);

			if ((c == ' ') || (c == '-')) {
				resultat.append(c); /* On rajoute le séparateur */
				majusculeSuivante = true; /* On previent que la prochaine lettre doit être en majuscule */
			} else if (majusculeSuivante) {
				resultat.append(Character.toUpperCase(c)); /* Mettre la lettre en majuscule et on la rajoute */
				majusculeSuivante = false; /* prevenir que la prochaine lettre sera en minuscule */
			} else {
				resultat.append(Character.toLowerCase(c)); /* Mettre la lettre en minuscule et on la rajoute */
			}
		}

		return resultat.toString();
	}

	@Override
	public String toString() {
		return getNomComplet();
	}

	/* Compare deux utilisateurs sur la base de leur nom et prénom */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Utilisateur) {
			Utilisateur u = (Utilisateur) o;
			return nom.equals(u.nom) && prenom.equals(u.prenom);
		}
		return false;
	}

	/* ================= MÉTHODES ABSTRAITES ================= * 
	 * Ces méthodes seront implémentées par les classes filles */

	/* Retourne le rôle de l'utilisateur ("MEDECIN", "ASSISTANTE" ou "PATIENT") */
	public abstract String getRole();

	/* Retourne le libellé complet pour les listes et menus déroulants */
	public abstract String getLibelle();

}