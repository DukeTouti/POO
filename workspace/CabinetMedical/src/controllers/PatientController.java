package controllers;

import models.*;
import models.Date;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* ******************************************************** *
 * Contrôleur de gestion des patients                       *
 * Gère les opérations CRUD sur les patients                *
 * ******************************************************** */

public class PatientController {
	
	/* Configuration de la base de données */
	private static final String URL = "jdbc:mysql://localhost:3306/projet_java_cabinet";
	private static final String USER_BD = "cabinet_user";
	private static final String MDP_BD = "cabinet123";
	
	/* ========== CONNEXION À LA BASE DE DONNÉES ========== */
	
	/* Établit une connexion à la base de données */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER_BD, MDP_BD);
	}
	
	/* ========== RÉCUPÉRATION D'UN PATIENT ========== */
	
	/* Récupère un patient par son ID */
	public Patient getPatientById(int id) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT u.id, u.nom, u.prenom, u.login, u.mdp, " +
			               "p.telephone, p.email, p.numero_secu, p.adresse, p.date_naissance, p.lieu_naissance " +
			               "FROM utilisateurs u " +
			               "JOIN patients p ON u.id = p.utilisateur_id " +
			               "WHERE u.id = ?";
			
			pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				/* Convertir la date de naissance */
				Date dateNaissance = convertirSQLDate(rs.getDate("date_naissance"));
				
				return new Patient(
					rs.getInt("id"),
					rs.getString("nom"),
					rs.getString("prenom"),
					rs.getString("login"),
					rs.getString("mdp"),
					rs.getString("telephone"),
					rs.getString("email"),
					rs.getString("numero_secu"),
					rs.getString("adresse"),
					dateNaissance,
					rs.getString("lieu_naissance")
				);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return null;
	}
	
	/* ========== RÉCUPÉRATION DE TOUS LES PATIENTS ========== */
	
	/* Récupère tous les patients (pour l'assistante) */
	public List<Patient> getTousPatients() {
		List<Patient> patients = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT u.id, u.nom, u.prenom, u.login, u.mdp, " +
			               "p.telephone, p.email, p.numero_secu, p.adresse, p.date_naissance, p.lieu_naissance " +
			               "FROM utilisateurs u " +
			               "JOIN patients p ON u.id = p.utilisateur_id " +
			               "WHERE u.role = 'PATIENT' " +
			               "ORDER BY u.nom, u.prenom";
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				/* Convertir la date de naissance */
				Date dateNaissance = convertirSQLDate(rs.getDate("date_naissance"));
				
				Patient patient = new Patient(
					rs.getInt("id"),
					rs.getString("nom"),
					rs.getString("prenom"),
					rs.getString("login"),
					rs.getString("mdp"),
					rs.getString("telephone"),
					rs.getString("email"),
					rs.getString("numero_secu"),
					rs.getString("adresse"),
					dateNaissance,
					rs.getString("lieu_naissance")
				);
				
				patients.add(patient);
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
		
		return patients;
	}
	
	/* ========== RECHERCHE DE PATIENTS ========== */
	
	/* Recherche un patient par nom */
	public List<Patient> rechercherParNom(String nom) {
		List<Patient> patients = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT u.id, u.nom, u.prenom, u.login, u.mdp, " +
			               "p.telephone, p.email, p.numero_secu, p.adresse, p.date_naissance, p.lieu_naissance " +
			               "FROM utilisateurs u " +
			               "JOIN patients p ON u.id = p.utilisateur_id " +
			               "WHERE u.role = 'PATIENT' AND u.nom LIKE ? " +
			               "ORDER BY u.nom, u.prenom";
			
			pst = conn.prepareStatement(query);
			pst.setString(1, "%" + nom + "%");
			rs = pst.executeQuery();
			
			while (rs.next()) {
				Date dateNaissance = convertirSQLDate(rs.getDate("date_naissance"));
				
				Patient patient = new Patient(
					rs.getInt("id"),
					rs.getString("nom"),
					rs.getString("prenom"),
					rs.getString("login"),
					rs.getString("mdp"),
					rs.getString("telephone"),
					rs.getString("email"),
					rs.getString("numero_secu"),
					rs.getString("adresse"),
					dateNaissance,
					rs.getString("lieu_naissance")
				);
				
				patients.add(patient);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return patients;
	}
	
	/* Recherche un patient par numéro de sécurité sociale */
	public Patient rechercherParNumeroSecu(String numeroSecu) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT u.id, u.nom, u.prenom, u.login, u.mdp, " +
			               "p.telephone, p.email, p.numero_secu, p.adresse, p.date_naissance, p.lieu_naissance " +
			               "FROM utilisateurs u " +
			               "JOIN patients p ON u.id = p.utilisateur_id " +
			               "WHERE p.numero_secu = ?";
			
			pst = conn.prepareStatement(query);
			pst.setString(1, numeroSecu);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				Date dateNaissance = convertirSQLDate(rs.getDate("date_naissance"));
				
				return new Patient(
					rs.getInt("id"),
					rs.getString("nom"),
					rs.getString("prenom"),
					rs.getString("login"),
					rs.getString("mdp"),
					rs.getString("telephone"),
					rs.getString("email"),
					rs.getString("numero_secu"),
					rs.getString("adresse"),
					dateNaissance,
					rs.getString("lieu_naissance")
				);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return null;
	}
	
	/* Recherche un patient par téléphone */
	public Patient rechercherParTelephone(String telephone) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT u.id, u.nom, u.prenom, u.login, u.mdp, " +
			               "p.telephone, p.email, p.numero_secu, p.adresse, p.date_naissance, p.lieu_naissance " +
			               "FROM utilisateurs u " +
			               "JOIN patients p ON u.id = p.utilisateur_id " +
			               "WHERE p.telephone = ?";
			
			pst = conn.prepareStatement(query);
			pst.setString(1, telephone);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				Date dateNaissance = convertirSQLDate(rs.getDate("date_naissance"));
				
				return new Patient(
					rs.getInt("id"),
					rs.getString("nom"),
					rs.getString("prenom"),
					rs.getString("login"),
					rs.getString("mdp"),
					rs.getString("telephone"),
					rs.getString("email"),
					rs.getString("numero_secu"),
					rs.getString("adresse"),
					dateNaissance,
					rs.getString("lieu_naissance")
				);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return null;
	}
	
	/* ========== CRÉATION D'UN PATIENT ========== */
	
	/* Crée un nouveau patient (utilisé par l'assistante) */
	public boolean createPatient(Patient patient) {
		Connection conn = null;
		PreparedStatement pstUser = null;
		PreparedStatement pstPatient = null;
		ResultSet generatedKeys = null;
		
		try {
			conn = getConnection();
			conn.setAutoCommit(false);	/* Transaction */
			
			/* 1. Insérer dans la table utilisateurs */
			String queryUser = "INSERT INTO utilisateurs (nom, prenom, login, mdp, role) VALUES (?, ?, ?, ?, 'PATIENT')";
			pstUser = conn.prepareStatement(queryUser, Statement.RETURN_GENERATED_KEYS);
			pstUser.setString(1, patient.getNom());
			pstUser.setString(2, patient.getPrenom());
			pstUser.setString(3, patient.getLogin());
			pstUser.setString(4, patient.getMdp());
			
			int resultUser = pstUser.executeUpdate();
			
			if (resultUser > 0) {
				/* Récupérer l'ID généré */
				generatedKeys = pstUser.getGeneratedKeys();
				if (generatedKeys.next()) {
					int utilisateurId = generatedKeys.getInt(1);
					
					/* 2. Insérer dans la table patients */
					String queryPatient = "INSERT INTO patients (utilisateur_id, telephone, email, numero_secu, adresse, date_naissance, lieu_naissance) " +
					                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
					pstPatient = conn.prepareStatement(queryPatient);
					pstPatient.setInt(1, utilisateurId);
					pstPatient.setString(2, patient.getTelephone());
					pstPatient.setString(3, patient.getEmail());
					pstPatient.setString(4, patient.getNumeroSecu());
					pstPatient.setString(5, patient.getAdresse());
					
					/* Convertir la date de naissance */
					Date dateNaissance = patient.getDateNaissance();
					if (dateNaissance != null) {
						java.util.Calendar cal = java.util.Calendar.getInstance();
						cal.set(dateNaissance.getAnnee(), dateNaissance.getMois() - 1, dateNaissance.getJour());
						pstPatient.setDate(6, new java.sql.Date(cal.getTimeInMillis()));
					} else {
						pstPatient.setNull(6, Types.DATE);
					}
					
					pstPatient.setString(7, patient.getLieuNaissance());
					
					int resultPatient = pstPatient.executeUpdate();
					
					if (resultPatient > 0) {
						conn.commit();
						return true;
					}
				}
			}
			
			conn.rollback();
			return false;
			
		} catch (SQLException e) {
			try {
				if (conn != null) conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (generatedKeys != null) generatedKeys.close();
				if (pstUser != null) pstUser.close();
				if (pstPatient != null) pstPatient.close();
				if (conn != null) {
					conn.setAutoCommit(true);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/* ========== MODIFICATION D'UN PATIENT ========== */
	
	/* Modifie les informations d'un patient */
	public boolean updatePatient(Patient patient) {
		Connection conn = null;
		PreparedStatement pstUser = null;
		PreparedStatement pstPatient = null;
		
		try {
			conn = getConnection();
			conn.setAutoCommit(false);	/* Transaction */
			
			/* 1. Mettre à jour la table utilisateurs */
			String queryUser = "UPDATE utilisateurs SET nom = ?, prenom = ?, login = ?, mdp = ? WHERE id = ?";
			pstUser = conn.prepareStatement(queryUser);
			pstUser.setString(1, patient.getNom());
			pstUser.setString(2, patient.getPrenom());
			pstUser.setString(3, patient.getLogin());
			pstUser.setString(4, patient.getMdp());
			pstUser.setInt(5, patient.getId());
			
			int resultUser = pstUser.executeUpdate();
			
			/* 2. Mettre à jour la table patients */
			String queryPatient = "UPDATE patients SET telephone = ?, email = ?, numero_secu = ?, adresse = ?, date_naissance = ?, lieu_naissance = ? " +
			                      "WHERE utilisateur_id = ?";
			pstPatient = conn.prepareStatement(queryPatient);
			pstPatient.setString(1, patient.getTelephone());
			pstPatient.setString(2, patient.getEmail());
			pstPatient.setString(3, patient.getNumeroSecu());
			pstPatient.setString(4, patient.getAdresse());
			
			/* Convertir la date de naissance */
			Date dateNaissance = patient.getDateNaissance();
			if (dateNaissance != null) {
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(dateNaissance.getAnnee(), dateNaissance.getMois() - 1, dateNaissance.getJour());
				pstPatient.setDate(5, new java.sql.Date(cal.getTimeInMillis()));
			} else {
				pstPatient.setNull(5, Types.DATE);
			}
			
			pstPatient.setString(6, patient.getLieuNaissance());
			pstPatient.setInt(7, patient.getId());
			
			int resultPatient = pstPatient.executeUpdate();
			
			if (resultUser > 0 && resultPatient > 0) {
				conn.commit();
				return true;
			}
			
			conn.rollback();
			return false;
			
		} catch (SQLException e) {
			try {
				if (conn != null) conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pstUser != null) pstUser.close();
				if (pstPatient != null) pstPatient.close();
				if (conn != null) {
					conn.setAutoCommit(true);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/* ========== SUPPRESSION D'UN PATIENT ========== */
	
	/* Supprime un patient (soft delete recommandé en production) */
	public boolean supprimerPatient(int patientId) {
		Connection conn = null;
		PreparedStatement pstPatient = null;
		PreparedStatement pstUser = null;
		
		try {
			conn = getConnection();
			conn.setAutoCommit(false);	/* Transaction */
			
			/* 1. Supprimer de la table patients */
			String queryPatient = "DELETE FROM patients WHERE utilisateur_id = ?";
			pstPatient = conn.prepareStatement(queryPatient);
			pstPatient.setInt(1, patientId);
			pstPatient.executeUpdate();
			
			/* 2. Supprimer de la table utilisateurs */
			String queryUser = "DELETE FROM utilisateurs WHERE id = ?";
			pstUser = conn.prepareStatement(queryUser);
			pstUser.setInt(1, patientId);
			int result = pstUser.executeUpdate();
			
			if (result > 0) {
				conn.commit();
				return true;
			}
			
			conn.rollback();
			return false;
			
		} catch (SQLException e) {
			try {
				if (conn != null) conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pstPatient != null) pstPatient.close();
				if (pstUser != null) pstUser.close();
				if (conn != null) {
					conn.setAutoCommit(true);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
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