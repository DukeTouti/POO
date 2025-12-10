package models;

/* ************************************* *
 * Enum représentant les statuts         *
 * possibles d'un rendez-vous            *
 * ************************************* */

public enum StatutRDV {
	
	PLANIFIE,	/* RDV créé, en attente de confirmation */
	CONFIRME,	/* RDV confirmé par le patient */
	ANNULE,		/* RDV annulé */
	TERMINE		/* RDV terminé (consultation effectuée) */
	
}