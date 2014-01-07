/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.invocation;

import fr.isima.javapro.exception.MethodInvocationException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class MethodManager {
    private static final Logger LOG = Logger.getLogger(MethodManager.class.getName());
    
    public static Method findMethodWithDeclaredAnnotation(
            Object bean,
            Class<? extends Annotation> annotationClass){
        
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods){            
            if (method.isAnnotationPresent(annotationClass)){
                return method;
            }
        }        
        
        return null;
    }
    
    public static Object invokeMethod (Object bean, Method method, Object[] args){
        Object result;
        
        try {
            LOG.entering(bean.getClass().getName(), method.getName());
            result = method.invoke(bean, args);
            LOG.exiting(bean.getClass().getName(), method.getName());
        } 
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            //LOG.throwing(bean.getClass().getName(), method.getName(), ex);           
            throw new MethodInvocationException(ex);
        }
        
        return result;
    }
    
    public static Object invokeMethodWithDeclaredAnnotation(
            Object bean,
            Class<? extends Annotation> annotationClass,
            Object[] args){
        
        Object result = null;
        Method method;
        
        try {
            method = findMethodWithDeclaredAnnotation(bean, annotationClass);
            if(method != null){
                LOG.entering(bean.getClass().getName(), method.getName());
                result = method.invoke(bean, args);
                LOG.exiting(bean.getClass().getName(), method.getName());
            }
        } 
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            //if (method != null) LOG.throwing(bean.getClass().getName(), method.getName(), ex);           
            throw new MethodInvocationException(ex);
        }
        
        return result;
    }       
}