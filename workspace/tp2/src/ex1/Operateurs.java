package ex1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Operateurs {
	private static final PrintStream ps = System.out;
	private static final InputStream is = System.in;

	public static void main(String[] args) throws IOException {
		String choix;

		ps.println("=== CALCULATRICE INTERACTIVE ===");
		ps.println("Operations disponibles: addition, division, perimetre, quit");

		do {
			ps.print("\nQuelle opération voulez-vous effectuer? ");
			String[] input = readValues(is);
			String ligne = String.join(" ", input).toLowerCase().trim();
			choix = ligne.split("\\s+")[0]; // Premier mot non-vide

			switch (choix) {
			case "addition":
				effectuerAddition();
				break;
			case "division":
				effectuerDivision();
				break;
			case "perimetre":
				effectuerPerimetre();
				break;
			case "quit":
				ps.println("Au revoir!");
				break;
			default:
				ps.println("Opération non reconnue. Utilisez: addition, division, perimetre, ou quit");
			}
		} while (!choix.equals("quit"));
	}

	private static void effectuerAddition() throws IOException {
		while (true) { // Boucle jusqu'à succès
			try {
				ps.print("Entrez votre addition (format: nombre + nombre): ");
				String[] entree = readValues(is);
				String line = String.join(" ", entree);

				if (!line.contains("+")) {
					ps.println("Erreur: L'addition doit contenir le symbole '+'");
					continue; // Redemander au lieu de return
				}

				String[] parties = line.split("\\+");
				if (parties.length != 2) {
					ps.println("Erreur: Format invalide. Utilisez: nombre + nombre");
					continue;
				}

				String aStr = parties[0].trim();
				String bStr = parties[1].trim();

				if (aStr.isEmpty() || bStr.isEmpty()) {
					ps.println("Erreur: Les deux nombres doivent être spécifiés");
					continue;
				}

				// Essayer de parser les deux nombres
				double a = Double.parseDouble(aStr);
				double b = Double.parseDouble(bStr);

				// Déterminer si le résultat doit être int ou double
				// Un nombre est considéré comme int s'il n'a pas de point ET si sa valeur est
				// entière
				boolean aEstInt = !aStr.contains(".") && a == (int) a;
				boolean bEstInt = !bStr.contains(".") && b == (int) b;

				double resultat = calculerSomme(a, b);

				if (aEstInt && bEstInt) {
					// Si le résultat est aussi entier, l'afficher comme int
					if (resultat == (int) resultat) {
						ps.print((int) a + " + " + (int) b + " = " + (int) resultat + "\n");
					} else {
						ps.print((int) a + " + " + (int) b + " = " + resultat + "\n");
					}
				} else if (aEstInt) {
					if (resultat == (int) resultat) {
						ps.print((int) a + " + " + b + " = " + (int) resultat + "\n");
					} else {
						ps.print((int) a + " + " + b + " = " + resultat + "\n");
					}
				} else if (bEstInt) {
					if (resultat == (int) resultat) {
						ps.print(a + " + " + (int) b + " = " + (int) resultat + "\n");
					} else {
						ps.print(a + " + " + (int) b + " = " + resultat + "\n");
					}
				} else {
					if (resultat == (int) resultat) {
						ps.print(a + " + " + b + " = " + (int) resultat + "\n");
					} else {
						ps.print(a + " + " + b + " = " + resultat + "\n");
					}
				}
				break; // Sortir de la boucle si succès

			} catch (NumberFormatException e) {
				ps.println("Erreur: Veuillez entrer des nombres valides");
				// Continue la boucle pour redemander
			}
		}
	}

	private static void effectuerDivision() throws IOException {
		while (true) { // Boucle jusqu'à succès
			try {
				ps.print("Entrez votre division (format: nombre / nombre ou  nombre ÷ nombre): ");
				String[] entree = readValues(is);
				String line = String.join(" ", entree);

				if (!line.contains("/") && !line.contains("÷")) {
					ps.println("Erreur: La division doit contenir le symbole '/' ou '÷'");
					continue;
				}

				String[] parties;
				char ope;
				if (line.contains("/")) {
					parties = line.split("/");
					ope = '/';
				} else {
					parties = line.split("÷");
					ope = '÷';
				}

				if (parties.length != 2) {
					ps.println("Erreur: Format invalide. Utilisez: nombre / nombre ou nombre ÷ nombre");
					continue;
				}

				String aStr = parties[0].trim();
				String bStr = parties[1].trim();

				if (aStr.isEmpty() || bStr.isEmpty()) {
					ps.println("Erreur: Les deux nombres doivent être spécifiés");
					continue;
				}

				double a = Double.parseDouble(aStr);
				double b = Double.parseDouble(bStr);

				if (b == 0) {
					ps.println("Erreur: Division par zéro impossible");
					continue;
				}

				double resultat = calculerDivision(a, b);

				boolean aEstInt = !aStr.contains(".") && a == (int) a;
				boolean bEstInt = !bStr.contains(".") && b == (int) b;

				// Si le résultat est un nombre entier, l'afficher comme int
				if (resultat == (int) resultat) {
					if (aEstInt && bEstInt) {
						ps.print((int) a + " " + ope + " " + (int) b + " = " + (int) resultat + "\n");
					} else if (aEstInt) {
						ps.print((int) a + " " + ope + " " + b + " = " + (int) resultat + "\n");
					} else if (bEstInt) {
						ps.print(a + " " + ope + " " + (int) b + " = " + (int) resultat + "\n");
					} else {
						ps.print((int) a + " " + ope + " " + b + " = " + (int) resultat + "\n");
					}

				} else {
					if (aEstInt && bEstInt) {
						ps.print((int) a + " " + ope + " " + (int) b + " = " + resultat + "\n");
					} else if (aEstInt) {
						ps.print((int) a + " " + ope + " " + b + " = " + resultat + "\n");
					} else if (bEstInt) {
						ps.print(a + " " + ope + " " + (int) b + " = " + resultat + "\n");
					} else {
						ps.print((int) a + " " + ope + " " + b + " = " + resultat + "\n");
					}
				}
				break; // Sortir de la boucle si succès

			} catch (NumberFormatException e) {
				ps.println("Erreur: Veuillez entrer des nombres valides");
			}
		}
	}

	private static void effectuerPerimetre() throws IOException {
		while (true) { // Boucle jusqu'à succès
			try {
				ps.print("Entrez le diamètre ou le rayon (format: d=valeur ou r=valeur): ");
				String[] input = readValues(is);
				String ligne = String.join(" ", input).toLowerCase().trim().replaceAll("\\s+", ""); // Supprimer TOUS
																									// les espaces

				if (ligne.startsWith("d=")) {
					String valeurStr = ligne.substring(2).trim();
					if (valeurStr.isEmpty()) {
						ps.println("Erreur: Veuillez spécifier une valeur pour le diamètre (ex: d=5)");
						continue;
					}
					double diametre = Double.parseDouble(valeurStr);
					if (diametre <= 0) {
						ps.println("Erreur: Le diamètre doit être positif");
						continue;
					}
					double perimetre = calculerPerimetreAvecDiametre(diametre);
					ps.print("Périmètre du cercle avec diamètre " + diametre + " = " + String.format("%.2f", perimetre)
							+ "\n");
					break;

				} else if (ligne.startsWith("r=")) {
					String valeurStr = ligne.substring(2).trim();
					if (valeurStr.isEmpty()) {
						ps.println("Erreur: Veuillez spécifier une valeur pour le rayon (ex: r=5)");
						continue;
					}
					double rayon = Double.parseDouble(valeurStr);
					if (rayon <= 0) {
						ps.println("Erreur: Le rayon doit être positif");
						continue;
					}
					double perimetre = calculerPerimetreAvecRayon(rayon);
					ps.print("Périmètre du cercle avec rayon " + rayon + " = " + String.format("%.2f", perimetre)
							+ "\n");
					break;

				} else {
					ps.println("Erreur: Format invalide. Utilisez: d=valeur ou r=valeur");
					continue;
				}

			} catch (NumberFormatException e) {
				ps.println("Erreur: Veuillez entrer une valeur numérique valide");
			}
		}
	}

	/*
	 * public static int calculerSommeInt(int a, int b) { return a + b; }
	 */

	public static double calculerSomme(double a, double b) {
		return a + b;
	}

	public static double calculerDivision(double a, double b) {
		return a / b;
	}

	public static double calculerPerimetreAvecDiametre(double diametre) {
		return Math.PI * diametre;
	}

	public static double calculerPerimetreAvecRayon(double rayon) {
		return 2 * Math.PI * rayon; // CORRECTION: était 2 * calculerPerimetreAvecDiametre(rayon) ce qui était faux
	}

	private static String[] readValues(InputStream in) throws IOException {
		InputStreamReader r = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(r);
		String line = br.readLine();
		String[] values = line.split(" ");
		return values;
	}
}