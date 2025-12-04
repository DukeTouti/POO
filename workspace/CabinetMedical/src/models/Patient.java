package models;

/* ****************************** *
 * Classe représentant un patient *
 * Hérite de Utilisateur          *
 * ****************************** */

public class Patient extends Utilisateur {

	/* Attributs spécifiques aux patients */
	private String telephone;
	private String email;
	private String numeroSecu; /* Numéro de sécurité sociale (15 chiffres) */
	private String adresse;
	private Date dateNaissance;
	private String lieuNaissance;

	/* Constructeur complet (utilisé lors de la récupération depuis la base de donnée) */
	public Patient(int id, String nom, String prenom, String login, String mdp, String telephone, String email,
			String numeroSecu, String adresse, Date dateNaissance, String lieuNaissance) {
		super(id, nom, prenom, login, mdp);
		// TODO Auto-generated constructor stub
		this.telephone = telephone;
		this.email = email;
		this.numeroSecu = numeroSecu;
		this.adresse = adresse;
		this.dateNaissance = dateNaissance;
		this.lieuNaissance = lieuNaissance;
	}
	
	/* Constructeur sans ID (utilisé lors de la création d'un nouveau compte) */
	public Patient(String nom, String prenom, String login, String mdp,
	               String telephone, String email, String numeroSecu, String adresse,
	               Date dateNaissance, String lieuNaissance) {
		super(nom, prenom, login, mdp);
		this.telephone = telephone;
		this.email = email;
		this.numeroSecu = numeroSecu;
		this.adresse = adresse;
		this.dateNaissance = dateNaissance;
		this.lieuNaissance = lieuNaissance;
	}

	/* ========== GETTERS & SETTERS ========== */
	
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumeroSecu() {
		return numeroSecu;
	}

	public void setNumeroSecu(String numeroSecu) {
		this.numeroSecu = numeroSecu;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getLieuNaissance() {
		return lieuNaissance;
	}

	public void setLieuNaissance(String lieuNaissance) {
		this.lieuNaissance = lieuNaissance;
	}

	/* ========== MÉTHODES UTILITAIRES ========== */

	/* ************************************************************* *
	 * Vérifie si le numéro de sécurité sociale est valide           *
	 * Format attendu : 15 chiffres exactement                       *
	 * Retourne true si valide, false sinon                          *
	 * ************************************************************* */
	public boolean isNumeroSecuValide() {
		if ((numeroSecu == null) || (numeroSecu.length() != 15)) {
			return false;
		}

		/* Vérifier que tous les caractères sont des chiffres */
		for (int i = 0; i < 15; i++) {
			if (!Character.isDigit(numeroSecu.charAt(i))) {
				return false;
			}
		}

		return true;
	}
	
	/* ************************************* *
	 * Calcule l'âge du patient              *
	 * Retourne 0 si date de naissance null  *
	 * ************************************* */
	public int getAge() {
		if (dateNaissance == null) {
			return 0;
		}

		int anneeActuelle = java.time.Year.now().getValue();
		return anneeActuelle - dateNaissance.getAnnee();
	}

	/* ========== IMPLÉMENTATION DES MÉTHODES ABSTRAITES ========== */
	
	@Override
	public String getRole() {
		// TODO Auto-generated method stub
		return "PATIENT";
	}

	@Override
	public String getLibelle() {
		// TODO Auto-generated method stub
		return getNomComplet() + " - N° Sécu: " + numeroSecu;
	}

	@Override
	public String toString() {
		return getNomComplet() + " (" + getAge() + " ans)";
	}
}
