package controllers;

import models.*;
import models.Date;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* ******************************************************** *
 * Contrôleur de gestion des consultations                  *
 * Gère les opérations CRUD sur les consultations           *
 * ******************************************************** */

public class ConsultationController {

	/* Configuration de la base de données */
	private static final String URL = "jdbc:mysql://localhost:3306/projet_java_cabinet";
	private static final String USER_BD = "cabinet_user";
	private static final String MDP_BD = "cabinet123";

	/* ========== CONNEXION À LA BASE DE DONNÉES ========== */

	/* Établit une connexion à la base de données */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER_BD, MDP_BD);
	}

	/* ========== CRÉATION D'UNE CONSULTATION ========== */

	/* Crée une nouvelle consultation dans la base de données */
	public boolean createConsultation(Consultation consultation) {
		Connection conn = null;
		PreparedStatement pstConsultation = null;
		PreparedStatement pstActe = null;
		ResultSet generatedKeys = null;

		try {
			conn = getConnection();
			conn.setAutoCommit(false); /* Transaction */

			/* 1. Insérer la consultation */
			String queryConsultation = "INSERT INTO consultations (rendez_vous_id, date_consultation, categorie_id, description, prix_consultation) "
					+ "VALUES (?, ?, ?, ?, ?)";
			pstConsultation = conn.prepareStatement(queryConsultation, Statement.RETURN_GENERATED_KEYS);
			pstConsultation.setInt(1, consultation.getRendezVous().getId());

			/* Convertir dateConsultation */
			Date dateConsult = consultation.getDateConsultation();
			java.util.Calendar cal = java.util.Calendar.getInstance();
			cal.set(dateConsult.getAnnee(), dateConsult.getMois() - 1, dateConsult.getJour());
			pstConsultation.setDate(2, new java.sql.Date(cal.getTimeInMillis()));

			pstConsultation.setInt(3, consultation.getCategorie().getId());
			pstConsultation.setString(4, consultation.getDescription());
			pstConsultation.setDouble(5, consultation.coutTotal());

			int resultConsultation = pstConsultation.executeUpdate();

			if (resultConsultation > 0) {
				/* Récupérer l'ID généré */
				generatedKeys = pstConsultation.getGeneratedKeys();
				if (generatedKeys.next()) {
					int consultationId = generatedKeys.getInt(1);

					/* 2. Insérer les actes */
					String queryActe = "INSERT INTO consultation_actes (consultation_id, code, coefficient) VALUES (?, ?, ?)";
					pstActe = conn.prepareStatement(queryActe);

					for (Acte acte : consultation.getActes()) {
						pstActe.setInt(1, consultationId);
						pstActe.setString(2, acte.getCode().name());
						pstActe.setInt(3, acte.getCoefficient());
						pstActe.addBatch();
					}

					pstActe.executeBatch();

					conn.commit();
					return true;
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
				if (pstConsultation != null)
					pstConsultation.close();
				if (pstActe != null)
					pstActe.close();
				if (conn != null) {
					conn.setAutoCommit(true);
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/* ========== RÉCUPÉRATION DES CONSULTATIONS PAR PATIENT ========== */

	/* Récupère toutes les consultations d'un patient */
	public List<Consultation> getConsultationsByPatient(Patient patient) {
		List<Consultation> consultations = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			String query = "SELECT c.id, c.date_consultation, c.description, c.prix_consultation, "
					+ "r.id as rdv_id, r.date_rdv, r.heure_rdv, r.motif, r.statut, "
					+ "cat.id as cat_id, cat.designation, cat.description as cat_desc, "
					+ "u_med.nom as med_nom, u_med.prenom as med_prenom, m.specialite " + "FROM consultations c "
					+ "JOIN rendez_vous r ON c.rendez_vous_id = r.id "
					+ "JOIN categories cat ON c.categorie_id = cat.id "
					+ "JOIN medecins m ON r.medecin_id = m.utilisateur_id "
					+ "JOIN utilisateurs u_med ON m.utilisateur_id = u_med.id " + "WHERE r.patient_id = ? "
					+ "ORDER BY c.date_consultation DESC";

			pst = conn.prepareStatement(query);
			pst.setInt(1, patient.getId());
			rs = pst.executeQuery();

			while (rs.next()) {
				/* Créer le médecin */
				Medecin medecin = new Medecin(rs.getString("med_nom"), rs.getString("med_prenom"), "", "",
						rs.getString("specialite"));

				/* Créer le RDV */
				Date dateRdv = convertirSQLDateEtHeure(rs.getDate("date_rdv"), rs.getString("heure_rdv"));
				RendezVous rdv = new RendezVous(rs.getInt("rdv_id"), patient, medecin, dateRdv,
						StatutRDV.valueOf(rs.getString("statut")), rs.getString("motif"));

				/* Créer la date de consultation */
				Date dateConsultation = convertirSQLDate(rs.getDate("date_consultation"));

				/* Créer la catégorie */
				Categorie categorie = new Categorie(rs.getInt("cat_id"), rs.getString("designation"),
						rs.getString("cat_desc"));

				/* Créer la consultation AVEC LE BON CONSTRUCTEUR */
				Consultation consultation = new Consultation(rs.getInt("id"), rdv, dateConsultation,
						rs.getString("description"), categorie);

				/* Charger les actes de cette consultation */
				List<Acte> actes = getActesByConsultation(rs.getInt("id"));
				for (Acte acte : actes) {
					consultation.ajouterActe(acte);
				}

				consultations.add(consultation);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}

		return consultations;
	}

	/* ========== RÉCUPÉRATION DES CONSULTATIONS PAR MÉDECIN ========== */

	/* Récupère toutes les consultations d'un médecin */
	public List<Consultation> getConsultationsByMedecin(Medecin medecin) {
		List<Consultation> consultations = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			String query = "SELECT c.id, c.date_consultation, c.description, c.prix_consultation, "
					+ "r.id as rdv_id, r.date_rdv, r.heure_rdv, r.motif, r.statut, "
					+ "cat.id as cat_id, cat.designation, cat.description as cat_desc, "
					+ "u_pat.nom as pat_nom, u_pat.prenom as pat_prenom " + "FROM consultations c "
					+ "JOIN rendez_vous r ON c.rendez_vous_id = r.id "
					+ "JOIN categories cat ON c.categorie_id = cat.id "
					+ "JOIN utilisateurs u_pat ON r.patient_id = u_pat.id " + "WHERE r.medecin_id = ? "
					+ "ORDER BY c.date_consultation DESC";

			pst = conn.prepareStatement(query);
			pst.setInt(1, medecin.getId());
			rs = pst.executeQuery();

			while (rs.next()) {
				/* Créer le patient simplifié */
				Patient patient = new Patient(rs.getString("pat_nom"), rs.getString("pat_prenom"), "", "", "", "", "",
						"", null, "");

				/* Créer le RDV */
				Date dateRdv = convertirSQLDateEtHeure(rs.getDate("date_rdv"), rs.getString("heure_rdv"));
				RendezVous rdv = new RendezVous(rs.getInt("rdv_id"), patient, medecin, dateRdv,
						StatutRDV.valueOf(rs.getString("statut")), rs.getString("motif"));

				/* Créer la date de consultation */
				Date dateConsultation = convertirSQLDate(rs.getDate("date_consultation"));

				/* Créer la catégorie */
				Categorie categorie = new Categorie(rs.getInt("cat_id"), rs.getString("designation"),
						rs.getString("cat_desc"));

				/* Créer la consultation */
				Consultation consultation = new Consultation(rs.getInt("id"), rdv, dateConsultation,
						rs.getString("description"), categorie);

				/* Charger les actes de cette consultation */
				List<Acte> actes = getActesByConsultation(rs.getInt("id"));
				for (Acte acte : actes) {
					consultation.ajouterActe(acte);
				}

				consultations.add(consultation);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}

		return consultations;
	}

	/* ========== RÉCUPÉRATION DES CONSULTATIONS PAR PÉRIODE ========== */

	/* Récupère les consultations d'un médecin sur une période */
	public List<Consultation> getConsultationsByMedecinEtPeriode(Medecin medecin, Date dateDebut, Date dateFin) {
		List<Consultation> consultations = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			String query = "SELECT c.id, c.date_consultation, c.description, c.prix_consultation, "
					+ "r.id as rdv_id, r.date_rdv, r.heure_rdv, r.motif, r.statut, "
					+ "cat.id as cat_id, cat.designation, cat.description as cat_desc, "
					+ "u_pat.nom as pat_nom, u_pat.prenom as pat_prenom " + "FROM consultations c "
					+ "JOIN rendez_vous r ON c.rendez_vous_id = r.id "
					+ "JOIN categories cat ON c.categorie_id = cat.id "
					+ "JOIN utilisateurs u_pat ON r.patient_id = u_pat.id "
					+ "WHERE r.medecin_id = ? AND c.date_consultation BETWEEN ? AND ? "
					+ "ORDER BY c.date_consultation DESC";

			pst = conn.prepareStatement(query);
			pst.setInt(1, medecin.getId());

			/* Convertir les dates */
			java.util.Calendar calDebut = java.util.Calendar.getInstance();
			calDebut.set(dateDebut.getAnnee(), dateDebut.getMois() - 1, dateDebut.getJour());
			pst.setDate(2, new java.sql.Date(calDebut.getTimeInMillis()));

			java.util.Calendar calFin = java.util.Calendar.getInstance();
			calFin.set(dateFin.getAnnee(), dateFin.getMois() - 1, dateFin.getJour());
			pst.setDate(3, new java.sql.Date(calFin.getTimeInMillis()));

			rs = pst.executeQuery();

			while (rs.next()) {
				/* Créer le patient simplifié */
				Patient patient = new Patient(rs.getString("pat_nom"), rs.getString("pat_prenom"), "", "", "", "", "",
						"", null, "");

				/* Créer le RDV */
				Date dateRdv = convertirSQLDateEtHeure(rs.getDate("date_rdv"), rs.getString("heure_rdv"));
				RendezVous rdv = new RendezVous(rs.getInt("rdv_id"), patient, medecin, dateRdv,
						StatutRDV.valueOf(rs.getString("statut")), rs.getString("motif"));

				/* Créer la date de consultation */
				Date dateConsultation = convertirSQLDate(rs.getDate("date_consultation"));

				/* Créer la catégorie */
				Categorie categorie = new Categorie(rs.getInt("cat_id"), rs.getString("designation"),
						rs.getString("cat_desc"));

				/* Créer la consultation */
				Consultation consultation = new Consultation(rs.getInt("id"), rdv, dateConsultation,
						rs.getString("description"), categorie);

				/* Charger les actes */
				List<Acte> actes = getActesByConsultation(rs.getInt("id"));
				for (Acte acte : actes) {
					consultation.ajouterActe(acte);
				}

				consultations.add(consultation);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}

		return consultations;
	}

	/* ========== RÉCUPÉRATION D'UNE CONSULTATION PAR ID ========== */

	/* Récupère une consultation par son ID */
	public Consultation getConsultationById(int id) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			String query = "SELECT c.id, c.date_consultation, c.description, c.prix_consultation, "
					+ "r.id as rdv_id, r.date_rdv, r.heure_rdv, r.motif, r.statut, "
					+ "cat.id as cat_id, cat.designation, cat.description as cat_desc " + "FROM consultations c "
					+ "JOIN rendez_vous r ON c.rendez_vous_id = r.id "
					+ "JOIN categories cat ON c.categorie_id = cat.id " + "WHERE c.id = ?";

			pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			rs = pst.executeQuery();

			if (rs.next()) {
				/* Créer le RDV simplifié */
				Date dateRdv = convertirSQLDateEtHeure(rs.getDate("date_rdv"), rs.getString("heure_rdv"));
				RendezVous rdv = new RendezVous(rs.getInt("rdv_id"), null, null, dateRdv,
						StatutRDV.valueOf(rs.getString("statut")), rs.getString("motif"));

				/* Créer la date de consultation */
				Date dateConsultation = convertirSQLDate(rs.getDate("date_consultation"));

				/* Créer la catégorie */
				Categorie categorie = new Categorie(rs.getInt("cat_id"), rs.getString("designation"),
						rs.getString("cat_desc"));

				/* Créer la consultation */
				Consultation consultation = new Consultation(rs.getInt("id"), rdv, dateConsultation,
						rs.getString("description"), categorie);

				/* Charger les actes */
				List<Acte> actes = getActesByConsultation(rs.getInt("id"));
				for (Acte acte : actes) {
					consultation.ajouterActe(acte);
				}

				return consultation;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}

		return null;
	}

	/* ========== RÉCUPÉRATION DES ACTES D'UNE CONSULTATION ========== */

	/* Récupère tous les actes d'une consultation */
	private List<Acte> getActesByConsultation(int consultationId) {
		List<Acte> actes = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			String query = "SELECT code, coefficient FROM consultation_actes WHERE consultation_id = ?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, consultationId);
			rs = pst.executeQuery();

			while (rs.next()) {
				Code code = Code.valueOf(rs.getString("code"));
				int coefficient = rs.getInt("coefficient");
				actes.add(new Acte(code, coefficient));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			fermerRessources(rs, pst, conn);
		}

		return actes;
	}

	/* ========== MODIFICATION D'UNE CONSULTATION ========== */

	/* Modifie une consultation existante */
	public boolean updateConsultation(Consultation consultation) {
		Connection conn = null;
		PreparedStatement pst = null;

		try {
			conn = getConnection();

			String query = "UPDATE consultations SET categorie_id = ?, description = ?, prix_consultation = ? WHERE id = ?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, consultation.getCategorie().getId());
			pst.setString(2, consultation.getDescription());
			pst.setDouble(3, consultation.coutTotal());
			pst.setInt(4, consultation.getId());

			int result = pst.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			fermerRessources(null, pst, conn);
		}
	}

	/* ========== SUPPRESSION D'UNE CONSULTATION ========== */

	/* Supprime une consultation */
	public boolean supprimerConsultation(int consultationId) {
		Connection conn = null;
		PreparedStatement pstActes = null;
		PreparedStatement pstConsultation = null;

		try {
			conn = getConnection();
			conn.setAutoCommit(false);

			/* 1. Supprimer les actes */
			String queryActes = "DELETE FROM consultation_actes WHERE consultation_id = ?";
			pstActes = conn.prepareStatement(queryActes);
			pstActes.setInt(1, consultationId);
			pstActes.executeUpdate();

			/* 2. Supprimer la consultation */
			String queryConsultation = "DELETE FROM consultations WHERE id = ?";
			pstConsultation = conn.prepareStatement(queryConsultation);
			pstConsultation.setInt(1, consultationId);
			int result = pstConsultation.executeUpdate();

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
				if (pstActes != null)
					pstActes.close();
				if (pstConsultation != null)
					pstConsultation.close();
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

		return new Date(cal.get(java.util.Calendar.DAY_OF_MONTH), cal.get(java.util.Calendar.MONTH) + 1,
				cal.get(java.util.Calendar.YEAR));
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

		return new Date(cal.get(java.util.Calendar.DAY_OF_MONTH), cal.get(java.util.Calendar.MONTH) + 1,
				cal.get(java.util.Calendar.YEAR), heure, minute);
	}

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