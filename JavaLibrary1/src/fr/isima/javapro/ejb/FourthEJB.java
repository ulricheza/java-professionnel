/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.ejb;

import fr.isima.javapro.annotation.PersistenceContext;
import fr.isima.javapro.annotation.Remove;
import fr.isima.javapro.annotation.Statefull;
import fr.isima.javapro.annotation.TransactionAttribute;
import fr.isima.javapro.annotation.TransactionAttributeType;
import fr.isima.javapro.persistence.EntityManager;
import javax.annotation.PostConstruct;

@Statefull
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class FourthEJB implements FourthEJBLocal{
    
    @PersistenceContext
    private EntityManager em;
    
    @PostConstruct
    public void initialize(){
        em.add("First Item");
    } 
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void addRequired(Object item){
        em.add(item);
        int i = 0/0;
    }
    
    @Override
    public void addRequiredNew(Object item){
        em.add(item);
        int i = 0/0;
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