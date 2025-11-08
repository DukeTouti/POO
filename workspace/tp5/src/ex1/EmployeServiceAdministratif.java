package ex1;

public class EmployeServiceAdministratif implements ICotisation{

	private String nom;
	private String prenom;
	private double salaire;
	
	private static final double TAUX_COTISATION = 0.07;
	
	
	public EmployeServiceAdministratif(String nom, String prenom, double salaire) {
		this.nom = nom;
		this.prenom = prenom;
		this.salaire = salaire;
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

	public double getSalaire() {
		return salaire;
	}

	public void setSalaire(double salaire) {
		this.salaire = salaire;
	}

	@Override
	public double calculerCotisation() {
		// TODO Auto-generated method stub
		return salaire * TAUX_COTISATION;
	}

	@Override
	public String toString() {
		return "EmployeServiceAdministratif [nom = " + nom + ", prenom = " + prenom + ", salaire = " + salaire
				+ ", calculerCotisation() = " + calculerCotisation() + "€ ]";
	}

}
