package ex4;

public class SwitchCase {
	private int jour;
	private int mois;
	private int annee;

	public SwitchCase(int jour, int mois, int annee) {
		this.jour = jour;
		this.mois = mois;
		this.annee = annee;
	}

	public String convertirDate() {
		String moisEnLettres = "";

		switch (mois) {
		case 1:
			moisEnLettres = "Janvier";
			break;
		case 2:
			moisEnLettres = "Février";
			break;
		case 3:
			moisEnLettres = "Mars";
			break;
		case 4:
			moisEnLettres = "Avril";
			break;
		case 5:
			moisEnLettres = "Mai";
			break;
		case 6:
			moisEnLettres = "Juin";
			break;
		case 7:
			moisEnLettres = "Juillet";
			break;
		case 8:
			moisEnLettres = "Août";
			break;
		case 9:
			moisEnLettres = "Septembre";
			break;
		case 10:
			moisEnLettres = "Octobre";
			break;
		case 11:
			moisEnLettres = "Novembre";
			break;
		case 12:
			moisEnLettres = "Décembre";
			break;
		default:
			moisEnLettres = "Mois invalide";
			break;
		}

		String affichageAnnee = new String();
		if (annee < 0) {
			affichageAnnee = (-annee) + " Avant Jésus-Christ";
		} else {
			affichageAnnee = String.valueOf(annee);
		}

		if (jour == 1) {
			return "1er " + moisEnLettres + " " + affichageAnnee;
		}

		return jour + " " + moisEnLettres + " " + affichageAnnee;
	}

	private static boolean estBissextile(int annee) {
		if (annee < 0) {
			annee = -annee; // gérer années négatives
		}
		return (annee % 4 == 0 && annee % 100 != 0) || (annee % 400 == 0);
	}

	public static boolean dateValide(int jour, int mois, int annee) {
		if (jour <= 0 || mois <= 0) {
			return false;
		}
		if (mois < 1 || mois > 12) {
			return false;
		}
		
		int maxJour;
		
		switch (mois) {
		case 4:
		case 6:
		case 9:
		case 11:
			maxJour = 30;
			break;
		case 2:
			maxJour = estBissextile(annee) ? 29 : 28;
			break;
		default:
			maxJour = 31;
			break;
		}

		return jour >= 1 && jour <= maxJour;
	}
}
