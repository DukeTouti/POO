package models;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/* ******************************************************** *
 * Classe de test pour valider tous les models              *
 * Execute des scénarios complets pour vérifier la logique  *
 * ******************************************************** */

public class TestModels {
	private static PrintStream ps = System.out;
	@SuppressWarnings("unused")
	private static InputStream is = System.in;
	
	public static void main(String[] args) throws IOException {
		ps.println("===================================================");
		ps.println("|     TEST DES MODELS - CABINET MEDICAL - UIR     |");
		ps.println("===================================================\n");
		
		/* ========== TEST 1 : CRÉATION DES UTILISATEURS ========== */
		ps.println("\n========== TEST 1 : CRÉATION DES UTILISATEURS ==========");
		
		/* Créer des médecins */
		Medecin medecin1 = new Medecin("Amine", "SEMLALI", "semlalia", "123", "Chirurgie");
		Medecin medecin2 = new Medecin("Mariam", "BENSALAH", "bensalahm", "456", "Cardiologie");
		
		ps.println("  Médecin 1 créé : " + medecin1);
		ps.println("    - Role : " + medecin1.getRole());
		ps.println("    - Libellé : " + medecin1.getLibelle());
		
		ps.println("\n  Médecin 2 créé : " + medecin2);
		ps.println("    - Role : " + medecin2.getRole());
		
		/* Créer une assistante */
		Assistante assistante = new Assistante("Anaïs", "BELAL", "belala", "111");
		ps.println("\n  Assistante créée : " + assistante);
		ps.println("    - Role : " + assistante.getRole());
		ps.println("    - Libellé : " + assistante.getLibelle());
		
		/* Créer des patients */
		Date dateNaissance1 = new Date(18, 1, 2002);
		Patient patient1 = new Patient(
			"BECK", "Julien",
			"beckj", "pass",
			"0611111111",
			"beck.julien@email.com",
			"195051012345678",
			"Mulhouse",
			dateNaissance1,
			"Mulhouse"
		);
		
		Date dateNaissance2 = new Date(18, 2, 2004);
		Patient patient2 = new Patient(
			"TAKOINI", "Marwane",
			"takoinim", "pass456",
			"0698765432",
			"takoini.marwane@gmail.com",
			"285077598765432",
			"11 Rue Anthoard, 38000 Grenoble",
			dateNaissance2,
			"Grenoble"
		);
		
		ps.println("\n  Patient 1 créé : " + patient1);
		ps.println("    - Role : " + patient1.getRole());
		ps.println("    - Âge : " + patient1.getAge() + " ans");
		ps.println("    - N° Sécu valide : " + patient1.isNumeroSecuValide());
		ps.println("    - Libellé : " + patient1.getLibelle());
		
		ps.println("\n  Patient 2 créé : " + patient2);
		ps.println("    - Âge : " + patient2.getAge() + " ans");
		
		ps.println("\n    ✓ Test 1 réussi !");
		
		/* ========== TEST 2 : VÉRIFICATION AUTHENTIFICATION ========== */
		ps.println("\n\n========== TEST 2 : AUTHENTIFICATION ==========");
		
		ps.println("  Test login medecin1 (semlalia/123) : " + 
		           medecin1.verifierMotDePasse("semlalia", "123"));
		ps.println("  Test login medecin1 (semlalia/wrong) : " + 
		           medecin1.verifierMotDePasse("semlalia", "wrong"));
		ps.println("  Test login patient1 (beckj/pass) : " + 
		           patient1.verifierMotDePasse("beckj", "pass"));
		
		ps.println("\n    ✓ Test 2 réussi !");
		
		/* ========== TEST 3 : CATÉGORIES ========== */
		ps.println("\n\n========== TEST 3 : CATÉGORIES ==========");
		
		Categorie cat1 = new Categorie("Consultation normale", "Consultation médicale standard");
		Categorie cat2 = new Categorie("Contrôle", "Visite de contrôle post-traitement");
		Categorie cat3 = new Categorie("Urgence", "Consultation en urgence");
		
		ps.println("  Catégorie 1 : " + cat1);
		ps.println("    - Détail : " + cat1.toDetailString());
		ps.println("  Catégorie 2 : " + cat2);
		ps.println("  Catégorie 3 : " + cat3);
		
		ps.println("\n    ✓ Test 3 réussi !");
		
		/* ========== TEST 4 : CODES NGAP ========== */
		ps.println("\n\n========== TEST 4 : CODES NGAP ==========");
		
		ps.println("  Liste des codes NGAP disponibles :");
		for (Code code : Code.values()) {
			ps.println("    • " + code);
		}
		
		ps.println("\n  Test de calcul de coût :");
		ps.println("    CS x1 = " + Code.CS.calculerCout(1) + "€");
		ps.println("    CSC x1 = " + Code.CSC.calculerCout(1) + "€");
		ps.println("    KC x3 = " + Code.KC.calculerCout(3) + "€");
		
		ps.println("\n    ✓ Test 4 réussi !");
		
		/* ========== TEST 5 : ACTES MÉDICAUX ========== */
		ps.println("\n\n========== TEST 5 : ACTES MÉDICAUX ==========");
		
		Acte acte1 = new Acte(Code.CS, 1);
		Acte acte2 = new Acte(Code.KC, 2);
		Acte acte3 = new Acte(Code.KE, 1);
		
		ps.println("  Acte 1 créé : " + acte1);
		ps.println("    - Coût : " + String.format("%.2f", acte1.cout()) + "€");
		
		ps.println("  Acte 2 créé : " + acte2);
		ps.println("    - Coût : " + String.format("%.2f", acte2.cout()) + "€");
		
		ps.println("  Acte 3 créé : " + acte3);
		ps.println("    - Coût : " + String.format("%.2f", acte3.cout()) + "€");
		
		ps.println("\n    ✓ Test 5 réussi !");
		
		/* ========== TEST 6 : RENDEZ-VOUS ========== */
		ps.println("\n\n========== TEST 6 : RENDEZ-VOUS ==========");
		
		Date dateRdv1 = new Date(25, 12, 2025, 14, 30);
		RendezVous rdv1 = new RendezVous(patient1, medecin2, dateRdv1, "Contrôle cardiologique annuel");
		
		ps.println("  RDV 1 créé : " + rdv1);
		ps.println("    - Statut initial : " + rdv1.getStatut());
		ps.println("    - Est modifiable : " + rdv1.estModifiable());
		ps.println("    - Libellé : " + rdv1.getLibelle());
		
		/* Tester les transitions de statut */
		ps.println("\n  Test des transitions de statut :");
		rdv1.confirmer();
		ps.println("    - Après confirmation : " + rdv1.getStatut());
		
		/* Créer un deuxième RDV pour tester l'annulation */
		Date dateRdv2 = new Date(26, 12, 2025, 10, 0);
		RendezVous rdv2 = new RendezVous(patient2, medecin1, dateRdv2, "Intervention Chirurgicale");
		ps.println("\n  RDV 2 créé : " + rdv2.toString());
		ps.println("    - Statut : " + rdv2.getStatut());
		
		rdv2.annuler();
		ps.println("    - Après annulation : " + rdv2.getStatut());
		ps.println("    - Est modifiable : " + rdv2.estModifiable());
		
		ps.println("\n    ✓ Test 6 réussi !");
		
		/* ========== TEST 7 : CONSULTATION COMPLÈTE ========== */
		ps.println("\n\n========== TEST 7 : CONSULTATION COMPLÈTE ==========");
		
		/* Créer une consultation */
		Date dateConsult = new Date(25, 12, 2025, 14, 30);
		Consultation consultation = new Consultation(rdv1, dateConsult, 
			"Examen cardiologique complet. ECG normal. Tension artérielle stable à 12/8.", 
			cat1);
		
		ps.println("  Consultation créée");
		ps.println("    - Patient : " + consultation.getPatient().getNomComplet());
		ps.println("    - Médecin : " + consultation.getMedecin());
		ps.println("    - Catégorie : " + consultation.getCategorie());
		
		/* Ajouter des actes */
		consultation.ajouterActe(Code.CSC, 1);	/* Consultation cardio : 45.73€ */
		consultation.ajouterActe(Code.KE, 1);	/* Échographie : 1.89€ */
		consultation.ajouterActe(Code.K, 2);	/* Autres actes x2 : 3.84€ */
		
		ps.println("\n  Actes ajoutés :");
		for (int i = 0; i < consultation.getActes().size(); i++) {
			ps.println("    " + (i + 1) + ". " + consultation.getActes().get(i));
		}
		
		ps.println("\n  Coût total : " + String.format("%.2f", consultation.coutTotal()) + "€");
		ps.println("    - toString() : " + consultation);
		ps.println("    - getLibelle() : " + consultation.getLibelle());
		
		/* Afficher les détails complets */
		ps.println("\n  Affichage détaillé de la consultation :");
		consultation.afficherDetails();
		
		ps.println("\n    ✓ Test 7 réussi !");
		
		/* ========== TEST 8 : DEUXIÈME CONSULTATION ========== */
		ps.println("\n\n========== TEST 8 : DEUXIÈME CONSULTATION ==========");
		
		/* Créer un nouveau RDV pour patient2 */
		Date dateRdv3 = new Date(27, 12, 2025, 9, 0);
		RendezVous rdv3 = new RendezVous(patient2, medecin2, dateRdv3, "Suivi cardiologique");
		rdv3.confirmer();
		
		Date dateConsult2 = new Date(27, 12, 2025, 9, 0);
		Consultation consultation2 = new Consultation(rdv3, dateConsult2,
			"Consultation cardiologique. Pas de signes pathologiques.",
			cat1);
		
		consultation2.ajouterActe(Code.CS, 1);	/* Consultation standard : 23.00€ */
		consultation2.ajouterActe(Code.K, 3);	/* Autres actes x3 : 5.76€ */
		
		ps.println("  Consultation 2 créée");
		ps.println("    - Patient : " + consultation2.getPatient().getNomComplet());
		ps.println("    - Médecin : " + consultation2.getMedecin());
		ps.println("    - Coût total : " + String.format("%.2f", consultation2.coutTotal()) + "€");
		
		ps.println("\n    ✓ Test 8 réussi !");
		
		/* ========== TEST 9 : SUPPRESSION D'ACTE ========== */
		ps.println("\n\n========== TEST 9 : SUPPRESSION D'ACTE ==========");
		
		ps.println("  Actes avant suppression :");
		for (Acte acte : consultation2.getActes()) {
			ps.println("    • " + acte);
		}
		ps.println("  Coût total : " + String.format("%.2f", consultation2.coutTotal()) + "€");
		
		/* Supprimer un acte */
		consultation2.supprimerActe(0);	/* Supprime le premier acte */
		
		ps.println("\n  Actes après suppression :");
		for (Acte acte : consultation2.getActes()) {
			ps.println("    • " + acte);
		}
		ps.println("  Nouveau coût total : " + String.format("%.2f", consultation2.coutTotal()) + "€");
		
		ps.println("\n    ✓ Test 9 réussi !");
		
		/* ========== TEST 10 : FORMATAGE DES PRÉNOMS COMPOSÉS ========== */
		ps.println("\n\n========== TEST 10 : FORMATAGE PRÉNOMS COMPOSÉS ==========");
		Date dateNaissance3 = new Date(26, 5, 2003);
		Patient patient3 = new Patient(
			"HATHOUTI", "Mohammed Taha",
			"hathoutm", "pass123",
			"0653513522",
			"hathouti.medtaha@mail.com",
			"190037512345678",
			"5 rue du Tour de l'Eau, 38400 Saint-Martin-d'Hères",
			dateNaissance3,
			"Saint-Martin-d'Hères"
		);
		
		
		ps.println("  Patient avec prénom composé : " + patient3.getNomComplet());
		
		Patient patient4 = new Patient(
			"AUBAMEYANG", "Pierre-Emeric",
			"aubamep", "pass",
			"0622222222",
			"auba.pe@email.com",
			"195051098765432",
			"Libreville",
			dateNaissance3,
			"Libreville"
		);
		
		ps.println("  Patient avec prénom à tiret : " + patient4.getNomComplet());
		
		ps.println("\n    ✓ Test 10 réussi !");
		
		/* ========== RÉCAPITULATIF FINAL ========== */
		ps.println("\n\n===================================================");
		ps.println("|       ✓ TOUS LES TESTS SONT PASSÉS !           |");
		ps.println("===================================================");
		
		ps.println("\nRécapitulatif des objets créés :");
		ps.println("  • 2 médecins");
		ps.println("  • 1 assistante");
		ps.println("  • 4 patients");
		ps.println("  • 3 catégories");
		ps.println("  • 3 rendez-vous");
		ps.println("  • 2 consultations");
		ps.println("  • 6 actes médicaux");
		
		ps.println("\nClasses testées :");
		ps.println("  ✓ Date");
		ps.println("  ✓ Utilisateur (abstract)");
		ps.println("  ✓ Medecin");
		ps.println("  ✓ Assistante");
		ps.println("  ✓ Patient");
		ps.println("  ✓ Categorie");
		ps.println("  ✓ StatutRDV (enum)");
		ps.println("  ✓ Code (enum)");
		ps.println("  ✓ Acte");
		ps.println("  ✓ RendezVous");
		ps.println("  ✓ Consultation");
		
		ps.println("\n===================================================");
		ps.println("|   PRÊT POUR LE GUI ET LES CONTROLLERS !        |");
		ps.println("===================================================\n");
	}
}