package models;

/* *********************************** *
 * Classe représentant une date        *
 * avec heure et minute                *
 * *********************************** */

public class Date implements Comparable<Date> {

	/* Attributs de la date */
	private int jour;
	private int mois;
	private int annee;
	private int heure;
	private int minute;

	/* Constructeur complet avec heure et minute */
	public Date(int jour, int mois, int annee, int heure, int minute) {
		this.jour = jour;
		this.mois = mois;
		this.annee = annee;
		this.heure = heure;
		this.minute = minute;
	}

	/* Constructeur sans heure (heure et minute à 0 par défaut) */
	public Date(int jour, int mois, int annee) {
		this(jour, mois, annee, 0, 0);
	}

	/* Constructeur vide */
	public Date() {
		this(1, 1, 2024, 0, 0);
	}

	/* ========== GETTERS & SETTERS ========== */

	public int getJour() {
		return jour;
	}

	public void setJour(int jour) {
		this.jour = jour;
	}

	public int getMois() {
		return mois;
	}

	public void setMois(int mois) {
		this.mois = mois;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public int getHeure() {
		return heure;
	}

	public void setHeure(int heure) {
		this.heure = heure;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	/* ========== MÉTHODES ========== */

	/* Affiche la date au format : jj/mm/aaaa à hh:mm */
	@Override
	public String toString() {
		return String.format("%02d/%02d/%04d à %02d:%02d", jour, mois, annee, heure, minute);
	}

	/* Compare deux dates */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Date) {
			Date d = (Date) o;
			return (jour == d.jour) && (mois == d.mois) && (annee == d.annee) && (heure == d.heure)
					&& (minute == d.minute);
		}
		return false;
	}

	/* Compare deux dates pour le tri (ordre chronologique) */
	@Override
	public int compareTo(Date d) {

		/* Comparaison de l'année */
		if (annee != d.annee) {
			return annee - d.annee;
		}

		/* Comparaison du mois */
		if (mois != d.mois) {
			return mois - d.mois;
		}

		/* Comparaison du jour */
		if (jour != d.jour) {
			return jour - d.jour;
		}

		/* Comparaison de l'heure */
		if (heure != d.heure) {
			return heure - d.heure;
		}

		/* Comparaison de la minute */
		return minute - d.minute;
	}

}