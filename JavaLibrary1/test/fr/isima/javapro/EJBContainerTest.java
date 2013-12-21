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
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ulrich EZA
 */
public class EJBContainerTest {
    
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
    
    /**
     * Checks the @PostConstruct and @PreDestroy annotation on 
     * Stateless EJB. The verification will be done with log outputs.
     */
    @Test
    public void postContructAndPreDestroy(){
        // The PostConstruct and PreDestroy methods should be called twice 
        assert(ejbStateless.getValue() == 0);
        
        // The second time, the two previous methods must be called again 
        assert(ejbStateless.getValue() == 0);
    }
    
    /**
     * Checks the @PostConstruct annotation on Statefull EJB
     */
    @Test
    public void postConstruct(){        
        // The PostConstruct is only called the first time 
        assert (ejbStatefull1.getValue() == 5);
        
        // The second time, nothing should be done 
        assert (ejbStatefull1.getValue() == 5);
    }
    
    /**
     * Checks the @Stateless annotation
     */
    @Test
    public void stateless() {
        ejbStateless.setValue(10);
        
        assert (ejbStateless.getValue() == 0);
    }
    
    /**
     * Checks the @Statefull annotation
     */
    @Test 
    public void statefull(){
        // ejbStatefull1 et ejbStatefull2 must not be the same objects 
        assert(!ejbStatefull1.equals(ejbStatefull2));
                
        ejbStatefull1.setValue(10);
       
        // Since SecondEJBLocal is statefull, the value of 
        // ebjStatefull1 and ejbStatefull2 must now equals 10 
        assert(ejbStatefull1.getValue() == 10);
        assert(ejbStatefull2.getValue() == 10);   
    }
    
    /**
     * Checks the @Singleton annotation
     */
    @Test
    public void singleton(){
        // ejbSingleton1 et ejbSingleton2 must be the same objects 
        assert (ejbSingleton1.equals(ejbSingleton2));
    }
}