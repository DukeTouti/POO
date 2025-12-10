package models;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Consultation {
	
	PrintStream ps = System.out;
	
	/* Attributs */
	private int id;
	private RendezVous rendezVous; /* Le RDV associé à cette consultation */
	private Date dateConsultation; /* Date réelle de la consultation */
	private String description; /* Compte-rendu médical */
	private double prixConsultation; /* Prix total (calculé automatiquement) */
	private Categorie categorie; /* Catégorie de consultation */
	private List<Acte> actes; /* Liste des actes médicaux effectués */
	
	/* Constructeur complet (utilisé lors de la récupération depuis la base de donnée) */
	public Consultation(int id, RendezVous rendezVous, Date dateConsultation, String description, Categorie categorie) {
		this.id = id;
		this.rendezVous = rendezVous;
		this.dateConsultation = dateConsultation;
		this.description = description;
		this.categorie = categorie;
		this.actes = new ArrayList<>();
		this.prixConsultation = 0.0;
	}
	
	/* Constructeur sans ID (utilisé lors de la création d'une nouvelle consultation) */
	public Consultation(RendezVous rendezVous, Date dateConsultation, String description, Categorie categorie) {
		this(-1, rendezVous, dateConsultation, description, categorie);
	}

	/* ========== GETTERS & SETTERS ========== */
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RendezVous getRendezVous() {
		return rendezVous;
	}

	public void setRendezVous(RendezVous rendezVous) {
		this.rendezVous = rendezVous;
	}

	public Date getDateConsultation() {
		return dateConsultation;
	}

	public void setDateConsultation(Date dateConsultation) {
		this.dateConsultation = dateConsultation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrixConsultation() {
		return prixConsultation;
	}

	public void setPrixConsultation(double prixConsultation) {
		this.prixConsultation = prixConsultation;
	}

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public List<Acte> getActes() {
		return actes;
	}

	/* Récupère le patient via le RDV */
	public Patient getPatient() {
		return rendezVous != null ? rendezVous.getPatient() : null;
	}
	
	/* Récupère le médecin via le RDV */
	public Medecin getMedecin() {
		return rendezVous != null ? rendezVous.getMedecin() : null;
	}
	
	/* ========== GESTION DES ACTES ========== */
	
	/* Ajoute un acte médical à la consultation */
	public void ajouterActe(Acte acte) {
		actes.add(acte);
		calculerCoutTotal();
	}
	
	/* Ajoute un acte avec code et coefficient directement */
	public void ajouterActe(Code code, int coefficient) {
		Acte acte = new Acte(code, coefficient);
		ajouterActe(acte);
	}
	
	/* Supprime un acte médical */
	public void supprimerActe(Acte acte) {
		actes.remove(acte);
		calculerCoutTotal();
	}
	
	/* Supprime un acte à un index donné */
	public void supprimerActe(int index) {
		if (index >= 0 && index < actes.size()) {
			actes.remove(index);
			calculerCoutTotal();
		}
	}
	
	/* ========== CALCUL DES COÛTS ========== */

	private void calculerCoutTotal() {
		// TODO Auto-generated method stub
		double total = 0.0;
		for (Acte acte : actes) {
			total += acte.cout();
		}
		
		this.prixConsultation = total;
	}
	
	/* Retourne le coût total actuel */
	public double coutTotal() {
		calculerCoutTotal();
		return prixConsultation;
	}
	
	/* ========== MÉTHODES D'AFFICHAGE ========== */
	
	/* Affiche les détails complets de la consultation dans la console */
	public void afficherDetails() {
		ps.println("========== CONSULTATION ==========");
		ps.println("Date : " + dateConsultation);
		if (rendezVous != null) {
			ps.println("Médecin : " + getMedecin());
			ps.println("Patient : " + getPatient());
		}
		
		ps.println("Catégorie : " + categorie);
		ps.println("Description : " + description);
		ps.println("\nActes médicaux :");
		for (int i = 0; i < actes.size(); i++) {
			ps.println("  " + (i + 1) + ". " + actes.get(i));
		}
		ps.println("\nCoût total : " + String.format("%.2f", coutTotal()) + "€");
		ps.println("==================================");
	}
	
	/* Affichage simple */
	@Override
	public String toString() {
		return dateConsultation + " - " + 
		       (getPatient() != null ? getPatient().getNomComplet() : "Pas de patient") +
		       " (" + String.format("%.2f", coutTotal()) + "€)";
	}
	
	/* Libellé complet pour les listes GUI */
	public String getLibelle() {
		return "Consultation du " + dateConsultation + 
		       " - " + (getPatient() != null ? getPatient().getNomComplet() : "Pas de patient") +
		       " - Catégorie: " + categorie + 
		       " - Total: " + String.format("%.2f", coutTotal()) + "€";
	}
}
