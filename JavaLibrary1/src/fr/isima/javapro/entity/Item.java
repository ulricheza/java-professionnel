/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.entity;

/**
 * Cette classe représente un EJB entity. Notre programme ne gérant pas
 * les annotations de ce type d'EJB, on utilisera un object Java normal.<br/>
 * On supposera également que : <br/>
 * <ul>
 *  <li>{@link Item#id} est annoté avec @Id et @GeneratedValue.</li>
 *  <li>{@link Item#version} est annoté avec @Version.</li>
 * </ul>
*/
public class Item {
    
    private Long id;
    private int version;
    private String nom;
    private int price;

    public Item(){
        
    }
    
    public Item(Long id){
        this.id = id;
    }
    
    public Item(Long id, int version, String nom, int price) {
        this.id = id;
        this.nom = nom;
        this.version = version;
        this.price = price;
    }    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}