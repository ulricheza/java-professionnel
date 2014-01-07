/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.interceptor;

import fr.isima.javapro.EJBContainer;
import fr.isima.javapro.invocation.MethodManager;
import fr.isima.javapro.invocation.Invocation;
import java.lang.reflect.Method;

public class MethodInterceptor implements Interceptor {
    
    @Override
    public Object invoke(Invocation invocation) {
        Object bean   = invocation.getBean();
        Method method = invocation.getMethod();
        Object[] args = invocation.getArgs();
        
        Object result = null;
        
        try{
           result = MethodManager.invokeMethod(bean, method, args);
        }
        catch(Exception e){
            if (invocation.isTransactionOpened()){
                EJBContainer.getInstance().rollback();
                return result;
            }
            else
                throw e;
        }
        
        if (invocation.isTransactionOpened()) 
            EJBContainer.getInstance().commit();
        
        return result;              
    }   
}