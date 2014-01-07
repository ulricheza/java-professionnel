/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.ejb;

import fr.isima.javapro.annotation.EJB;
import fr.isima.javapro.annotation.PersistenceContext;
import fr.isima.javapro.annotation.Statefull;
import fr.isima.javapro.persistence.EntityManager;

@Statefull
public class FifthEJB implements FifthEJBLocal {
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private FourthEJBLocal ejb;
    
    @Override
    public void addRequired(Object item){
        em.add(item);
        ejb.addRequired(item);
    }
    
    @Override
    public void addRequiredNew(Object item){
        em.add(item);
        ejb.addRequiredNew(item);
    }
    
    @Override
    public void clear(){
        em.clear();
    }
}