/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro;

import fr.isima.javapro.annotation.EJB;
import fr.isima.javapro.ejb.FirstEJB;
import fr.isima.javapro.ejb.FirstEJBLocal;
import fr.isima.javapro.ejb.SecondEJB;
import fr.isima.javapro.ejb.SecondEJBLocal;
import fr.isima.javapro.ejb.ThirdEJB;
import fr.isima.javapro.ejb.ThirdEJBLocal;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ulrich EZA
 */
public class EJBContainer {
    
    private static EJBContainer INSTANCE;
    private final Map<Class<?>, Class<?>> registry;
    
    public static EJBContainer getInstance() {
        if (INSTANCE == null) INSTANCE = new EJBContainer();
        return INSTANCE;
    }
    
    private EJBContainer(){
        registry = new HashMap<>(); 
        bootstrapInit();
    }
    
    private void bootstrapInit() {
        // scan all classes of the classLoader
        
        // find EJB interfaces map EJB to implementation
        registry.put(FirstEJBLocal.class, FirstEJB.class);
        registry.put(SecondEJBLocal.class,SecondEJB.class);
        registry.put(ThirdEJBLocal.class, ThirdEJB.class);
        
        // manage errors
    }
    
    public void inject (Object o){
        
        try{
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field f : fields ) {
                if (f.isAnnotationPresent(EJB.class)){
                    Class<?> beanInterface = (Class<?>) f.getGenericType();
                    Class<?> beanClass     = registry.get(beanInterface);

                    // Création du proxy ici plutôt que l'instance
                    f.set(o, beanClass.newInstance());
                }
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InstantiationException | SecurityException ex) {
            Logger.getLogger(EJBContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
