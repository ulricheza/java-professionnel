/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.ejb;

import fr.isima.javapro.annotation.Local;

/**
 *
 * @author Ulrich EZA
 */
@Local
public interface FourthEJBLocal {
    
    void add(Object item);
    int count();
    void remove();
}
