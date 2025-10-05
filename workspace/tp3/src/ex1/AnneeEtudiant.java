package ex1;

public class AnneeEtudiant {
	private int id_etudiant;
	private int nb_modules;
	private ResultatModule[] modules;
	
	private static final int MAX_MODULES = 20;

	public AnneeEtudiant(int id_etudiant) {
		// TODO Auto-generated constructor stub
		this.id_etudiant = id_etudiant;
		this.nb_modules = 0;
		this.modules = new ResultatModule[MAX_MODULES];
	}

	public int getIdEtudiant() {
		return id_etudiant;
	}

	public void setIdEtudiant(int id_etudiant) {
		this.id_etudiant = id_etudiant;
	}

	public int getNbModules() {
		return nb_modules;
	}

	public void setNbModules(int nb_modules) {
		this.nb_modules = nb_modules;
	}

	public ResultatModule[] getModules() {
		return modules;
	}

	public void setModules(ResultatModule[] modules) {
		this.modules = modules;
	}
	
	public double moyenneAnnee() {
		if (nb_modules == 0) {
			return 0;
		}
		
		double somme = 0;
		
		for (int i = 0 ; i < nb_modules ; i++) {
			somme += modules[i].calculeMoyenne();
		}
		
		return somme / nb_modules;
	}
	
	public int nombreValides() {
		int cmpt = 0;
		
		for (int i = 0 ; i < nb_modules ; i++) {
			if (modules[i].valideModule()) {
				cmpt++;
			}
		}
		
		return cmpt;
	}

}
