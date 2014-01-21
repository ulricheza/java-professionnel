/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.ejb;

import fr.isima.javapro.annotation.Local;
import fr.isima.javapro.entity.Item;

@Local
public interface FourthEJBLocal {
    
    void addRequired(Item item);
    void addRequiredNew(Item item);
    int count();
    void remove();
}
