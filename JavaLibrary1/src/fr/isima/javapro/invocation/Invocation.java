/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.invocation;

import fr.isima.javapro.interceptor.Interceptor;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 *
 * @author Ulrich EZA
 */
public class Invocation {
    
    private final Iterator<Interceptor> interceptors;
    private final Object bean;
    private final Method method;
    private final Object[] args;
    private Object result;
    
    public Invocation(Iterator<Interceptor> interceptors, Object bean, Method method, Object[] args){
        this.interceptors = interceptors;
        this.bean = bean;
        this.method = method;
        this.args = args;
    }
    
    public Object getBean(){
        return bean;
    }
       
    public Method getMethod(){
        return method;
    }
    
    public Object[] getArgs() {
        return args;
    }
    
    public void setResult(Object result){
        this.result = result;
    }
    
    public Object invoke(){         
        while (interceptors.hasNext()){
            interceptors.next().invoke(this);
        }
     
        return result;
    }
}