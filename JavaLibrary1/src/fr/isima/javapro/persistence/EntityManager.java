/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.persistence;

import fr.isima.javapro.entity.Item;
import fr.isima.javapro.exception.JPAException;

public class EntityManager {
    
    private static final Database database = Database.getInstance();
       
    /**
     * Sauvegarde l'item en base.
     * @param item
     */
    public void persist(Item item){
        if (item.getId() != null) throw new JPAException("Entity Id is not null");
        database.insert(item);
    }

    /**
     * Met à jour l'item en base. On ne retourne  
     * pas l'item car on travaille en local.
     * @param item
     */
    public void merge(Item item){
        if (!isManaged(item)) throw new JPAException("Entity item is not managed.");
        database.update(item);
    }

    /**
     * Supprime l'item de la base de donnée.
     * @param item
     */
    public void remove(Item item){
        if (!isManaged(item)) throw new JPAException("Entity item is not managed.");
        database.delete(item);
    }    
    
    public void clear(){
        database.clear();
    }  
    
    /**
     * Recherche l'objet ayant l'id spécifié.
     * @param id
     * @return
     */
    public Item find(Long id){
        return database.select(id);        
    }
    
    public int count(){
        return database.count();
    } 
    
    /**
     * Vérifie qu'un item est managé. Celà revient à 
     * comparer les références entre items de même id.
     * @param item
     * @return 
     */
    private boolean isManaged(Item item){
        Item i = database.select(item.getId());
        return (i == item);        
    }
}