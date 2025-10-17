package ex2;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class Main {
	private static PrintStream ps = System.out;
	private static InputStream is = System.in;
	private static Banque banque;

	public static void main(String[] args) throws IOException {
		// Authentification
		if (!authentifier()) {
			ps.println("\nNombre maximum de tentatives atteint. Programme termine.");
			return;
		}

		// Initialisation de la banque
		banque = new Banque(1, "UIR Bank");
		
		String choix;

		ps.println("\n=== SYSTEME DE GESTION BANCAIRE ===");
		ps.println("Operations disponibles :");
		ps.println(" - creer : creer un nouveau compte bancaire");
		ps.println(" - afficher : afficher tous les comptes bancaires");
		ps.println(" - supprimer : supprimer un compte bancaire");
		ps.println(" - virement : effectuer un virement entre deux comptes");
		ps.println(" - retrait : retrait d'argent d'un compte");
		ps.println(" - consulter : consulter le solde d'un compte");
		ps.println(" - quit : quitter le programme");

		do {
			ps.print("\nQue voulez-vous faire ? ");
			String[] input = Keyboard.readValues(is);
			String ligne = String.join(" ", input).toLowerCase().trim();
			choix = ligne.split("\\s+")[0];

			switch (choix) {
			case "creer":
				int idCompte = 0;
				String nomClient = "";
				int soldeInitial = 0;
				boolean donneesValides = false;

				do {
					ps.print("\nEntrez l'ID du compte : ");
					String[] idInput = Keyboard.readValues(is);

					ps.print("Entrez le nom du client : ");
					String[] nomInput = Keyboard.readValues(is);
					nomClient = String.join(" ", nomInput);

					ps.print("Entrez le solde initial : ");
					String[] soldeInput = Keyboard.readValues(is);

					try {
						idCompte = Integer.parseInt(idInput[0]);
						soldeInitial = Integer.parseInt(soldeInput[0]);

						if (idCompte <= 0) {
							ps.println("Erreur: l'ID doit etre un nombre positif");
							continue;
						}

						if (nomClient.trim().isEmpty()) {
							ps.println("Erreur: le nom du client ne peut pas etre vide");
							continue;
						}

						if (soldeInitial < 0) {
							ps.println("Erreur: le solde initial ne peut pas etre negatif");
							continue;
						}

						donneesValides = true;
					} catch (NumberFormatException e) {
						ps.println("Erreur: entrez uniquement des nombres pour l'ID et le solde !");
					}
				} while (!donneesValides);

				ps.println("\n========== RESULTAT ==========");
				banque.creerCompte(idCompte, nomClient, soldeInitial);
				ps.println("==============================");
				break;

			case "afficher":
				ps.println("\n========== RESULTAT ==========");
				banque.afficherTousLesComptes();
				ps.println("==============================");
				break;

			case "supprimer":
				int idSupprimer = 0;
				boolean idValideSupp = false;

				do {
					ps.print("\nEntrez l'ID du compte a supprimer : ");
					String[] idInput = Keyboard.readValues(is);

					try {
						idSupprimer = Integer.parseInt(idInput[0]);
						if (idSupprimer > 0) {
							idValideSupp = true;
						} else {
							ps.println("Erreur: l'ID doit etre un nombre positif");
						}
					} catch (NumberFormatException e) {
						ps.println("Erreur: entrez uniquement des nombres !");
					}
				} while (!idValideSupp);

				ps.println("\n========== RESULTAT ==========");
				banque.supprimerCompte(idSupprimer);
				ps.println("==============================");
				break;

			case "virement":
				int idSource = 0, idDest = 0, montantVirement = 0;
				boolean virementValide = false;

				do {
					ps.print("\nEntrez l'ID du compte source : ");
					String[] sourceInput = Keyboard.readValues(is);

					ps.print("Entrez l'ID du compte destination : ");
					String[] destInput = Keyboard.readValues(is);

					ps.print("Entrez le montant du virement : ");
					String[] montantInput = Keyboard.readValues(is);

					try {
						idSource = Integer.parseInt(sourceInput[0]);
						idDest = Integer.parseInt(destInput[0]);
						montantVirement = Integer.parseInt(montantInput[0]);

						if (idSource <= 0 || idDest <= 0) {
							ps.println("Erreur: les IDs doivent etre des nombres positifs");
							continue;
						}

						if (idSource == idDest) {
							ps.println("Erreur: le compte source et destination doivent etre differents");
							continue;
						}

						if (montantVirement <= 0) {
							ps.println("Erreur: le montant doit etre un nombre positif");
							continue;
						}

						virementValide = true;
					} catch (NumberFormatException e) {
						ps.println("Erreur: entrez uniquement des nombres !");
					}
				} while (!virementValide);

				ps.println("\n========== RESULTAT ==========");
				banque.virement(idSource, idDest, montantVirement);
				ps.println("==============================");
				break;

			case "retrait":
				int idRetrait = 0, montantRetrait = 0;
				boolean retraitValide = false;

				do {
					ps.print("\nEntrez l'ID du compte : ");
					String[] idInput = Keyboard.readValues(is);

					ps.print("Entrez le montant du retrait : ");
					String[] montantInput = Keyboard.readValues(is);

					try {
						idRetrait = Integer.parseInt(idInput[0]);
						montantRetrait = Integer.parseInt(montantInput[0]);

						if (idRetrait <= 0) {
							ps.println("Erreur: l'ID doit etre un nombre positif");
							continue;
						}

						if (montantRetrait <= 0) {
							ps.println("Erreur: le montant doit etre un nombre positif");
							continue;
						}

						retraitValide = true;
					} catch (NumberFormatException e) {
						ps.println("Erreur: entrez uniquement des nombres !");
					}
				} while (!retraitValide);

				ps.println("\n========== RESULTAT ==========");
				banque.retraitArgent(idRetrait, montantRetrait);
				ps.println("==============================");
				break;

			case "consulter":
				int idConsulter = 0;
				boolean idValideConsult = false;

				do {
					ps.print("\nEntrez l'ID du compte : ");
					String[] idInput = Keyboard.readValues(is);

					try {
						idConsulter = Integer.parseInt(idInput[0]);
						if (idConsulter > 0) {
							idValideConsult = true;
						} else {
							ps.println("Erreur: l'ID doit etre un nombre positif");
						}
					} catch (NumberFormatException e) {
						ps.println("Erreur: entrez uniquement des nombres !");
					}
				} while (!idValideConsult);

				ps.println("\n========== RESULTAT ==========");
				banque.consulterSolde(idConsulter);
				ps.println("==============================");
				break;

			case "quit":
				ps.println("\nAu revoir !");
				break;

			default:
				ps.println("\nOperation non reconnue. Utilisez: creer, afficher, supprimer, virement, retrait, consulter ou quit");
				break;
			}
		} while (!choix.equals("quit"));
	}

	private static boolean authentifier() throws IOException {
		ps.println("=== AUTHENTIFICATION ===");
		ps.println("Veuillez vous authentifier pour acceder au systeme");
		
		int tentatives = 0;
		final int MAX_TENTATIVES = 3;
		final String LOGIN_CORRECT = "Admin";
		final String MDP_CORRECT = "1234";

		while (tentatives < MAX_TENTATIVES) {
			ps.print("\nLogin : ");
			String[] loginInput = Keyboard.readValues(is);
			String login = loginInput[0];

			ps.print("Mot de passe : ");
			String[] mdpInput = Keyboard.readValues(is);
			String mdp = mdpInput[0];

			if (login.equals(LOGIN_CORRECT) && mdp.equals(MDP_CORRECT)) {
				ps.println("\n========== AUTHENTIFICATION REUSSIE ==========");
				ps.println("Bienvenue " + login + " !");
				ps.println("==============================================");
				return true;
			} else {
				tentatives++;
				int restantes = MAX_TENTATIVES - tentatives;
				
				if (restantes > 0) {
					ps.println("\nErreur: login ou mot de passe incorrect !");
					ps.println("Il vous reste " + restantes + " tentative(s)");
				}
			}
		}

		return false;
	}
}