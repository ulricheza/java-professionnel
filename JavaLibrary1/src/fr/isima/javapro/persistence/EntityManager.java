/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ulrich EZA
 */
public class EntityManager {
    
    private static final List<Object> database = new ArrayList<>();
    
    public void add (Object item){
        database.add(item);
    }
    
    public List<Object> getAllItems(){
        return database;
    }
    
    public int count(){
        return database.size();
    }
    
    public void clear(){
        database.clear();
    }
}
