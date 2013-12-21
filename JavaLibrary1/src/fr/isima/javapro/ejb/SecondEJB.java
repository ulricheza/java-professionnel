/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.ejb;

import fr.isima.javapro.annotation.Statefull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *
 * @author Ulrich EZA
 */
@Statefull
public class SecondEJB extends EJBDefaultImpl implements SecondEJBLocal {
    
    @PostConstruct
    public void postConstruct(){
        value+= 5;
    }
    
    @PreDestroy
    public void preDestroy(){
        value -= 5;
    }
}