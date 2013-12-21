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

/**
 *
 * @author Ulrich EZA
 */
public class MethodInterceptor implements Interceptor {
    private static final Logger LOG = Logger.getLogger(MethodInterceptor.class.getName());

    @Override
    public void invoke(Invocation invocation) {
        try {
            Object bean = invocation.getBean();
            Method method = invocation.getMethod();
            Object[] args = invocation.getArgs();
            
            Object result = method.invoke(bean, args);
            invocation.setResult(result);
            
            String msg = bean.getClass().getName()+" : \""+method.getName()+ "\" called";
            LOG.info(msg);
        } 
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }   
}