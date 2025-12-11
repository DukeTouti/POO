package gui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import controllers.*;
import models.*;

/**
 * MedecinFrame - Interface pour les médecins
 */
@SuppressWarnings({ "unused", "serial" })
public class MedecinFrame extends JFrame {

    static final Font POLICE_TITRE = new Font("Segoe UI", Font.BOLD, 14);
    static final Font POLICE_TEXTE = new Font("Segoe UI", Font.PLAIN, 14);
    
    /* Médecin connecté */
    private Medecin medecin;//Instance du medecin connecte
    
    /* Controllers */
    private RendezVousController rdvController;//Gere les RDV
    private ConsultationController consultationController;//Gere les consultations
    private CategorieController categorieController;//Gere les categories d actes
    
    /* Composants pour la gestion des RDV */
    private JTable tableRDVJour;//Tableau des RDV du jour
    private DefaultTableModel modeleTableRDVJour;//Modele de donnees jour
    private JTable tableRDVTous;//Tableau de tous les RDV
    private DefaultTableModel modeleTableRDVTous;//Modele de donnees total
    
    /* Liste des RDV pour référence */
    private List<RendezVous> listeRDVJour;//Liste memoire jour
    private List<RendezVous> listeRDVTous;//Liste memoire total

    public MedecinFrame(Medecin medecin) {
        // =============================================
        // INITIALISATION
        // =============================================
        this.medecin = medecin;//Stocke le medecin courant
        this.listeRDVJour = new ArrayList<>();//Initialise liste vide
        this.listeRDVTous = new ArrayList<>();//Initialise liste vide
        
        // Initialisation des controllers
        this.rdvController = new RendezVousController();//Instancie controller
        this.consultationController = new ConsultationController();//Instancie controller
        this.categorieController = new CategorieController();//Instancie controller

        // Configuration de la fenêtre
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("/home/hathouti/Bureau/UIR/3A/S5/POO/Projet/LOGO_APP.jpg"));//Icone
        this.setTitle("ESPACE MÉDECIN - Dr. " + medecin.getNomComplet());//Titre avec nom
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Ferme tout sur X
        this.setSize(1400, 800); //Dimensions
        this.setLocationRelativeTo(null); //Centre a l ecran
        
        // =============================
        // Conteneur Racine
        // =============================
        JPanel mainPanel = new JPanel();//Panel principal
        mainPanel.setLayout(new GridBagLayout()); //Regles de placement
        mainPanel.setBackground(Color.WHITE); //Fond blanc
        this.setContentPane(mainPanel);//Definit comme contenu
        
        // ================================================================
        // PANNEAU DU HAUT - NAVIGATION (avec profil et déconnexion)
        // ================================================================
        JPanel topPanel = new JPanel(new GridLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);//Appel parent
                ImageIcon icon = new ImageIcon("/home/hathouti/Bureau/UIR/3A/S5/POO/Projet/TOP_PANEL3.jpg");//Charge image
                if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);//Dessine image
                } else {
                    // Fallback si l'image ne charge pas
                    g.setColor(new Color(70, 130, 180));//Couleur secours
                    g.fillRect(0, 0, getWidth(), getHeight());//Remplissage
                }
            }
        };
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));//Marges
        topPanel.setLayout(new BorderLayout());//Layout
 
        // Panneau gauche avec Profil et Déconnexion
        JPanel leftButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));//Alignement droite
        leftButtonsPanel.setOpaque(false);//Transparent
        
        JButton btnProfil = createStyledButton("Profil", Color.BLACK);//Bouton noir
        btnProfil.addActionListener(e -> afficherProfil());//Action profil
        leftButtonsPanel.add(btnProfil);//Ajout
        
        JButton btnDeconnexion = createStyledButton("Déconnexion", Color.RED);//Bouton rouge
        btnDeconnexion.addActionListener(e -> {
            int choix = JOptionPane.showConfirmDialog(this, 
                "Voulez-vous vraiment vous déconnecter ?", 
                "Confirmation", 
                JOptionPane.YES_NO_OPTION);//Confirmation
            if (choix == JOptionPane.YES_OPTION) {
                this.dispose();//Fermeture
                new LoginFrame();//Retour login
            }
        });
        leftButtonsPanel.add(btnDeconnexion);//Ajout
        
        // Label central avec le nom du médecin
        JLabel lblNomMedecin = new JLabel("Dr. " + medecin.getNomComplet() + " - " + medecin.getSpecialite(), SwingConstants.CENTER);//Texte centre
        lblNomMedecin.setFont(new Font("Segoe UI", Font.BOLD, 18));//Police
        lblNomMedecin.setForeground(Color.WHITE);//Couleur
        
        topPanel.add(leftButtonsPanel, BorderLayout.WEST);//Boutons a gauche
        topPanel.add(lblNomMedecin, BorderLayout.CENTER);//Nom au centre
        
        GridBagConstraints gbc = new GridBagConstraints();//Regles grille
        
        // Placement du topPanel
        gbc.gridx = 0; //Colonne 0
        gbc.gridy = 0;//Ligne 0
        gbc.gridwidth = 2; //Largeur 2 colonnes
        gbc.weightx = 1.0; //Prend espace horizontal
        gbc.fill = GridBagConstraints.BOTH; //Remplit cellule
        gbc.insets = new Insets(0, 0, 0, 0); //Pas de marges
        mainPanel.add(topPanel, gbc);//Ajout au main
        
        // ================================
        // CONTENEUR CENTRAL - ONGLETS
        // ================================
        JTabbedPane tabbedPane = new JTabbedPane();//Systeme onglets
        tabbedPane.setFont(POLICE_TITRE);//Police onglets
        
        // === ONGLET 1 : MES RDV DU JOUR ===
        JPanel panelRDVJour = creerPanelRDVJour();//Cree panel jour
        tabbedPane.addTab("RDV du Jour", panelRDVJour);//Ajoute onglet
        
        // === ONGLET 2 : TOUS MES RDV ===
        JPanel panelRDVTous = creerPanelRDVTous();//Cree panel tous
        tabbedPane.addTab("Tous mes RDV", panelRDVTous);//Ajoute onglet
        
        // === ONGLET 3 : CRÉER UNE CONSULTATION ===
        JPanel panelConsultation = creerPanelConsultation();//Cree panel consult
        tabbedPane.addTab("Nouvelle Consultation", panelConsultation);//Ajoute onglet
        
        // === ONGLET 4 : HISTORIQUE CONSULTATIONS ===
        JPanel panelHistorique = creerPanelHistorique();//Cree panel historique
        tabbedPane.addTab("Historique", panelHistorique);//Ajoute onglet
        
        // === ONGLET 5 : STATISTIQUES ===
        JPanel panelStatistiques = creerPanelStatistiques();//Cree panel stats
        tabbedPane.addTab("Statistiques", panelStatistiques);//Ajoute onglet
        
        // ============================================
        // PLACEMENT FINAL
        // ============================================
        gbc.gridx = 0; //Colonne 0
        gbc.gridy = 1; //Ligne 1
        gbc.gridwidth = 2;//Largeur 2
        gbc.weightx = 1.0; //Poids horizontal
        gbc.weighty = 1.0; //Poids vertical
        gbc.fill = GridBagConstraints.BOTH;//Remplissage total
        mainPanel.add(tabbedPane, gbc);//Ajout onglets
        
        // ============================================
        // CHARGEMENT INITIAL DES DONNÉES
        // ============================================
        chargerRDVDuJour();//Charge RDV jour
        chargerTousRDV();//Charge historique RDV
        
        this.setVisible(true);//Affiche la fenetre
    }

    // =========================================================
    // ONGLET 1 : RDV DU JOUR
    // =========================================================
    
    private JPanel creerPanelRDVJour() {
        JPanel panel = new JPanel(new BorderLayout());//Layout
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));//Marges
        
        JLabel lblTitre = new JLabel("Mes rendez-vous d'aujourd'hui", SwingConstants.CENTER);//Titre
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 16));//Style
        panel.add(lblTitre, BorderLayout.NORTH);//Haut
        
        // Table des RDV du jour
        String[] colonnes = {"Heure", "Patient", "Téléphone", "Motif", "Statut"};//Colonnes
        modeleTableRDVJour = new DefaultTableModel(colonnes, 0) {//Modele
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//Non editable
            }
        };
        tableRDVJour = new JTable(modeleTableRDVJour);//Table
        tableRDVJour.setFont(POLICE_TEXTE);//Police
        tableRDVJour.setRowHeight(30);//Hauteur
        tableRDVJour.getTableHeader().setFont(POLICE_TITRE);//Header
        
        JScrollPane scrollTable = new JScrollPane(tableRDVJour);//Scroll
        panel.add(scrollTable, BorderLayout.CENTER);//Centre
        
        // Boutons d'action
        JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));//Panel boutons
        
        JButton btnActualiser = new JButton("Actualiser");//Bouton
        btnActualiser.setFont(POLICE_TITRE);//Style
        btnActualiser.addActionListener(e -> chargerRDVDuJour());//Action
        panelActions.add(btnActualiser);//Ajout
        
        JButton btnCreerConsultation = new JButton("Créer Consultation");//Bouton
        btnCreerConsultation.setFont(POLICE_TITRE);//Style
        btnCreerConsultation.addActionListener(e -> creerConsultationDepuisRDV());//Action
        panelActions.add(btnCreerConsultation);//Ajout
        
        JButton btnConfirmer = new JButton("Confirmer RDV");//Bouton
        btnConfirmer.setFont(POLICE_TITRE);//Style
        btnConfirmer.addActionListener(e -> confirmerRDVSelectionne());//Action
        panelActions.add(btnConfirmer);//Ajout
        
        panel.add(panelActions, BorderLayout.SOUTH);//Bas
        
        return panel;//Retour
    }
    
    private void chargerRDVDuJour() {
        modeleTableRDVJour.setRowCount(0);//Vide table
        listeRDVJour.clear();//Vide liste
        
        try {
            // Créer la date d'aujourd'hui
            Calendar cal = Calendar.getInstance();//Calendrier
            models.Date aujourdhui = new models.Date(
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.YEAR)
            );//Date jour
            
            List<RendezVous> rdvJour = rdvController.getRDVByMedecinEtDate(medecin, aujourdhui);//Recup RDV
            
            // Trier par heure
            rdvJour.sort((r1, r2) -> {
                int h1 = r1.getDateRdv().getHeure() * 60 + r1.getDateRdv().getMinute();
                int h2 = r2.getDateRdv().getHeure() * 60 + r2.getDateRdv().getMinute();
                return h1 - h2;//Comparaison minutes
            });
            
            listeRDVJour = rdvJour;//Stocke liste
            
            for (RendezVous rdv : rdvJour) {
                String heure = String.format("%02d:%02d", 
                    rdv.getDateRdv().getHeure(), 
                    rdv.getDateRdv().getMinute());//Format heure
                
                Object[] ligne = {
                    heure,
                    rdv.getPatient().getNomComplet(),
                    rdv.getPatient().getTelephone(),
                    rdv.getMotif(),
                    rdv.getStatut().toString()
                };//Donnees ligne
                modeleTableRDVJour.addRow(ligne);//Ajout
            }
            
        } catch (Exception e) {
            e.printStackTrace();//Erreur
            JOptionPane.showMessageDialog(this, 
                "Erreur chargement RDV : " + e.getMessage(),
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);//Popup
        }
    }
    
    private void confirmerRDVSelectionne() {
        int selectedRow = tableRDVJour.getSelectedRow();//Ligne selectionnee
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un rendez-vous",
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);//Rien selectionne
            return;
        }
        
        try {
            RendezVous rdv = listeRDVJour.get(selectedRow);//Objet RDV
            
            if (rdv.getStatut() == StatutRDV.CONFIRME) {
                JOptionPane.showMessageDialog(this, 
                    "Ce RDV est déjà confirmé",
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE);//Deja fait
                return;
            }
            
            boolean succes = rdvController.confirmerRDV(rdv.getId());//Appel controller
            
            if (succes) {
                JOptionPane.showMessageDialog(this, 
                    "RDV confirmé avec succès",
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);//Succes
                chargerRDVDuJour();//Refresh
            }
        } catch (Exception e) {
            e.printStackTrace();//Erreur
        }
    }

    // =========================================================
    // ONGLET 2 : TOUS MES RDV
    // =========================================================
    
    private JPanel creerPanelRDVTous() {
        JPanel panel = new JPanel(new BorderLayout());//Layout
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));//Marges
        
        JLabel lblTitre = new JLabel("Tous mes rendez-vous", SwingConstants.CENTER);//Titre
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 16));//Style
        panel.add(lblTitre, BorderLayout.NORTH);//Haut
        
        // Table de tous les RDV
        String[] colonnes = {"Date", "Heure", "Patient", "Téléphone", "Motif", "Statut"};//Colonnes
        modeleTableRDVTous = new DefaultTableModel(colonnes, 0) {//Modele
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//No edit
            }
        };
        tableRDVTous = new JTable(modeleTableRDVTous);//Table
        tableRDVTous.setFont(POLICE_TEXTE);//Police
        tableRDVTous.setRowHeight(25);//Hauteur
        tableRDVTous.getTableHeader().setFont(POLICE_TITRE);//Header
        
        JScrollPane scrollTable = new JScrollPane(tableRDVTous);//Scroll
        panel.add(scrollTable, BorderLayout.CENTER);//Centre
        
        // Boutons d'action
        JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));//Panel boutons
        
        JButton btnActualiser = new JButton("Actualiser");//Bouton
        btnActualiser.setFont(POLICE_TITRE);//Style
        btnActualiser.addActionListener(e -> chargerTousRDV());//Action
        panelActions.add(btnActualiser);//Ajout
        
        panel.add(panelActions, BorderLayout.SOUTH);//Bas
        
        return panel;//Retour
    }
    
    private void chargerTousRDV() {
        modeleTableRDVTous.setRowCount(0);//Vide table
        listeRDVTous.clear();//Vide liste
        
        try {
            List<RendezVous> tousRDV = rdvController.getRDVByMedecin(medecin);//Get all RDV
            
            // Trier par date décroissante
            tousRDV.sort((r1, r2) -> r2.getDateRdv().compareTo(r1.getDateRdv()));//Tri
            
            listeRDVTous = tousRDV;//Store
            
            for (RendezVous rdv : tousRDV) {
                models.Date dateRdv = rdv.getDateRdv();
                
                String dateStr = String.format("%02d/%02d/%04d", 
                    dateRdv.getJour(), dateRdv.getMois(), dateRdv.getAnnee());//Format date
                
                String heureStr = String.format("%02d:%02d", 
                    dateRdv.getHeure(), dateRdv.getMinute());//Format heure
                
                Object[] ligne = {
                    dateStr,
                    heureStr,
                    rdv.getPatient().getNomComplet(),
                    rdv.getPatient().getTelephone(),
                    rdv.getMotif(),
                    rdv.getStatut().toString()
                };//Donnees
                modeleTableRDVTous.addRow(ligne);//Ajout
            }
            
        } catch (Exception e) {
            e.printStackTrace();//Erreur
        }
    }

    // =========================================================
    // ONGLET 3 : CRÉER UNE CONSULTATION
    // =========================================================
    
    private JPanel creerPanelConsultation() {
        JPanel panel = new JPanel(new BorderLayout());//Layout
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));//Marges
        
        JLabel lblInfo = new JLabel("<html><center>Créer une nouvelle consultation<br>" +
                                     "Sélectionnez un RDV dans l'onglet 'RDV du Jour' puis cliquez sur 'Créer Consultation'</center></html>", 
                                     SwingConstants.CENTER);//Texte informatif
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));//Police
        panel.add(lblInfo, BorderLayout.CENTER);//Centre
        
        return panel;//Retour
    }
    
    private void creerConsultationDepuisRDV() {
        int selectedRow = tableRDVJour.getSelectedRow();//Ligne selectionnee
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un RDV dans l'onglet 'RDV du Jour'",
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);//Rien selectionne
            return;
        }
        
        try {
            RendezVous rdv = listeRDVJour.get(selectedRow);//Objet RDV
            
            // Dialog pour créer la consultation
            JDialog dialog = new JDialog(this, "Créer une consultation", true);//Modale
            dialog.setSize(600, 500);//Taille
            dialog.setLocationRelativeTo(this);//Centre
            dialog.setLayout(new BorderLayout(10, 10));//Layout
            
            JPanel contentPanel = new JPanel();//Panel contenu
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));//Vertical
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));//Marges
            
            // Info patient
            JLabel lblPatient = new JLabel("Patient : " + rdv.getPatient().getNomComplet());//Info
            lblPatient.setFont(POLICE_TITRE);//Style
            contentPanel.add(lblPatient);//Ajout
            contentPanel.add(Box.createVerticalStrut(10));//Espace
            
            // Catégorie
            JLabel lblCategorie = new JLabel("Catégorie :");//Label
            contentPanel.add(lblCategorie);//Ajout
            
            JComboBox<Categorie> comboCategorie = new JComboBox<>();//Combo
            List<Categorie> categories = categorieController.getToutesCategories();//Get categories
            for (Categorie cat : categories) {
                comboCategorie.addItem(cat);//Remplissage
            }
            comboCategorie.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                              int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Categorie) {
                        setText(((Categorie) value).getDesignation());//Affiche designation
                    }
                    return this;
                }
            });
            contentPanel.add(comboCategorie);//Ajout
            contentPanel.add(Box.createVerticalStrut(15));//Espace
            
            // Description
            JLabel lblDescription = new JLabel("Description / Diagnostic :");//Label
            contentPanel.add(lblDescription);//Ajout
            
            JTextArea txtDescription = new JTextArea(5, 40);//Zone texte
            txtDescription.setLineWrap(true);//Wrap
            txtDescription.setWrapStyleWord(true);//Word
            JScrollPane scrollDesc = new JScrollPane(txtDescription);//Scroll
            contentPanel.add(scrollDesc);//Ajout
            contentPanel.add(Box.createVerticalStrut(15));//Espace
            
            // Actes NGAP
            JLabel lblActes = new JLabel("Actes médicaux (Format: CODE COEF, ex: CS 1, K 2) :");//Label instruction
            contentPanel.add(lblActes);//Ajout
            
            JTextArea txtActes = new JTextArea(3, 40);//Zone texte
            txtActes.setLineWrap(true);//Wrap
            txtActes.setWrapStyleWord(true);//Word
            JScrollPane scrollActes = new JScrollPane(txtActes);//Scroll
            contentPanel.add(scrollActes);//Ajout
            
            dialog.add(new JScrollPane(contentPanel), BorderLayout.CENTER);//Ajout au dialog
            
            // Boutons
            JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER));//Panel boutons
            
            JButton btnValider = new JButton("Créer la consultation");//Valider
            btnValider.setFont(POLICE_TITRE);//Style
            btnValider.addActionListener(e -> {
                try {
                    Categorie catChoisie = (Categorie) comboCategorie.getSelectedItem();//Recup cat
                    String description = txtDescription.getText().trim();//Recup desc
                    
                    if (description.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "La description est obligatoire");//Verif
                        return;
                    }
                    
                    // Créer la date de consultation (maintenant)
                    Calendar cal = Calendar.getInstance();//Maintenant
                    models.Date dateConsult = new models.Date(
                        cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE)
                    );//Objet date
                    
                    // Créer la consultation
                    Consultation consultation = new Consultation(rdv, dateConsult, description, catChoisie);//Objet consult
                    
                    // Parser et ajouter les actes
                    String actesStr = txtActes.getText().trim();//Recup actes
                    if (!actesStr.isEmpty()) {
                        String[] lignes = actesStr.split("\n");//Split lignes
                        for (String ligne : lignes) {
                            ligne = ligne.trim();
                            if (!ligne.isEmpty()) {
                                String[] parts = ligne.split(" ");//Split code coef
                                if (parts.length >= 2) {
                                    try {
                                        Code code = Code.valueOf(parts[0].toUpperCase());//Enum Code
                                        int coef = Integer.parseInt(parts[1]);//Int coef
                                        consultation.ajouterActe(code, coef);//Ajout acte
                                    } catch (Exception ex) {
                                        System.err.println("Erreur parsing acte: " + ligne);//Log erreur
                                    }
                                }
                            }
                        }
                    }
                    
                    // Enregistrer en BD
                    boolean succes = consultationController.createConsultation(consultation);//Save
                    
                    if (succes) {
                        // Marquer le RDV comme terminé
                        rdvController.terminerRDV(rdv.getId());//Update statut
                        
                        JOptionPane.showMessageDialog(dialog, 
                            "Consultation créée avec succès !\n\nCoût total : " + 
                            String.format("%.2f€", consultation.coutTotal()),
                            "Succès", 
                            JOptionPane.INFORMATION_MESSAGE);//Succes
                        
                        dialog.dispose();//Close
                        chargerRDVDuJour();//Refresh
                    } else {
                        JOptionPane.showMessageDialog(dialog, 
                            "Erreur lors de la création",
                            "Erreur", 
                            JOptionPane.ERROR_MESSAGE);//Echec
                    }
                    
                } catch (Exception ex) {
                    ex.printStackTrace();//Erreur
                    JOptionPane.showMessageDialog(dialog, 
                        "Erreur : " + ex.getMessage(),
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);//Popup
                }
            });
            panelBoutons.add(btnValider);//Ajout
            
            JButton btnAnnuler = new JButton("Annuler");//Annuler
            btnAnnuler.setFont(POLICE_TITRE);//Style
            btnAnnuler.addActionListener(e -> dialog.dispose());//Close
            panelBoutons.add(btnAnnuler);//Ajout
            
            dialog.add(panelBoutons, BorderLayout.SOUTH);//Bas
            
            dialog.setVisible(true);//Show
            
        } catch (Exception e) {
            e.printStackTrace();//Erreur
        }
    }

    // =========================================================
    // ONGLET 4 : HISTORIQUE
    // =========================================================
    
    private JPanel creerPanelHistorique() {
        JPanel panel = new JPanel(new BorderLayout());//Layout
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));//Marges
        
        JLabel lblTitre = new JLabel("Historique de mes consultations", SwingConstants.CENTER);//Titre
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 16));//Style
        panel.add(lblTitre, BorderLayout.NORTH);//Haut
        
        // Table des consultations
        String[] colonnes = {"Date", "Patient", "Catégorie", "Prix", "Actes"};//Colonnes
        DefaultTableModel modele = new DefaultTableModel(colonnes, 0) {//Modele
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//No edit
            }
        };
        JTable table = new JTable(modele);//Table
        table.setFont(POLICE_TEXTE);//Police
        table.setRowHeight(25);//Hauteur
        table.getTableHeader().setFont(POLICE_TITRE);//Header
        
        JScrollPane scrollTable = new JScrollPane(table);//Scroll
        panel.add(scrollTable, BorderLayout.CENTER);//Centre
        
        // Charger les consultations
        try {
            List<Consultation> consultations = consultationController.getConsultationsByMedecin(medecin);//Get all
            
            for (Consultation c : consultations) {
                models.Date dateConsult = c.getDateConsultation();
                String dateStr = String.format("%02d/%02d/%04d %02d:%02d",
                    dateConsult.getJour(), dateConsult.getMois(), dateConsult.getAnnee(),
                    dateConsult.getHeure(), dateConsult.getMinute());//Format date
                
                String actesStr = c.getActes().size() + " acte(s)";//Resume actes
                
                Object[] ligne = {
                    dateStr,
                    c.getPatient().getNomComplet(),
                    c.getCategorie().getDesignation(),
                    String.format("%.2f€", c.coutTotal()),
                    actesStr
                };//Donnees
                modele.addRow(ligne);//Ajout
            }
        } catch (Exception e) {
            e.printStackTrace();//Erreur
        }
        
        return panel;//Retour
    }

    // =========================================================
    // ONGLET 5 : STATISTIQUES
    // =========================================================
    
    private JPanel creerPanelStatistiques() {
        JPanel panel = new JPanel(new BorderLayout());//Layout
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));//Marges
        
        JLabel lblTitre = new JLabel("Statistiques mensuelles", SwingConstants.CENTER);//Titre
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));//Style
        panel.add(lblTitre, BorderLayout.NORTH);//Haut
        
        // Zone de texte pour les stats
        JTextArea txtStats = new JTextArea();//TextArea
        txtStats.setEditable(false);//No edit
        txtStats.setFont(new Font("Segoe UI", Font.PLAIN, 14));//Police
        
        // Calculer les stats
        try {
            Calendar cal = Calendar.getInstance();//Calendar
            models.Date dateDebut = new models.Date(1, cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));//Debut mois
            
            int dernierJour = cal.getActualMaximum(Calendar.DAY_OF_MONTH);//Dernier jour
            models.Date dateFin = new models.Date(dernierJour, cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));//Fin mois
            
            List<Consultation> consultations = consultationController.getConsultationsByMedecinEtPeriode(
                medecin, dateDebut, dateFin);//Get consults periode
            
            double chiffreAffaires = 0;
            for (Consultation c : consultations) {
                chiffreAffaires += c.coutTotal();//Somme
            }
            
            StringBuilder stats = new StringBuilder();//Builder
            stats.append("═══════════════════════════════════════\n");
            stats.append("     STATISTIQUES DU MOIS EN COURS\n");
            stats.append("═══════════════════════════════════════\n\n");
            stats.append("Période : ").append(dateDebut.getJour()).append("/")
                 .append(dateDebut.getMois()).append(" - ").append(dateFin.getJour()).append("/")
                 .append(dateFin.getMois()).append("/").append(dateFin.getAnnee()).append("\n\n");
            stats.append("Nombre de consultations : ").append(consultations.size()).append("\n\n");
            stats.append("Chiffre d'affaires : ").append(String.format("%.2f€", chiffreAffaires)).append("\n\n");
            
            if (consultations.size() > 0) {
                double moyenne = chiffreAffaires / consultations.size();//Moyenne
                stats.append("Prix moyen consultation : ").append(String.format("%.2f€", moyenne)).append("\n\n");
            }
            
            stats.append("═══════════════════════════════════════\n");
            
            txtStats.setText(stats.toString());//Affichage
            
        } catch (Exception e) {
            txtStats.setText("Erreur chargement statistiques");//Erreur
            e.printStackTrace();
        }
        
        JScrollPane scrollStats = new JScrollPane(txtStats);//Scroll
        panel.add(scrollStats, BorderLayout.CENTER);//Centre
        
        return panel;//Retour
    }

    // =========================================================
    // MÉTHODES UTILITAIRES
    // =========================================================
    
    /**
     * Affiche le profil du médecin
     */
    private void afficherProfil() {
        StringBuilder profil = new StringBuilder();//Texte
        profil.append("══════════════════════════════\n");
        profil.append("        MON PROFIL\n");
        profil.append("══════════════════════════════\n\n");
        profil.append("Dr. ").append(medecin.getNom()).append(" ").append(medecin.getPrenom()).append("\n");
        profil.append("Spécialité : ").append(medecin.getSpecialite()).append("\n");
        profil.append("Login : ").append(medecin.getLogin()).append("\n");
        
        JOptionPane.showMessageDialog(this, 
            profil.toString(),
            "Mon Profil", 
            JOptionPane.INFORMATION_MESSAGE);//Popup
    }
    
    // --- METHODES STYLE ---
    private void appliquerStyle(JTextField field, String titre) {
        field.setFont(POLICE_TEXTE);//Police
        field.setOpaque(false); //Transp
        field.setBackground(new Color(0, 0, 0, 0)); //Bg
        field.setForeground(Color.WHITE); //Fg
        field.setHorizontalAlignment(JTextField.CENTER);//Centre
        field.setBorder(BorderFactory.createTitledBorder(
                new RoundedBorder(Color.WHITE, 20), titre,
                TitledBorder.CENTER, TitledBorder.TOP, POLICE_TITRE, Color.WHITE));//Bordure
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);//Bouton
        button.setFont(POLICE_TITRE);//Police
        button.setForeground(color);//Couleur
        button.setFocusPainted(false);//No focus
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));//Main
        button.setBorder(new RoundedBorder(color, 20)); //Rond
        button.setContentAreaFilled(false); //Transp
        return button;//Retour
    }
    
    static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;
        RoundedBorder(Color c, int r) { this.color = c; this.radius = r; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//Lissage
            g.setColor(color);//Couleur
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);//Dessin
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(radius/2, radius/2, radius/2, radius/2); }//Marge
        @Override public Insets getBorderInsets(Component c, Insets i) { return getBorderInsets(c); }
    }
}
