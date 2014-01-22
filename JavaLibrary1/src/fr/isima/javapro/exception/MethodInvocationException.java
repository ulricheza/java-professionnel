/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.exception;

import fr.isima.javapro.annotation.TransactionAttributeType;

public class MethodInvocationException extends RuntimeException {
    
    private TransactionAttributeType level;
    
    public MethodInvocationException(String msg){
        super(msg);
    }
    
    public MethodInvocationException(Throwable th){
        super(th);
    }
    
    public MethodInvocationException(Throwable th, TransactionAttributeType level){
        super(th);
        this.level = level;
    }

    public TransactionAttributeType getLevel() {
        return level;
    }

    public void setLevel(TransactionAttributeType level) {
        this.level = level;
    }
}