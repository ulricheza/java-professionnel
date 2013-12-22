/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.interceptor;

import fr.isima.javapro.invocation.MethodManager;
import fr.isima.javapro.invocation.Invocation;
import java.lang.reflect.Method;

/**
 *
 * @author Ulrich EZA
 */
public class MethodInterceptor implements Interceptor {
    
    @Override
    public Object invoke(Invocation invocation) {
        Object bean   = invocation.getBean();
        Method method = invocation.getMethod();
        Object[] args = invocation.getArgs();
            
        return MethodManager.invokeMethod(bean, method, args);
    }   
}