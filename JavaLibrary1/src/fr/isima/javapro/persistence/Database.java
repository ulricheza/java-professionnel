/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.persistence;

import fr.isima.javapro.entity.Item;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cette classe représente notre base de données. Les ordres SQL (rollback,commit,select,etc...)
 * y sont implémentés et se veulent de représenter les fonctionnalités d'une vraie 
 * base de données.
 * 
 * Ayant qu'un seul EJB entity ({@link fr.isima.javapro.entity.Item}) dans l'application
 * et étant donné que la table correspondante serait, dans le cas normal, générée
 * automatiquement par JPA, nous l'avons représenté ici par la map {@link Database#items} items.
*/
public class Database {
    
    private static Database INSTANCE;
    
    private long nextItemId = 1;
    private final Map<Long,Item> items;
    private final List<Item> rollbackSegment;
    private final Deque<Item[]> suspendedTransactions;    
    
    public static Database getInstance(){
        if (INSTANCE == null)
            INSTANCE = new Database();
       
        return INSTANCE;
    }
    
    private Database(){
        items = new HashMap<>();
        rollbackSegment = new ArrayList<>();
        suspendedTransactions = new ArrayDeque<>();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Ordres SQL">
    /**
     * Equivalent de "INSERT INTO Item values(...)".
     * @param item 
     */
    public void insert(Item item){
        item.setId(nextItemId++);
        items.put(item.getId(), item);
    }
    
    /**
     * Equivalent de "UPDATE Item SET .. WHERE id=item.id".
     * @param item 
     */
    public void update(Item item){
        items.put(item.getId(), item);
    }
    
    /**
     * Equivalent de "DELETE FROM Item WHERE id=item.id"
     * @param item 
     */
    public void delete(Item item){
        items.remove(item.getId());
    }
    
    /**
     * Equivalent à "DELETE FROM Item".
     */
    public void clear(){
        items.clear();
    }
    
    /**
     * Equivalent de "SELECT FROM Item WHERE id=id"
     * @param id
     * @return 
     */
    public Item select (Long id){
        return items.get(id);
    }
    
    /**
     * Equivalent de "SELECT COUNT(*) FROM Item"
     * @return 
     */
    public int count(){
        return items.size();
    }
        
    /**
     * Equivalent de "ROLLBACK".
     */
    public void rollback(){
        items.clear();
        
        for (Item item : rollbackSegment)
            items.put(item.getId(), item);
        
        int size = rollbackSegment.size();
        if (size > 0)
            nextItemId = rollbackSegment.get(size-1).getId()+1;
        else
            nextItemId = 1;
    }
    
    /**
     * Equivalent de "COMMIT WORK".
     */
    public void commit(){
        rollbackSegment.clear();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Gestion de transactions">
    public void openTransaction(){
        rollbackSegment.clear();
        rollbackSegment.addAll(Arrays.asList(items.values().toArray(new Item[0])));
    }
    
    public void suspendTransaction(){
        suspendedTransactions.addFirst(rollbackSegment.toArray(new Item[0]));
    }
    
    public void resumeTransaction(){
        rollbackSegment.clear();
        rollbackSegment.addAll(Arrays.asList(suspendedTransactions.removeFirst()));
    } 
    // </editor-fold>
}