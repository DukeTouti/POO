package controllers;

import models.*;
import models.Date;

import java.sql.*;

/* ******************************************************** *
 * Contrôleur d'authentification                            *
 * Gère la connexion des utilisateurs via MySQL            *
 * ******************************************************** */

public class AuthController {
	
	/* Configuration de la base de données */
	private static final String URL = "jdbc:mysql://localhost:3306/projet_java";
	private static final String USER_BD = "root";
	private static final String MDP_BD = "";
	
	/* ========== CONNEXION À LA BASE DE DONNÉES ========== */
	
	/* Établit une connexion à la base de données */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER_BD, MDP_BD);
	}
	
	/* ========== AUTHENTIFICATION ========== */
	
	/* ********************************************************************** *
	 * Authentifie un utilisateur avec login et mot de passe                  *
	 * Retourne l'objet Utilisateur complet (Medecin/Patient/Assistante)     *
	 * Retourne null si les identifiants sont incorrects                      *
	 * ********************************************************************** */
	public Utilisateur authentifier(String login, String mdp) {
		/* Vérifier que les champs ne sont pas vides */
		if (login == null || mdp == null || login.trim().isEmpty() || mdp.trim().isEmpty()) {
			return null;
		}
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			/* Se connecter à la BD */
			conn = getConnection();
			
			/* Requête pour vérifier login/mdp et récupérer le role */
			String query = "SELECT * FROM utilisateurs WHERE login = ? AND mdp = ?";
			pst = conn.prepareStatement(query);
			pst.setString(1, login);
			pst.setString(2, mdp);
			
			rs = pst.executeQuery();
			
			/* Si utilisateur trouvé */
			if (rs.next()) {
				int id = rs.getInt("id");
				String nom = rs.getString("nom");
				String prenom = rs.getString("prenom");
				String role = rs.getString("role");
				
				/* Charger l'utilisateur complet selon son rôle */
				Utilisateur utilisateur = null;
				
				switch (role) {
					case "MEDECIN":
						utilisateur = chargerMedecin(id, nom, prenom, login, mdp);
						break;
						
					case "PATIENT":
						utilisateur = chargerPatient(id, nom, prenom, login, mdp);
						break;
						
					case "ASSISTANTE":
						utilisateur = chargerAssistante(id, nom, prenom, login, mdp);
						break;
				}
				
				return utilisateur;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			/* Fermer les ressources */
			fermerRessources(rs, pst, conn);
		}
		
		return null;	/* Identifiants incorrects */
	}
	
	/* ========== CHARGEMENT DES UTILISATEURS SPÉCIFIQUES ========== */
	
	/* Charge un médecin depuis la BD */
	private Medecin chargerMedecin(int id, String nom, String prenom, String login, String mdp) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			/* Récupérer les infos spécifiques du médecin */
			String query = "SELECT * FROM medecins WHERE utilisateur_id = ?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				String specialite = rs.getString("specialite");
				return new Medecin(id, nom, prenom, login, mdp, specialite);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return null;
	}
	
	/* Charge un patient depuis la BD */
	private Patient chargerPatient(int id, String nom, String prenom, String login, String mdp) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			/* Récupérer les infos spécifiques du patient */
			String query = "SELECT * FROM patients WHERE utilisateur_id = ?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				String telephone = rs.getString("telephone");
				String email = rs.getString("email");
				String numeroSecu = rs.getString("numero_secu");
				String adresse = rs.getString("adresse");
				String lieuNaissance = rs.getString("lieu_naissance");
				
				/* Récupérer la date de naissance */
				java.sql.Date sqlDate = rs.getDate("date_naissance");
				Date dateNaissance = null;
				if (sqlDate != null) {
					/* Convertir java.sql.Date en notre classe Date */
					java.util.Calendar cal = java.util.Calendar.getInstance();
					cal.setTime(sqlDate);
					dateNaissance = new Date(
						cal.get(java.util.Calendar.DAY_OF_MONTH),
						cal.get(java.util.Calendar.MONTH) + 1,	/* Mois commence à 0 en Calendar */
						cal.get(java.util.Calendar.YEAR)
					);
				}
				
				return new Patient(id, nom, prenom, login, mdp, 
				                   telephone, email, numeroSecu, adresse, 
				                   dateNaissance, lieuNaissance);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return null;
	}
	
	/* Charge une assistante depuis la BD */
	private Assistante chargerAssistante(int id, String nom, String prenom, String login, String mdp) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			/* Vérifier que l'assistante existe dans la table */
			String query = "SELECT * FROM assistantes WHERE utilisateur_id = ?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				/* Assistante n'a pas d'attributs supplémentaires */
				return new Assistante(id, nom, prenom, login, mdp);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return null;
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
	
	/* Vérifie si la connexion à la BD fonctionne */
	public boolean testerConnexion() {
		try {
			Connection conn = getConnection();
			boolean isValid = conn.isValid(2);
			conn.close();
			return isValid;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}