/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 *
 * @author Ulrich EZA
 */
public class MethodFinder {
    
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
}
