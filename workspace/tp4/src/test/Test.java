package test;

import java.io.PrintStream;

import entity.ChambreDouble;
import entity.ChambreSimple;
import entity.Reservation;
import entity.SuiteRoyale;

public class Test {
	static PrintStream ps = System.out;

	public static Reservation[] tblResa = new Reservation[100];

	public static void main(String[] args) {
		/*
		 * ChambreSimple chs = new ChambreSimple(1, true, false); ChambreDouble chd =
		 * new ChambreDouble(2, true, true, 2); SuiteRoyale sr = new SuiteRoyale(3,
		 * true, true, "ingSuite");
		 * 
		 * ChambreSimple chs2 = new ChambreSimple(1, true, false); ChambreSimple chd2 =
		 * new ChambreDouble(222, true, true, 2); ChambreSimple sr2 = new SuiteRoyale(3,
		 * true, true, "ingSuite");
		 * 
		 * System.out.println(chs2); System.out.println(chd2); System.out.println(sr2);
		 * 
		 * ChambreSimple[] tblCH = new ChambreSimple[10]; tblCH[0] = chs; tblCH[1] =
		 * chd; tblCH[2] = sr;
		 */

		boolean res = Reservation.creerResa(); // resa 1
		if (res)
			System.out.println("Bravo resa enregistre");
		else
			System.out.println("prob d enregistrement de la resa ");

		Reservation.creerResa(); // resa 2

		ps.println("===============================");
		ps.println("La liste des reservations cr�es");
		ps.println("===============================");
		for (int i = 0; i < tblResa.length; i++) {
			if (tblResa[i] != null)
				System.out.println(tblResa[i]);

		}

		int pos = Reservation.rechResa(1);
		if (pos != -1) {
			ps.println("La reservation existe dans la position " + pos);
		} else {
			ps.println("La reservation 1 n'existe pas dans le tblResa");
		}
	}

}
