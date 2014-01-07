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

public class TestPostConstruct {
    
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
     * Checks the @PostConstruct annotation on Statefull EJB
     */
    @Test
    public void postConstruct(){        
        // The PostConstruct is only called the first time 
        // and it increments the value by 5
        assert (ejbStatefull.getValue() == 5);
        
        // The second time, nothing should be done 
        assert (ejbStatefull.getValue() == 5);
    }
}