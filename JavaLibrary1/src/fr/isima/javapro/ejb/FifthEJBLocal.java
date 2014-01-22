/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.ejb;

import fr.isima.javapro.annotation.Local;

@Local
public interface FifthEJBLocal {
    
    void addRequired(Object item);
    void addRequiredNew(Object item);
    int count();
    void clear();
}