/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro;

import fr.isima.javapro.annotation.EJB;
import fr.isima.javapro.ejb.FifthEJBLocal;
import fr.isima.javapro.ejb.FourthEJBLocal;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestTransactionAttribute {
    
    @EJB
    private FourthEJBLocal ejb1;
     
    @EJB
    private FifthEJBLocal ejb2;
    
    @Before
    public void setUp() {
        EJBContainer.getInstance().inject(this);
    }
    
    @After
    public void tearDown() {
        EJBContainer.getInstance().close();
    }
    
    @Test
    public void testRequired1(){
        try{
            ejb1.addRequired(new Object());
            Assert.fail();
       }
       catch (Exception e){
           assert(rootCause(e) instanceof ArithmeticException);
           assert(ejb1.count() == 0);
           ejb1.remove();      
       }        
    }
    
    @Test
    public void testRequired2(){        
       try{
            ejb2.addRequired(new Object());
            Assert.fail();
       }
       catch (Exception e){
           assert(rootCause(e) instanceof ArithmeticException);
           assert(ejb2.count() == 0);
           ejb2.clear();      
       }
    }
    
    @Test
    public void testRequiredNew1(){
        
       try{
            ejb1.addRequiredNew(new Object());
            Assert.fail();
       }
       catch (Exception e){
           assert(rootCause(e) instanceof ArithmeticException);
           assert(ejb1.count() == 0);
           ejb1.remove();
       }
    } 
    
    @Test
    public void testRequiredNew2(){
        
       try{
            ejb2.addRequiredNew(new Object());
            Assert.fail();
       }
       catch (Exception e){
           assert(rootCause(e) instanceof ArithmeticException);
           assert(ejb2.count() == 1);
           ejb2.clear();      
       }
    } 
    
    private Throwable rootCause(Exception e){
        Throwable th = e;
        while(th.getCause() != null) th= th.getCause();
        return th;
    }
}