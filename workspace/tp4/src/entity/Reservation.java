package entity;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import test.Test;

public class Reservation {
	static InputStream is = System.in;
	static PrintStream ps = System.out;

	public int idResa;
	public static String nomClt;
	public static int nbrNuit;
	public ChambreSimple local; // <<<=========
	public static String dateResa; // pour simplifier

	public static int cmpResa = 1;
	public static Scanner scN = new Scanner(is);
	public static Scanner scS = new Scanner(is);

	private static HashSet<String> chambresReservees = new HashSet<String>();

	private static HashMap<String, Integer> statsChambres = new HashMap<>();

	static {
		statsChambres.put("Simple", 0);
		statsChambres.put("Double", 0);
		statsChambres.put("Royal", 0);
	}

	public Reservation(String nomClt, int nbrNuit, ChambreSimple local, String dateResa) {

		this.idResa = cmpResa++;
		this.nomClt = nomClt;
		this.nbrNuit = nbrNuit;
		this.local = local;
		this.dateResa = dateResa;

		marquerChambreReservee(local.numCh, dateResa, nbrNuit);

		mettreAJourStats(local, true);
	}

	@Override
	public String toString() {
		return "Reservation [idResa=" + idResa + ", nomClt=" + nomClt + ", nbrNuit=" + nbrNuit + ", local=" + local
				+ ", dateResa=" + dateResa + "]";
	}

	public static boolean estDisponible(int numCh, String dateDebut, int nbrNuits) {

		for (int i = 0; i < nbrNuits; i++) {
			String cle = numCh + "-" + dateDebut + "-jour" + i;

			if (chambresReservees.contains(cle)) {
				return false;
			}
		}
		return true;
	}

	private static void marquerChambreReservee(int numCh, String dateDebut, int nbrNuit) {
		for (int i = 0; i < nbrNuit; i++) {
			String cle = numCh + "-" + dateDebut + "-jour" + i;
			chambresReservees.add(cle);
		}
	}

	private static void libererChambre(int numCh, String dateDebut, int nbrNuit) {
		for (int i = 0; i < nbrNuit; i++) {
			String cle = numCh + "-" + dateDebut + "-jour" + i;
			chambresReservees.remove(cle);
		}
	}

	private static void mettreAJourStats(ChambreSimple chs, boolean ajouter) {
		String type;
		if (chs instanceof SuiteRoyale) {
			type = "Royal";
		} else if (chs instanceof ChambreDouble) {
			type = "Double";
		} else {
			type = "Simple";
		}

		int valeur = statsChambres.get(type);
		statsChambres.put(type, ajouter ? valeur + 1 : valeur - 1);
	}

	// methode de création de reservation
	public static boolean creerResa() {
		ps.println("\n===========================================");
		ps.println("||    CRÉATION D'UNE RÉSERVATION         ||");
		ps.println("===========================================");

		//INFORMATION CLIENT
		ps.print("Nom du client : ");
		nomClt = scS.nextLine().trim();

		if (nomClt.isEmpty()) {
			ps.println("Le nom ne peut pas être vide !");
			return false;
		}

		//DATE DE RÉSERVATION
		ps.print("Date de réservation (format JJ/MM/AAAA) : ");
		dateResa = scS.nextLine().trim();

		//NOMBRE DE NUITS
		ps.print("Nombre de nuits : ");
		nbrNuit = scN.nextInt();

		if (nbrNuit <= 0) {
			ps.println("Le nombre de nuits doit être positif !");
			return false;
		}
		
		ps.println("\n-----------------------------------");
        ps.println("|     TYPE DE CHAMBRE             |");
        ps.println("|---------------------------------|");
        ps.println("|  1. Chambre Simple              |");
        ps.println("|  2. Chambre Double              |");
        ps.println("|  3. Suite Royale                |");
        ps.println("-----------------------------------");
        ps.print("Votre choix : ");
        int choixType = scN.nextInt();    
        
        String typeChoisi = "";
        switch (choixType) {
            case 1:
                typeChoisi = "Simple";
                break;
            case 2:
                typeChoisi = "Double";
                break;
            case 3:
                typeChoisi = "Royal";
                break;
            default:
                ps.println("Choix invalide !");
                return false;
        }
        
		return true;

	}

	// methode de recherche de resa
	public static int rechResa(int idResa) {
		int position = -1;

		return position;
	}

	// methode de comparaison
	public int equals(Reservation r2) {

		int res = 0;

		if (this.nbrNuit > r2.nbrNuit) {
			res = -1;
		} else if (this.nbrNuit < r2.nbrNuit) {
			res = 1;
		}

		return res;
	}

	// tri des resa
	public static void resaSort() {
		Reservation resaTemp = null;

	}

}
