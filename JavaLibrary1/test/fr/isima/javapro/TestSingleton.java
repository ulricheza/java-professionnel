/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro;

import fr.isima.javapro.annotation.EJB;
import fr.isima.javapro.ejb.ThirdEJBLocal;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ulrich EZA
 */
public class TestSingleton {
    
    @EJB
    ThirdEJBLocal ejbSingleton1;
    @EJB
    ThirdEJBLocal ejbSingleton2;
    
    @Before
    public void setUp()  {
        EJBContainer.getInstance().inject(this);        
    }
    
    /**
     * Checks the @Singleton annotation
     */
    @Test
    public void singleton(){
        // ejbSingleton1 et ejbSingleton2 must be the same objects 
        assert (ejbSingleton1 == ejbSingleton2);
    }
}