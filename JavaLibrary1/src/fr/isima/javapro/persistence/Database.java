/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.persistence;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class Database {
    
    private static Database INSTANCE;
    
    private final List<Object> items;
    private final List<Object> rollbackSegment;
    private final Deque<Object[]> suspendedTransactions;    
    
    public static Database getInstance(){
        if (INSTANCE == null)
            INSTANCE = new Database();
       
        return INSTANCE;
    }
    
    private Database(){
        items = new ArrayList<>();
        rollbackSegment = new ArrayList<>();
        suspendedTransactions = new ArrayDeque<>();
    }
    
    // <editor-fold defaultstate="collapsed" desc="SQL Commands">
    public void insert(Object item){
        items.add(item);
    }
    
    public void clear(){
        items.clear();
    }
    
    public int count(){
        return items.size();
    }
    
    public void rollback(){
        items.clear();        
        items.addAll(rollbackSegment);
    }
    
    public void commit(){
        rollbackSegment.clear();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Transactions management">
    public void openTransaction(){
        rollbackSegment.clear();
        rollbackSegment.addAll(items);
    }
    
    public void suspendTransaction(){
        suspendedTransactions.addFirst(rollbackSegment.toArray());
    }
    
    public void resumeTransaction(){
        rollbackSegment.clear();
        rollbackSegment.addAll(Arrays.asList(suspendedTransactions.removeFirst()));
    } 
    // </editor-fold>
}