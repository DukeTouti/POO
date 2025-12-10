# Système de Gestion de Cabinet Médical

## Description du Projet

Application desktop Java pour la gestion complète d'un cabinet médical, inspirée du système Améli (Sécurité Sociale française). Le système permet la gestion des rendez-vous, consultations médicales avec codes NGAP, et offre trois interfaces distinctes selon le profil utilisateur.

### Objectifs

- Gestion des rendez-vous patients (comme Doctolib)
- Consultation médicale avec actes NGAP
- Suivi médical et historique patient
- Statistiques et bilans mensuels
- Interface multi-utilisateurs (Patient, Assistante, Médecin)

---

## Architecture du Projet

### Structure des Packages

```
CabinetMedical/
│
├── src/
│   ├── models/                                   # Entités et classes métier
│   │   ├── Utilisateur.java                      # Implemented
│   │   ├── Medecin.java                          # Implemented
│   │   ├── Assistante.java                       # Implemented
│   │   ├── Patient.java                          # Implemented
│   │   ├── Consultation.java                     # Implemented
│   │   ├── Keyboard.java                         # Implemented
│   │   ├── Categorie.java                        # Implemented
│   │   ├── StatutRDV.java                        # Implemented
│   │   ├── RendezVous.java                       # Implemented
│   │   ├── TestModels.java                       # Implemented
│   │   ├── Acte.java                             # Imlpemented
│   │   ├── Code.java                             # Implemented
│   │   └── Date.java                             # Implemented
│   │
│   ├── controllers/                              # Logique métier
│   │   ├── AuthController.java
│   │   ├── PatientController.java
│   │   ├── ConsultationController.java
│   │   └── RendezVousController.java
│   │
│   ├── gui/                                      # Interfaces graphiques
│   │   ├── LoginFrame.java
│   │   │
│   │   ├── patient/                              # Interface Patient
│   │   │   ├── PatientMainFrame.java
│   │   │   ├── PrendreRDV.java
│   │   │   ├── MesRDVPanel.java
│   │   │   └── HistoriquePanel.java
│   │   │
│   │   ├── assistante/                           # Interface Assistante
│   │   │   ├── AssistanteMainFrame.java
│   │   │   ├── GestionRDVPanel.java
│   │   │   ├── GestionPatientsPanel.java
│   │   │   └── ValidationPaiementPanel.java
│   │   │
│   │   └── medecin/                              # Interface Médecin
│   │       ├── MedecinMainFrame.java
│   │       ├── ConsultationForm.java
│   │       ├── FicheSoins.java
│   │       ├── ConsultationsJourPanel.java
│   │       └── StatistiquesPanel.java
│   │
│   └── Main.java                                 # MAIN

```

---

## Contribution

### Équipe de Développement

- Étudiant 1 : [HATHOUTI Mohammed Taha]
- Étudiant 2 : [JIDAL Ilyas]  
- Étudiant 3 : [KABORE Mohammed Sharif Jonathan]

---

Projet académique - ESIN 3A - UIR - 2025

**Dernière mise à jour** : 04 Décembre 2025
