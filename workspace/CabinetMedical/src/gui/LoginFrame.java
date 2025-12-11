package gui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.Toolkit;
import controllers.AuthController;
import models.*;

//LoginFrame EST une fenetre. 
//Elle possede toutes les capacites d'une fenetre Windows classique.
@SuppressWarnings({ "unused", "serial" })
public class LoginFrame extends JFrame {

    /*
     * this.dispose(); // Ferme le login new HomeFrame(); // Ouvre l'accueil
     */

    //Controller d'authentification
    private AuthController authController;

    //On definit les styles ici en utilisant des constantes globales. (static final)
    //Utile pour des changement rapide en dehors des fonctions elle memes
    static final Color FOND_TRANSPARENT = new Color(255, 255, 255, 200);
    static final Font POLICE_TITRE = new Font("Segoe UI", Font.BOLD, 14);
    static final Font POLICE_TEXTE = new Font("Segoe UI", Font.PLAIN, 14);

    public LoginFrame() {
        //Initialiser le controller
        this.authController = new AuthController();

        // ============================
        // Configuration de la fenetre
        // ============================

        this.setIconImage(
                Toolkit.getDefaultToolkit().getImage("/home/hathouti/Bureau/UIR/3A/S5/POO/Projet/LOGO_APP.jpg"));
        this.setTitle("Login");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Tue le programme si ferme (la croix)
        this.setSize(1000, 700);//Dimmensions de la fenetre
        /*
        Centre la fenetre sur l'ecran, nice pour l'affichage et le pop Pas de
        composant de reference, donc la fenetre se centre automatiquement sur l'écran
        principal.
        */
        this.setLocationRelativeTo(null);

        // =============================
        // Installation du Fond d'ecran
        // =============================
        //On utilise notre class BackgroundPanel
        BackgroundPanel mainPanel = new BackgroundPanel("/home/hathouti/Bureau/UIR/3A/S5/POO/Projet/FOND.jpg");
        mainPanel.setLayout(new GridBagLayout());//Organise les elements comme une grille intelligente centree
        this.setContentPane(mainPanel);//On rempalce le ContentPane de base

        // ===============================================================================
        // CONFIGURATION DU LAYOUT (avec gestionnaire de disposition GridBagLayout)
        // ===============================================================================

        //GridBagConstraints sert a positionner et organiser les 3 elements du formulaire de connexion
        //Comme une "regle/objet de configuration" on ecrase les reglages standards
        //(centre, pas d'etirement, pas de marge).
        GridBagConstraints gbc = new GridBagConstraints();//permet de definir comment chaque composant doit être positionne
        gbc.insets = new Insets(15, 0, 15, 0);//Espacement vertical de 15px en haut ains qu'en bas (padding -> (Top, Left, Bottom, Right)
        gbc.gridx = 0; //Tous les composants sont dans la meme colonne (ici la colonne 0)
        gbc.fill = GridBagConstraints.HORIZONTAL;//Les champs ont un etirement horizontale
        gbc.fill = GridBagConstraints.VERTICAL;//Les champs ont un etirement verticale

        // =====================
        // CREATION DES CHAMPS
        // =====================

        //CHAM NOM
        JTextField champNom = new JTextField(20);
        appliquerStyle(champNom, "Nom d'utilisateur");//On appelle la fonction pour le style
        gbc.gridy = 0; //0 donc premier ligne de notre grille GridBagLayout
        this.add(champNom, gbc);//On ajoute le champ a la fenetre avec les regles gbc

        //gbc.fill = GridBagConstraints.xxxx;
        //gbc.weighty gbc.weight

        //CHAM MOT DE PASSE (MDP)
        JPasswordField champMdp = new JPasswordField(20);
        appliquerStyle(champMdp, "Mot de passe");//On appelle la fonction pour le style
        gbc.gridy = 1;//1 donc deuxieme ligne de notre grille GridBagLayout
        this.add(champMdp, gbc);//On ajoute le champ a la fenetre avec les regles gbc

        // =========================================================
        // AJOUT DU CHECKBOX POUR AFFICHER LE MDP
        // =========================================================

        JCheckBox checkShowPass = new JCheckBox("Afficher le mot de passe");
        checkShowPass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        checkShowPass.setForeground(Color.WHITE); //Texte en blanc pour le contraste
        checkShowPass.setOpaque(false); //Fond transparent pour voir l'image

        //On sauvegarde le caractere cache ou a cache (charactere d'avant)
        char defaultChar = champMdp.getEchoChar();

        checkShowPass.addActionListener(e -> {
            if (checkShowPass.isSelected()) {
                //Si coche on enleve le caractere de masquage (affiche le texte clair donc)
                champMdp.setEchoChar((char) 0);
            } else {
                //Si decoche on remet le caractere de masquage
                champMdp.setEchoChar(defaultChar);
            }
        });

        //Configuration specifique pour le placement du checkbox
        GridBagConstraints gbcCheck = new GridBagConstraints();
        gbcCheck.gridx = 0;
        gbcCheck.gridy = 2; //Ligne 2 (Juste sous le MDP)
        gbcCheck.anchor = GridBagConstraints.CENTER; //On colle le texte à droite
        gbcCheck.insets = new Insets(0, 0, 10, 0); //Petit espacement

        this.add(checkShowPass, gbcCheck);

        // =========================================================
        // AJOUT DU BOUTON DE CONNEXION
        // =========================================================

        //Création d'un bouton de connexion
        JButton btnLogin = new JButton("ACCEDER") {//On modifie le "pinceau" ou facon de draw le bouton
            @Override //On ecrase la fonction de draw de base par defaut
            protected void paintComponent(Graphics g) {
                //Graphics2D est la version plus recente de l'outil de "dessin" on "cast" en quelque sotte
                //g.create() permet de passer une copie temporaire du pinceau g
                Graphics2D g2 = (Graphics2D) g.create();//g est ici l outil de dessin standard de java
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//lissage
                g2.setColor(new Color(255, 255, 255, 50)); //Alpha de 50
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20); //dessines un rectangle avec des coins arrondis
                g2.dispose();//On ferme la configuration du "Pinceau"
                //On "passe la main" au pinceau standard JButton
                super.paintComponent(g);//Aapres la finalisation on laisse la main pour que le systeme puisse ecrire par dessus
            }
        };

        //Style du bouton
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);//Empeche le cadre gris de highlight d'apparaitre
        btnLogin.setBorder(new RoundedBorder(Color.WHITE, 20));
        btnLogin.setContentAreaFilled(false);//Empeche la peinture du fonc en blanc
        //On cree une nouvelle isntance de type curseur
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));//Change souris quand elle survole le bouton

        //ACTION :On clique On change de page
        //e represente l'evenement click
        //-> consequence
        btnLogin.addActionListener(e -> {
            System.out.println("========================================");
            System.out.println("=== BOUTON ACCEDER CLIQUÉ ===");
            System.out.println("========================================");
            
            String login = champNom.getText().trim();
            String mdp = new String(champMdp.getPassword());
            
            System.out.println("Login récupéré du champ: '" + login + "'");
            System.out.println("MDP récupéré du champ: '" + mdp + "'");
            System.out.println("Longueur login: " + login.length());
            System.out.println("Longueur mdp: " + mdp.length());
            
            System.out.println("Appel authController.authentifier()...");
            
            //Authentifier avec le controller
            Utilisateur utilisateur = authController.authentifier(login, mdp);
            
            System.out.println("Résultat authentification: " + (utilisateur != null ? "SUCCÈS" : "ÉCHEC"));

            if (utilisateur != null) {
                System.out.println("Utilisateur trouvé: " + utilisateur.getNomComplet());
                System.out.println("Role: " + utilisateur.getRole());
                
                //On passe directement l'utilisateur complet à IntermediateFrame
                this.dispose(); //On ferme la fenetre actuelle
                new IntermediateFrame(utilisateur); //On passe l'objet Utilisateur complet
            } else {
                System.out.println("Affichage message d'erreur...");
                JOptionPane.showMessageDialog(this, "Nom d'utilisateur ou mot de passe incorrect.",
                        "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
            }
        });

        //Placement du bouton
        gbc.gridy = 3; //En dessous du mot de passe (et du checkbox)
        gbc.gridx = 0; //En dessous du mot de passe
        gbc.insets = new Insets(30, 0, 15, 0); //Un peu d'espace au dessus
        gbc.fill = GridBagConstraints.HORIZONTAL;//Le champ a un etirement horizontale
        gbc.fill = GridBagConstraints.VERTICAL;//Le champ a un etirement verticale
        //Sans ces 2 lignes bouton aura la taille exacte du texte (on veut autre chose)
        gbc.ipadx = 40; //Un peu plus large
        gbc.ipady = 10; //Un peu plus haut

        this.add(btnLogin, gbc);//On rajoute le bouton avec les regles que l'on a definit

        //On rend la fenetre visible a la fin des modification pour eviter toute erreur
        //et afficher le bouton
        this.setVisible(true);

    }

    // =========================================================
    // METHODES ET CLASSES UTILITAIRES
    // =========================================================

    // =========================================================
    // Methode appliquerStyle pour les 2 champs
    // =========================================================

    //On evite de refaire ca pour chacun des zones de saisit...
    //Prend un champ "vide" (field) et un titre (titre)et le transforme en champ
    //transparent avec une bordure.
    //Private car c est que pour la fenetre de login
    private void appliquerStyle(JTextField field, String titre) {
        field.setFont(POLICE_TEXTE);
        field.setOpaque(false); //Pour que les boutons soient transparents
        field.setBackground(new Color(0, 0, 0, 0)); //On n'est jamais sur (d'ou le alpha a 0)
        field.setForeground(Color.BLACK); //Texte noir pour contraste
        field.setHorizontalAlignment(JTextField.CENTER);//Force le curseur au milieu

        //On cree un bordure complexe
        //createTitledBorder : On demande une bordure qui contient du texte (un titre)
        //incruste dans le trait
        field.setBorder(BorderFactory.createTitledBorder(new RoundedBorder(Color.WHITE, 20), titre, //Texte a afficher..
                TitledBorder.CENTER, //le titre est centre horizontalement
                TitledBorder.TOP, //le titre est place sur la bordure hautew (va couper la bordure)
                POLICE_TITRE, //La police du titre que l on a passe avant en parametre
                Color.WHITE//lA COULEUR DU TITRE
        ));
    }

    // =========================================================
    // Class RoundedBorder pour les 2 champs et le boutons
    // =========================================================

    //Static car interne et n'as pas besoin de connaitre le reste de la fenetre
    static class RoundedBorder extends AbstractBorder {//Herite de bordure car on va modifier que l'apparence
        private final Color color; //La couleur de la courbure
        private final int radius;//La courbure

        RoundedBorder(Color c, int r) {
            this.color = c;
            this.radius = r;
        }//Constructeur

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(color);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        public Insets getBorderInsets(Component c, Insets i) {
            return getBorderInsets(c);
        }
    }

    // =========================================================
    // Class BackgroundPanel pour l'image de fond
    // =========================================================

    //Static car interne et n'as pas besoin de connaitre le reste de la fenetre
    static class BackgroundPanel extends JPanel {////Herite de bordure car on va "ameliorer"
        private Image img;//private car pas besoin d'etre connu en dehors

        BackgroundPanel(String path) {
            this.img = new ImageIcon(path).getImage();
        }//Transforme le fichier en object Image Brut

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);//On appelle la methode du parent
            //On dessine l'image avec les parametre passe
            //getWidth(), getHeight() pour redimensionnement dynamique et automatique
            if (img != null)
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);//Securite pour evite crash absolut
        }
    }

}

/*
 * Image est une class abstraite ImageIcon n'est pas abstraite elle contient :
 * * 1. Ouvrir le disque dur.
 * * 2. Lire les octets du fichier.
 * * 3. Comprendre si c'est un JPG ou un PNG ou un autre format etc....
 * * 4. Fabriquer l'objet Image compatible....etc...
 */
