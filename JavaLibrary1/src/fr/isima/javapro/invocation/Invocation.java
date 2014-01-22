/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.invocation;

import fr.isima.javapro.annotation.TransactionAttributeType;
import fr.isima.javapro.interceptor.Interceptor;
import java.lang.reflect.Method;

public class Invocation {
    
    private final Interceptor[] interceptors;
    private final Object bean;
    private final Method method;
    private final Object[] args;
    private boolean hasCreatedTransaction;
    private TransactionAttributeType transactionLevel;
    private int index;
    
    public Invocation(Interceptor[] interceptors, Object bean, Method method, Object[] args){
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
    
    public boolean getHasCreatedTransaction(){
        return hasCreatedTransaction;
    }
    public void setHasCreatedTransaction(boolean hasCreatedTransaction){
        this.hasCreatedTransaction = hasCreatedTransaction;
    }
    
    public TransactionAttributeType getTransactionLevel() {
        return transactionLevel;
    }

    public void setTransactionLevel(TransactionAttributeType transactionLevel) {
        this.transactionLevel = transactionLevel;
    }
    
    public Object nextInterceptor(){         
        return interceptors[index++].invoke(this);
    }  
}