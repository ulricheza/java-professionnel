/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro;

import fr.isima.javapro.annotation.EJB;
import fr.isima.javapro.annotation.PersistenceContext;
import fr.isima.javapro.ejb.FifthEJBLocal;
import fr.isima.javapro.persistence.EntityManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTransactionAttribute {
    
    @EJB
    private FifthEJBLocal ejb;
    
    @PersistenceContext
    private EntityManager em;
    
    @Before
    public void setUp() {
        EJBContainer.getInstance().inject(this);
    }
    
    @After
    public void tearDown() {
        EJBContainer.getInstance().close();
    }
    
    @Test
    public void testRequiredNew(){
        try{
            ejb.addRequiredNew("1");
        }
        catch(Exception e){
            
        }
        finally{        
            assert(em.count() == 2);
            ejb.clear();
        }
    }   
    
    @Test
    public void testRequired(){
        try{
            ejb.addRequired("1");
        }
        catch(Exception e){
            
        }
        assert(em.count() == 0);     
    }   
}