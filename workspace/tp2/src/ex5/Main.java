package ex5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Random;

public class Main {
	private static PrintStream ps = System.out;
	private static InputStream is = System.in;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ps.println("=== JEU DU NOMBRE À DEVINER ===");
		ps.println("Vous avez 3 tentatives pour deviner le nombre.");
		ps.println("Attention : vous devez entrer un seul nombre entier à chaque fois.\n");

		boolean rejouer = true;
		
		while (rejouer) {
			Random rand = new Random();
			Guess guess = new Guess(rand.nextInt(101), 3);
			Game game = new Game(guess);

			int erreursConsecutives = 0;
			//BufferedReader br = new BufferedReader(new InputStreamReader(is));

			while (game.encoreDesTentatives()) {
				ps.print("\nEntrez un nombre : ");
				String[] input = readValues(is);

				if (input.length == 1 && input[0].toLowerCase().equals("quit")) {
					ps.println("Vous avez quitté le jeu. Retourne jouer sérieusement !");
					return;
				}

				if (input.length != 1) {
					ps.println("Erreur : entrez exactement UN seul nombre !");
					erreursConsecutives++;
				} else {
					try {
						int proposition = Integer.parseInt(input[0]);
						erreursConsecutives = 0;

						String resultat = game.proposition(proposition);
						ps.println(resultat);

						if (proposition == guess.getNombreADeviner()) {
							break;
						}

						ps.println("Tentatives restantes : " + game.getTentativesRestantes());

					} catch (NumberFormatException e) {
						ps.println("Erreur : ce n'est pas un nombre entier !");
						erreursConsecutives++;
					}
				}

				if (erreursConsecutives >= 3) {
					ps.println("\nTrop de bêtises ! Le jeu se termine. Retourne jouer sérieusement !");
					return;
				}
			}

			if (!game.encoreDesTentatives()
					&& game.proposition(guess.getNombreADeviner()) != "Bravo ! Vous avez trouvé le nombre.") {
				ps.println("\nDommage ! Vous avez épuisé vos tentatives.");
				ps.println("Le nombre à deviner était : " + guess.getNombreADeviner());
			}
			
			ps.print("\nVoulez-vous rejouer ? (oui/non) : ");
            String[] replayInput = readValues(is);
            if (replayInput.length == 0 || !replayInput[0].toLowerCase().equals("oui")) {
                rejouer = false;
                ps.println("\nMerci d'avoir joué ! À bientôt !");
            }
		}

		ps.println("\n=== FIN DU JEU ===");
	}

	private static String[] readValues(InputStream in) throws IOException {
		InputStreamReader r = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(r);
		String line = br.readLine();
		String[] values = line.split(" ");
		return values;
	}
}
