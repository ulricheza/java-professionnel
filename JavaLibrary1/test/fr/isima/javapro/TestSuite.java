/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    fr.isima.javapro.TestInject.class, 
    fr.isima.javapro.TestPersistenceContext.class,
    fr.isima.javapro.TestPostConstruct.class,
    fr.isima.javapro.TestRemove.class,
    fr.isima.javapro.TestStatefull.class,
    fr.isima.javapro.TestStateless.class,
    fr.isima.javapro.TestTransactionAttribute.class
})
public class TestSuite {}