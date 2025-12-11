package controllers;

import models.*;
import models.Date;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* ******************************************************** *
 * Contrôleur de gestion des rendez-vous                    *
 * Gère les opérations CRUD sur les RDV                     *
 * ******************************************************** */

public class RendezVousController {
	
	/* Configuration de la base de données */
	private static final String URL = "jdbc:mysql://localhost:3306/projet_java";
	private static final String USER_BD = "root";
	private static final String MDP_BD = "";
	
	/* ========== CONNEXION À LA BASE DE DONNÉES ========== */
	
	/* Établit une connexion à la base de données */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER_BD, MDP_BD);
	}
	
	/* ========== CRÉATION DE RDV ========== */
	
	/* ********************************************************************** *
	 * Crée un nouveau rendez-vous dans la base de données                    *
	 * Retourne true si création réussie, false sinon                         *
	 * ********************************************************************** */
	public boolean createRDV(RendezVous rdv) {
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			conn = getConnection();
			
			String query = "INSERT INTO rendez_vous (patient_id, medecin_id, date_rdv, heure_rdv, motif, statut) " +
			               "VALUES (?, ?, ?, ?, ?, ?)";
			pst = conn.prepareStatement(query);
			
			/* Remplir les paramètres */
			pst.setInt(1, rdv.getPatient().getId());
			pst.setInt(2, rdv.getMedecin().getId());
			
			/* Convertir notre Date en java.sql.Date */
			Date dateRdv = rdv.getDateRdv();
			java.util.Calendar cal = java.util.Calendar.getInstance();
			cal.set(dateRdv.getAnnee(), dateRdv.getMois() - 1, dateRdv.getJour());
			pst.setDate(3, new java.sql.Date(cal.getTimeInMillis()));
			
			/* Heure au format HH:MM */
			String heureStr = String.format("%02d:%02d:00", dateRdv.getHeure(), dateRdv.getMinute());
			pst.setString(4, heureStr);
			
			pst.setString(5, rdv.getMotif());
			pst.setString(6, rdv.getStatut().name());
			
			int result = pst.executeUpdate();
			return result > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			fermerRessources(null, pst, conn);
		}
	}
	
	/* ========== RÉCUPÉRATION DES MÉDECINS ========== */
	
	/* Récupère tous les médecins (pour sélection lors de prise de RDV) */
	public List<Medecin> getTousMedecins() {
		List<Medecin> medecins = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT u.id, u.nom, u.prenom, u.login, u.mdp, m.specialite " +
			               "FROM utilisateurs u " +
			               "JOIN medecins m ON u.id = m.utilisateur_id " +
			               "WHERE u.role = 'MEDECIN'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String login = rs.getString("login");
				String mdp = rs.getString("mdp");
				String specialite = rs.getString("specialite");
				
				medecins.add(new Medecin(id, nom, prenom, login, mdp, specialite));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			/* Fermer les ressources */
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return medecins;
	}
	
	/* ========== RÉCUPÉRATION DES RDV PAR PATIENT ========== */
	
	/* Récupère tous les RDV d'un patient */
	public List<RendezVous> getRDVByPatient(Patient patient) {
		List<RendezVous> rdvList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT r.id, r.date_rdv, r.heure_rdv, r.motif, r.statut, " +
			               "u_med.nom as med_nom, u_med.prenom as med_prenom, m.specialite " +
			               "FROM rendez_vous r " +
			               "JOIN medecins m ON r.medecin_id = m.utilisateur_id " +
			               "JOIN utilisateurs u_med ON m.utilisateur_id = u_med.id " +
			               "WHERE r.patient_id = ? " +
			               "ORDER BY r.date_rdv DESC, r.heure_rdv DESC";
			
			pst = conn.prepareStatement(query);
			pst.setInt(1, patient.getId());
			rs = pst.executeQuery();
			
			while (rs.next()) {
				/* Créer le médecin */
				Medecin medecin = new Medecin(rs.getString("med_nom"), rs.getString("med_prenom"), "", "", rs.getString("specialite"));
				
				/* Créer la date */
				Date dateRdv = convertirSQLDateEtHeure(rs.getDate("date_rdv"), rs.getString("heure_rdv"));
				
				/* Créer le RDV */
				RendezVous rdv = new RendezVous(rs.getInt("id"), patient, medecin, dateRdv, StatutRDV.valueOf(rs.getString("statut")), 
						rs.getString("motif")
				);
				
				rdvList.add(rdv);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return rdvList;
	}
	
	/* ========== RÉCUPÉRATION DES RDV PAR MÉDECIN ========== */
	
	/* Récupère les RDV d'un médecin pour une date donnée */
	public List<RendezVous> getRDVByMedecinEtDate(Medecin medecin, Date date) {
		List<RendezVous> rdvList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT r.id, r.date_rdv, r.heure_rdv, r.motif, r.statut, " +
			               "u_pat.nom as pat_nom, u_pat.prenom as pat_prenom, " +
			               "p.telephone, p.email, p.numero_secu, p.adresse, p.date_naissance, p.lieu_naissance " +
			               "FROM rendez_vous r " +
			               "JOIN patients p ON r.patient_id = p.utilisateur_id " +
			               "JOIN utilisateurs u_pat ON p.utilisateur_id = u_pat.id " +
			               "WHERE r.medecin_id = ? AND r.date_rdv = ? " +
			               "ORDER BY r.heure_rdv ASC";
			
			pst = conn.prepareStatement(query);
			pst.setInt(1, medecin.getId());
			
			/* Convertir notre Date en java.sql.Date */
			java.util.Calendar cal = java.util.Calendar.getInstance();
			cal.set(date.getAnnee(), date.getMois() - 1, date.getJour());
			pst.setDate(2, new java.sql.Date(cal.getTimeInMillis()));
			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				/* Créer le patient */
				Date dateNaissance = convertirSQLDate(rs.getDate("date_naissance"));
				
				Patient patient = new Patient(
					rs.getString("pat_nom"),
					rs.getString("pat_prenom"),
					"", "",
					rs.getString("telephone"),
					rs.getString("email"),
					rs.getString("numero_secu"),
					rs.getString("adresse"),
					dateNaissance,
					rs.getString("lieu_naissance")
				);
				
				/* Créer la date RDV */
				Date dateRdv = convertirSQLDateEtHeure(rs.getDate("date_rdv"), rs.getString("heure_rdv"));
				
				/* Créer le RDV */
				RendezVous rdv = new RendezVous(
					rs.getInt("id"),
					patient,
					medecin,
					dateRdv,
					StatutRDV.valueOf(rs.getString("statut")),
					rs.getString("motif")
				);
				
				rdvList.add(rdv);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return rdvList;
	}
	
	/* Récupère tous les RDV d'un médecin */
	public List<RendezVous> getRDVByMedecin(Medecin medecin) {
		List<RendezVous> rdvList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT r.id, r.date_rdv, r.heure_rdv, r.motif, r.statut, " +
			               "u_pat.nom as pat_nom, u_pat.prenom as pat_prenom " +
			               "FROM rendez_vous r " +
			               "JOIN utilisateurs u_pat ON r.patient_id = u_pat.id " +
			               "WHERE r.medecin_id = ? " +
			               "ORDER BY r.date_rdv DESC, r.heure_rdv DESC";
			
			pst = conn.prepareStatement(query);
			pst.setInt(1, medecin.getId());
			rs = pst.executeQuery();
			
			while (rs.next()) {
				/* Créer un patient simplifié */
				Patient patient = new Patient(
					rs.getString("pat_nom"),
					rs.getString("pat_prenom"),
					"", "", "", "", "", "", null, ""
				);
				
				/* Créer la date */
				Date dateRdv = convertirSQLDateEtHeure(rs.getDate("date_rdv"), rs.getString("heure_rdv"));
				
				/* Créer le RDV */
				RendezVous rdv = new RendezVous(
					rs.getInt("id"),
					patient,
					medecin,
					dateRdv,
					StatutRDV.valueOf(rs.getString("statut")),
					rs.getString("motif")
				);
				
				rdvList.add(rdv);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return rdvList;
	}
	
	/* ========== RÉCUPÉRATION DE TOUS LES RDV (ASSISTANTE) ========== */
	
	/* Récupère tous les RDV (pour l'assistante) */
	public List<RendezVous> getTousRDV() {
		List<RendezVous> rdvList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT r.id, r.date_rdv, r.heure_rdv, r.motif, r.statut, " +
			               "u_pat.nom as pat_nom, u_pat.prenom as pat_prenom, " +
			               "u_med.nom as med_nom, u_med.prenom as med_prenom, m.specialite " +
			               "FROM rendez_vous r " +
			               "JOIN utilisateurs u_pat ON r.patient_id = u_pat.id " +
			               "JOIN medecins m ON r.medecin_id = m.utilisateur_id " +
			               "JOIN utilisateurs u_med ON m.utilisateur_id = u_med.id " +
			               "ORDER BY r.date_rdv DESC, r.heure_rdv DESC";
			
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
			
			while (rs.next()) {
				/* Créer patient simplifié */
				Patient patient = new Patient(
					rs.getString("pat_nom"),
					rs.getString("pat_prenom"),
					"", "", "", "", "", "", null, ""
				);
				
				/* Créer médecin simplifié */
				Medecin medecin = new Medecin(
					rs.getString("med_nom"),
					rs.getString("med_prenom"),
					"", "",
					rs.getString("specialite")
				);
				
				/* Créer la date */
				Date dateRdv = convertirSQLDateEtHeure(rs.getDate("date_rdv"), rs.getString("heure_rdv"));
				
				/* Créer le RDV */
				RendezVous rdv = new RendezVous(
					rs.getInt("id"),
					patient,
					medecin,
					dateRdv,
					StatutRDV.valueOf(rs.getString("statut")),
					rs.getString("motif")
				);
				
				rdvList.add(rdv);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return rdvList;
	}
	
	/* ========== MODIFICATION DES STATUTS ========== */
	
	/* Confirme un rendez-vous */
	public boolean confirmerRDV(int rdvId) {
		/* 1. Récupérer le RDV depuis la BD */
		RendezVous rdv = getRDVById(rdvId);
		if (rdv == null) return false;
		
		/* 2. Utiliser la logique du model */
		rdv.confirmer();
		
		/* 3. Sauvegarder dans la BD */
		return updateStatut(rdvId, rdv.getStatut());
	}
	
	/* Annule un rendez-vous */
	public boolean annulerRDV(int rdvId) {
		RendezVous rdv = getRDVById(rdvId);
		if (rdv == null) return false;
		
		rdv.annuler();
		return updateStatut(rdvId, rdv.getStatut());
	}
	
	/* Termine un rendez-vous */
	public boolean terminerRDV(int rdvId) {
		RendezVous rdv = getRDVById(rdvId);
		if (rdv == null) return false;
		
		rdv.terminer();
		return updateStatut(rdvId, rdv.getStatut());
	}
	
	/* Méthode privée pour récupérer un RDV par ID */
	private RendezVous getRDVById(int rdvId) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT * FROM rendez_vous WHERE id = ?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, rdvId);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				/* Créer et retourner le RDV (simplifié pour changement de statut) */
				Date dateRdv = convertirSQLDateEtHeure(rs.getDate("date_rdv"), rs.getString("heure_rdv"));
				StatutRDV statut = StatutRDV.valueOf(rs.getString("statut"));
				
				return new RendezVous(rs.getInt("id"), null, null, dateRdv, statut,	rs.getString("motif"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return null;
	}
	
	/* Méthode privée pour mettre à jour le statut */
	private boolean updateStatut(int rdvId, StatutRDV nouveauStatut) {
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			conn = getConnection();
			
			String query = "UPDATE rendez_vous SET statut = ? WHERE id = ?";
			pst = conn.prepareStatement(query);
			pst.setString(1, nouveauStatut.name());
			pst.setInt(2, rdvId);
			
			int result = pst.executeUpdate();
			return result > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			fermerRessources(null, pst, conn);
		}
	}
	
	/* ========== MODIFICATION DE RDV ========== */
	
	/* Modifie un rendez-vous existant */
	public boolean modifierRDV(RendezVous rdv) {
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			conn = getConnection();
			
			String query = "UPDATE rendez_vous SET date_rdv = ?, heure_rdv = ?, motif = ? WHERE id = ?";
			pst = conn.prepareStatement(query);
			
			/* Convertir date */
			Date dateRdv = rdv.getDateRdv();
			java.util.Calendar cal = java.util.Calendar.getInstance();
			cal.set(dateRdv.getAnnee(), dateRdv.getMois() - 1, dateRdv.getJour());
			pst.setDate(1, new java.sql.Date(cal.getTimeInMillis()));
			
			/* Heure */
			String heureStr = String.format("%02d:%02d:00", dateRdv.getHeure(), dateRdv.getMinute());
			pst.setString(2, heureStr);
			
			pst.setString(3, rdv.getMotif());
			pst.setInt(4, rdv.getId());
			
			int result = pst.executeUpdate();
			return result > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			fermerRessources(null, pst, conn);
		}
	}
	
	/* ========== SUPPRESSION DE RDV ========== */
	
	/* Supprime un rendez-vous */
	public boolean supprimerRDV(int rdvId) {
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			conn = getConnection();
			
			String query = "DELETE FROM rendez_vous WHERE id = ?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, rdvId);
			
			int result = pst.executeUpdate();
			return result > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			fermerRessources(null, pst, conn);
		}
	}
	
	/* ========== MÉTHODES UTILITAIRES ========== */
	
	/* Convertit java.sql.Date en notre classe Date */
	private Date convertirSQLDate(java.sql.Date sqlDate) {
		if (sqlDate == null) {
			return null;
		}
		
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(sqlDate);
		
		return new Date(
			cal.get(java.util.Calendar.DAY_OF_MONTH),
			cal.get(java.util.Calendar.MONTH) + 1,
			cal.get(java.util.Calendar.YEAR)
		);
	}
	
	/* Convertit java.sql.Date + heure String en notre classe Date */
	private Date convertirSQLDateEtHeure(java.sql.Date sqlDate, String heureStr) {
		if (sqlDate == null) {
			return null;
		}
		
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(sqlDate);
		
		int heure = 0;
		int minute = 0;
		
		/* Parser l'heure au format HH:MM:SS ou HH:MM */
		if (heureStr != null && !heureStr.isEmpty()) {
			String[] parts = heureStr.split(":");
			if (parts.length >= 2) {
				heure = Integer.parseInt(parts[0]);
				minute = Integer.parseInt(parts[1]);
			}
		}
		
		return new Date(
			cal.get(java.util.Calendar.DAY_OF_MONTH),
			cal.get(java.util.Calendar.MONTH) + 1,
			cal.get(java.util.Calendar.YEAR),
			heure,
			minute
		);
	}
	
	/* Ferme proprement les ressources JDBC */
	private void fermerRessources(ResultSet rs, PreparedStatement pst, Connection conn) {
		try {
			if (rs != null) rs.close();
			if (pst != null) pst.close();
			if (conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}