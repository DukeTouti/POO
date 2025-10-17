package ex2;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unused")
public class Banque {
	PrintStream ps = System.out;
	private int id_banque;
	private String nom_banque;
	private ArrayList<CompteBancaire> tblCmp;
	
	public Banque(int id_banque, String nom_banque) {
		this.id_banque = id_banque;
		this.nom_banque = nom_banque;
		this.tblCmp = new ArrayList<CompteBancaire>();
	}

	@Override
	public String toString() {
		return "Banque [ps=" + ps + ", id_banque=" + id_banque + ", nom_banque=" + nom_banque + ", tblCmp=" + tblCmp
				+ "]";
	}

	public void creerCompte(int id, String nom, int solde) {
		tblCmp.add(new CompteBancaire(id, nom, solde));
		ps.println("Compte créé avec succès !");
    }
	
	private CompteBancaire rechercherCompte(int id) {
		for (CompteBancaire compte : tblCmp) {
			if(compte.getId_client() == id) {
				return compte;
			}
		}
		
		return null;
	}
	
	public void virement(int idSource, int idDest, int montant) {
		CompteBancaire source = rechercherCompte(idSource);
		CompteBancaire dest = rechercherCompte(idDest);
		
		if(source == null) {
			ps.println("Compte source introuvable !");
			return;
		}
		
		if(dest == null) {
			ps.println("Compte destination introuvable !");
			return;
		}
		
		if (source.retrait(montant)) {
			dest.depot(montant);
			ps.println("Virement d'un montant de " + montant + "€ a été effectué !");
		} else {
			ps.println("Solde insuffisant pour effectuer votre opération !");
		}
	}

	public void retraitArgent(int id, int montant) {
		CompteBancaire compte = rechercherCompte(id);
		
		if (compte == null) {
			ps.println("Compte introuvable !");
			return;
		}
		
		if (compte.retrait(montant)) {
			ps.println("Retrait d'un montant de " + montant + "€ a été effectué !");
			ps.println("Actualisation de votre solde !");
			ps.println("Nouveau Solde : " + compte.getSolde_client() + "€");
		} else {
			ps.println("Solde insuffisant pour effectuer votre opération !");
		}
	}

	public void supprimerCompte(int id) {
		CompteBancaire compte = rechercherCompte(id);
		
		if (compte != null) {
			tblCmp.remove(compte);
			ps.println("Compte supprimé !");
		} else {
			ps.println("Compte introuvable !");
		}
	}
	
	public void afficherTousLesComptes() {
		if (tblCmp.isEmpty()) {
        	ps.println("Aucun compte bancaire.");
            return;
        }
        ps.println("\n========== Liste des comptes ==========");
        for (CompteBancaire compte : tblCmp) {
        	ps.println(compte.toString());
        }
        ps.println("========================================\n");
	}
	
	public void consulterSolde(int id) {
		CompteBancaire compte = rechercherCompte(id);
        
		if (compte != null) {
        	ps.println("Solde du compte " + id + " : " + compte.getSolde_client() + "€");
        } else {
        	ps.println("Compte introuvable !");
        }
	}
	
}
