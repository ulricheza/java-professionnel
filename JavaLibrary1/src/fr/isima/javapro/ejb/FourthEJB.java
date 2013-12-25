/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.ejb;

import fr.isima.javapro.annotation.PersistenceContext;
import fr.isima.javapro.annotation.Remove;
import fr.isima.javapro.annotation.Statefull;
import fr.isima.javapro.persistence.EntityManager;
import javax.annotation.PostConstruct;

/**
 *
 * @author Ulrich EZA
 */
@Statefull
public class FourthEJB implements FourthEJBLocal{
    
    @PersistenceContext
    private EntityManager em;
    
    @PostConstruct
    public void initialize(){
        em.add("First Item");
    } 
    
    @Override
    public void add(Object item){
        em.add(item);
    }
    
    @Override
    public int count(){
        return em.count();
    }
    
    @Remove
    @Override
    public void remove(){
        em.clear();
    }    
}