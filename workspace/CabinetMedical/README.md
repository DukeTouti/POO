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
│   │   ├── Utilisateur.java
│   │   ├── Medecin.java
│   │   ├── Assistante.java
│   │   ├── Patient.java
│   │   ├── Consultation.java
│   │   ├── Keyboard.java
│   │   ├── Categorie.java
│   │   ├── StatutRDV.java
│   │   ├── RendezVous.java
│   │   ├── TestModels.java
│   │   ├── Acte.java
│   │   ├── Code.java
│   │   └── Date.java
│   │
│   ├── controllers/                              # Logique métier
│   │   ├── AuthController.java
│   │   ├── PatientController.java
│   │   ├── ConsultationController.java
│   │   ├── CategorieController.java
│   │   ├── MedecinController.java
│   │   ├── AssistanteController.java
│   │   └── RendezVousController.java
│   │
│   ├── gui/                                      # Interfaces graphiques
│   │   ├── LoginFrame.java
│   │   ├── IntermediateFrame.java
│   │   ├── PatientFrame.java
│   │   ├── AssistanteFrame.java
│   │   └── MedecinFrame.java
│   │
│   └── Main.java                                 # MAIN

```

---

## Contribution

### Équipe de Développement

- Étudiant 1 : HATHOUTI Mohammed Taha
- Étudiant 2 : JIDAL Ilyas  
- Étudiant 3 : KABORE Mohammed Sharif Jonathan

---

Projet académique - ESIN 3A - UIR - 2025

**Dernière mise à jour** : 11 Décembre 2025
