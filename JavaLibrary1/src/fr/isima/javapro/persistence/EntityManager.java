/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.persistence;

public class EntityManager {
    
    private static final Database database = Database.getInstance();
    
    public void add (Object item){
        database.add(item);
    }
    
    public Object[] getAllItems(){
        return database.getAllItems();
    }
    
    public int count(){
        return database.count();
    }
    
    public void clear(){
        database.clear();
    }
}