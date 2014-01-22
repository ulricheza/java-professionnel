/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro;

import fr.isima.javapro.annotation.EJB;
import fr.isima.javapro.annotation.PersistenceContext;
import fr.isima.javapro.ejb.FourthEJBLocal;
import fr.isima.javapro.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPersistenceContext {
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private FourthEJBLocal ejb;
    
    @Before
    public void setUp() {
        EJBContainer.getInstance().inject(this);
    }
    
    @After
    public void tearDown() {
         EJBContainer.getInstance().close();
    }
    
    @Test
    public void persistenceContext(){
        assert(em instanceof EntityManager);
        assert(ejb.count() == 0);
        
        em.persist(new Object());
        assert(ejb.count() == 1);
        
        ejb.remove();
        assert(em.count() == 0);
    }
}