package ex2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Main {
	private static PrintStream ps = System.out;
	private static InputStream is = System.in;
	private static StructuresIteratives si;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String choix;

		ps.println("=== RECHERCHE DE DIVISEURS INTERACTIVE ===");
		ps.println("Operations disponibles :");
		ps.println(" - diviseurs : afficher tous ses diviseurs");
		ps.println(" - somme : calculer la somme des nombres pairs inferieurs");
		ps.println(" - quit : quitter le programme");

		do {
			ps.print("\nQue voulez-vous faire ? ");
			String[] input = readValues(is);
			String ligne = String.join(" ", input).toLowerCase().trim();
			choix = ligne.split("\\s+")[0];

			switch (choix) {
			case "diviseurs":
				boolean nb = false;
				int nombre1;
				do {
					ps.print("\nEntrez un nombre entier positif : ");
					String[] ip1 = readValues(is);
					nombre1 = Integer.parseInt(ip1[0]);
					if (nombre1 <= 0) {
						ps.println("Erreur: Le nombre doit etre positif");
					} else {
						nb = true;
					}

				} while (nb == false);
				

				si = new StructuresIteratives(nombre1);
				afficherDiviseurs();
				break;

			case "somme":
				boolean nb2 = false;
				int nombre2;
				do {
					ps.print("\nEntrez un nombre entier positif : ");
					String[] ip2 = readValues(is);
					nombre2 = Integer.parseInt(ip2[0]);
					if (nombre2 <= 0) {
						ps.println("Erreur: Le nombre doit etre positif");
					} else {
						nb2 = true;
					}

				} while (nb2 == false);

				si = new StructuresIteratives(nombre2);
				afficherSomme();
				break;

			case "quit":
				ps.println("\nAu revoir !");
				break;

			default:
				ps.println("\nOperation non reconnue. Utilisez: diviseurs, somme, ou quit");
				break;
			}
		} while (!choix.equals("quit"));
	}

	private static void afficherDiviseurs() {
		if (si == null) {
			ps.println("Erreur: objet non initialise");
			return;
		}

		ps.println("\n========== RESULTAT ==========");
		ps.println("Nombre analyse: " + si.getNombre());

		int[] diviseurs = si.diviseurs();
		ps.print("Diviseurs: [");
		for (int i = 0; i < diviseurs.length; i++) {
			if (i == 0) {
				ps.print(diviseurs[i]);
			} else {
				ps.print(", " + diviseurs[i]);
			}
		}
		ps.println("]");

		ps.println("Nombre total de diviseurs: " + diviseurs.length);

		if (si.estPremier()) {
			ps.println("-> " + si.getNombre() + " est un nombre PREMIER !");
		}

		ps.println("==============================");
	}

	private static void afficherSomme() {
		if (si == null) {
			ps.println("Erreur: objet non initialise");
			return;
		}

		ps.println("\n========== RESULTAT ==========");
		ps.println("Nombre limite: " + si.getNombre());

		int somme = si.somme();
		ps.println("Somme des nombres pairs inferieurs a " + si.getNombre() + ": " + somme);

		// Afficher les nombres pairs
		ps.print("Nombres pairs: ");
		for (int i = 2; i < si.getNombre(); i += 2) {
			ps.print(i);
			if ((i + 2) < si.getNombre()) {
				ps.print(", ");
			}
		}
		ps.println();

		ps.println("==============================");
	}

	private static String[] readValues(InputStream in) throws IOException {
		InputStreamReader r = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(r);
		String line = br.readLine();
		String[] values = line.split(" ");
		return values;
	}

}
