/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro;

import fr.isima.javapro.annotation.EJB;
import fr.isima.javapro.ejb.FifthEJBLocal;
import fr.isima.javapro.entity.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTransactionAttribute {
    
    @EJB
    private FifthEJBLocal ejb;
    
    @Before
    public void setUp() {
        EJBContainer.getInstance().inject(this);
    }
    
    @After
    public void tearDown() {
        EJBContainer.getInstance().close();
    }
    
    @Test
    public void testRequired(){
        
        ejb.addRequired(new Item());
        assert(ejb.count() == 0);  
        ejb.clear();
    }  
    
    @Test
    public void testRequiredNew(){
        
        ejb.addRequiredNew(new Item());
        assert(ejb.count() == 1);
        ejb.clear();
    }    
}