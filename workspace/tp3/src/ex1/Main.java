package ex1;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class Main {
	private static PrintStream ps = System.out;
	private static InputStream is = System.in;
	private static AnneeEtudiant[] etudiants = new AnneeEtudiant[100];
	private static int nbEtudiants = 0;
	private static AnneeEtudiant etudiantActuel;

	public static void main(String[] args) throws IOException {
		String choix;
		boolean etudiantCree = false;

		ps.println("=== GESTION DES RESULTATS ETUDIANT ===");
		ps.println("Operations disponibles :");
		ps.println(" - creer : creer un nouvel etudiant");
		ps.println(" - ajouter : ajouter un module");
		ps.println(" - moyenne : afficher la moyenne de l'annee");
		ps.println(" - valides : afficher le nombre de modules valides");
		ps.println(" - afficher : afficher tous les resultats");
		ps.println(" - quit : quitter le programme");

		do {
			ps.print("\nQue voulez-vous faire ? ");
			String[] input = Keyboard.readValues(is);
			String ligne = String.join(" ", input).toLowerCase().trim();
			choix = ligne.split("\\s+")[0];

			switch (choix) {
			case "creer":
				int idEtudiant = 0;
				boolean idValide = false;
				
				do {
					ps.print("\nEntrez l'ID de l'etudiant : ");
					String[] idInput = Keyboard.readValues(is);
					
					try {
						idEtudiant = Integer.parseInt(idInput[0]);
						if (idEtudiant > 0) {
							idValide = true;
						} else {
							ps.println("Erreur: l'ID doit etre un nombre positif");
						}
					} catch (NumberFormatException e) {
						ps.println("Erreur: entrez uniquement des nombres !");
					}
				} while (!idValide);
				
				etudiantActuel = new AnneeEtudiant(idEtudiant);
				etudiants[nbEtudiants] = etudiantActuel;
				nbEtudiants++;
				etudiantCree = true;
				ps.println("\n========== RESULTAT ==========");
				ps.println("Etudiant cree avec l'ID : " + idEtudiant);
				ps.println("==============================");
				break;

			case "ajouter":
				if (nbEtudiants == 0) {
					ps.println("\nErreur: veuillez d'abord creer un etudiant avec la commande 'creer'");
					break;
				}
				
				ps.println("\n========== LISTE DES ETUDIANTS ==========");
				for (int i = 0; i < nbEtudiants; i++) {
					ps.println((i + 1) + ". ID: " + etudiants[i].getIdEtudiant() + 
							   " | Modules: " + etudiants[i].getNbModules() + 
							   " | Moyenne: " + (etudiants[i].getNbModules() > 0 ? 
							   String.format("%.2f", etudiants[i].moyenneAnnee()) : "N/A"));
				}
				ps.println("=========================================");
				
				int choixEtudiantAjout = 0;
				boolean choixValideAjout = false;
				
				do {
					ps.print("\nChoisissez un etudiant (1-" + nbEtudiants + ") : ");
					String[] choixInput = Keyboard.readValues(is);
					
					try {
						choixEtudiantAjout = Integer.parseInt(choixInput[0]);
						if (choixEtudiantAjout >= 1 && choixEtudiantAjout <= nbEtudiants) {
							choixValideAjout = true;
						} else {
							ps.println("Erreur: choisissez un numero entre 1 et " + nbEtudiants);
						}
					} catch (NumberFormatException e) {
						ps.println("Erreur: entrez uniquement des nombres !");
					}
				} while (!choixValideAjout);
				
				etudiantActuel = etudiants[choixEtudiantAjout - 1];
				etudiantCree = true;
				
				if (etudiantActuel.getNbModules() >= 20) {
					ps.println("\nErreur: nombre maximum de modules atteint (20)");
					break;
				}
				
				double cc = 0, tp = 0, exam = 0;
				boolean notesValides = false;
				
				do {
					ps.print("\nEntrez la note CC (sur 20) : ");
					String[] ccInput = Keyboard.readValues(is);
					
					ps.print("Entrez la note TP (sur 20) : ");
					String[] tpInput = Keyboard.readValues(is);
					
					ps.print("Entrez la note Exam (sur 20) : ");
					String[] examInput = Keyboard.readValues(is);
					
					try {
						cc = Double.parseDouble(ccInput[0]);
						tp = Double.parseDouble(tpInput[0]);
						exam = Double.parseDouble(examInput[0]);
						
						if (cc >= 0 && cc <= 20 && tp >= 0 && tp <= 20 && exam >= 0 && exam <= 20) {
							notesValides = true;
						} else {
							ps.println("Erreur: les notes doivent etre entre 0 et 20");
						}
					} catch (NumberFormatException e) {
						ps.println("Erreur: entrez uniquement des nombres !");
					}
				} while (!notesValides);
				
				ResultatModule module = new ResultatModule(cc, tp, exam);
				etudiantActuel.getModules()[etudiantActuel.getNbModules()] = module;
				etudiantActuel.setNbModules(etudiantActuel.getNbModules() + 1);
				
				ps.println("\n========== RESULTAT ==========");
				ps.println("Module " + module.getIdModule() + " ajoute avec succes pour l'etudiant " + etudiantActuel.getIdEtudiant());
				ps.println("Moyenne du module : " + String.format("%.2f", module.calculeMoyenne()));
				ps.println("Module valide : " + (module.valideModule() ? "OUI" : "NON"));
				ps.println("==============================");
				break;

			case "moyenne":
				if (!etudiantCree) {
					ps.println("\nErreur: veuillez d'abord creer un etudiant avec la commande 'creer'");
					break;
				}
				
				if (etudiantActuel.getNbModules() == 0) {
					ps.println("\nErreur: aucun module n'a ete ajoute");
					break;
				}
				
				ps.println("\n========== RESULTAT ==========");
				ps.println("Moyenne de l'annee : " + String.format("%.2f", etudiantActuel.moyenneAnnee()));
				ps.println("==============================");
				break;

			case "valides":
				if (!etudiantCree) {
					ps.println("\nErreur: veuillez d'abord creer un etudiant avec la commande 'creer'");
					break;
				}
				
				if (etudiantActuel.getNbModules() == 0) {
					ps.println("\nErreur: aucun module n'a ete ajoute");
					break;
				}
				
				ps.println("\n========== RESULTAT ==========");
				ps.println("Nombre de modules valides : " + etudiantActuel.nombreValides() + "/" + etudiantActuel.getNbModules());
				ps.println("==============================");
				break;

			case "afficher":
				if (nbEtudiants == 0) {
					ps.println("\nErreur: aucun etudiant n'a ete cree");
					break;
				}
				
				ps.println("\n========== LISTE DES ETUDIANTS ==========");
				for (int i = 0; i < nbEtudiants; i++) {
					ps.println((i + 1) + ". ID: " + etudiants[i].getIdEtudiant() + 
							   " | Modules: " + etudiants[i].getNbModules() + 
							   " | Moyenne: " + (etudiants[i].getNbModules() > 0 ? 
							   String.format("%.2f", etudiants[i].moyenneAnnee()) : "N/A"));
				}
				ps.println("=========================================");
				
				int choixEtudiant = 0;
				boolean choixValide = false;
				
				do {
					ps.print("\nChoisissez un etudiant (1-" + nbEtudiants + ") : ");
					String[] choixInput = Keyboard.readValues(is);
					
					try {
						choixEtudiant = Integer.parseInt(choixInput[0]);
						if (choixEtudiant >= 1 && choixEtudiant <= nbEtudiants) {
							choixValide = true;
						} else {
							ps.println("Erreur: choisissez un numero entre 1 et " + nbEtudiants);
						}
					} catch (NumberFormatException e) {
						ps.println("Erreur: entrez uniquement des nombres !");
					}
				} while (!choixValide);
				
				AnneeEtudiant etudiantChoisi = etudiants[choixEtudiant - 1];
				
				if (etudiantChoisi.getNbModules() == 0) {
					ps.println("\n========== RESULTAT ==========");
					ps.println("Cet etudiant n'a aucun module");
					ps.println("==============================");
					break;
				}
				
				ps.println("\n========== RESULTATS DE L'ETUDIANT ==========");
				ps.println("ID Etudiant : " + etudiantChoisi.getIdEtudiant());
				ps.println("Nombre de modules : " + etudiantChoisi.getNbModules());
				ps.println();
				
				for (int i = 0; i < etudiantChoisi.getModules().length; i++) {
					if (etudiantChoisi.getModules()[i] != null) {
						ResultatModule m = etudiantChoisi.getModules()[i];
						ps.println("Module " + m.getIdModule() + " :");
						ps.println("  CC: " + String.format("%.2f", m.getCc()) + 
								   " | TP: " + String.format("%.2f", m.getTp()) + 
								   " | Exam: " + String.format("%.2f", m.getExam()));
						ps.println("  Moyenne: " + String.format("%.2f", m.calculeMoyenne()) + 
								   " | Valide: " + (m.valideModule() ? "OUI" : "NON"));
						ps.println();
					}
				}
				
				ps.println("Moyenne de l'annee : " + String.format("%.2f", etudiantChoisi.moyenneAnnee()));
				ps.println("Modules valides : " + etudiantChoisi.nombreValides() + "/" + etudiantChoisi.getNbModules());
				ps.println("=============================================");
				break;

			case "quit":
				ps.println("\nAu revoir !");
				break;

			default:
				ps.println("\nOperation non reconnue. Utilisez: creer, ajouter, moyenne, valides, afficher ou quit");
				break;
			}
		} while (!choix.equals("quit"));
	}
}