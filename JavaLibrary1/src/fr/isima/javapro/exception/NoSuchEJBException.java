/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.exception;

/**
 *
 * @author Ulrich EZA
 */
public class NoSuchEJBException extends RuntimeException {
    
    public NoSuchEJBException(String msg){
        super(msg);
    }
}
