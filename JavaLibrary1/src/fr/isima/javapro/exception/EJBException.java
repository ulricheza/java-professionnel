/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.isima.javapro.exception;

public class EJBException extends RuntimeException {
    
    public EJBException(String msg){
        super(msg);
    }
    
    public EJBException(Throwable th){
        super(th);
    }
}