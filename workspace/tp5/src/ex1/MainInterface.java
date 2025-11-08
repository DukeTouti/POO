package ex1;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class MainInterface {
	private static PrintStream ps = System.out;
	private static InputStream is = System.in;
	private static List<ICotisation> employes = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		ps.println("==================================================");
		ps.println("|    SYSTÈME DE GESTION DES COTISATIONS AMELI    |");
		ps.println("==================================================\n");

		String choix;

		ps.println("Opérations disponibles :");
		ps.println(" - ajouter : ajouter un nouvel employé");
		ps.println(" - afficher : afficher tous les employés");
		ps.println(" - calculer : calculer les cotisations totales");
		ps.println(" - rechercher : rechercher un employé par nom");
		ps.println(" - supprimer : supprimer un employé");
		ps.println(" - quit : quitter le programme");

		do {
			ps.print("\n--> Que voulez-vous faire ? ");
			String[] input = Keyboard.readValues(is);
			choix = input[0].toLowerCase().trim();

			switch (choix) {
			case "ajouter":
				ajouterEmploye();
				break;

			case "afficher":
				afficherEmployes();
				break;

			case "calculer":
				calculerCotisations();
				break;

			case "rechercher":
				rechercherEmploye();
				break;

			case "supprimer":
				supprimerEmploye();
				break;

			case "quit":
				ps.println("\n  Au revoir !");
				break;

			default:
				ps.println("\n  Opération non reconnue !");
				break;
			}
		} while (!choix.equals("quit"));
	}

	private static void ajouterEmploye() throws IOException {
		ps.println("\n=== AJOUTER UN EMPLOYÉ ===");

		// Choisir le type
		ps.println("\nType d'employé :");
		ps.println(" 1 - Service Technique (6%)");
		ps.println(" 2 - Service Administratif (7%)");
		ps.print("-->Votre choix : ");

		String[] typeInput = Keyboard.readValues(is);
		int type = Integer.parseInt(typeInput[0]);

		if (type != 1 && type != 2) {
			ps.println("  Type invalide !");
			return;
		}

		ps.print("\n--> Nom : ");
		String[] nomInput = Keyboard.readValues(is);
		String nom = String.join(" ", nomInput);

		ps.print("--> Prénom : ");
		String[] prenomInput = Keyboard.readValues(is);
		String prenom = String.join(" ", prenomInput);

		ps.print("--> Salaire (en €) : ");
		String[] salaireInput = Keyboard.readValues(is);
		double salaire = Double.parseDouble(salaireInput[0]);

		if (type == 1) {
			employes.add(new EmployeServiceTechnique(nom, prenom, salaire));
			ps.println("\n  Employé Service Technique ajouté !");
		} else {
			employes.add(new EmployeServiceAdministratif(nom, prenom, salaire));
			ps.println("\n  Employé Service Administratif ajouté !");
		}

		ps.printf("  Cotisation : %.2f €\n", employes.get(employes.size() - 1).calculerCotisation());
	}

	private static void afficherEmployes() {
		ps.println("\n=== LISTE DES EMPLOYÉS ===");

		if (employes.isEmpty()) {
			ps.println("Aucun employé enregistré.");
			return;
		}

		for (int i = 0; i < employes.size(); i++) {
			ps.println("\n[" + (i + 1) + "] " + employes.get(i));
		}
	}

	private static void calculerCotisations() {
		ps.println("\n=== CALCUL DES COTISATIONS ===");

		if (employes.isEmpty()) {
			ps.println("Aucun employé enregistré.");
			return;
		}

		double total = 0;
		for (ICotisation emp : employes) {
			total += emp.calculerCotisation();
		}

		ps.printf("\n--> Nombre d'employés : %d\n", employes.size());
		ps.printf("--> Total des cotisations : %.2f €\n", total);
		ps.printf("--> Moyenne par employé : %.2f €\n", total / employes.size());
	}

	private static void rechercherEmploye() throws IOException {
		ps.print("\n--> Nom à rechercher : ");
		String[] nomInput = Keyboard.readValues(is);
		String nomRecherche = String.join(" ", nomInput).toLowerCase();

		boolean trouve = false;
		ps.println("\n=== RÉSULTATS ===");

		for (ICotisation emp : employes) {
			String empStr = emp.toString().toLowerCase();
			
			if (empStr.contains(nomRecherche)) {
				ps.println(emp);
				trouve = true;
			}
		}

		if (!trouve) {
			ps.println("  Aucun employé trouvé avec ce nom.");
		}
	}

	private static void supprimerEmploye() throws IOException {
		if (employes.isEmpty()) {
			ps.println("\n  Aucun employé à supprimer.");
			return;
		}

		afficherEmployes();

		ps.print("\n--> Numéro de l'employé à supprimer : ");
		String[] numInput = Keyboard.readValues(is);
		int num = Integer.parseInt(numInput[0]);

		if (num > 0 && num <= employes.size()) {
			employes.remove(num - 1);
			ps.println("\n  Employé supprimé !");
		} else {
			ps.println("\n  Numéro invalide !");
		}
	}
}