/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro;

import fr.isima.javapro.annotation.EJB;
import fr.isima.javapro.ejb.SecondEJBLocal;
import fr.isima.javapro.exception.NoSuchEJBException;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;

public class TestRemove {
    
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
    
    @Test 
    public void remove() {
        assert (ejbStatefull.getValue() == 5);        
        ejbStatefull.remove(); // The EJB should be released after this method (@Remove annotation)
        try{
            ejbStatefull.getValue();
            Assert.fail();
        }
        catch(Exception e){
            assert (e instanceof NoSuchEJBException);
        }
    }
}