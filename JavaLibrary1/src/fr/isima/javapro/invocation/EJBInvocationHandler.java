/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.invocation;

import fr.isima.javapro.annotation.Stateless;
import fr.isima.javapro.interceptor.Interceptor;
import fr.isima.javapro.interceptor.MethodInterceptor;
import fr.isima.javapro.interceptor.PostConstructInterceptor;
import fr.isima.javapro.interceptor.PreDestroyInterceptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ulrich EZA
 */
public class EJBInvocationHandler implements InvocationHandler {
    
    private final Class<?> beanClass;
    private Object bean;
   
    public EJBInvocationHandler(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    
        // the methods interceptors of the bean
        List<Interceptor> interceptors = new ArrayList<>();
        
        // if stateless bean, instanciate a new object
        if (beanClass.isAnnotationPresent(Stateless.class)){          
            bean = beanClass.newInstance();
            
            interceptors.add(new PostConstructInterceptor());
            interceptors.add(new MethodInterceptor());
            interceptors.add(new PreDestroyInterceptor());
        }
        else if (bean == null){ // otherwise instanciate only if bean is null
            bean = beanClass.newInstance();
            
            interceptors.add(new PostConstructInterceptor());
            interceptors.add(new MethodInterceptor());
        }
        else
            interceptors.add(new MethodInterceptor());
        
        // instanciate new invocation object
        Invocation invocation = new Invocation(interceptors.iterator(), bean, method, args);
        return invocation.invoke();
    }
}