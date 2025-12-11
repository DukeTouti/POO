package controllers;

import models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* ******************************************************** *
 * Contrôleur de gestion des médecins                       *
 * Gère les opérations CRUD sur les médecins                *
 * ******************************************************** */

public class MedecinController {
	
	/* Configuration de la base de données */
	private static final String URL = "jdbc:mysql://localhost:3306/projet_java_cabinet";
	private static final String USER_BD = "cabinet_user";
	private static final String MDP_BD = "cabinet123";
	
	/* ========== CONNEXION À LA BASE DE DONNÉES ========== */
	
	/* Établit une connexion à la base de données */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER_BD, MDP_BD);
	}
	
	/* ========== RÉCUPÉRATION D'UN MÉDECIN ========== */
	
	/* Récupère un médecin par son ID */
	public Medecin getMedecinById(int id) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT u.id, u.nom, u.prenom, u.login, u.mdp, m.specialite " +
			               "FROM utilisateurs u " +
			               "JOIN medecins m ON u.id = m.utilisateur_id " +
			               "WHERE u.id = ?";
			
			pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				return new Medecin(
					rs.getInt("id"),
					rs.getString("nom"),
					rs.getString("prenom"),
					rs.getString("login"),
					rs.getString("mdp"),
					rs.getString("specialite")
				);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return null;
	}
	
	/* ========== RÉCUPÉRATION DE TOUS LES MÉDECINS ========== */
	
	/* Récupère tous les médecins */
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
			               "WHERE u.role = 'MEDECIN' " +
			               "ORDER BY u.nom, u.prenom";
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				Medecin medecin = new Medecin(
					rs.getInt("id"),
					rs.getString("nom"),
					rs.getString("prenom"),
					rs.getString("login"),
					rs.getString("mdp"),
					rs.getString("specialite")
				);
				
				medecins.add(medecin);
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
		
		return medecins;
	}
	
	/* ========== RECHERCHE DE MÉDECINS ========== */
	
	/* Recherche un médecin par nom */
	public List<Medecin> rechercherParNom(String nom) {
		List<Medecin> medecins = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT u.id, u.nom, u.prenom, u.login, u.mdp, m.specialite " +
			               "FROM utilisateurs u " +
			               "JOIN medecins m ON u.id = m.utilisateur_id " +
			               "WHERE u.role = 'MEDECIN' AND u.nom LIKE ? " +
			               "ORDER BY u.nom, u.prenom";
			
			pst = conn.prepareStatement(query);
			pst.setString(1, "%" + nom + "%");
			rs = pst.executeQuery();
			
			while (rs.next()) {
				Medecin medecin = new Medecin(
					rs.getInt("id"),
					rs.getString("nom"),
					rs.getString("prenom"),
					rs.getString("login"),
					rs.getString("mdp"),
					rs.getString("specialite")
				);
				
				medecins.add(medecin);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return medecins;
	}
	
	/* Recherche des médecins par spécialité */
	public List<Medecin> rechercherParSpecialite(String specialite) {
		List<Medecin> medecins = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT u.id, u.nom, u.prenom, u.login, u.mdp, m.specialite " +
			               "FROM utilisateurs u " +
			               "JOIN medecins m ON u.id = m.utilisateur_id " +
			               "WHERE m.specialite LIKE ? " +
			               "ORDER BY u.nom, u.prenom";
			
			pst = conn.prepareStatement(query);
			pst.setString(1, "%" + specialite + "%");
			rs = pst.executeQuery();
			
			while (rs.next()) {
				Medecin medecin = new Medecin(
					rs.getInt("id"),
					rs.getString("nom"),
					rs.getString("prenom"),
					rs.getString("login"),
					rs.getString("mdp"),
					rs.getString("specialite")
				);
				
				medecins.add(medecin);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return medecins;
	}
	
	/* ========== CRÉATION D'UN MÉDECIN ========== */
	
	/* Crée un nouveau médecin */
	public boolean createMedecin(Medecin medecin) {
		Connection conn = null;
		PreparedStatement pstUser = null;
		PreparedStatement pstMedecin = null;
		ResultSet generatedKeys = null;
		
		try {
			conn = getConnection();
			conn.setAutoCommit(false);	/* Transaction */
			
			/* 1. Insérer dans la table utilisateurs */
			String queryUser = "INSERT INTO utilisateurs (nom, prenom, login, mdp, role) VALUES (?, ?, ?, ?, 'MEDECIN')";
			pstUser = conn.prepareStatement(queryUser, Statement.RETURN_GENERATED_KEYS);
			pstUser.setString(1, medecin.getNom());
			pstUser.setString(2, medecin.getPrenom());
			pstUser.setString(3, medecin.getLogin());
			pstUser.setString(4, medecin.getMdp());
			
			int resultUser = pstUser.executeUpdate();
			
			if (resultUser > 0) {
				/* Récupérer l'ID généré */
				generatedKeys = pstUser.getGeneratedKeys();
				if (generatedKeys.next()) {
					int utilisateurId = generatedKeys.getInt(1);
					
					/* 2. Insérer dans la table medecins */
					String queryMedecin = "INSERT INTO medecins (utilisateur_id, specialite) VALUES (?, ?)";
					pstMedecin = conn.prepareStatement(queryMedecin);
					pstMedecin.setInt(1, utilisateurId);
					pstMedecin.setString(2, medecin.getSpecialite());
					
					int resultMedecin = pstMedecin.executeUpdate();
					
					if (resultMedecin > 0) {
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
				if (pstMedecin != null) pstMedecin.close();
				if (conn != null) {
					conn.setAutoCommit(true);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/* ========== MODIFICATION D'UN MÉDECIN ========== */
	
	/* Modifie les informations d'un médecin */
	public boolean updateMedecin(Medecin medecin) {
		Connection conn = null;
		PreparedStatement pstUser = null;
		PreparedStatement pstMedecin = null;
		
		try {
			conn = getConnection();
			conn.setAutoCommit(false);	/* Transaction */
			
			/* 1. Mettre à jour la table utilisateurs */
			String queryUser = "UPDATE utilisateurs SET nom = ?, prenom = ?, login = ?, mdp = ? WHERE id = ?";
			pstUser = conn.prepareStatement(queryUser);
			pstUser.setString(1, medecin.getNom());
			pstUser.setString(2, medecin.getPrenom());
			pstUser.setString(3, medecin.getLogin());
			pstUser.setString(4, medecin.getMdp());
			pstUser.setInt(5, medecin.getId());
			
			int resultUser = pstUser.executeUpdate();
			
			/* 2. Mettre à jour la table medecins */
			String queryMedecin = "UPDATE medecins SET specialite = ? WHERE utilisateur_id = ?";
			pstMedecin = conn.prepareStatement(queryMedecin);
			pstMedecin.setString(1, medecin.getSpecialite());
			pstMedecin.setInt(2, medecin.getId());
			
			int resultMedecin = pstMedecin.executeUpdate();
			
			if (resultUser > 0 && resultMedecin > 0) {
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
				if (pstMedecin != null) pstMedecin.close();
				if (conn != null) {
					conn.setAutoCommit(true);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/* ========== SUPPRESSION D'UN MÉDECIN ========== */
	
	/* Supprime un médecin */
	public boolean supprimerMedecin(int medecinId) {
		Connection conn = null;
		PreparedStatement pstMedecin = null;
		PreparedStatement pstUser = null;
		
		try {
			conn = getConnection();
			conn.setAutoCommit(false);	/* Transaction */
			
			/* 1. Supprimer de la table medecins */
			String queryMedecin = "DELETE FROM medecins WHERE utilisateur_id = ?";
			pstMedecin = conn.prepareStatement(queryMedecin);
			pstMedecin.setInt(1, medecinId);
			pstMedecin.executeUpdate();
			
			/* 2. Supprimer de la table utilisateurs */
			String queryUser = "DELETE FROM utilisateurs WHERE id = ?";
			pstUser = conn.prepareStatement(queryUser);
			pstUser.setInt(1, medecinId);
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
				if (pstMedecin != null) pstMedecin.close();
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