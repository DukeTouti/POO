package gui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import controllers.*;
import models.*;

/**
 * PatientFrame - Interface pour les patients
 * Permet de :
 * - Prendre des RDV avec recherche médecin/spécialité
 * - Voir tous ses RDV (passés et à venir)
 * - Voir le détail de ses consultations avec actes
 */
public class PatientFrame extends JFrame {

    static final Font POLICE_TITRE = new Font("Segoe UI", Font.BOLD, 14);
    static final Font POLICE_TEXTE = new Font("Segoe UI", Font.PLAIN, 14);
    
    /* Patient connecté */
    private Patient patient;
    
    /* Controllers */
    private MedecinController medecinController;
    private RendezVousController rdvController;
    private ConsultationController consultationController;
    
    /* Composants pour la prise de RDV */
    private JTextField searchField;
    private JList<Medecin> listeMedecins;
    private DefaultListModel<Medecin> modeleMedecins;
    private JSpinner spinnerDate;
    private JSpinner spinnerHeure;
    private JTextArea txtMotif;
    
    /* Composants pour affichage RDV et consultations */
    private JTable tableRDV;
    private DefaultTableModel modeleTableRDV;
    private JTextArea txtDetailsConsultation;
    
    /* Liste des RDV pour référence */
    private List<RendezVous> listeRDVActuelle;

    public PatientFrame(Patient patient) {
        // =============================================
        // INITIALISATION
        // =============================================
        this.patient = patient;
        this.listeRDVActuelle = new ArrayList<>();
        
        // Initialisation des controllers
        this.medecinController = new MedecinController();
        this.rdvController = new RendezVousController();
        this.consultationController = new ConsultationController();

        // Configuration de la fenêtre
        try {
            this.setIconImage(Toolkit.getDefaultToolkit().getImage("Projet/LOGO_APP.jpg"));
        } catch (Exception e) {
             // Ignorer si pas d'image
        }
        this.setTitle("ESPACE PATIENT - " + patient.getNomComplet());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1400, 800); 
        this.setLocationRelativeTo(null); 
        
        // =============================
        // Conteneur Racine
        // =============================
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout()); 
        mainPanel.setBackground(Color.WHITE); 
        this.setContentPane(mainPanel);
        
        // ================================================================
        // PANNEAU DU HAUT - NAVIGATION (avec profil et déconnexion)
        // ================================================================
        JPanel topPanel = new JPanel(new GridLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("Projet/TOP_PANEL3.jpg");
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Fallback si l'image ne charge pas
                    g.setColor(new Color(70, 130, 180));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topPanel.setLayout(new BorderLayout());
 
        // Panneau gauche avec Profil et Déconnexion
        JPanel leftButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftButtonsPanel.setOpaque(false);
        
        JButton btnProfil = createStyledButton("Profil", Color.BLACK);
        btnProfil.addActionListener(e -> afficherProfil());
        leftButtonsPanel.add(btnProfil);
        
        // --- BOUTON DECONNEXION ---
        JButton btnDeconnexion = createStyledButton("Déconnexion", Color.RED);
        btnDeconnexion.addActionListener(e -> {
            // Utilisation de la méthode stylée et centrée avec tes commentaires
            afficherPopupDeconnexion();
        });
        leftButtonsPanel.add(btnDeconnexion);
        
        // Label central avec le nom du patient
        JLabel lblNomPatient = new JLabel("Bienvenue, " + patient.getPrenom() + " !", SwingConstants.CENTER);
        lblNomPatient.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNomPatient.setForeground(Color.BLACK);
        
        topPanel.add(leftButtonsPanel, BorderLayout.WEST);
        topPanel.add(lblNomPatient, BorderLayout.CENTER);
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Placement du topPanel
        gbc.gridx = 0; 
        gbc.gridy = 0;
        gbc.gridwidth = 2; 
        gbc.weightx = 1.0; 
        gbc.fill = GridBagConstraints.BOTH; 
        gbc.insets = new Insets(0, 0, 0, 0); 
        mainPanel.add(topPanel, gbc);
        
        // ================================
        // CONTENEUR GAUCHE - PRISE DE RDV
        // ================================
        JPanel leftContainer = new JPanel(new BorderLayout()); 
        
        // Panneau de prise de RDV
        JPanel rdvPanel = new JPanel();
        rdvPanel.setBackground(new Color(100, 100, 255)); 
        rdvPanel.setLayout(new BoxLayout(rdvPanel, BoxLayout.Y_AXIS)); 
        rdvPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        
        // TITRE
        JLabel lblTitreRDV = new JLabel("PRENDRE UN RENDEZ-VOUS");
        lblTitreRDV.setForeground(Color.WHITE);
        lblTitreRDV.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitreRDV.setAlignmentX(Component.CENTER_ALIGNMENT);
        rdvPanel.add(lblTitreRDV);
        rdvPanel.add(Box.createVerticalStrut(20));
        
        // === 1. RECHERCHE MÉDECIN/SPÉCIALITÉ ===
        JLabel lblRecherche = new JLabel("Rechercher un médecin ou une spécialité :");
        lblRecherche.setForeground(Color.WHITE);
        lblRecherche.setAlignmentX(Component.LEFT_ALIGNMENT);
        rdvPanel.add(lblRecherche);
        rdvPanel.add(Box.createVerticalStrut(5));
        
        searchField = new JTextField();
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        searchField.setFont(POLICE_TEXTE);
        rdvPanel.add(searchField);
        rdvPanel.add(Box.createVerticalStrut(10));
        
        // Liste des médecins trouvés
        modeleMedecins = new DefaultListModel<>();
        listeMedecins = new JList<>(modeleMedecins);
        listeMedecins.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listeMedecins.setFont(POLICE_TEXTE);
        listeMedecins.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Medecin) {
                    Medecin m = (Medecin) value;
                    setText("Dr. " + m.getNom() + " " + m.getPrenom() + " - " + m.getSpecialite());
                }
                return this;
            }
        });
        
        JScrollPane scrollMedecins = new JScrollPane(listeMedecins);
        scrollMedecins.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        rdvPanel.add(scrollMedecins);
        rdvPanel.add(Box.createVerticalStrut(15));
        
        // Action de recherche en temps réel
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                rechercherMedecins(searchField.getText().trim());
            }
        });
        
        // === 2. CALENDRIER DATE ===
        JLabel lblDate = new JLabel("Choisissez la date :");
        lblDate.setForeground(Color.WHITE);
        lblDate.setAlignmentX(Component.LEFT_ALIGNMENT);
        rdvPanel.add(lblDate);
        rdvPanel.add(Box.createVerticalStrut(5));
        
        // Date spinner avec sélecteur de date
        SpinnerDateModel modelDate = new SpinnerDateModel();
        modelDate.setValue(new java.util.Date()); // Date du jour par défaut
        spinnerDate = new JSpinner(modelDate);
        JSpinner.DateEditor editorDate = new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy");
        spinnerDate.setEditor(editorDate);
        spinnerDate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        rdvPanel.add(spinnerDate);
        rdvPanel.add(Box.createVerticalStrut(15));
        
        // === 3. SÉLECTEUR D'HEURE ===
        JLabel lblHeure = new JLabel("Choisissez l'heure :");
        lblHeure.setForeground(Color.WHITE);
        lblHeure.setAlignmentX(Component.LEFT_ALIGNMENT);
        rdvPanel.add(lblHeure);
        rdvPanel.add(Box.createVerticalStrut(5));
        
        // Créer un spinner avec heures de 08h00 à 18h00
        SpinnerDateModel modeleHeure = new SpinnerDateModel();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 9);
        cal.set(Calendar.MINUTE, 0);
        modeleHeure.setValue(cal.getTime());
        
        spinnerHeure = new JSpinner(modeleHeure);
        JSpinner.DateEditor editorHeure = new JSpinner.DateEditor(spinnerHeure, "HH:mm");
        spinnerHeure.setEditor(editorHeure);
        spinnerHeure.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        rdvPanel.add(spinnerHeure);
        rdvPanel.add(Box.createVerticalStrut(15));
        
        // === 4. MOTIF ===
        JLabel lblMotif = new JLabel("Motif de la consultation :");
        lblMotif.setForeground(Color.WHITE);
        lblMotif.setAlignmentX(Component.LEFT_ALIGNMENT);
        rdvPanel.add(lblMotif);
        rdvPanel.add(Box.createVerticalStrut(5));
        
        txtMotif = new JTextArea(3, 20);
        txtMotif.setLineWrap(true);
        txtMotif.setWrapStyleWord(true);
        txtMotif.setFont(POLICE_TEXTE);
        JScrollPane scrollMotif = new JScrollPane(txtMotif);
        scrollMotif.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        rdvPanel.add(scrollMotif);
        rdvPanel.add(Box.createVerticalStrut(20));
        
        // === 5. BOUTON CONFIRMER ===
        JButton btnConfirmerRDV = createStyledButton("Confirmer le rendez-vous", Color.WHITE);
        btnConfirmerRDV.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfirmerRDV.addActionListener(e -> creerRendezVous());
        rdvPanel.add(btnConfirmerRDV);
        
        rdvPanel.add(Box.createVerticalGlue());
        
        // ScrollPane pour le panneau RDV
        JScrollPane scrollPaneRDV = new JScrollPane(rdvPanel);
        scrollPaneRDV.setBorder(null);
        scrollPaneRDV.getVerticalScrollBar().setUnitIncrement(16);
        
        leftContainer.add(scrollPaneRDV, BorderLayout.CENTER);
        
        // =============================
        // PANNEAU DE DROITE - MES RDV ET CONSULTATIONS
        // =============================
        JPanel rightPanel = new JPanel(new BorderLayout()); 
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Onglets pour RDV et Consultations
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(POLICE_TITRE);
        
        // === ONGLET 1 : MES RENDEZ-VOUS ===
        JPanel panelRDVList = new JPanel(new BorderLayout());
        panelRDVList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitreRDVList = new JLabel("Mes Rendez-vous", SwingConstants.CENTER);
        lblTitreRDVList.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelRDVList.add(lblTitreRDVList, BorderLayout.NORTH);
        
        // Table des RDV
        String[] colonnesRDV = {"Date", "Heure", "Médecin", "Spécialité", "Motif", "Statut"};
        modeleTableRDV = new DefaultTableModel(colonnesRDV, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Lecture seule
            }
        };
        tableRDV = new JTable(modeleTableRDV);
        tableRDV.setFont(POLICE_TEXTE);
        tableRDV.setRowHeight(25);
        tableRDV.getTableHeader().setFont(POLICE_TITRE);
        
        JScrollPane scrollTableRDV = new JScrollPane(tableRDV);
        panelRDVList.add(scrollTableRDV, BorderLayout.CENTER);
        
        // Boutons d'action sur RDV
        JPanel panelActionsRDV = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnActualiser = new JButton("Actualiser");
        btnActualiser.setFont(POLICE_TITRE);
        btnActualiser.addActionListener(e -> chargerMesRDV());
        panelActionsRDV.add(btnActualiser);
        
        JButton btnAnnuler = new JButton("Annuler RDV sélectionné");
        btnAnnuler.setFont(POLICE_TITRE);
        btnAnnuler.addActionListener(e -> annulerRDVSelectionne());
        panelActionsRDV.add(btnAnnuler);
        
        panelRDVList.add(panelActionsRDV, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Mes RDV", panelRDVList);
        
        // === ONGLET 2 : MES CONSULTATIONS ===
        JPanel panelConsultations = new JPanel(new BorderLayout());
        panelConsultations.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitreConsult = new JLabel("Mes Consultations passées", SwingConstants.CENTER);
        lblTitreConsult.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelConsultations.add(lblTitreConsult, BorderLayout.NORTH);
        
        // Table des consultations
        String[] colonnesConsult = {"Date", "Médecin", "Catégorie", "Prix", "Détails"};
        DefaultTableModel modeleTableConsult = new DefaultTableModel(colonnesConsult, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tableConsultations = new JTable(modeleTableConsult);
        tableConsultations.setFont(POLICE_TEXTE);
        tableConsultations.setRowHeight(25);
        tableConsultations.getTableHeader().setFont(POLICE_TITRE);
        
        JScrollPane scrollTableConsult = new JScrollPane(tableConsultations);
        panelConsultations.add(scrollTableConsult, BorderLayout.CENTER);
        
        // Zone de détails consultation
        JPanel panelDetailsConsult = new JPanel(new BorderLayout());
        panelDetailsConsult.setBorder(BorderFactory.createTitledBorder("Détails de la consultation"));
        
        txtDetailsConsultation = new JTextArea(8, 40);
        txtDetailsConsultation.setEditable(false);
        txtDetailsConsultation.setFont(POLICE_TEXTE);
        txtDetailsConsultation.setLineWrap(true);
        txtDetailsConsultation.setWrapStyleWord(true);
        
        JScrollPane scrollDetails = new JScrollPane(txtDetailsConsultation);
        panelDetailsConsult.add(scrollDetails, BorderLayout.CENTER);
        
        // Split pane pour table et détails
        JSplitPane splitConsult = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
                                                 scrollTableConsult, 
                                                 panelDetailsConsult);
        splitConsult.setDividerLocation(300);
        splitConsult.setResizeWeight(0.6);
        
        panelConsultations.add(splitConsult, BorderLayout.CENTER);
        
        // Listener pour afficher détails au clic
        tableConsultations.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableConsultations.getSelectedRow();
                if (row >= 0) {
                    afficherDetailsConsultation(row);
                }
            }
        });
        
        // Charger les consultations
        chargerMesConsultations(modeleTableConsult);
        
        tabbedPane.addTab("Mes Consultations", panelConsultations);
        
        rightPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // ============================================
        // PLACEMENT FINAL
        // ============================================
        
        // Placement du leftContainer
        gbc.gridx = 0; 
        gbc.gridy = 1; 
        gbc.gridwidth = 1;
        gbc.weightx = 0.35; 
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(leftContainer, gbc);
        
        // Placement du rightPanel
        gbc.gridx = 1; 
        gbc.gridy = 1; 
        gbc.gridwidth = 1;
        gbc.weightx = 0.65; 
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(rightPanel, gbc);
        
        // ============================================
        // CHARGEMENT INITIAL DES DONNÉES
        // ============================================
        chargerTousMedecins(); // Charger tous les médecins au démarrage
        chargerMesRDV();       // Charger les RDV du patient
        
        this.setVisible(true);
    }

    // =========================================================
    // POPUP DECONNEXION (AVEC VOS COMMENTAIRES)
    // =========================================================
    
    private void afficherPopupDeconnexion() {
        
        Color orDore   = new Color(212, 175, 55);  // couleur or
        Color chicDark = new Color(44, 62, 80);    // bleu nuit/gris
        Color chicGray = new Color(80, 80, 80);    // gris doux
        Color rougeFonce = new Color(180, 0, 0);
        
        //Creation de la boite de dialogue propre et stylise
        JDialog dialog = new JDialog(this, true); //On bloque la fenetre derriere.
        dialog.setUndecorated(true); // Enleve la bordure grise 
        dialog.setSize(400, 230); // La taille....
        dialog.setLocationRelativeTo(this); // Centre au milieu de la fenetre
        
        //Panneau blanc, bordure Rouge (maintenant Or)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE); //Fond blanc
        contentPanel.setBorder(BorderFactory.createLineBorder(orDore, 2)); //bordure Rouge (ici Dorée)

        // --- ELEMENTS ---
        JLabel icon = new JLabel("!"); // Icône
        icon.setFont(new Font("Segoe UI", Font.BOLD, 50));
        icon.setForeground(orDore);
        icon.setHorizontalAlignment(SwingConstants.CENTER); 

        JLabel text = new JLabel("Voulez-vous vraiment partir ?"); //Le text a afficher
        text.setFont(new Font("Segoe UI", Font.BOLD, 18));
        text.setForeground(chicDark);
        text.setHorizontalAlignment(SwingConstants.CENTER); 

        //La les 2 boutons oui et non sont avec le style custom qu'on a depuis login
        JButton btnOui = createStyledButton("Oui, Déconnexion", rougeFonce); //Rouge
        JButton btnNon = createStyledButton("Annuler", chicGray); //Gris

        //On met le oui sous "ecoute"
        btnOui.addActionListener(e -> {
            dialog.dispose(); //Ferme la popup
            this.dispose();   //Ferme la fenetre Patient
            try { new LoginFrame(); } catch(Exception ex) {} // Retour Login
        });
        
        //On met le non sous "ecoute" aussi
        btnNon.addActionListener(e -> dialog.dispose()); //Ferme juste la popup basic

        //On definit les regles de placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); //un peu de marge pour respirer
        
        // IMPORTANT POUR LE CENTRAGE : on etire la zone
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.anchor = GridBagConstraints.CENTER; //On force l'ancrage au centre
        
        gbc.gridx = 0; gbc.gridy = 0; 
        contentPanel.add(icon, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 20, 10); 
        contentPanel.add(text, gbc); //Texte au milieu
        
        // Panel pour aligner les boutons cote à cote
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnNon);
        btnPanel.add(btnOui);
        
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE; // Pas d'etirement ici
        contentPanel.add(btnPanel, gbc); //On rajoute avec les regles custom

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    // =========================================================
    // MÉTHODES MÉTIER - RECHERCHE MÉDECINS
    // =========================================================
    
    /**
     * Recherche intelligente : cherche dans nom, prénom ET spécialité
     */
    private void rechercherMedecins(String recherche) {
        modeleMedecins.clear();
        
        if (recherche.isEmpty()) {
            // Si vide, afficher tous les médecins
            chargerTousMedecins();
            return;
        }
        
        try {
            List<Medecin> medecins = medecinController.getTousMedecins();
            String rechercheMin = recherche.toLowerCase();
            
            // Filtrer les médecins qui matchent la recherche
            for (Medecin m : medecins) {
                String nom = m.getNom().toLowerCase();
                String prenom = m.getPrenom().toLowerCase();
                String specialite = m.getSpecialite().toLowerCase();
                
                // Cherche dans nom, prénom OU spécialité
                if (nom.contains(rechercheMin) || 
                    prenom.contains(rechercheMin) || 
                    specialite.contains(rechercheMin)) {
                    modeleMedecins.addElement(m);
                }
            }
            
            if (modeleMedecins.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Aucun médecin trouvé pour : " + recherche,
                    "Recherche", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de la recherche : " + e.getMessage(),
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Charge tous les médecins au démarrage
     */
    private void chargerTousMedecins() {
        modeleMedecins.clear();
        
        try {
            List<Medecin> medecins = medecinController.getTousMedecins();
            for (Medecin m : medecins) {
                modeleMedecins.addElement(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // =========================================================
    // MÉTHODES MÉTIER - GESTION RDV
    // =========================================================
    
    /**
     * Crée un nouveau rendez-vous
     */
    private void creerRendezVous() {
        // Vérifications
        Medecin medecinSelectionne = listeMedecins.getSelectedValue();
        if (medecinSelectionne == null) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un médecin",
                "Erreur", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String motif = txtMotif.getText().trim();
        if (motif.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez saisir un motif",
                "Erreur", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Récupérer date et heure
            java.util.Date dateSpinner = (java.util.Date) spinnerDate.getValue();
            java.util.Date heureSpinner = (java.util.Date) spinnerHeure.getValue();
            
            Calendar calDate = Calendar.getInstance();
            calDate.setTime(dateSpinner);
            
            Calendar calHeure = Calendar.getInstance();
            calHeure.setTime(heureSpinner);
            
            // CRÉER UN SEUL OBJET Date AVEC TOUT (date + heure + minute)
            models.Date dateRdvComplete = new models.Date(
                calDate.get(Calendar.DAY_OF_MONTH),
                calDate.get(Calendar.MONTH) + 1,
                calDate.get(Calendar.YEAR),
                calHeure.get(Calendar.HOUR_OF_DAY),  // Heure
                calHeure.get(Calendar.MINUTE)        // Minute
            );
            
            // Créer le RDV avec la date complète
            RendezVous rdv = new RendezVous(
                patient,
                medecinSelectionne,
                dateRdvComplete,
                motif
            );
            
            System.out.println("=== Création RDV ===");
            System.out.println("Date complète: " + dateRdvComplete.toString());
            
            // Enregistrer en BD
            boolean succes = rdvController.createRDV(rdv);
            
            if (succes) {
                JOptionPane.showMessageDialog(this, 
                    "Rendez-vous créé avec succès !\n\n" +
                    "Date : " + dateRdvComplete.toString() + "\n" +
                    "Médecin : Dr. " + medecinSelectionne.getNomComplet(),
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Réinitialiser le formulaire
                txtMotif.setText("");
                searchField.setText("");
                chargerTousMedecins();
                
                // Actualiser la liste des RDV
                chargerMesRDV();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de la création du RDV",
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erreur : " + e.getMessage(),
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Charge tous les RDV du patient
     */
    private void chargerMesRDV() {
        modeleTableRDV.setRowCount(0); // Vider la table
        listeRDVActuelle.clear();
        
        try {
            List<RendezVous> mesRDV = rdvController.getRDVByPatient(patient);
            
            // Trier par date (plus récents d'abord)
            mesRDV.sort((r1, r2) -> r2.getDateRdv().compareTo(r1.getDateRdv()));
            
            listeRDVActuelle = mesRDV;
            
            for (RendezVous rdv : mesRDV) {
                models.Date dateRdv = rdv.getDateRdv();
                
                // Formater la date sans heure
                String dateStr = String.format("%02d/%02d/%04d", 
                    dateRdv.getJour(), dateRdv.getMois(), dateRdv.getAnnee());
                
                // Formater l'heure
                String heureStr = String.format("%02d:%02d", 
                    dateRdv.getHeure(), dateRdv.getMinute());
                
                Object[] ligne = {
                    dateStr,
                    heureStr,
                    "Dr. " + rdv.getMedecin().getNomComplet(),
                    rdv.getMedecin().getSpecialite(),
                    rdv.getMotif(),
                    rdv.getStatut().toString()
                };
                modeleTableRDV.addRow(ligne);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Erreur chargement RDV : " + e.getMessage(),
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Annule le RDV sélectionné
     */
    private void annulerRDVSelectionne() {
        int selectedRow = tableRDV.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un rendez-vous à annuler",
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String statut = (String) modeleTableRDV.getValueAt(selectedRow, 5);
        if (statut.equals("ANNULE") || statut.equals("TERMINE")) {
            JOptionPane.showMessageDialog(this, 
                "Ce rendez-vous ne peut pas être annulé (statut : " + statut + ")",
                "Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int choix = JOptionPane.showConfirmDialog(this, 
            "Voulez-vous vraiment annuler ce rendez-vous ?",
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
            
        if (choix == JOptionPane.YES_OPTION) {
            try {
                // Récupérer le RDV correspondant
                if (selectedRow < listeRDVActuelle.size()) {
                    RendezVous rdvAnnuler = listeRDVActuelle.get(selectedRow);
                    
                    boolean succes = rdvController.annulerRDV(rdvAnnuler.getId());
                    
                    if (succes) {
                        JOptionPane.showMessageDialog(this, 
                            "Rendez-vous annulé avec succès",
                            "Succès", 
                            JOptionPane.INFORMATION_MESSAGE);
                        chargerMesRDV();
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Erreur lors de l'annulation",
                            "Erreur", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Erreur : " + e.getMessage(),
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // =========================================================
    // MÉTHODES MÉTIER - CONSULTATIONS
    // =========================================================
    
    /**
     * Charge toutes les consultations du patient
     */
    private void chargerMesConsultations(DefaultTableModel modele) {
        modele.setRowCount(0);
        
        try {
            List<Consultation> consultations = consultationController.getConsultationsByPatient(patient);
            
            for (Consultation c : consultations) {
                models.Date dateConsult = c.getDateConsultation();
                
                // Formater la date/heure complète
                String dateHeureStr = String.format("%02d/%02d/%04d à %02d:%02d",
                    dateConsult.getJour(), dateConsult.getMois(), dateConsult.getAnnee(),
                    dateConsult.getHeure(), dateConsult.getMinute());
                
                Object[] ligne = {
                    dateHeureStr,
                    "Dr. " + c.getRendezVous().getMedecin().getNomComplet(),
                    c.getCategorie() != null ? c.getCategorie().getDesignation() : "N/A",
                    String.format("%.2f€", c.coutTotal()),
                    "Voir détails"
                };
                modele.addRow(ligne);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Affiche les détails d'une consultation
     */
    private void afficherDetailsConsultation(int row) {
        try {
            List<Consultation> consultations = consultationController.getConsultationsByPatient(patient);
            if (row < consultations.size()) {
                Consultation c = consultations.get(row);
                
                StringBuilder details = new StringBuilder();
                details.append("═══════════════════════════════════════\n");
                details.append("      DÉTAILS DE LA CONSULTATION\n");
                details.append("═══════════════════════════════════════\n\n");
                
                models.Date dateRdv = c.getRendezVous().getDateRdv();
                details.append("    Date : ").append(dateRdv.toString()).append("\n\n");
                
                details.append("    Médecin : Dr. ").append(c.getRendezVous().getMedecin().getNomComplet())
                        .append(" (").append(c.getRendezVous().getMedecin().getSpecialite()).append(")\n\n");
                
                if (c.getCategorie() != null) {
                    details.append("    Catégorie : ").append(c.getCategorie().getDesignation()).append("\n\n");
                }
                
                details.append("    Description :\n");
                details.append(c.getDescription() != null ? "      " + c.getDescription() : "Aucune description").append("\n\n");
                
                details.append("───────────────────────────────────────\n");
                details.append("      ACTES MÉDICAUX RÉALISÉS\n");
                details.append("───────────────────────────────────────\n\n");
                
                if (c.getActes() != null && !c.getActes().isEmpty()) {
                    for (Acte acte : c.getActes()) {
                        details.append("• ").append(acte.getCode().name())
                               .append(" x").append(acte.getCoefficient())
                               .append(" → ").append(String.format("%.2f€", acte.cout()))
                               .append("\n");
                    }
                } else {
                    details.append("Aucun acte enregistré\n");
                }
                
                details.append("\n═══════════════════════════════════════\n");
                details.append("  COÛT TOTAL : ").append(String.format("%.2f€", c.coutTotal())).append("\n");
                details.append("═══════════════════════════════════════\n");
                
                txtDetailsConsultation.setText(details.toString());
                txtDetailsConsultation.setCaretPosition(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            txtDetailsConsultation.setText("Erreur lors du chargement des détails");
        }
    }
    
    // =========================================================
    // MÉTHODES UTILITAIRES
    // =========================================================
    
    /**
     * Affiche le profil du patient
     */
    private void afficherProfil() {
        StringBuilder profil = new StringBuilder();
        profil.append("══════════════════════════════\n");
        profil.append("        MON PROFIL\n");
        profil.append("══════════════════════════════\n\n");
        profil.append("Nom : ").append(patient.getNom()).append("\n");
        profil.append("Prénom : ").append(patient.getPrenom()).append("\n");
        profil.append("Email : ").append(patient.getEmail()).append("\n");
        profil.append("Téléphone : ").append(patient.getTelephone()).append("\n");
        profil.append("N° Sécu : ").append(patient.getNumeroSecu()).append("\n");
        profil.append("Adresse : ").append(patient.getAdresse()).append("\n");
        if (patient.getDateNaissance() != null) {
            profil.append("Date de naissance : ").append(patient.getDateNaissance().toString()).append("\n");
        }
        profil.append("Lieu de naissance : ").append(patient.getLieuNaissance()).append("\n");
        
        JOptionPane.showMessageDialog(this, 
            profil.toString(),
            "Mon Profil", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Crée un bouton stylisé
     */
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(POLICE_TITRE);
        button.setForeground(color);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new RoundedBorder(color, 20)); 
        button.setContentAreaFilled(false); 
        return button;
    }
    
    /**
     * Bordure arrondie personnalisée
     */
    static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;
        
        RoundedBorder(Color c, int r) { 
            this.color = c; 
            this.radius = r; 
        }
        
        @Override 
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(color);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
        
        @Override 
        public Insets getBorderInsets(Component c) { 
            return new Insets(radius/2, radius/2, radius/2, radius/2); 
        }
        
        @Override 
        public Insets getBorderInsets(Component c, Insets i) { 
            return getBorderInsets(c); 
        }
    }
}