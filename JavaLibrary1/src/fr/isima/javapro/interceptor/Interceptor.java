/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro.interceptor;

import fr.isima.javapro.invocation.Invocation;

public interface Interceptor { 
    Object invoke(Invocation invocation);
}