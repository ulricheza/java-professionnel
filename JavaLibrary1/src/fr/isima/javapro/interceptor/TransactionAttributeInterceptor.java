/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.interceptor;

import fr.isima.javapro.EJBContainer;
import fr.isima.javapro.annotation.TransactionAttribute;
import fr.isima.javapro.annotation.TransactionAttributeType;
import fr.isima.javapro.invocation.Invocation;
import java.lang.reflect.Method;

public class TransactionAttributeInterceptor implements Interceptor {

    @Override
    public Object invoke(Invocation invocation) {
     
        TransactionAttributeType type = TransactionAttributeType.REQUIRED; //default behavior        
        Class<?> beanClass = invocation.getBean().getClass();
        Method method = invocation.getMethod();
        
        if (method.isAnnotationPresent(TransactionAttribute.class))
            type = method.getAnnotation(TransactionAttribute.class).value();
        else if (beanClass.isAnnotationPresent(TransactionAttribute.class))
            type = beanClass.getAnnotation(TransactionAttribute.class).value();
            
        invocation.setTransactionLevel(type);
        switch(type){
            case REQUIRED:
                if(!EJBContainer.getInstance().isTransactionOpened())
                    openTransaction(invocation);
                
                break;
                
            case REQUIRES_NEW:
                openTransaction(invocation);
                break;
        }
        
        return invocation.nextInterceptor();
    }   
    
    private void openTransaction(Invocation invocation){
        EJBContainer.getInstance().openTransaction();
        invocation.setHasCreatedTransaction(true);
    }
}