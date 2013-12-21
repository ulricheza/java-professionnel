/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.ejb;

/**
 *
 * @author Ulrich EZA
 */
public class EJBDefaultImpl implements EJBInterface {
    
    protected int value;
    
    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }    
}
