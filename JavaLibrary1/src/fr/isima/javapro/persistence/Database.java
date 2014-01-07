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
    
    private final List<Object> datas;  
    private final List<Object> rollbackSegment;
    private final Deque<Object[]> suspendedTransactions;
    private static Database INSTANCE;
    
    public static Database getInstance(){
        if (INSTANCE == null)
            INSTANCE = new Database();
       
        return INSTANCE;
    }
    
    private Database(){
        datas = new ArrayList<>();
        rollbackSegment = new ArrayList<>();
        suspendedTransactions = new ArrayDeque<>();
    }
        
    public void add(Object item){
        datas.add(item);
    }
    
    public void clear(){
        datas.clear();
    }
    
    public int count(){
        return datas.size();
    }
    
    public Object[] getAllItems(){
        return datas.toArray();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Transactions Management">
    public void openTransaction(){
        rollbackSegment.clear();
        rollbackSegment.addAll(datas);
    }
    
    public void suspendTransaction(){
        suspendedTransactions.addFirst(rollbackSegment.toArray());
    }
    
    public void resumeTransaction(){
        rollbackSegment.clear();
        rollbackSegment.addAll(Arrays.asList(suspendedTransactions.removeFirst()));
    }
    
    public void rollback(){
        datas.clear();
        datas.addAll(rollbackSegment);
    }
    
    public void commit(){
        rollbackSegment.clear();
    }
    // </editor-fold>
}