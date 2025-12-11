package main;

import gui.LoginFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("=== DÉMARRAGE APPLICATION ===");
        System.out.println("========================================");
        
        SwingUtilities.invokeLater(() -> {
            System.out.println("=== Création LoginFrame ===");
            new LoginFrame();
            System.out.println("=== LoginFrame créé avec succès ===");
        });
        
        System.out.println("=== Application lancée ===");
    }
}