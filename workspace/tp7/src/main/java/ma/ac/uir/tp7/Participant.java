/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.ac.uir.tp7;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/*****************
 * @author Najib *
 *****************/
public class Participant {

	private String cne;
	private String nom;
	private String prenom;
	private String profession;
	private String civilite;
	private String email;
	private int age;

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Participant() {
	}

	public String getCne() {
		return cne;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public String getProfession() {
		return profession;
	}

	public String getCivilite() {
		return civilite;
	}

	public String getEmail() {
		return email;
	}

	public int getAge() {
		return age;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setCne(String cne) {
		this.cne = cne;
	}

	public Participant(String cne, String nom, String prenom, String profession, String civilite, String email,
			int age) {
		this.cne = cne;
		this.nom = nom;
		this.prenom = prenom;
		this.profession = profession;
		this.civilite = civilite;
		this.email = email;
		this.age = age;
	}

	public Participant(int id, String cne, String nom, String prenom, String profession, String civilite, String email,
			int age) {
		this.id = id;
		this.cne = cne;
		this.nom = nom;
		this.prenom = prenom;
		this.profession = profession;
		this.civilite = civilite;
		this.email = email;
		this.age = age;
	}

	@Override
	public String toString() {
		return "Participant{" + "cne=" + cne + ", nom=" + nom + ", prenom=" + prenom + ", profession=" + profession
				+ ", civilite=" + civilite + ", email=" + email + ", age=" + age + '}';
	}

	// Méthodes JDBC
	// Ajouter
	public boolean addParticipant() {
		boolean res = false;
		java.sql.Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = Connection.connect();
			String sql = "INSERT INTO participants (cne, nom, prenom, profession, civilite, email, age) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, this.cne);
			pstmt.setString(2, this.nom);
			pstmt.setString(3, this.prenom);
			pstmt.setString(4, this.profession);
			pstmt.setString(5, this.civilite);
			pstmt.setString(6, this.email);
			pstmt.setInt(7, this.age);

			int rowsAffected = pstmt.executeUpdate();
			res = (rowsAffected > 0);

			if (res) {
				System.out.println("Participant ajouté avec succès !");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("Erreur lors de l'ajout du participant : " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	// récupérer la liste des particpants
	public static LinkedList<Participant> getAllParticipants() {

		LinkedList<Participant> listeP = new LinkedList<>();
		java.sql.Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = Connection.connect();
			stmt = conn.createStatement();
			String sql = "SELECT * FROM participants ORDER BY id";
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Participant p = new Participant(rs.getInt("id"), rs.getString("cne"), rs.getString("nom"),
						rs.getString("prenom"), rs.getString("profession"), rs.getString("civilite"),
						rs.getString("email"), rs.getInt("age"));
				listeP.add(p);
			}

			System.out.println("Nombre de participants récupérés : " + listeP.size());

		} catch (SQLException e) {
			System.err.println("Erreur lors de la récupération des participants : " + e.getMessage());
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

		return listeP;
	}

	// Supprimer un etudiant
	public static boolean supprimerParticipant(int id) {
		boolean res = false;
		java.sql.Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = Connection.connect();
			String sql = "DELETE FROM participants WHERE id = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);

			int rowsAffected = pstmt.executeUpdate();
			res = (rowsAffected > 0);

			if (res) {
				System.out.println("Participant supprimé avec succès (ID: " + id + ")");
			} else {
				System.out.println("Aucun participant trouvé avec l'ID: " + id);
			}

		} catch (SQLException e) {
			System.err.println("Erreur lors de la suppression du participant : " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return res;
	}

	// MAJ d'un particpant
	public boolean updateParticpant() {
		boolean res = false;
		java.sql.Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = Connection.connect();
			String sql = "UPDATE participants SET cne=?, nom=?, prenom=?, profession=?, "
					+ "civilite=?, email=?, age=? WHERE id=?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, this.cne);
			pstmt.setString(2, this.nom);
			pstmt.setString(3, this.prenom);
			pstmt.setString(4, this.profession);
			pstmt.setString(5, this.civilite);
			pstmt.setString(6, this.email);
			pstmt.setInt(7, this.age);
			pstmt.setInt(8, this.id);

			int rowsAffected = pstmt.executeUpdate();
			res = (rowsAffected > 0);

			if (res) {
				System.out.println("Participant mis à jour avec succès (ID: " + this.id + ")");
			} else {
				System.out.println("Aucun participant trouvé avec l'ID: " + this.id);
			}

		} catch (SQLException e) {
			System.err.println("Erreur lors de la mise à jour du participant : " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return res;
	}

	public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		// names of columns
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column));
		}
		// data of the table
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}
		return new DefaultTableModel(data, columnNames);
	}

}
