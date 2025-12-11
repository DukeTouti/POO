package controllers;

import models.Assistante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* ******************************************************** *
 * Contrôleur de gestion des assistantes                    *
 * Gère les opérations CRUD sur les assistantes             *
 * ******************************************************** */

public class AssistanteController {

	/* Configuration de la base de données */
	private static final String URL = "jdbc:mysql://localhost:3306/projet_java_cabinet";
	private static final String USER_BD = "cabinet_user";
	private static final String MDP_BD = "cabinet123";
	/* ========== CONNEXION À LA BASE DE DONNÉES ========== */

	/* Établit une connexion à la base de données */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER_BD, MDP_BD);
	}

	/* ========== RÉCUPÉRATION D'UNE ASSISTANTE ========== */

	/* Récupère une assistante par son ID */
	public Assistante getAssistanteById(int id) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			String query = "SELECT u.id, u.nom, u.prenom, u.login, u.mdp " + "FROM utilisateurs u "
					+ "JOIN assistantes a ON u.id = a.utilisateur_id " + "WHERE u.id = ?";

			pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			rs = pst.executeQuery();

			if (rs.next()) {
				return new Assistante(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"),
						rs.getString("login"), rs.getString("mdp"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}

		return null;
	}

	/* ========== RÉCUPÉRATION DE TOUTES LES ASSISTANTES ========== */

	/* Récupère toutes les assistantes */
	public List<Assistante> getToutesAssistantes() {
		List<Assistante> assistantes = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			String query = "SELECT u.id, u.nom, u.prenom, u.login, u.mdp " + "FROM utilisateurs u "
					+ "JOIN assistantes a ON u.id = a.utilisateur_id " + "WHERE u.role = 'ASSISTANTE' "
					+ "ORDER BY u.nom, u.prenom";

			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);

			while (rs.next()) {
				Assistante assistante = new Assistante(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"),
						rs.getString("login"), rs.getString("mdp"));

				assistantes.add(assistante);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return assistantes;
	}

	/* ========== CRÉATION D'UNE ASSISTANTE ========== */

	/* Crée une nouvelle assistante */
	public boolean createAssistante(Assistante assistante) {
		Connection conn = null;
		PreparedStatement pstUser = null;
		PreparedStatement pstAssistante = null;
		ResultSet generatedKeys = null;

		try {
			conn = getConnection();
			conn.setAutoCommit(false); /* Transaction */

			/* 1. Insérer dans la table utilisateurs */
			String queryUser = "INSERT INTO utilisateurs (nom, prenom, login, mdp, role) VALUES (?, ?, ?, ?, 'ASSISTANTE')";
			pstUser = conn.prepareStatement(queryUser, Statement.RETURN_GENERATED_KEYS);
			pstUser.setString(1, assistante.getNom());
			pstUser.setString(2, assistante.getPrenom());
			pstUser.setString(3, assistante.getLogin());
			pstUser.setString(4, assistante.getMdp());

			int resultUser = pstUser.executeUpdate();

			if (resultUser > 0) {
				/* Récupérer l'ID généré */
				generatedKeys = pstUser.getGeneratedKeys();
				if (generatedKeys.next()) {
					int utilisateurId = generatedKeys.getInt(1);

					/* 2. Insérer dans la table assistantes */
					String queryAssistante = "INSERT INTO assistantes (utilisateur_id) VALUES (?)";
					pstAssistante = conn.prepareStatement(queryAssistante);
					pstAssistante.setInt(1, utilisateurId);

					int resultAssistante = pstAssistante.executeUpdate();

					if (resultAssistante > 0) {
						conn.commit();
						return true;
					}
				}
			}

			conn.rollback();
			return false;

		} catch (SQLException e) {
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (generatedKeys != null)
					generatedKeys.close();
				if (pstUser != null)
					pstUser.close();
				if (pstAssistante != null)
					pstAssistante.close();
				if (conn != null) {
					conn.setAutoCommit(true);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/* ========== MODIFICATION D'UNE ASSISTANTE ========== */

	/* Modifie les informations d'une assistante */
	public boolean updateAssistante(Assistante assistante) {
		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = getConnection();

			/* Mettre à jour la table utilisateurs */
			String query = "UPDATE utilisateurs SET nom = ?, prenom = ?, login = ?, mdp = ? WHERE id = ?";
			pst = conn.prepareStatement(query);
			pst.setString(1, assistante.getNom());
			pst.setString(2, assistante.getPrenom());
			pst.setString(3, assistante.getLogin());
			pst.setString(4, assistante.getMdp());
			pst.setInt(5, assistante.getId());

			int result = pst.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			fermerRessources(null, pst, conn);
		}
	}

	/* ========== SUPPRESSION D'UNE ASSISTANTE ========== */

	/* Supprime une assistante */
	public boolean supprimerAssistante(int assistanteId) {
		Connection conn = null;
		PreparedStatement pstAssistante = null;
		PreparedStatement pstUser = null;

		try {
			conn = getConnection();
			conn.setAutoCommit(false); /* Transaction */

			/* 1. Supprimer de la table assistantes */
			String queryAssistante = "DELETE FROM assistantes WHERE utilisateur_id = ?";
			pstAssistante = conn.prepareStatement(queryAssistante);
			pstAssistante.setInt(1, assistanteId);
			pstAssistante.executeUpdate();

			/* 2. Supprimer de la table utilisateurs */
			String queryUser = "DELETE FROM utilisateurs WHERE id = ?";
			pstUser = conn.prepareStatement(queryUser);
			pstUser.setInt(1, assistanteId);
			int result = pstUser.executeUpdate();

			if (result > 0) {
				conn.commit();
				return true;
			}

			conn.rollback();
			return false;

		} catch (SQLException e) {
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pstAssistante != null)
					pstAssistante.close();
				if (pstUser != null)
					pstUser.close();
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
			if (rs != null)
				rs.close();
			if (pst != null)
				pst.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}