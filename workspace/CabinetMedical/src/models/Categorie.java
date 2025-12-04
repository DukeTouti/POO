package models;

/* ************************************************* *
 * Classe représentant une catégorie de consultation *
 * (ex: Consultation normale, Contrôle, Urgence)     *
 * ************************************************* */

public class Categorie {

	/* Attributs */
	private int id;
	private String designation;	/* Nom de la catégorie */
	private String description;	/* Description détaillée */

	/* Constructeur complet (utilisé lors de la récupération depuis la base de donnée) */
	public Categorie(int id, String designation, String description) {
		this.id = id;
		this.designation = designation;
		this.description = description;
	}

	/* Constructeur sans ID (utilisé lors de la création d'une nouvelle catégorie) */
	public Categorie(String designation, String description) {
		this(-1, designation, description);
	}

	/* ========== GETTERS & SETTERS ========== */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/* ========== MÉTHODES ========== */

	/* Affichage simple (pour les listes déroulantes) */
	@Override
	public String toString() {
		return designation;
	}

	/* Affichage détaillé avec description */
	public String toDetailString() {
		return designation + " - " + description;
	}

	/* Compare deux catégories sur la base de leur désignation */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Categorie) {
			Categorie c = (Categorie) o;
			return designation.equals(c.designation);
		}
		return false;
	}

}