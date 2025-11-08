package ex1;

public abstract class Employe {

	protected String nom;
	protected String prenom;
	protected double salaire;

	public Employe(String nom, String prenom, double salaire) {
		this.nom = nom;
		this.prenom = prenom;
		this.salaire = salaire;
	}

	public abstract double calculerCotisation();

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

	public double getSalaire() {
		return salaire;
	}

	public void setSalaire(double salaire) {
		this.salaire = salaire;
	}

	@Override
	public String toString() {
		return "Employe [nom = " + nom + ", prenom = " + prenom + ", salaire = " + salaire + "€]";
	}
}