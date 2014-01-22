/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.interceptor;

import fr.isima.javapro.EJBContainer;
import fr.isima.javapro.annotation.TransactionAttributeType;
import fr.isima.javapro.exception.MethodInvocationException;
import fr.isima.javapro.invocation.MethodManager;
import fr.isima.javapro.invocation.Invocation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInterceptor implements Interceptor {
   
    @Override
    public Object invoke(Invocation invocation) {
        try{
            Object bean   = invocation.getBean();
            Method method = invocation.getMethod();
            Object[] args = invocation.getArgs();
        
            Object result = MethodManager.invokeMethod(bean, method, args);
            
            if (invocation.getHasCreatedTransaction()) 
                EJBContainer.getInstance().commit();
           
            return result;      
        }
        catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
            
            Throwable th = e.getCause();
            if (th instanceof MethodInvocationException){ //Exception in nested methods
                TransactionAttributeType level = ((MethodInvocationException)th).getLevel();
                switch(level){
                    case REQUIRES_NEW:
                        // We don't need to do a rollback since the error occured 
                        // in sub transactions --> commit
                        if (invocation.getHasCreatedTransaction()) 
                            EJBContainer.getInstance().commit();
                        break;
                    case REQUIRED:
                        // The error occured in the same transaction
                        // --> rollback
                        if (invocation.getHasCreatedTransaction())
                            EJBContainer.getInstance().rollback();  
                        break;
                }
                while (th.getCause() != null) th = th.getCause();                
                throw new MethodInvocationException(th);
            }
            else{    // Exception in the root method        
                if (invocation.getHasCreatedTransaction())
                    EJBContainer.getInstance().rollback();  

                throw new MethodInvocationException(e,invocation.getTransactionLevel());
            }
        }                
    }   
}