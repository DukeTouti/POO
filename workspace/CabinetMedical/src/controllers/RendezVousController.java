package controllers;

import models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/* ******************************************************** *
 * Contrôleur de gestion des rendez-vous                   *
 * Gère les opérations CRUD sur les rendez-vous            *
 * ******************************************************** */

public class RendezVousController {
	
	/* Configuration de la base de données */
	private static final String URL = "jdbc:mysql://localhost:3306/projet_java_cabinet";
	private static final String USER_BD = "cabinet_user";
	private static final String MDP_BD = "cabinet123";
	
	/* ========== CONNEXION À LA BASE DE DONNÉES ========== */
	
	/* Établit une connexion à la base de données */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER_BD, MDP_BD);
	}
	
	/* ========== RÉCUPÉRATION D'UN RDV ========== */
	
	/* Récupère un RDV par son ID */
	public RendezVous getRDVById(int id) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT * FROM rendez_vous WHERE id = ?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				return construireRDVDepuisResultSet(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return null;
	}
	
	/* ========== RÉCUPÉRATION DE TOUS LES RDV ========== */
	
	/* Récupère tous les RDV */
	public List<RendezVous> getTousRDV() {
		List<RendezVous> rdvList = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT * FROM rendez_vous ORDER BY date_rdv, heure_rdv";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				RendezVous rdv = construireRDVDepuisResultSet(rs);
				if (rdv != null) {
					rdvList.add(rdv);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return rdvList;
	}
	
	/* ========== RÉCUPÉRATION PAR PATIENT ========== */
	
	/* Récupère tous les RDV d'un patient */
	public List<RendezVous> getRDVByPatient(Patient patient) {
		List<RendezVous> rdvList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT * FROM rendez_vous WHERE patient_id = ? ORDER BY date_rdv DESC, heure_rdv DESC";
			pst = conn.prepareStatement(query);
			pst.setInt(1, patient.getId());
			rs = pst.executeQuery();
			
			while (rs.next()) {
				RendezVous rdv = construireRDVDepuisResultSet(rs);
				if (rdv != null) {
					rdvList.add(rdv);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return rdvList;
	}
	
	/* ========== RÉCUPÉRATION PAR MÉDECIN ========== */
	
	/* Récupère tous les RDV d'un médecin */
	public List<RendezVous> getRDVByMedecin(Medecin medecin) {
		List<RendezVous> rdvList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT * FROM rendez_vous WHERE medecin_id = ? ORDER BY date_rdv, heure_rdv";
			pst = conn.prepareStatement(query);
			pst.setInt(1, medecin.getId());
			rs = pst.executeQuery();
			
			while (rs.next()) {
				RendezVous rdv = construireRDVDepuisResultSet(rs);
				if (rdv != null) {
					rdvList.add(rdv);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return rdvList;
	}
	
	/* Récupère les RDV d'un médecin pour une date donnée */
	public List<RendezVous> getRDVByMedecinEtDate(Medecin medecin, models.Date date) {
		List<RendezVous> rdvList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT * FROM rendez_vous WHERE medecin_id = ? AND date_rdv = ? ORDER BY heure_rdv";
			pst = conn.prepareStatement(query);
			pst.setInt(1, medecin.getId());
			
			// Convertir notre Date en java.sql.Date
			Calendar cal = Calendar.getInstance();
			cal.set(date.getAnnee(), date.getMois() - 1, date.getJour());
			pst.setDate(2, new java.sql.Date(cal.getTimeInMillis()));
			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				RendezVous rdv = construireRDVDepuisResultSet(rs);
				if (rdv != null) {
					rdvList.add(rdv);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return rdvList;
	}
	
	/* ========== CRÉATION D'UN RDV ========== */
	
	/* Crée un nouveau RDV */
	public boolean createRDV(RendezVous rdv) {
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			conn = getConnection();
			
			String query = "INSERT INTO rendez_vous (patient_id, medecin_id, date_rdv, heure_rdv, motif, statut) VALUES (?, ?, ?, ?, ?, ?)";
			pst = conn.prepareStatement(query);
			pst.setInt(1, rdv.getPatient().getId());
			pst.setInt(2, rdv.getMedecin().getId());
			
			// Convertir notre Date en java.sql.Date (juste la date, pas l'heure)
			models.Date dateRdv = rdv.getDateRdv();
			Calendar cal = Calendar.getInstance();
			cal.set(dateRdv.getAnnee(), dateRdv.getMois() - 1, dateRdv.getJour());
			pst.setDate(3, new java.sql.Date(cal.getTimeInMillis()));
			
			// ✅ CORRECTION : Convertir l'heure en java.sql.Time (pour le type TIME de MySQL)
			Calendar calHeure = Calendar.getInstance();
			calHeure.set(Calendar.HOUR_OF_DAY, dateRdv.getHeure());
			calHeure.set(Calendar.MINUTE, dateRdv.getMinute());
			calHeure.set(Calendar.SECOND, 0);
			pst.setTime(4, new java.sql.Time(calHeure.getTimeInMillis()));
			
			pst.setString(5, rdv.getMotif());
			pst.setString(6, rdv.getStatut().toString());
			
			int result = pst.executeUpdate();
			return result > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			fermerRessources(null, pst, conn);
		}
	}
	
	/* ========== MODIFICATION D'UN RDV ========== */
	
	/* Modifie un RDV existant */
	public boolean updateRDV(RendezVous rdv) {
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			conn = getConnection();
			
			String query = "UPDATE rendez_vous SET patient_id = ?, medecin_id = ?, date_rdv = ?, heure_rdv = ?, motif = ?, statut = ? WHERE id = ?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, rdv.getPatient().getId());
			pst.setInt(2, rdv.getMedecin().getId());
			
			// Convertir notre Date
			models.Date dateRdv = rdv.getDateRdv();
			Calendar cal = Calendar.getInstance();
			cal.set(dateRdv.getAnnee(), dateRdv.getMois() - 1, dateRdv.getJour());
			pst.setDate(3, new java.sql.Date(cal.getTimeInMillis()));
			
			// Convertir l'heure en TIME
			Calendar calHeure = Calendar.getInstance();
			calHeure.set(Calendar.HOUR_OF_DAY, dateRdv.getHeure());
			calHeure.set(Calendar.MINUTE, dateRdv.getMinute());
			calHeure.set(Calendar.SECOND, 0);
			pst.setTime(4, new java.sql.Time(calHeure.getTimeInMillis()));
			
			pst.setString(5, rdv.getMotif());
			pst.setString(6, rdv.getStatut().toString());
			pst.setInt(7, rdv.getId());
			
			int result = pst.executeUpdate();
			return result > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			fermerRessources(null, pst, conn);
		}
	}
	
	/* ========== ANNULATION D'UN RDV ========== */
	
	/* Annule un RDV (change son statut) */
	public boolean annulerRDV(int rdvId) {
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			conn = getConnection();
			
			String query = "UPDATE rendez_vous SET statut = 'ANNULE' WHERE id = ?";
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
	
	/* Confirme un RDV (change son statut de PLANIFIE à CONFIRME) */
	public boolean confirmerRDV(int rdvId) {
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			conn = getConnection();
			
			String query = "UPDATE rendez_vous SET statut = 'CONFIRME' WHERE id = ? AND statut = 'PLANIFIE'";
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
	
	/* Termine un RDV (change son statut à TERMINE) */
	public boolean terminerRDV(int rdvId) {
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			conn = getConnection();
			
			String query = "UPDATE rendez_vous SET statut = 'TERMINE' WHERE id = ?";
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
	
	/* ========== SUPPRESSION D'UN RDV ========== */
	
	/* Supprime un RDV (déconseillé, préférer l'annulation) */
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
	
	/* Construit un objet RendezVous depuis un ResultSet */
	private RendezVous construireRDVDepuisResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		int patientId = rs.getInt("patient_id");
		int medecinId = rs.getInt("medecin_id");
		String motif = rs.getString("motif");
		String statutStr = rs.getString("statut");
		
		// Récupérer la date
		java.sql.Date sqlDate = rs.getDate("date_rdv");
		java.sql.Time sqlTime = rs.getTime("heure_rdv");
		
		// Convertir en notre objet Date
		Calendar cal = Calendar.getInstance();
		cal.setTime(sqlDate);
		
		// ✅ CORRECTION : Extraire heure et minute depuis TIME
		int heure = 0;
		int minute = 0;
		if (sqlTime != null) {
			Calendar calTime = Calendar.getInstance();
			calTime.setTime(sqlTime);
			heure = calTime.get(Calendar.HOUR_OF_DAY);
			minute = calTime.get(Calendar.MINUTE);
		}
		
		models.Date dateRdv = new models.Date(
			cal.get(Calendar.DAY_OF_MONTH),
			cal.get(Calendar.MONTH) + 1,
			cal.get(Calendar.YEAR),
			heure,
			minute
		);
		
		// Récupérer le patient et le médecin via leurs controllers
		PatientController patientCtrl = new PatientController();
		MedecinController medecinCtrl = new MedecinController();
		
		Patient patient = patientCtrl.getPatientById(patientId);
		Medecin medecin = medecinCtrl.getMedecinById(medecinId);
		
		// Convertir le statut
		StatutRDV statut = StatutRDV.valueOf(statutStr);
		
		return new RendezVous(id, patient, medecin, dateRdv, statut, motif);
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