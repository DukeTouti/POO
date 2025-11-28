/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ma.ac.uir.tp7;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Najib
 */
public class Participant {
    
    private String cne;
    private String nom;
    private String prenom; 
    private String profession; 
    private String civilite; 
    private String email;
    private int age; 
    
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public Participant() {
    }
    
    public String getCne() {
        return cne;
    }

    
    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getProfession() {
        return profession;
    }

    public String getCivilite() {
        return civilite;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCne(String cne) {
        this.cne = cne;
    }
    

    public Participant(String cne, String nom, String prenom, String profession, String civilite, String email, int age) {
        this.cne = cne;
        this.nom = nom;
        this.prenom = prenom;
        this.profession = profession;
        this.civilite = civilite;
        this.email = email;
        this.age = age;
    }
    
    public Participant(int id, String cne, String nom, String prenom, String profession, String civilite, String email, int age) {
        this.id = id;
        this.cne = cne;
        this.nom = nom;
        this.prenom = prenom;
        this.profession = profession;
        this.civilite = civilite;
        this.email = email;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Participant{" + "cne=" + cne + ", nom=" + nom + ", prenom=" + prenom + ", profession=" + profession + ", civilite=" + civilite + ", email=" + email + ", age=" + age + '}';
    }
    // Méthodes JDBC 
    // Ajouter 
    public boolean addParticipant() {
        boolean res = false;

        return res;
    }
    
    //récupérer la liste des particpants
    public static LinkedList<Participant> getAllParticipants() {

        LinkedList<Participant> listeP = new LinkedList<>();

        return listeP;
    }
    //Supprimer un etudiant
     public static boolean supprimerParticipant(int id) {
        boolean res = false;

        return res;
    }
        
    // MAJ d'un particpant 
    public boolean updateParticpant() {
        boolean res = false;

        return res;
    }
    
    public static DefaultTableModel buildTableModel(ResultSet rs)
        throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }
        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }

}
