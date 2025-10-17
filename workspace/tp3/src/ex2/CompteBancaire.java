package ex2;

public class CompteBancaire {
	private int id_client;
	private String nom_client;
	private int solde_client;
	
	public CompteBancaire(int id_client, String nom_client, int solde_client) {
		this.id_client = id_client;
		this.nom_client = nom_client;
		this.solde_client = solde_client;
	}

	public int getId_client() {
		return id_client;
	}

	public void setId_client(int id_client) {
		this.id_client = id_client;
	}

	public String getNom_client() {
		return nom_client;
	}

	public void setNom_client(String nom_client) {
		this.nom_client = nom_client;
	}

	public int getSolde_client() {
		return solde_client;
	}

	public void setSolde_client(int solde_client) {
		this.solde_client = solde_client;
	}

	@Override
	public String toString() {
		return "CompteBancaire [id_client = " + id_client + ", nom_client = " + nom_client + ", solde_client = "
				+ solde_client + "]";
	}
	
	

}
