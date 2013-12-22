/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro;

import fr.isima.javapro.annotation.EJB;
import fr.isima.javapro.ejb.FirstEJBLocal;
import fr.isima.javapro.ejb.SecondEJBLocal;
import fr.isima.javapro.ejb.ThirdEJBLocal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ulrich EZA
 */
public class TestInject {
    
    @EJB
    FirstEJBLocal ejbStateless;
    
    @EJB
    SecondEJBLocal ejbStatefull1;
    @EJB
    SecondEJBLocal ejbStatefull2;
    
    @EJB
    ThirdEJBLocal ejbSingleton1;
    @EJB
    ThirdEJBLocal ejbSingleton2;
    
    @Before
    public void setUp()  {
        EJBContainer.getInstance().inject(this);        
    }
    
    @After
    public void tearDown() {
        EJBContainer.getInstance().close();
    }
    
    /**
     * Checks if the injection is performed properly
     */
    @Test
    public void inject() {
        assert (ejbStateless instanceof FirstEJBLocal);
        assert (ejbStatefull1 instanceof SecondEJBLocal);
        assert (ejbStatefull1 instanceof SecondEJBLocal);
        assert (ejbSingleton1 instanceof ThirdEJBLocal);
        assert (ejbSingleton2 instanceof ThirdEJBLocal);
    }
}