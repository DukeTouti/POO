package gui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import controllers.*;
import models.*;
import models.Date;

/**
 * AssistanteFrame - Interface pour les assistantes
 */

@SuppressWarnings("serial")
public class AssistanteFrame extends JFrame {

	// les styles, police et taille du titre de la fenetre et du texte
	static final Font POLICE_TITRE = new Font("Segoe UI", Font.BOLD, 14);
	static final Font POLICE_TEXTE = new Font("Segoe UI", Font.PLAIN, 14);

	/* Assistante connectée */
	private Assistante assistante;// On delcare une instance private

	/* Controllers */
	private RendezVousController rdvController;// On delcare une instance private
	private PatientController patientController;// On delcare une instance private
	private MedecinController medecinController;// On delcare une instance private

	/* Composants pour la gestion des RDV */
	private JTable tableRDV;/// On delcare une jtable pour les donne tabulaires
	private DefaultTableModel modeleTableRDV;// On delcare une instance private
	private List<RendezVous> listeRDV;// On delcare une structure dynamique (list)

	/* Composants pour la gestion des patients */
	private JTable tablePatients;/// On delcare une jtable pour les donne tabulaires
	private DefaultTableModel modeleTablePatients;// Implementation ready deja prete
	private List<Patient> listePatients;// On delcare une structure dynamique (list)

	/*
	 * JTable est le composant graphique qui affiche les donnees DefaultTableModel
	 * est la structure de donnees qui stocke et gere ces donnees.
	 */

	public AssistanteFrame(Assistante assistante) {
		// =============================================
		// INITIALISATION
		// =============================================
		this.assistante = assistante;// On delcare une instance private
		this.listeRDV = new ArrayList<>();// On initialize une structure dynamique (list)
		this.listePatients = new ArrayList<>();// On initialize une structure dynamique (list)

		// Initialisation des controllers
		this.rdvController = new RendezVousController();// On instaincie un controlleur
		this.patientController = new PatientController();// On instancie un controlleur
		this.medecinController = new MedecinController();// On instancie un controlleur

		// Configuration de la fenetre
		// Le logo de la fenetre et dans la task bar
		/*
		 * Le titre + le nom que l on a retrieve/fetch
		 */
		this.setIconImage(
				Toolkit.getDefaultToolkit().getImage("/home/hathouti/Bureau/UIR/3A/S5/POO/Projet/LOGO_APP.jpg"));
		this.setTitle("ESPACE ASSISTANTE - " + assistante.getNomComplet());// Le titre + le nom que l on a
																			// retrieve/fetch
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// on clique sur X donc on sort
		this.setSize(1400, 800);// La taille
		this.setLocationRelativeTo(null); // Pour que ca s'ouvre par defaut au milieu de l'ecran

		// =============================
		// Conteneur Racine
		// =============================
		JPanel mainPanel = new JPanel();// LE PANEL DE BASE SUR LEQUEL ON POSE LE RESTE
		mainPanel.setLayout(new GridBagLayout());// Pour les regles custom
		mainPanel.setBackground(Color.WHITE);// Couleur de fond
		this.setContentPane(mainPanel);// remplace le contenu principal de la fenetre par mainPanel

		// ================================================================
		// PANNEAU DU HAUT - NAVIGATION (avec profil et déconnexion)
		// ================================================================
		JPanel topPanel = new JPanel(new GridLayout()) {
			@Override // On reecrit la fonction de dessin/draw
			// On redefinit la methode paintComponent
			// g est l’objet Graphics fourni par Swing pour dessiner sur le composant par
			// defaut
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);// On garde tout de meme la base avec super
				// On donne une image a ce top panel
				ImageIcon icon = new ImageIcon("/home/hathouti/Bureau/UIR/3A/S5/POO/Projet/TOP_PANEL3.jpg");
				if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {// Check l'etat du chargement
					g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);// Alors on dessine a partir de la
																						// gauche
				} else {
					// Secours si l'image ne charge pas
					g.setColor(new Color(70, 130, 180));// Un sorte de Steel blue
					g.fillRect(0, 0, getWidth(), getHeight());// On peint apres avoir remplie le "pinceau"
				}
			}
		};
		// BorderFactory est une class qui encapsule les fonctionnalite d'autres classes
		// topPanel est un conteneur
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));// Bordure pour pas toucher les bouton
		topPanel.setLayout(new BorderLayout());// On declare pour gerer le positionnement

		// Panneau gauche avec Profil et Deconnexion
		JPanel leftButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		leftButtonsPanel.setOpaque(false);// transparent

		JButton btnProfil = createStyledButton("Profil", Color.BLACK);// Le button custom est noir
		btnProfil.addActionListener(e -> afficherProfil());// Function lamba qui affiche si on clique
		leftButtonsPanel.add(btnProfil);// On rajoute le bouton au panel.

		// On creer le bouton
		JButton btnDeconnexion = createStyledButton("Déconnexion", Color.RED);// Boutton rouge
		btnDeconnexion.addActionListener(e -> {// Function lambda
			int choix = JOptionPane.showConfirmDialog(this, // this donc
					"Voulez-vous vraiment vous déconnecter ?", "Confirmation", JOptionPane.YES_NO_OPTION);// Un option
																											// pane avec
																											// 2
																											// options..
			if (choix == JOptionPane.YES_OPTION) {// Si on a clique sur yes..
				this.dispose();// On ferme la fenetre actuel
				new LoginFrame();// On ouvre une page de login
			}
		});
		leftButtonsPanel.add(btnDeconnexion);// On rajoute ce bouton au panel

		// Tire/label central avec le nom de l'assistante
		// On CENTRE LE TEXTE avec SwingConstants.CENTER
		JLabel lblNom = new JLabel("Assistante - " + assistante.getNomComplet(), SwingConstants.CENTER);
		lblNom.setFont(new Font("Segoe UI", Font.BOLD, 18));// Style de la police
		lblNom.setForeground(Color.BLACK);// Texte de couleur noire

		// On rajoute le boutton et label
		topPanel.add(leftButtonsPanel, BorderLayout.WEST);// PLace a gauche
		topPanel.add(lblNom, BorderLayout.CENTER);// Place layout au milieu

		GridBagConstraints gbc = new GridBagConstraints();// On intancie une entite "regle"

		// Placement du topPanel
		gbc.gridx = 0; // Colonne 0
		gbc.gridy = 0;// Ligne 0
		gbc.gridwidth = 2; // Il prend 2 grilles/colonnes
		gbc.weightx = 1.0; // Il prend tout l'espace qu'il peut
		gbc.fill = GridBagConstraints.BOTH; // S'etend sur tout l'espace des 2 dimmensions
		gbc.insets = new Insets(0, 0, 0, 0);// Pas de bordure specific ici
		mainPanel.add(topPanel, gbc);// On applique les regles a ce panel et on le rajoute

		// ================================
		// CONTENEUR CENTRAL : ONGLETS
		// ================================

		/*
		 * //plusieurs panneaux (JPanel) sous forme d’onglet //Apres on leur donne un
		 * nom. Ici, pour chaque TabbedPane on declare une variable pour contenir un
		 * panneau (un conteneur graphique) Puis on appelle la methode qui correspond
		 * Puis on rajoute je Jpanel a la liste des Jpanel du JTabbedPane
		 */

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(POLICE_TITRE);// On chosiit la police

		// ONGLET 1 : GESTION DES RDV
		JPanel panelRDV = creerPanelRDV();
		tabbedPane.addTab("Gestion RDV", panelRDV);// on rajoute je Jpanel a la liste des Jpanel du JTabbedPane

		// ONGLET 2 : CREER UN RDV
		JPanel panelCreerRDV = creerPanelCreerRDV();
		tabbedPane.addTab("Créer RDV", panelCreerRDV);// on rajoute je Jpanel a la liste des Jpanel du JTabbedPane

		// ONGLET 3 : GESTION PATIENTS
		JPanel panelPatients = creerPanelPatients();
		tabbedPane.addTab("Patients", panelPatients);// on rajoute je Jpanel a la liste des Jpanel du JTabbedPane

		// ONGLET 4 : VUE MEDECINS
		JPanel panelMedecins = creerPanelMedecins();
		tabbedPane.addTab("Médecins", panelMedecins);// on rajoute je Jpanel a la liste des Jpanel du JTabbedPane

		// ONGLET 5 : VUE D'ENSEMBLE
		JPanel panelVueEnsemble = creerPanelVueEnsemble();
		tabbedPane.addTab("Vue d'ensemble", panelVueEnsemble);// on rajoute je Jpanel a la liste des Jpanel du
																// JTabbedPane

		// ============================================
		// PLACEMENT FINAL
		// ============================================
		gbc.gridx = 0; // colonne 0
		gbc.gridy = 1; // colonne 0 en dessous de TopPanel
		gbc.gridwidth = 2;// S'etend sur 2 colonnes horizontalement
		gbc.weightx = 1.0;// Prend tout l'espace horizontal disponible
		gbc.weighty = 1.0;// Prend tout l'espace vertical disponible
		gbc.fill = GridBagConstraints.BOTH;// S'etend verticalement et horizontalement
		mainPanel.add(tabbedPane, gbc);// On ajoute le systeme d'onglet au panneau principale avec les regles en plus

		// ============================================
		// CHARGEMENT INITIAL DES DONNÉES
		// ============================================

		/*
		 * Recupere la liste complete des rendez-vous depuis le controleur puis les
		 * trie, et les afficher dans le tableau (JTable
		 */
		chargerTousRDV();

		this.setVisible(true);// Rend la fenetre (AssistanteFrame)visible a l'utilisateur.
	}

	// =========================================================
	// ONGLET 1 : GESTION DES RDV
	// =========================================================

	// Cree un panel pour RDV
	// BorderLayout() Decompose en plusieurs zones
	// NORTH, SOUTH, EAST, WEST, CENTER
	private JPanel creerPanelRDV() {
		JPanel panel = new JPanel(new BorderLayout());// On definitt l arrangement des composant
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));// On espace entre composant et bords

		// On definit le titre qui cera centra
		JLabel lblTitre = new JLabel("Tous les rendez-vous du cabinet", SwingConstants.CENTER);
		lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 16));// Definit le style
		panel.add(lblTitre, BorderLayout.NORTH);// On place le label en haut

		// Table des RDV
		String[] colonnes = { "Date", "Heure", "Patient", "Téléphone", "Médecin", "Motif", "Statut" };
		modeleTableRDV = new DefaultTableModel(colonnes, 0) {// On cree le modele
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;// On empeche la modification directe
			}
		};
		tableRDV = new JTable(modeleTableRDV);// On cree la jtable avec le modele
		tableRDV.setFont(POLICE_TEXTE);// Police du contenu
		tableRDV.setRowHeight(25);// Hauteur ligne
		tableRDV.getTableHeader().setFont(POLICE_TITRE);// Police header

		JScrollPane scrollTable = new JScrollPane(tableRDV);// On met dans un scroll
		panel.add(scrollTable, BorderLayout.CENTER);// Au centre

		// Boutons d'action
		JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));// Panel boutons

		JButton btnActualiser = new JButton("Actualiser");// Bouton actualiser
		btnActualiser.setFont(POLICE_TITRE);// Police
		btnActualiser.addActionListener(e -> chargerTousRDV());// Action refresh
		panelActions.add(btnActualiser);// Ajout bouton

		JButton btnConfirmer = new JButton("Confirmer");// Bouton confirmer
		btnConfirmer.setFont(POLICE_TITRE);// Police
		btnConfirmer.addActionListener(e -> confirmerRDVSelectionne());// Action confirmer
		panelActions.add(btnConfirmer);// Ajout bouton

		JButton btnAnnuler = new JButton("Annuler");// Bouton annuler
		btnAnnuler.setFont(POLICE_TITRE);// Police
		btnAnnuler.addActionListener(e -> annulerRDVSelectionne());// Action annuler
		panelActions.add(btnAnnuler);// Ajout bouton

		JButton btnModifier = new JButton("Modifier");// Bouton modifier
		btnModifier.setFont(POLICE_TITRE);// Police
		btnModifier.addActionListener(e -> modifierRDVSelectionne());// Action modifier
		panelActions.add(btnModifier);// Ajout bouton

		panel.add(panelActions, BorderLayout.SOUTH);// En bas

		return panel;// On retourne le panel
	}

	private void chargerTousRDV() {
		modeleTableRDV.setRowCount(0);// On vide le tableau
		listeRDV.clear();// On vide la liste locale

		try {
			List<RendezVous> tousRDV = rdvController.getTousRDV();// On recupere via controller

			// Trier par date décroissante
			tousRDV.sort((r1, r2) -> r2.getDateRdv().compareTo(r1.getDateRdv()));// Tri du plus recent

			listeRDV = tousRDV;// On met a jour la liste

			for (RendezVous rdv : tousRDV) {// On parcourt tout
				models.Date dateRdv = rdv.getDateRdv();

				String dateStr = String.format("%02d/%02d/%04d", dateRdv.getJour(), dateRdv.getMois(),
						dateRdv.getAnnee());// Format date

				String heureStr = String.format("%02d:%02d", dateRdv.getHeure(), dateRdv.getMinute());// Format heure

				Object[] ligne = { dateStr, heureStr, rdv.getPatient().getNomComplet(), rdv.getPatient().getTelephone(),
						"Dr. " + rdv.getMedecin().getNomComplet(), rdv.getMotif(), rdv.getStatut().toString() };// Donnees
																												// de la
																												// ligne
				modeleTableRDV.addRow(ligne);// On rajoute la ligne
			}

		} catch (Exception e) {
			e.printStackTrace();// Erreur console
			JOptionPane.showMessageDialog(this, "Erreur chargement RDV : " + e.getMessage(), "Erreur",
					JOptionPane.ERROR_MESSAGE);// Popup erreur
		}
	}

	private void confirmerRDVSelectionne() {
		int selectedRow = tableRDV.getSelectedRow();// Ligne selectionnee
		if (selectedRow < 0) {
			JOptionPane.showMessageDialog(this, "Veuillez sélectionner un rendez-vous", "Information",
					JOptionPane.INFORMATION_MESSAGE);// Si rien selectionne
			return;
		}

		try {
			RendezVous rdv = listeRDV.get(selectedRow);// On recupere l objet

			if (rdv.getStatut() == StatutRDV.CONFIRME) {
				JOptionPane.showMessageDialog(this, "Ce RDV est déjà confirmé", "Information",
						JOptionPane.INFORMATION_MESSAGE);// Deja fait
				return;
			}

			boolean succes = rdvController.confirmerRDV(rdv.getId());// Appel controller

			if (succes) {
				JOptionPane.showMessageDialog(this, "RDV confirmé avec succès", "Succès",
						JOptionPane.INFORMATION_MESSAGE);// Succes
				chargerTousRDV();// Refresh
			}
		} catch (Exception e) {
			e.printStackTrace();// Erreur
		}
	}

	private void annulerRDVSelectionne() {
		int selectedRow = tableRDV.getSelectedRow();// Ligne selectionnee
		if (selectedRow < 0) {
			JOptionPane.showMessageDialog(this, "Veuillez sélectionner un rendez-vous", "Information",
					JOptionPane.INFORMATION_MESSAGE);// Si rien selectionne
			return;
		}

		try {
			RendezVous rdv = listeRDV.get(selectedRow);// On recupere l objet

			if (rdv.getStatut() == StatutRDV.ANNULE || rdv.getStatut() == StatutRDV.TERMINE) {
				JOptionPane.showMessageDialog(this, "Ce RDV ne peut pas être annulé (statut : " + rdv.getStatut() + ")",
						"Information", JOptionPane.WARNING_MESSAGE);// Pas annulable
				return;
			}

			int choix = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment annuler ce rendez-vous ?",
					"Confirmation", JOptionPane.YES_NO_OPTION);// Confirmation

			if (choix == JOptionPane.YES_OPTION) {
				boolean succes = rdvController.annulerRDV(rdv.getId());// Appel controller

				if (succes) {
					JOptionPane.showMessageDialog(this, "RDV annulé avec succès", "Succès",
							JOptionPane.INFORMATION_MESSAGE);// Succes
					chargerTousRDV();// Refresh
				}
			}
		} catch (Exception e) {
			e.printStackTrace();// Erreur
		}
	}

	private void modifierRDVSelectionne() {
		int selectedRow = tableRDV.getSelectedRow();// Ligne selectionnee
		if (selectedRow < 0) {
			JOptionPane.showMessageDialog(this, "Veuillez sélectionner un rendez-vous", "Information",
					JOptionPane.INFORMATION_MESSAGE);// Rien selectionne
			return;
		}

		try {
			RendezVous rdv = listeRDV.get(selectedRow);// On recupere l objet

			if (rdv.getStatut() == StatutRDV.TERMINE) {
				JOptionPane.showMessageDialog(this, "Un RDV terminé ne peut pas être modifié", "Information",
						JOptionPane.WARNING_MESSAGE);// Bloque
				return;
			}

			// Dialog pour modifier le RDV
			JDialog dialog = new JDialog(this, "Modifier le rendez-vous", true);// Fenetre modale
			dialog.setSize(500, 400);// Taille
			dialog.setLocationRelativeTo(this);// Centre
			dialog.setLayout(new BorderLayout(10, 10));// Layout

			JPanel contentPanel = new JPanel();// Contenu
			contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));// Vertical
			contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));// Marges

			// Info patient/médecin
			JLabel lblInfo = new JLabel(String.format("Patient: %s - Médecin: Dr. %s", rdv.getPatient().getNomComplet(),
					rdv.getMedecin().getNomComplet()));// Infos
			lblInfo.setFont(POLICE_TITRE);// Police
			contentPanel.add(lblInfo);// Ajout
			contentPanel.add(Box.createVerticalStrut(15));// Espace

			// Date
			JLabel lblDate = new JLabel("Nouvelle date :");// Label
			contentPanel.add(lblDate);// Ajout

			SpinnerDateModel modelDate = new SpinnerDateModel();// Modele
			Calendar cal = Calendar.getInstance();// Calendar
			cal.set(rdv.getDateRdv().getAnnee(), rdv.getDateRdv().getMois() - 1, rdv.getDateRdv().getJour());// Set date
			modelDate.setValue(cal.getTime());// Set value

			JSpinner spinnerDate = new JSpinner(modelDate);// Input date
			JSpinner.DateEditor editorDate = new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy");// Format
			spinnerDate.setEditor(editorDate);// Editor
			contentPanel.add(spinnerDate);// Ajout
			contentPanel.add(Box.createVerticalStrut(10));// Espace

			// Heure
			JLabel lblHeure = new JLabel("Nouvelle heure :");// Label
			contentPanel.add(lblHeure);// Ajout

			SpinnerDateModel modeleHeure = new SpinnerDateModel();// Modele
			Calendar calHeure = Calendar.getInstance();// Calendar
			calHeure.set(Calendar.HOUR_OF_DAY, rdv.getDateRdv().getHeure());// Set heure
			calHeure.set(Calendar.MINUTE, rdv.getDateRdv().getMinute());// Set minute
			modeleHeure.setValue(calHeure.getTime());// Set value

			JSpinner spinnerHeure = new JSpinner(modeleHeure);// Input heure
			JSpinner.DateEditor editorHeure = new JSpinner.DateEditor(spinnerHeure, "HH:mm");// Format
			spinnerHeure.setEditor(editorHeure);// Editor
			contentPanel.add(spinnerHeure);// Ajout
			contentPanel.add(Box.createVerticalStrut(10));// Espace

			// Motif
			JLabel lblMotif = new JLabel("Motif :");// Label
			contentPanel.add(lblMotif);// Ajout

			JTextArea txtMotif = new JTextArea(rdv.getMotif(), 3, 30);// Zone texte
			txtMotif.setLineWrap(true);// Retour ligne
			txtMotif.setWrapStyleWord(true);// Mot entier
			JScrollPane scrollMotif = new JScrollPane(txtMotif);// Scroll
			contentPanel.add(scrollMotif);// Ajout

			dialog.add(new JScrollPane(contentPanel), BorderLayout.CENTER);// Centre dialog

			// Boutons
			JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER));// Boutons

			JButton btnValider = new JButton("Enregistrer");// Save
			btnValider.setFont(POLICE_TITRE);// Police
			btnValider.addActionListener(e -> {
				try {
					// Récupérer la nouvelle date
					java.util.Date dateSpinnerVal = (java.util.Date) spinnerDate.getValue();// Get date
					java.util.Date heureSpinnerVal = (java.util.Date) spinnerHeure.getValue();// Get heure

					Calendar calDateNew = Calendar.getInstance();
					calDateNew.setTime(dateSpinnerVal);// Conv calendar

					Calendar calHeureNew = Calendar.getInstance();
					calHeureNew.setTime(heureSpinnerVal);// Conv calendar

					models.Date nouvelleDate = new models.Date(calDateNew.get(Calendar.DAY_OF_MONTH),
							calDateNew.get(Calendar.MONTH) + 1, calDateNew.get(Calendar.YEAR),
							calHeureNew.get(Calendar.HOUR_OF_DAY), calHeureNew.get(Calendar.MINUTE));// Nouvel objet
																										// date

					rdv.setDateRdv(nouvelleDate);// Maj date
					rdv.setMotif(txtMotif.getText().trim());// Maj motif

					boolean succes = rdvController.updateRDV(rdv);// Save en bdd

					if (succes) {
						JOptionPane.showMessageDialog(dialog, "RDV modifié avec succès", "Succès",
								JOptionPane.INFORMATION_MESSAGE);// Succes
						dialog.dispose();// Ferme fenetre
						chargerTousRDV();// Refresh
					} else {
						JOptionPane.showMessageDialog(dialog, "Erreur lors de la modification", "Erreur",
								JOptionPane.ERROR_MESSAGE);// Echec
					}

				} catch (Exception ex) {
					ex.printStackTrace();// Erreur
				}
			});
			panelBoutons.add(btnValider);// Ajout bouton

			JButton btnAnnuler = new JButton("Annuler");// Cancel
			btnAnnuler.setFont(POLICE_TITRE);// Police
			btnAnnuler.addActionListener(e -> dialog.dispose());// Ferme fenetre
			panelBoutons.add(btnAnnuler);// Ajout bouton

			dialog.add(panelBoutons, BorderLayout.SOUTH);// Bas dialog

			dialog.setVisible(true);// Affiche

		} catch (Exception e) {
			e.printStackTrace();// Erreur
		}
	}

	// =========================================================
	// ONGLET 2 : CRÉER UN RDV
	// =========================================================

	private JPanel creerPanelCreerRDV() {
		JPanel panel = new JPanel();// Panel vide
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));// Vertical
		panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));// Marges

		JLabel lblTitre = new JLabel("Créer un nouveau rendez-vous", SwingConstants.CENTER);// Titre
		lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 16));// Style
		lblTitre.setAlignmentX(Component.CENTER_ALIGNMENT);// Centre
		panel.add(lblTitre);// Ajout
		panel.add(Box.createVerticalStrut(20));// Espace

		// Sélection patient
		JLabel lblPatient = new JLabel("Patient :");// Label
		lblPatient.setAlignmentX(Component.LEFT_ALIGNMENT);// Gauche
		panel.add(lblPatient);// Ajout

		JComboBox<Patient> comboPatient = new JComboBox<>();// Menu deroulant
		List<Patient> patients = patientController.getTousPatients();// Get all
		for (Patient p : patients) {
			comboPatient.addItem(p);// Remplissage
		}
		comboPatient.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Patient) {
					Patient p = (Patient) value;
					setText(p.getNomComplet() + " - " + p.getTelephone());// Affiche nom tel
				}
				return this;
			}
		});
		comboPatient.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));// Max width
		comboPatient.setAlignmentX(Component.LEFT_ALIGNMENT);// Gauche
		panel.add(comboPatient);// Ajout
		panel.add(Box.createVerticalStrut(15));// Espace

		// Sélection médecin
		JLabel lblMedecin = new JLabel("Médecin :");// Label
		lblMedecin.setAlignmentX(Component.LEFT_ALIGNMENT);// Gauche
		panel.add(lblMedecin);// Ajout

		JComboBox<Medecin> comboMedecin = new JComboBox<>();// Menu deroulant
		List<Medecin> medecins = medecinController.getTousMedecins();// Get all
		for (Medecin m : medecins) {
			comboMedecin.addItem(m);// Remplissage
		}
		comboMedecin.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Medecin) {
					Medecin m = (Medecin) value;
					setText("Dr. " + m.getNomComplet() + " - " + m.getSpecialite());// Affiche nom spe
				}
				return this;
			}
		});
		comboMedecin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));// Max width
		comboMedecin.setAlignmentX(Component.LEFT_ALIGNMENT);// Gauche
		panel.add(comboMedecin);// Ajout
		panel.add(Box.createVerticalStrut(15));// Espace

		// Date
		JLabel lblDate = new JLabel("Date :");// Label
		lblDate.setAlignmentX(Component.LEFT_ALIGNMENT);// Gauche
		panel.add(lblDate);// Ajout

		SpinnerDateModel modelDate = new SpinnerDateModel();// Modele
		modelDate.setValue(new java.util.Date());// Value
		JSpinner spinnerDate = new JSpinner(modelDate);// Input
		JSpinner.DateEditor editorDate = new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy");// Format
		spinnerDate.setEditor(editorDate);// Editor
		spinnerDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));// Max width
		spinnerDate.setAlignmentX(Component.LEFT_ALIGNMENT);// Gauche
		panel.add(spinnerDate);// Ajout
		panel.add(Box.createVerticalStrut(15));// Espace

		// Heure
		JLabel lblHeure = new JLabel("Heure :");// Label
		lblHeure.setAlignmentX(Component.LEFT_ALIGNMENT);// Gauche
		panel.add(lblHeure);// Ajout

		SpinnerDateModel modeleHeure = new SpinnerDateModel();// Modele
		Calendar cal = Calendar.getInstance();// Calendar
		cal.set(Calendar.HOUR_OF_DAY, 9);// 9h default
		cal.set(Calendar.MINUTE, 0);// 00 default
		modeleHeure.setValue(cal.getTime());// Value

		JSpinner spinnerHeure = new JSpinner(modeleHeure);// Input
		JSpinner.DateEditor editorHeure = new JSpinner.DateEditor(spinnerHeure, "HH:mm");// Format
		spinnerHeure.setEditor(editorHeure);// Editor
		spinnerHeure.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));// Max width
		spinnerHeure.setAlignmentX(Component.LEFT_ALIGNMENT);// Gauche
		panel.add(spinnerHeure);// Ajout
		panel.add(Box.createVerticalStrut(15));// Espace

		// Motif
		JLabel lblMotif = new JLabel("Motif :");// Label
		lblMotif.setAlignmentX(Component.LEFT_ALIGNMENT);// Gauche
		panel.add(lblMotif);// Ajout

		JTextArea txtMotif = new JTextArea(3, 40);// Text area
		txtMotif.setLineWrap(true);// Wrap
		txtMotif.setWrapStyleWord(true);// Word
		txtMotif.setFont(POLICE_TEXTE);// Police
		JScrollPane scrollMotif = new JScrollPane(txtMotif);// Scroll
		scrollMotif.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));// Max height
		scrollMotif.setAlignmentX(Component.LEFT_ALIGNMENT);// Gauche
		panel.add(scrollMotif);// Ajout
		panel.add(Box.createVerticalStrut(25));// Espace

		// Bouton créer
		JButton btnCreer = new JButton("Créer le rendez-vous");// Bouton
		btnCreer.setFont(new Font("Segoe UI", Font.BOLD, 16));// Style
		btnCreer.setAlignmentX(Component.CENTER_ALIGNMENT);// Centre
		btnCreer.addActionListener(e -> {
			try {
				Patient patientChoisi = (Patient) comboPatient.getSelectedItem();// Recup patient
				Medecin medecinChoisi = (Medecin) comboMedecin.getSelectedItem();// Recup medecin
				String motif = txtMotif.getText().trim();// Recup motif

				if (patientChoisi == null || medecinChoisi == null) {
					JOptionPane.showMessageDialog(this, "Veuillez sélectionner un patient et un médecin", "Erreur",
							JOptionPane.WARNING_MESSAGE);// Erreur
					return;
				}

				if (motif.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Veuillez saisir un motif", "Erreur",
							JOptionPane.WARNING_MESSAGE);// Erreur
					return;
				}

				// Récupérer date et heure
				java.util.Date dateSpinner = (java.util.Date) spinnerDate.getValue();// Val date
				java.util.Date heureSpinner = (java.util.Date) spinnerHeure.getValue();// Val heure

				Calendar calDate = Calendar.getInstance();
				calDate.setTime(dateSpinner);// Calendar

				Calendar calHeure = Calendar.getInstance();
				calHeure.setTime(heureSpinner);// Calendar

				models.Date dateRdvComplete = new models.Date(calDate.get(Calendar.DAY_OF_MONTH),
						calDate.get(Calendar.MONTH) + 1, calDate.get(Calendar.YEAR), calHeure.get(Calendar.HOUR_OF_DAY),
						calHeure.get(Calendar.MINUTE));// Objet date

				RendezVous rdv = new RendezVous(patientChoisi, medecinChoisi, dateRdvComplete, motif);// Objet RDV

				boolean succes = rdvController.createRDV(rdv);// Save

				if (succes) {
					JOptionPane.showMessageDialog(this, "Rendez-vous créé avec succès !", "Succès",
							JOptionPane.INFORMATION_MESSAGE);// Succes

					// Réinitialiser
					txtMotif.setText("");// Reset text
					chargerTousRDV();// Refresh table
				} else {
					JOptionPane.showMessageDialog(this, "Erreur lors de la création du RDV", "Erreur",
							JOptionPane.ERROR_MESSAGE);// Echec
				}

			} catch (Exception ex) {
				ex.printStackTrace();// Erreur
				JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);// Popup
																														// erreur
			}
		});
		panel.add(btnCreer);// Ajout

		panel.add(Box.createVerticalGlue());// Push top

		JScrollPane scrollPanel = new JScrollPane(panel);// Scroll form
		scrollPanel.setBorder(null);// No border

		JPanel wrapperPanel = new JPanel(new BorderLayout());// Wrapper
		wrapperPanel.add(scrollPanel, BorderLayout.CENTER);// Center

		return wrapperPanel;// Retour
	}

	// =========================================================
	// ONGLET 3 : GESTION PATIENTS
	// =========================================================

	private JPanel creerPanelPatients() {
		JPanel panel = new JPanel(new BorderLayout());// Layout
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));// Marges

		JLabel lblTitre = new JLabel("👥 Gestion des patients", SwingConstants.CENTER);// Titre
		lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 16));// Style
		panel.add(lblTitre, BorderLayout.NORTH);// Haut

		// Table des patients
		String[] colonnes = { "Nom", "Prénom", "Téléphone", "Email", "N° Sécu" };// Colonnes
		modeleTablePatients = new DefaultTableModel(colonnes, 0) {// Modele
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;// No edit
			}
		};
		tablePatients = new JTable(modeleTablePatients);// Table
		tablePatients.setFont(POLICE_TEXTE);// Police
		tablePatients.setRowHeight(25);// Hauteur
		tablePatients.getTableHeader().setFont(POLICE_TITRE);// Header

		// Charger les patients
		chargerTousPatients();// Load initial

		JScrollPane scrollTable = new JScrollPane(tablePatients);// Scroll
		panel.add(scrollTable, BorderLayout.CENTER);// Center

		// Boutons d'action
		JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));// Actions

		JButton btnActualiser = new JButton("Actualiser");// Bouton
		btnActualiser.setFont(POLICE_TITRE);// Style
		btnActualiser.addActionListener(e -> chargerTousPatients());// Action
		panelActions.add(btnActualiser);// Ajout

		JButton btnAjouter = new JButton("Nouveau Patient");// Bouton
		btnAjouter.setFont(POLICE_TITRE);// Style
		btnAjouter.addActionListener(e -> ouvrirDialogNouveauPatient());
		panelActions.add(btnAjouter);// Ajout

		panel.add(panelActions, BorderLayout.SOUTH);// Bas

		return panel;// Retour
	}

	private void ouvrirDialogNouveauPatient() {
		// TODO Auto-generated method stub
		JDialog dialog = new JDialog(this, "Nouveau Patient", true);
		dialog.setLayout(new BorderLayout(10, 10));
		dialog.setSize(500, 650);

		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Créer le formulaire
		ChampsPatient champs = creerFormulairePatient();

		// Configurer les champs spéciaux
		configurerChampNumeroSecu(champs.txtNumSecu);
		configurerLoginAutomatique(champs.txtNom, champs.txtPrenom, champs.txtLogin);

		// Créer le panel de boutons
		JPanel buttonPanel = creerPanelBoutons(dialog, champs);

		// Assembler
		mainPanel.add(champs.formPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		dialog.add(mainPanel);
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	private class ChampsPatient {
		JPanel formPanel;
		JTextField txtNom, txtPrenom, txtLogin, txtTelephone, txtEmail, txtNumSecu, txtLieuNaissance;
		JPasswordField txtMdp;
		JTextArea txtAdresse;
		JSpinner spinnerDateNaissance;
	}

	private ChampsPatient creerFormulairePatient() {
		// TODO Auto-generated method stub
		ChampsPatient champs = new ChampsPatient();
		champs.formPanel = new JPanel(new GridLayout(11, 2, 10, 10));

		// Initialisation des champs à remplir
		champs.txtNom = new JTextField(20);
		champs.txtPrenom = new JTextField(20);
		champs.txtLogin = new JTextField(20);
		champs.txtMdp = new JPasswordField(20);
		champs.txtTelephone = new JTextField(20);
		champs.txtEmail = new JTextField(20);
		champs.txtNumSecu = new JTextField(20);
		champs.txtAdresse = new JTextArea(3, 20);
		champs.txtLieuNaissance = new JTextField(20);

		// Login en lecture seule (généré automatiquement : nom + 1ere lettre prenom
		// exception si le nom est trop long on gardera le nom)
		champs.txtLogin.setEditable(false);
		champs.txtLogin.setBackground(new Color(240, 240, 240));
		champs.txtLogin.setToolTipText("Généré automatiquement");

		// Date de naissance (défaut -30 ans)
		SpinnerDateModel dateModel = new SpinnerDateModel();
		champs.spinnerDateNaissance = new JSpinner(dateModel);
		champs.spinnerDateNaissance.setEditor(new JSpinner.DateEditor(champs.spinnerDateNaissance, "dd/MM/yyyy"));
		Calendar calDefault = Calendar.getInstance();
		calDefault.add(Calendar.YEAR, -30);
		champs.spinnerDateNaissance.setValue(calDefault.getTime());

		// Ajouter au panel
		JScrollPane scrollAdresse = new JScrollPane(champs.txtAdresse);

		champs.formPanel.add(new JLabel("Nom * :"));
		champs.formPanel.add(champs.txtNom);
		champs.formPanel.add(new JLabel("Prénom * :"));
		champs.formPanel.add(champs.txtPrenom);
		champs.formPanel.add(new JLabel("Login (auto) * :"));
		champs.formPanel.add(champs.txtLogin);
		champs.formPanel.add(new JLabel("Mot de passe * :"));
		champs.formPanel.add(champs.txtMdp);
		champs.formPanel.add(new JLabel("Téléphone * :"));
		champs.formPanel.add(champs.txtTelephone);
		champs.formPanel.add(new JLabel("Email :"));
		champs.formPanel.add(champs.txtEmail);
		champs.formPanel.add(new JLabel("N° Sécurité Sociale * :"));
		champs.formPanel.add(champs.txtNumSecu);
		champs.formPanel.add(new JLabel("Adresse :"));
		champs.formPanel.add(scrollAdresse);
		champs.formPanel.add(new JLabel("Date de naissance * :"));
		champs.formPanel.add(champs.spinnerDateNaissance);
		champs.formPanel.add(new JLabel("Lieu de naissance :"));
		champs.formPanel.add(champs.txtLieuNaissance);
		champs.formPanel.add(new JLabel(""));
		champs.formPanel.add(new JLabel("* Champs obligatoires"));

		return champs;
	}

	// Configuration du champ de Numero de Secu (15 chiffres)
	private void configurerChampNumeroSecu(JTextField txtNumSecu) {
		// TODO Auto-generated method stub
		((AbstractDocument) txtNumSecu.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				if (string != null && string.matches("\\d+")
						&& (fb.getDocument().getLength() + string.length()) == 15) {
					super.insertString(fb, offset, string, attr);
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				if (text == null) {
					super.replace(fb, offset, length, text, attrs);
					return;
				}
				if (text.matches("\\d*") && (fb.getDocument().getLength() - length + text.length()) <= 15) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});
	}

	// Configuration login automatique
	private void configurerLoginAutomatique(JTextField txtNom, JTextField txtPrenom, JTextField txtLogin) {
		// TODO Auto-generated method stub
		DocumentListener loginGenerator = new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				generer();
			}

			public void removeUpdate(DocumentEvent e) {
				generer();
			}

			public void insertUpdate(DocumentEvent e) {
				generer();
			}

			private void generer() {
				String nom = txtNom.getText().trim();
				String prenom = txtPrenom.getText().trim();

				if (!nom.isEmpty() && !prenom.isEmpty()) {
					txtLogin.setText(genererLoginDepuisNomPrenom(nom, prenom));
				} else if (!nom.isEmpty()) {
					txtLogin.setText(nom.toLowerCase().replaceAll("\\s+", ""));
				} else {
					txtLogin.setText("");
				}
			}
		};

		txtNom.getDocument().addDocumentListener(loginGenerator);
		txtPrenom.getDocument().addDocumentListener(loginGenerator);
	}
	
	// Création du panel de boutons
	private JPanel creerPanelBoutons(JDialog dialog, ChampsPatient champs) {
		// TODO Auto-generated method stub
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    
	    JButton btnValider = new JButton("Créer le Patient");
	    btnValider.setBackground(new Color(76, 175, 80));
	    btnValider.setForeground(Color.WHITE);
	    btnValider.setFocusPainted(false);
	    
	    JButton btnAnnuler = new JButton("Annuler");
	    btnAnnuler.setBackground(new Color(244, 67, 54));
	    btnAnnuler.setForeground(Color.WHITE);
	    btnAnnuler.setFocusPainted(false);
	    
	    btnValider.addActionListener(e -> validerEtCreerPatient(dialog, champs));
	    btnAnnuler.addActionListener(e -> dialog.dispose());
	    
	    buttonPanel.add(btnValider);
	    buttonPanel.add(btnAnnuler);
	    
	    return buttonPanel;
	}
	
	// Validation complète + création
	private void validerEtCreerPatient(JDialog dialog, ChampsPatient champs) {
		// TODO Auto-generated method stub
		
	    // Validation par étapes
	    if (!validerChampsObligatoires(dialog, champs)) return;
	    if (!validerNumeroSecu(dialog, champs.txtNumSecu)) return;
	    if (!validerTelephone(dialog, champs.txtTelephone)) return;
	    if (!validerEmail(dialog, champs.txtEmail)) return;
	    if (!validerMotDePasse(dialog, champs.txtMdp)) return;
	    if (!validerDateNaissance(dialog, champs.spinnerDateNaissance)) return;
	    
	    // Créer le patient
	    Patient nouveauPatient = creerPatientDepuisFormulaire(champs);
	    
	    // Sauvegarder
	    boolean succes = patientController.createPatient(nouveauPatient);
	    
	    if (succes) {
	        JOptionPane.showMessageDialog(dialog,
	            "Patient créé avec succès !\n\n" +
	            "Nom : " + nouveauPatient.getNomComplet() + "\n" +
	            "Login : " + nouveauPatient.getLogin() + "\n" +
	            "N° Sécu : " + nouveauPatient.getNumeroSecu(),
	            "Succès",
	            JOptionPane.INFORMATION_MESSAGE);
	        
	        chargerTousPatients();
	        dialog.dispose();
	    } else {
	        JOptionPane.showMessageDialog(dialog,
	            "Erreur lors de la création.\nLe login existe peut-être déjà.",
	            "Erreur",
	            JOptionPane.ERROR_MESSAGE);
	    }
	}

	// Validation des champs obligatoires
	private boolean validerChampsObligatoires(JDialog dialog, ChampsPatient champs) {
	    if (champs.txtNom.getText().trim().isEmpty() || 
	        champs.txtPrenom.getText().trim().isEmpty() ||
	        champs.txtLogin.getText().trim().isEmpty() ||
	        new String(champs.txtMdp.getPassword()).trim().isEmpty() ||
	        champs.txtTelephone.getText().trim().isEmpty() ||
	        champs.txtNumSecu.getText().trim().isEmpty()) {
	        
	        JOptionPane.showMessageDialog(dialog,
	            "Veuillez remplir tous les champs obligatoires (*)",
	            "Champs manquants",
	            JOptionPane.WARNING_MESSAGE);
	        return false;
	    }
	    return true;
	}
	
	// Validation du numéro de sécu
	private boolean validerNumeroSecu(JDialog dialog, JTextField txtNumSecu) {
	    String numSecu = txtNumSecu.getText().trim();
	    
	    // Vérifier longueur
	    if (numSecu.length() != 15) {
	        JOptionPane.showMessageDialog(dialog,
	            "Le N° de sécu doit contenir exactement 15 chiffres.\n" +
	            "Actuellement : " + numSecu.length() + " chiffres",
	            "Format invalide",
	            JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    
	    // Vérifier doublon
	    Patient existant = patientController.rechercherParNumeroSecu(numSecu);
	    if (existant != null) {
	        JOptionPane.showMessageDialog(dialog,
	            "Ce numéro de sécurité sociale existe déjà.\n" +
	            "Patient : " + existant.getNomComplet(),
	            "Numéro déjà utilisé",
	            JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    
	    return true;
	}

	// Validation du téléphone
	private boolean validerTelephone(JDialog dialog, JTextField txtTelephone) {
	    String telephone = txtTelephone.getText().trim();
	    String telChiffres = telephone.replaceAll("[\\s-]", "");
	    
	    if (!telChiffres.matches("^\\d{10,}$")) {
	        JOptionPane.showMessageDialog(dialog,
	            "Le téléphone doit contenir au moins 10 chiffres.",
	            "Format invalide",
	            JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    return true;
	}

	// Validation de l'email
	private boolean validerEmail(JDialog dialog, JTextField txtEmail) {
	    String email = txtEmail.getText().trim();
	    
	    if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
	        JOptionPane.showMessageDialog(dialog,
	            "L'adresse email n'est pas valide.",
	            "Format invalide",
	            JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    return true;
	}

	// Validation du mot de passe
	private boolean validerMotDePasse(JDialog dialog, JPasswordField txtMdp) {
	    String mdp = new String(txtMdp.getPassword());
	    
	    if (mdp.length() < 3) {
	        JOptionPane.showMessageDialog(dialog,
	            "Le mot de passe doit contenir au moins 3 caractères.",
	            "Mot de passe trop court",
	            JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    return true;
	}

	// Validation de la date de naissance
	private boolean validerDateNaissance(JDialog dialog, JSpinner spinnerDateNaissance) {
	    java.util.Date dateNaissanceUtil = (java.util.Date) spinnerDateNaissance.getValue();
	    Calendar calNaissance = Calendar.getInstance();
	    calNaissance.setTime(dateNaissanceUtil);
	    
	    Calendar aujourdhui = Calendar.getInstance();
	    
	    // Pas dans le futur
	    if (calNaissance.after(aujourdhui)) {
	        JOptionPane.showMessageDialog(dialog,
	            "La date de naissance ne peut pas être dans le futur.",
	            "Date invalide",
	            JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    
	    // Pas plus de 150 ans
	    Calendar calMin = Calendar.getInstance();
	    calMin.add(Calendar.YEAR, -150);
	    if (calNaissance.before(calMin)) {
	        JOptionPane.showMessageDialog(dialog,
	            "La date de naissance semble incorrecte (> 150 ans).",
	            "Date invalide",
	            JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    
	    return true;
	}

	// Création de l'objet Patient depuis le formulaire
	private Patient creerPatientDepuisFormulaire(ChampsPatient champs) {
	    // Convertir date de naissance
	    java.util.Date dateNaissanceUtil = (java.util.Date) champs.spinnerDateNaissance.getValue();
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(dateNaissanceUtil);
	    Date dateNaissance = new Date(
	        cal.get(Calendar.DAY_OF_MONTH),
	        cal.get(Calendar.MONTH) + 1,
	        cal.get(Calendar.YEAR)
	    );
	    
	    // Récupérer les valeurs
	    String email = champs.txtEmail.getText().trim();
	    String adresse = champs.txtAdresse.getText().trim();
	    String lieuNaissance = champs.txtLieuNaissance.getText().trim();
	    
	    return new Patient(
	        0,  // ID généré par la BD
	        champs.txtNom.getText().trim(),
	        champs.txtPrenom.getText().trim(),
	        champs.txtLogin.getText().trim(),
	        new String(champs.txtMdp.getPassword()),
	        champs.txtTelephone.getText().trim(),
	        email.isEmpty() ? null : email,
	        champs.txtNumSecu.getText().trim(),
	        adresse.isEmpty() ? null : adresse,
	        dateNaissance,
	        lieuNaissance.isEmpty() ? null : lieuNaissance
	    );
	}

	// Génération du login (déjà modulaire)
	private String genererLoginDepuisNomPrenom(String nom, String prenom) {
	    String nomClean = nom.trim().toLowerCase()
	                         .replaceAll("\\s+", "")
	                         .replaceAll("[àâä]", "a")
	                         .replaceAll("[éèêë]", "e")
	                         .replaceAll("[îï]", "i")
	                         .replaceAll("[ôö]", "o")
	                         .replaceAll("[ùûü]", "u")
	                         .replaceAll("[ç]", "c")
	                         .replaceAll("[^a-z]", "");
	    
	    String prenomClean = prenom.trim().toLowerCase()
	                               .replaceAll("\\s+", "")
	                               .replaceAll("[àâä]", "a")
	                               .replaceAll("[éèêë]", "e")
	                               .replaceAll("[îï]", "i")
	                               .replaceAll("[ôö]", "o")
	                               .replaceAll("[ùûü]", "u")
	                               .replaceAll("[ç]", "c")
	                               .replaceAll("[^a-z]", "");
	    
	    String premiereLettre = prenomClean.isEmpty() ? "" : prenomClean.substring(0, 1);
	    String login = nomClean + premiereLettre;
	    
	    if (login.length() > 20) {
	        login = nomClean.length() > 20 ? nomClean.substring(0, 20) : nomClean;
	    }
	    
	    return login;
	}


	private void chargerTousPatients() {
		modeleTablePatients.setRowCount(0);// Vide table
		listePatients.clear();// Vide liste

		try {
			List<Patient> patients = patientController.getTousPatients();// Get all
			listePatients = patients;// Store

			for (Patient p : patients) {
				Object[] ligne = { p.getNom(), p.getPrenom(), p.getTelephone(), p.getEmail(), p.getNumeroSecu() };// Donnees
				modeleTablePatients.addRow(ligne);// Add row
			}

		} catch (Exception e) {
			e.printStackTrace();// Erreur
		}
	}

	// =========================================================
	// ONGLET 4 : VUE MÉDECINS
	// =========================================================

	private JPanel creerPanelMedecins() {
		JPanel panel = new JPanel(new BorderLayout());// Layout
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));// Marges

		JLabel lblTitre = new JLabel("Liste des médecins", SwingConstants.CENTER);// Titre
		lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 16));// Style
		panel.add(lblTitre, BorderLayout.NORTH);// Haut

		// Table des médecins
		String[] colonnes = { "Nom", "Prénom", "Spécialité" };// Colonnes
		DefaultTableModel modele = new DefaultTableModel(colonnes, 0) {// Modele
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;// No edit
			}
		};
		JTable table = new JTable(modele);// Table
		table.setFont(POLICE_TEXTE);// Police
		table.setRowHeight(30);// Hauteur
		table.getTableHeader().setFont(POLICE_TITRE);// Header

		// Charger les médecins
		try {
			List<Medecin> medecins = medecinController.getTousMedecins();// Get all

			for (Medecin m : medecins) {
				Object[] ligne = { m.getNom(), m.getPrenom(), m.getSpecialite() };// Donnees
				modele.addRow(ligne);// Add row
			}

		} catch (Exception e) {
			e.printStackTrace();// Erreur
		}

		JScrollPane scrollTable = new JScrollPane(table);// Scroll
		panel.add(scrollTable, BorderLayout.CENTER);// Centre

		return panel;// Retour
	}

	// =========================================================
	// ONGLET 5 : VUE D'ENSEMBLE
	// =========================================================

	private JPanel creerPanelVueEnsemble() {
		JPanel panel = new JPanel(new BorderLayout());// Layout
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));// Marges

		JLabel lblTitre = new JLabel("Vue d'ensemble du cabinet", SwingConstants.CENTER);// Titre
		lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));// Style
		panel.add(lblTitre, BorderLayout.NORTH);// Haut

		// Zone de texte pour les stats
		JTextArea txtStats = new JTextArea();// TextArea
		txtStats.setEditable(false);// No edit
		txtStats.setFont(new Font("Segoe UI", Font.PLAIN, 14));// Police

		// Calculer les stats
		try {
			int nbPatients = patientController.getTousPatients().size();// Count patients
			int nbMedecins = medecinController.getTousMedecins().size();// Count medecins
			List<RendezVous> tousRDV = rdvController.getTousRDV();// Get rdv

			int nbRDVPlanifie = 0;
			int nbRDVConfirme = 0;
			int nbRDVTermine = 0;
			int nbRDVAnnule = 0;

			for (RendezVous rdv : tousRDV) {
				switch (rdv.getStatut()) {
				case PLANIFIE:
					nbRDVPlanifie++;// Compteur
					break;
				case CONFIRME:
					nbRDVConfirme++;// Compteur
					break;
				case TERMINE:
					nbRDVTermine++;// Compteur
					break;
				case ANNULE:
					nbRDVAnnule++;// Compteur
					break;
				}
			}

			StringBuilder stats = new StringBuilder();// Builder
			stats.append("═══════════════════════════════════════\n");
			stats.append("     STATISTIQUES DU CABINET\n");
			stats.append("═══════════════════════════════════════\n\n");
			stats.append("Nombre de patients : ").append(nbPatients).append("\n\n");
			stats.append("Nombre de médecins : ").append(nbMedecins).append("\n\n");
			stats.append("Total RDV : ").append(tousRDV.size()).append("\n\n");
			stats.append("───────────────────────────────────────\n");
			stats.append("Détail des RDV :\n");
			stats.append("───────────────────────────────────────\n\n");
			stats.append("  • Planifiés : ").append(nbRDVPlanifie).append("\n");
			stats.append("  • Confirmés : ").append(nbRDVConfirme).append("\n");
			stats.append("  • Terminés : ").append(nbRDVTermine).append("\n");
			stats.append("  • Annulés : ").append(nbRDVAnnule).append("\n\n");
			stats.append("═══════════════════════════════════════\n");

			txtStats.setText(stats.toString());// Affichage

		} catch (Exception e) {
			txtStats.setText("Erreur chargement statistiques");// Erreur
			e.printStackTrace();
		}

		JScrollPane scrollStats = new JScrollPane(txtStats);// Scroll
		panel.add(scrollStats, BorderLayout.CENTER);// Centre

		return panel;// Retour
	}

	// =========================================================
	// MÉTHODES UTILITAIRES
	// =========================================================

	/**
	 * Affiche le profil de l'assistante
	 */
	private void afficherProfil() {
		StringBuilder profil = new StringBuilder();// Texte
		profil.append("══════════════════════════════\n");
		profil.append("        MON PROFIL\n");
		profil.append("══════════════════════════════\n\n");
		profil.append(assistante.getNom()).append(" ").append(assistante.getPrenom()).append("\n");
		profil.append("Fonction : Assistante médicale\n");
		profil.append("Login : ").append(assistante.getLogin()).append("\n");

		JOptionPane.showMessageDialog(this, profil.toString(), "Mon Profil", JOptionPane.INFORMATION_MESSAGE);// Popup
	}

	private JButton createStyledButton(String text, Color color) {
		JButton button = new JButton(text);// Cree bouton
		button.setFont(POLICE_TITRE);// Police
		button.setForeground(color);// Couleur texte
		button.setFocusPainted(false);// Pas de focus moche
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));// Curseur main
		button.setBorder(new RoundedBorder(color, 20));// Bordure arrondie
		button.setContentAreaFilled(false);// Fond transparent
		return button;// Retour
	}

	static class RoundedBorder extends AbstractBorder {
		private final Color color;
		private final int radius;

		RoundedBorder(Color c, int r) {
			this.color = c;
			this.radius = r;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);// Lissage
			g.setColor(color);// Couleur
			g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);// Dessine
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
		}// Marge

		@Override
		public Insets getBorderInsets(Component c, Insets i) {
			return getBorderInsets(c);
		}
	}
}
