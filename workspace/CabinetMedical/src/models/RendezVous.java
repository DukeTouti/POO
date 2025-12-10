package models;

/* ******************************************** *
 * Classe représentant un rendez-vous           *
 * entre un patient et un médecin               *
 * ******************************************** */

public class RendezVous {
	
	/* Attributs */
	private int id;
	private Patient patient;		/* Le patient concerné */
	private Medecin medecin;		/* Le médecin consulté */
	private Date dateRdv;			/* Date et heure du RDV */
	private StatutRDV statut;		/* Statut actuel du RDV */
	private String motif;			/* Motif de la consultation */
	
	/* Constructeur complet (utilisé lors de la récupération depuis la base de donnée) */
	public RendezVous(int id, Patient patient, Medecin medecin, Date dateRdv, StatutRDV statut, String motif) {
		this.id = id;
		this.patient = patient;
		this.medecin = medecin;
		this.dateRdv = dateRdv;
		this.statut = statut;
		this.motif = motif;
	}
	
	/* Constructeur sans ID (utilisé lors de la création d'un nouveau RDV) */
	public RendezVous(Patient patient, Medecin medecin, Date dateRdv, String motif) {
		this(-1, patient, medecin, dateRdv, StatutRDV.PLANIFIE, motif);
	}
	
	/* Constructeur simplifié sans motif de consultation */
	public RendezVous(Patient patient, Medecin medecin, Date dateRdv) {
		this(patient, medecin, dateRdv, "");
	}
	
	/* ========== GETTERS & SETTERS ========== */
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public Medecin getMedecin() {
		return medecin;
	}
	
	public void setMedecin(Medecin medecin) {
		this.medecin = medecin;
	}
	
	public Date getDateRdv() {
		return dateRdv;
	}
	
	public void setDateRdv(Date dateRdv) {
		this.dateRdv = dateRdv;
	}
	
	public StatutRDV getStatut() {
		return statut;
	}
	
	public void setStatut(StatutRDV statut) {
		this.statut = statut;
	}
	
	public String getMotif() {
		return motif;
	}
	
	public void setMotif(String motif) {
		this.motif = motif;
	}
	
	/* ========== MÉTHODES UTILITAIRES ========== */
	
	/* Vérifie si le RDV est encore modifiable (pas terminé ou annulé) */
	public boolean estModifiable() {
		return statut != StatutRDV.TERMINE && statut != StatutRDV.ANNULE;
	}
	
	/* Confirme le rendez-vous (passe de PLANIFIE à CONFIRME) */
	public void confirmer() {
		if (statut == StatutRDV.PLANIFIE) {
			this.statut = StatutRDV.CONFIRME;
		}
	}
	
	/* Annule le rendez-vous */
	public void annuler() {
		if (estModifiable()) {
			this.statut = StatutRDV.ANNULE;
		}
	}
	
	/* Marque le rendez-vous comme terminé */
	public void terminer() {
		if (statut == StatutRDV.CONFIRME || statut == StatutRDV.PLANIFIE) {
			this.statut = StatutRDV.TERMINE;
		}
	}
	
	/* Affichage simple */
	@Override
	public String toString() {
		return dateRdv.toString() + " - " + patient.getNomComplet();
	}

	/* Libellé complet pour les listes GUI */
	public String getLibelle() {
		return "RDV du " + dateRdv + " - " + patient.getNomComplet() + 
		       " avec " + medecin.getLibelle() + " [" + statut + "]";
	}
	
}