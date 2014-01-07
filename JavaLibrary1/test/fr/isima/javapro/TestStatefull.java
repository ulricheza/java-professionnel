/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro;

import fr.isima.javapro.annotation.EJB;
import fr.isima.javapro.ejb.SecondEJBLocal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestStatefull {
    
    @EJB
    SecondEJBLocal ejbStatefull;
  
    @Before
    public void setUp()  {
        EJBContainer.getInstance().inject(this);        
    }
    
    @After
    public void tearDown() {
        EJBContainer.getInstance().close();
    }
    
    /**
     * Checks the @Statefull annotation
     */
    @Test 
    public void statefull(){
        ejbStatefull.setValue(10);        
        assert(ejbStatefull.getValue() == 10);
    }
}