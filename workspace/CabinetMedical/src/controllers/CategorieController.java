package controllers;

import models.Categorie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* ******************************************************** *
 * Contrôleur de gestion des catégories                     *
 * Gère les opérations CRUD sur les catégories              *
 * ******************************************************** */

public class CategorieController {
	
	/* Configuration de la base de données */
	private static final String URL = "jdbc:mysql://localhost:3306/projet_java_cabinet";
	private static final String USER_BD = "cabinet_user";
	private static final String MDP_BD = "cabinet123";
	
	/* ========== CONNEXION À LA BASE DE DONNÉES ========== */
	
	/* Établit une connexion à la base de données */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER_BD, MDP_BD);
	}
	
	/* ========== RÉCUPÉRATION D'UNE CATÉGORIE ========== */
	
	/* Récupère une catégorie par son ID */
	public Categorie getCategorieById(int id) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT * FROM categories WHERE id = ?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				return new Categorie(
					rs.getInt("id"),
					rs.getString("designation"),
					rs.getString("description")
				);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}
		
		return null;
	}
	
	/* ========== RÉCUPÉRATION DE TOUTES LES CATÉGORIES ========== */
	
	/* Récupère toutes les catégories */
	public List<Categorie> getToutesCategories() {
		List<Categorie> categories = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String query = "SELECT * FROM categories ORDER BY designation";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				Categorie categorie = new Categorie(
					rs.getInt("id"),
					rs.getString("designation"),
					rs.getString("description")
				);
				
				categories.add(categorie);
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
		
		return categories;
	}
	
	/* ========== CRÉATION D'UNE CATÉGORIE ========== */
	
	/* Crée une nouvelle catégorie */
	public boolean createCategorie(Categorie categorie) {
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			conn = getConnection();
			
			String query = "INSERT INTO categories (designation, description) VALUES (?, ?)";
			pst = conn.prepareStatement(query);
			pst.setString(1, categorie.getDesignation());
			pst.setString(2, categorie.getDescription());
			
			int result = pst.executeUpdate();
			return result > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			fermerRessources(null, pst, conn);
		}
	}
	
	/* ========== MODIFICATION D'UNE CATÉGORIE ========== */
	
	/* Modifie une catégorie existante */
	public boolean updateCategorie(Categorie categorie) {
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			conn = getConnection();
			
			String query = "UPDATE categories SET designation = ?, description = ? WHERE id = ?";
			pst = conn.prepareStatement(query);
			pst.setString(1, categorie.getDesignation());
			pst.setString(2, categorie.getDescription());
			pst.setInt(3, categorie.getId());
			
			int result = pst.executeUpdate();
			return result > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			fermerRessources(null, pst, conn);
		}
	}
	
	/* ========== SUPPRESSION D'UNE CATÉGORIE ========== */
	
	/* Supprime une catégorie */
	public boolean supprimerCategorie(int categorieId) {
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			conn = getConnection();
			
			String query = "DELETE FROM categories WHERE id = ?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, categorieId);
			
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