package ex1;

public class EmpServiceAdmin extends Employe {
	
	private static final double TAUX_COTISATION = 0.07;
	
	public EmpServiceAdmin(String nom, String prenom, double salaire) {
		super(nom, prenom, salaire);
	}
	
	@Override
	public double calculerCotisation() {
		return salaire * TAUX_COTISATION;
	}
	
	@Override
	public String toString() {
		return "EmployeServiceAdministratif [nom = " + nom + ", prenom = " + prenom + ", salaire = " + salaire
				+ ", calculerCotisation() = " + calculerCotisation() + "€ ]";
	}
}