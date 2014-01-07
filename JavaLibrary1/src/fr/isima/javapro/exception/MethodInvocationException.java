/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.exception;

public class MethodInvocationException extends RuntimeException {
    
    public MethodInvocationException(String msg){
        super(msg);
    }
    
    public MethodInvocationException(Throwable th){
        super(th);
    }
}