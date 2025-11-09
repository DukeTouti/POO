package ex2;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Main {
	private static PrintStream ps = System.out;
	private static InputStream is = System.in;
	private static List<Etudiant> listeEtudiants = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		ps.println("===================================================");
		ps.println("|     SYSTÈME DE GESTION DES ÉTUDIANTS - UIR      |");
		ps.println("===================================================\n");

		String choix = "";
		
		ps.println("\n\n========== MENU PRINCIPAL ==========");
		ps.println("Opérations disponibles :");
		ps.println(" - ajouter : ajouter un nouvel étudiant");
		ps.println(" - afficher : afficher tous les étudiants");
		ps.println(" - rechercher : rechercher un étudiant par code");
		ps.println(" - trier : trier la liste par code");
		ps.println(" - supprimer : supprimer un étudiant");
		ps.println(" - vider : supprimer tous les étudiants");
		ps.println(" - quit : quitter le programme");
		ps.println("====================================");
		
		do {
			boolean choixValide = false;
			
			do {
				ps.print("\n--> Que voulez-vous faire ? ");
				
				try {
					String[] input = Keyboard.readValues(is);
					choix = input[0].toLowerCase().trim();

					if (estChoixValide(choix)) {
						choixValide = true;
					} else {
						ps.println("    Opération non reconnue !");
						ps.println("  Veuillez choisir parmi : ajouter, afficher, rechercher, trier, supprimer, vider, demo, quit");
					}
					
				} catch (Exception e) {
					ps.println("    Erreur de saisie ! Réessayez.");
				}
				
			} while (!choixValide);

			switch (choix) {
				case "ajouter":
					ajouterEtudiant();
					break;
					
				case "afficher":
					afficherEtudiants();
					break;
					
				case "rechercher":
					rechercherEtudiant();
					break;
					
				case "trier":
					trierEtudiants();
					break;
					
				case "supprimer":
					supprimerEtudiant();
					break;
					
				case "vider":
					viderListe();
					break;
										
				case "quit":
					ps.println("\n  Au revoir !");
					break;
			}
			
		} while (!choix.equals("quit"));
	}
	
	private static boolean estChoixValide(String choix) {
		String[] choixValides = {"ajouter", "afficher", "rechercher", "trier", 
		                         "supprimer", "vider", "demo", "quit"};
		
		for (String c : choixValides) {
			if (c.equals(choix)) {
				return true;
			}
		}
		return false;
	}
	
	private static void ajouterEtudiant() throws IOException {
		ps.println("\n=== AJOUTER UN ÉTUDIANT ===");
		
		int code = 0;
		String nom = "";
		String prenom = "";
		String filiere = "";
		boolean donneesValides = false;
		
		do {
			try {
				ps.print("\n--> Code étudiant : ");
				String[] codeInput = Keyboard.readValues(is);
				code = Integer.parseInt(codeInput[0]);
				
				ps.print("--> Nom : ");
				String[] nomInput = Keyboard.readValues(is);
				nom = String.join(" ", nomInput);
				
				ps.print("--> Prénom : ");
				String[] prenomInput = Keyboard.readValues(is);
				prenom = String.join(" ", prenomInput);
				
				ps.print("--> Filière : ");
				String[] filiereInput = Keyboard.readValues(is);
				filiere = String.join(" ", filiereInput);
				
				if (code <= 0) {
					ps.println("  Erreur : le code doit être un nombre positif");
					continue;
				}
				
				if (Etudiant.recherche(listeEtudiants, code)) {
					ps.println("  Erreur : un étudiant avec ce code existe déjà !");
					continue;
				}
				
				if (nom.trim().isEmpty() || prenom.trim().isEmpty() || filiere.trim().isEmpty()) {
					ps.println("  Erreur : tous les champs doivent être remplis");
					continue;
				}
				
				donneesValides = true;
				
			} catch (NumberFormatException e) {
				ps.println("  Erreur : entrez un nombre valide pour le code !");
			}
		} while (!donneesValides);
		
		listeEtudiants.add(new Etudiant(code, nom, prenom, filiere));
		
		ps.println("\n    Étudiant ajouté avec succès !");
		ps.println("  Code : " + code + " | Nom : " + nom + " " + prenom + " | Filière : " + filiere);
	}
	
	private static void afficherEtudiants() {
		ps.println("\n=== AFFICHAGE (MÉTHODE STATIQUE Q2) ===");
		
		Etudiant.affichage(listeEtudiants);
	}
	
	private static void rechercherEtudiant() throws IOException {
		ps.println("\n=== RECHERCHER PAR CODE (MÉTHODE STATIQUE Q2) ===");
		
		if (listeEtudiants.isEmpty()) {
			ps.println("  Aucun étudiant dans la liste.");
			return;
		}
		
		ps.print("\n--> Code à rechercher : ");
		try {
			String[] codeInput = Keyboard.readValues(is);
			int code = Integer.parseInt(codeInput[0]);
			
			boolean trouve = Etudiant.recherche(listeEtudiants, code);
			
			if (trouve) {
				ps.println("\n    Étudiant avec le code " + code + " trouvé !");
				
				for (Etudiant e : listeEtudiants) {
					if (e.code == code) {
						ps.println("  " + e);
						break;
					}
				}
			} else {
				ps.println("\n    Aucun étudiant trouvé avec le code " + code);
			}
			
		} catch (NumberFormatException e) {
			ps.println("  Erreur : entrez un nombre valide !");
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void trierEtudiants() {
		ps.println("\n=== TRIER LA LISTE (MÉTHODE STATIQUE Q2) ===");
		
		if (listeEtudiants.isEmpty()) {
			ps.println("  Aucun étudiant dans la liste.");
			return;
		}
		
		ps.println("\nListe AVANT tri :");
		for (int i = 0; i < listeEtudiants.size(); i++) {
			Etudiant e = listeEtudiants.get(i);
			ps.println("  [" + (i+1) + "] Code " + e.code + " : " + e.nom + " " + e.prenom);
		}
		
		List<Etudiant> listeTriee = Etudiant.trierListe(listeEtudiants);
		
		ps.println("\nListe APRÈS tri (ordre croissant des codes) :");
		for (int i = 0; i < listeTriee.size(); i++) {
			Etudiant e = listeTriee.get(i);
			ps.println("  [" + (i+1) + "] Code " + e.code + " : " + e.nom + " " + e.prenom);
		}
		
		ps.println("\n    Liste triée ! (l'originale reste inchangée)");
		
		ps.print("\n--> Voulez-vous remplacer la liste originale par la version triée ? (oui/non) : ");
		
		try {
			String[] reponse = Keyboard.readValues(is);
			if (reponse[0].equalsIgnoreCase("oui")) {
				listeEtudiants = listeTriee;
				ps.println("    Liste originale remplacée !");
			}
		} catch (IOException e) {
		}
	}
	
	private static void supprimerEtudiant() throws IOException {
		if (listeEtudiants.isEmpty()) {
			ps.println("\n  Aucun étudiant à supprimer.");
			return;
		}
		
		Etudiant.affichage(listeEtudiants);
		
		ps.print("\n--> Numéro de l'étudiant à supprimer : ");
		try {
			String[] numInput = Keyboard.readValues(is);
			int num = Integer.parseInt(numInput[0]);
			
			if (num > 0 && num <= listeEtudiants.size()) {
				Etudiant supprime = listeEtudiants.get(num - 1);
				listeEtudiants.remove(num - 1);
				ps.println("\n    Étudiant supprimé : " + supprime.nom + " " + supprime.prenom);
			} else {
				ps.println("\n    Numéro invalide !");
			}
		} catch (NumberFormatException e) {
			ps.println("  Erreur : entrez un nombre valide !");
		}
	}
	
	private static void viderListe() {
		if (listeEtudiants.isEmpty()) {
			ps.println("\n  La liste est déjà vide.");
			return;
		}
		
		int taille = listeEtudiants.size();
		listeEtudiants.clear();
		
		ps.println("\n    Liste vidée ! (" + taille + " étudiant(s) supprimé(s))");
		ps.println("  Taille actuelle : " + listeEtudiants.size());
	}
}