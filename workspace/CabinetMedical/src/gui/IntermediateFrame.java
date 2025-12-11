package gui;

import javax.swing.*;
import models.*;
import java.awt.*;

//IntermediateFrame EST une fenetre. 
//Elle possede toutes les capacites d'une fenetre Windows classique.
public class IntermediateFrame extends JFrame {
    
    private Utilisateur utilisateur;
    
    public IntermediateFrame(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("/home/hathouti/Bureau/UIR/3A/S5/POO/Projet/LOGO_APP.jpg"));
        // ============================
        // Configuration de la fenetre
        // ============================
        this.setTitle("Gestion Hospitalière - Accueil");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null); 
        this.setLayout(new GridBagLayout()); // On garde ton centrage
        
        // ============================
        // TEXT A AFFICHER
        // ============================
        JLabel label = new JLabel("Bienvenue " + utilisateur.getNomComplet() + " !");
        label.setFont(new Font("Segoe UI", Font.BOLD, 30));
        label.setForeground(new Color(33, 37, 41));
        //On place ca au centre avec un encrage (anchor)
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;//Colonne 0
        gbc.gridy = 0;//Ligne 0
        gbc.anchor = GridBagConstraints.CENTER;//On encre le texte / bloc au centre
        gbc.insets = new Insets(50, 0, 10, 0);//POUR espacer les 2 textes
        this.add(label, gbc);//On applique les regles
        
        JLabel label_chargement = new JLabel("Chargement....");
        label_chargement.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        label_chargement.setForeground(new Color(33, 37, 41));
        //On place ca au centre avec un encrage (anchor)
        gbc.gridx = 0;//Colonne 0
        gbc.gridy = 1;//Ligne 1
        //Force la flottaison au milieu de l'espace disponible ici le content pane
        gbc.anchor = GridBagConstraints.SOUTH;//On encre le texte bloc en bas
        this.add(label_chargement, gbc);//On applique les regles
        
        // =======================================
        // LE MINUTEUR (TIMER) POUR LA TRANSITION 
        // =======================================
        
        // 950 ms = 0.95 secondes
        //e Est l'evenement Timer arrive a la fin
        Timer timer = new Timer(950, e -> {
            
            // On ferme cette fenetre qui est juste Intermediate
            this.dispose(); 
            
            switch (utilisateur.getRole()) {
                case "MEDECIN":
                    // On caste l'utilisateur en Medecin et on ouvre MedecinFrame
                    new MedecinFrame((Medecin) utilisateur);
                    break;
                    
                case "PATIENT":
                    // On caste l'utilisateur en Patient et on ouvre PatientFrame
                    new PatientFrame((Patient) utilisateur);
                    break;
                    
                case "ASSISTANTE":
                    // On caste l'utilisateur en Assistante et on ouvre AssistanteFrame
                    new AssistanteFrame((Assistante) utilisateur);
                    break;
                    
                default:
                    // Si le role est inconnu, on affiche une erreur
                    JOptionPane.showMessageDialog(null, "Rôle inconnu : " + utilisateur.getRole());
                    break;
            }
        });
        
        // IMPORTANT : On dit au timer de ne s'executer qu'une SEULE fois.
        // Sinon, il va rouvrir les Frames toutes les 0.95 secondes en boucle !
        /*NOTE IMPORTANT !!! 
        Par defaut, un Timer en Java est concu pour etre rEpetitif (cExample un clignotant de voiture).
        */
        timer.setRepeats(false);
        // On demarre le chrono
        timer.start();
        
        // ============================
        this.setVisible(true);
    }
}
