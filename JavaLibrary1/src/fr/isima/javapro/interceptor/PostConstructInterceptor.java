/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.interceptor;

import fr.isima.javapro.invocation.Invocation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;

/**
 *
 * @author Ulrich EZA
 */
public class PostConstructInterceptor implements Interceptor {
  
    private static final Logger LOG =  Logger.getLogger(PostConstructInterceptor.class.getName());

    @Override
    public void invoke(Invocation invocation) {
        
        // search for the @PostConstruct annotated method in the bean
        Object bean = invocation.getBean();        
        Method method = MethodFinder.findMethodWithDeclaredAnnotation(bean, PostConstruct.class);
        
        if (method != null){
            try {                
                LOG.entering(bean.getClass().getName(), method.getName());
                method.invoke(bean, (Object[]) null);
                LOG.exiting(bean.getClass().getName(), method.getName());
            }
            catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }
}