package ex3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import ManipulationTableau;

public class Main {
	private static final PrintStream ps = System.out;
	private static final InputStream is = System.in;
	private static ManipulationTableau mt;


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String choix;

		ps.println("=== MANIPULATION INTERACTIVE DE TABLEAU ===");
		ps.println("Operations disponibles: choisir uniquement la taille (taille), choisir la taille et la valeur maximale dans le tableau taille_max,"
				+ "entrer votre propre tableau (tableau), generer aléatoirement un tableau (aleatoir), quit");

		do {
			ps.print("\nQue voulez-vous faire ? ");
			String[] input = readValues(is);
			String ligne = String.join(" ", input).toLowerCase().trim();
			choix = ligne.split("\\s+")[0]; // Premier mot non-vide

			switch (choix) {
			case "taille":
				ps.print("\nEntrez la taille : ");
				String[] ip = readValues(is);
				int taille = Integer.parseInt(ip[0]);
				mt.ManipulationTableau(taille);
				
				break;
			
				ps.println("Opération non reconnue. Utilisez: addition, division, perimetre, ou quit");
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
