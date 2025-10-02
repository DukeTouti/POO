package ex3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Main {
	private static final PrintStream ps = System.out;
	private static final InputStream is = System.in;
	private static ManipulationTableau mt;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String choix;

		ps.println("=== MANIPULATION INTERACTIVE DE TABLEAU ===");
		ps.println("Operations disponibles :");
		ps.println("  - taille : choisir uniquement la taille");
		ps.println("  - taille_max : choisir la taille et la valeur maximale");
		ps.println("  - tableau : entrer votre propre tableau");
		ps.println("  - aleatoire : generer aleatoirement un tableau");
		ps.println("  - quit : quitter le programme");

		do {
			ps.print("\nQue voulez-vous faire ? ");
			String[] input = readValues(is);
			String ligne = String.join(" ", input).toLowerCase().trim();
			choix = ligne.split("\\s+")[0]; // Premier mot non-vide

			switch (choix) {
			case "taille":
				ps.print("\nEntrez la taille : ");

				String[] ip1 = readValues(is);
				int taille = Integer.parseInt(ip1[0]);

				mt = new ManipulationTableau(taille);
				afficherTableauetSomme();
				break;

			case "taille_max":
				ps.print("\nEntrez la taille : ");

				String[] ip2 = readValues(is);
				int taille2 = Integer.parseInt(ip2[0]);

				ps.print("Entrez la valeur maximale : ");
				String[] ip3 = readValues(is);
				int max = Integer.parseInt(ip3[0]);

				mt = new ManipulationTableau(taille2, max);
				afficherTableauetSomme();
				break;

			case "tableau":
				ps.print("\nEntrez les valeurs separees par des espaces : ");
				String[] ip4 = readValues(is);
				int[] tableau = new int[ip4.length];

				for (int i = 0; i < ip4.length; i++) {
					tableau[i] = Integer.parseInt(ip4[i]);
				}

				mt = new ManipulationTableau(tableau);
				afficherTableauetSomme();
				break;

			case "aleatoire":
				mt = new ManipulationTableau();
				afficherTableauetSomme();
				break;

			case "quit":
				ps.println("\nAu revoir !");
				break;

			default:
				ps.println("\nOperation non reconnue. Utilisez: taille, taille_max, tableau, aleatoire, ou quit");
				break;
			}
		} while (!choix.equals("quit"));
	}

	private static void afficherTableauetSomme() {
		// TODO Auto-generated method stub
		if (mt == null) {
			ps.println("Erreur: tableau non initialisé");
			return;
		}

		ps.println("\n========== RESULTAT ==========");
		ps.println("Taille du tableau: " + mt.getTaille());

		if (mt.getMax() > 0) {
			ps.println("Valeur maximale: " + mt.getMax());
		}

		if (mt.getTaille() <= 20) {
			ps.print("Contenu: [");
			int[] tab = mt.getTableau();
			for (int i = 0; i < tab.length; i++) {
				ps.print(tab[i]);
				if (i < tab.length - 1) {
					ps.print(", ");
				}
			}
			ps.println("]");
		} else {
			ps.println("Tableau trop grand pour etre affiche (taille > 20)");
            ps.println("Apercu des 5 premieres valeurs: ");
            int[] tab = mt.getTableau();
            ps.print("[");
            for (int i = 0; i < 5; i++) {
                ps.print(tab[i]);
                if (i < 4) ps.print(", ");
            }
            ps.println(", ...]");
		}
		
		int somme = mt.sommeValeurTableau();
        ps.println("Somme des valeurs: " + somme);
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
