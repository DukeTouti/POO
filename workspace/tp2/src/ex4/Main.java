package ex4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Main {
	private static PrintStream ps = System.out;
	private static InputStream is = System.in;
	private static SwitchCase sc;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String choix;

		ps.println("=== CONVERTISSEUR DE DATE INTERACTIF ===");
		ps.println("Operations disponibles :");
		ps.println(" - date : convertir une date jj/mm/aaaa en texte");
		ps.println(" - quit : quitter le programme");

		do {
			ps.print("\nQue voulez-vous faire ? ");
			String[] input = readValues(is);
			String ligne = String.join(" ", input).toLowerCase().trim();
			choix = ligne.split("\\s+")[0];

			switch (choix) {
			case "date":
				boolean date = false;
				int jour;
				int mois;
				int annee;
				String[] parts;
				do {
                    ps.print("\nEntrez une date au format jj/mm/aaaa : ");
                    String[] ip = readValues(is);
                    parts = ip[0].split("/");

                    if (parts.length != 3) {
                        ps.println("Erreur: format invalide (attendu jj/mm/aaaa)");
                        continue;
                    }

                    try {
                        jour = Integer.parseInt(parts[0]);
                        mois = Integer.parseInt(parts[1]);
                        annee = Integer.parseInt(parts[2]);

                        if (!SwitchCase.dateValide(jour, mois, annee)) {
                            ps.println("Erreur: date invalide");
                        } else {
                            date = true;
                        }
                    } catch (NumberFormatException e) {
                        ps.println("Erreur: entrez uniquement des nombres !");
                    }

                } while (!date);
				

				jour = Integer.parseInt(parts[0]);
				mois = Integer.parseInt(parts[1]);
				annee = Integer.parseInt(parts[2]);

				sc = new SwitchCase(jour, mois, annee);
				ps.println("\n========== RESULTAT ==========");
				ps.println("Date convertie : " + sc.convertirDate());
				ps.println("==============================");
				break;

			case "quit":
				ps.println("\nAu revoir !");
				break;

			default:
				ps.println("\nOperation non reconnue. Utilisez: date ou quit");
				break;
			}
		} while (!choix.equals("quit"));
	}

	private static String[] readValues(InputStream in) throws IOException {
		InputStreamReader r = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(r);
		String line = br.readLine();
		String[] values = line.split(" ");
		return values;
	}

}
