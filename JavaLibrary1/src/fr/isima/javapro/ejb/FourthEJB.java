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
import fr.isima.javapro.entity.Item;
import fr.isima.javapro.persistence.EntityManager;

@Statefull
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class FourthEJB implements FourthEJBLocal{
    
    @PersistenceContext
    private EntityManager em;
        
    @Override
    public void addRequired(Item item){
        em.persist(item);
        float i = 0/0;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void addRequiredNew(Item item){
        em.persist(item);
        float i = 0/0;
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