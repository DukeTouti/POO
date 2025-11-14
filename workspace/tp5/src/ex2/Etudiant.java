package ex2;

import java.util.ArrayList;
import java.util.List;
import java.io.PrintStream;

public class Etudiant {
	public static PrintStream ps = System.out;

	public int code;
	public String nom, prenom, filiere;

	public Etudiant() {
		super();

	}

	public Etudiant(int code, String nom, String prenom, String filiere) {
		super();
		this.code = code;
		this.nom = nom;
		this.prenom = prenom;
		this.filiere = filiere;
	}

	@Override
	public String toString() {
		return "Etudiant [code = " + code + ", nom = " + nom + ", prenom = " + prenom + ", filiere = " + filiere + "]";
	}

	public static boolean recherche(List<Etudiant> liste, int code) {

		for (Etudiant etudiant : liste) {
			if (etudiant.code == code) {
				return true;
			}
		}
		return false;
	}

	public static void affichage(List<Etudiant> liste) {
		if (liste.isEmpty()) {
			ps.println("La liste est vide");
			return;
		}

		ps.println("\n===   Liste des Etudiants   ===");
		ps.println("Nombre total : " + liste.size());

		for (int i = 0; i < liste.size(); i++) {
			ps.println("[ " + (i + 1) + " ] " + liste.get(i));
		}
	}

	@SuppressWarnings("rawtypes")
	public static List trierListe(List<Etudiant> liste) {
		List<Etudiant> liste_triee = new ArrayList<>(liste);

		liste_triee.sort((etudiant1, etudiant2) -> Integer.compare(etudiant1.code, etudiant2.code));

		return liste_triee;
	}

	public static void supprimerEtudiant(List<Etudiant> l, int code) {
		boolean supprime = false;

		for (int i = 0; i < l.size(); i++) {
			if (l.get(i).code == code) {
				Etudiant etudiantSupprime = l.get(i);
				l.remove(i);
				ps.println("  Étudiant supprimé : " + etudiantSupprime);
				supprime = true;
				break;
			}
		}

		if (!supprime) {
			ps.println("  Aucun étudiant trouvé avec le code " + code);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static List listeSpecialite(List<Etudiant> l, String spe) {
		List<Etudiant> listeFiliere = new ArrayList<>();
		
		for(Etudiant etudiant : l) {
			if (etudiant.filiere.equalsIgnoreCase(spe)) {
				listeFiliere.add(etudiant);
			}
		}
		return listeFiliere;
	}

}