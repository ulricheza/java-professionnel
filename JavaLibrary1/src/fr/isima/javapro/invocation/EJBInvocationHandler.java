/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.invocation;

import fr.isima.javapro.EJBContainer;
import fr.isima.javapro.annotation.Remove;
import fr.isima.javapro.annotation.Statefull;
import fr.isima.javapro.annotation.Stateless;
import fr.isima.javapro.exception.NoSuchEJBException;
import fr.isima.javapro.interceptor.Interceptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *
 * @author Ulrich EZA
 */
public class EJBInvocationHandler implements InvocationHandler {
    
    private final Class<?> beanClass;
    private final Interceptor[] interceptors;
    private Object bean;
    private EJBContainer.EJBStatus ejbStatus;
   
    public EJBInvocationHandler(Class<?> beanClass, Interceptor[] interceptors) {
        this.beanClass = beanClass;
        this.interceptors = interceptors;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    
        boolean postConstruct = false;
        boolean preDestroy    = false;
        Object result;
        
        // can't find the annotations above the method otherwise
        // proxy --> Interface and annotations --> Class
        method = beanClass.getMethod(method.getName(), method.getParameterTypes());
                   
        // if stateless bean, instanciate a new object
        if (bean == null || beanClass.isAnnotationPresent(Stateless.class)){
            bean = beanClass.newInstance();
            postConstruct = true;
            
            ejbStatus = new EJBContainer.EJBStatus(bean);
            EJBContainer.getInstance().addEJB(ejbStatus);
        }
        
        //Check that the bean has not been removed
        if (ejbStatus.removed) throw new NoSuchEJBException("The EJB has already been released");
        
        // EJB removal at the end of the method execution ?
        // Singleton and Statefull -> EJBContainer
        if (
                beanClass.isAnnotationPresent(Statefull.class) &&
                method.isAnnotationPresent(Remove.class)
           )
        {
            preDestroy = true;
        }
        
        if (postConstruct)
            MethodManager.invokeMethodWithDeclaredAnnotation(bean, PostConstruct.class, null);
        
        // execute the method
        Invocation invocation = new Invocation(interceptors,bean,method,args);
        result = invocation.nextInterceptor();
        
        if(preDestroy){
            MethodManager.invokeMethodWithDeclaredAnnotation(bean, PreDestroy.class, null); 
            ejbStatus.removed = true;
        }
        
        return result;
    }
}