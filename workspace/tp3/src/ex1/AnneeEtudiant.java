package ex1;

public class AnneeEtudiant {
	private int idEtudiant;
	private int nbModules;
	private ResultatModule[] modules;
	
	private static final int MAX_MODULES = 20;

	public AnneeEtudiant(int idEtudiant) {
		// TODO Auto-generated constructor stub
		this.setIdEtudiant(idEtudiant);
		this.setNbModules(0);
		this.setModules(new ResultatModule[MAX_MODULES]);
	}

	public int getIdEtudiant() {
		return idEtudiant;
	}

	public void setIdEtudiant(int idEtudiant) {
		this.idEtudiant = idEtudiant;
	}

	public int getNbModules() {
		return nbModules;
	}

	public void setNbModules(int nbModules) {
		this.nbModules = nbModules;
	}

	public ResultatModule[] getModules() {
		return modules;
	}

	public void setModules(ResultatModule[] modules) {
		this.modules = modules;
	}

}
