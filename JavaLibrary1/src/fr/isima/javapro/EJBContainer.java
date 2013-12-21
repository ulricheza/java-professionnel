/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro;

import fr.isima.javapro.annotation.EJB;
import fr.isima.javapro.annotation.Singleton;
import fr.isima.javapro.ejb.FirstEJB;
import fr.isima.javapro.ejb.FirstEJBLocal;
import fr.isima.javapro.ejb.SecondEJB;
import fr.isima.javapro.ejb.SecondEJBLocal;
import fr.isima.javapro.ejb.ThirdEJB;
import fr.isima.javapro.ejb.ThirdEJBLocal;
import fr.isima.javapro.invocation.EJBInvocationHandler;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ulrich EZA
 */
public class EJBContainer {
    
    private static EJBContainer INSTANCE;
    private final Map<Class<?>, Class<?>> registry;
    private final Map<Class<?>, Object> singletonProxys;
    private static final Logger LOG = Logger.getLogger(EJBContainer.class.getName());
    
    public static EJBContainer getInstance() {
        if (INSTANCE == null) INSTANCE = new EJBContainer();
        return INSTANCE;
    }
    
    private EJBContainer(){
        registry = new HashMap<>();
        singletonProxys = new HashMap<>();
        
        configureLogging();
        bootstrapInit();
    }
    
    private void configureLogging() {
        Logger root = Logger.getLogger("");
        root.setLevel(Level.ALL);
        for (Handler handler : root.getHandlers()) {          
            if (handler instanceof ConsoleHandler) {
                handler.setLevel(Level.FINER);
            }
        }
    }
    
    private void bootstrapInit() {
        // scan all classes of the classLoader
        
        // find EJB interfaces map EJB to implementation
        registry.put(FirstEJBLocal.class, FirstEJB.class);
        registry.put(SecondEJBLocal.class,SecondEJB.class);
        registry.put(ThirdEJBLocal.class, ThirdEJB.class);
        
        // manage errors
    }
    
    private Object createProxy(Class<?> beanInterface, Class<?> beanClass){
         return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[] {beanInterface},
                new EJBInvocationHandler(beanClass));
    }
    
    private Object createProxy(Class<?> beanInterface){
        Class<?> beanClass = registry.get(beanInterface);
               
        // singleton EJBs must be unique in the JVM
        if (beanClass.isAnnotationPresent(Singleton.class)){
            if (!singletonProxys.containsKey(beanClass)){
                singletonProxys.put(beanClass, createProxy(beanInterface,beanClass));
            }
            
            return singletonProxys.get(beanClass);
        }
        
        return createProxy(beanInterface,beanClass);
    }
    
    public void inject (Object o){
        
        try{
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field f : fields ) {
                if (f.isAnnotationPresent(EJB.class)){                    
                    Class<?> beanInterface = (Class<?>) f.getGenericType();
                    
                    // proxy creation
                    Object proxy = createProxy(beanInterface);
                    
                    // set field value to proxy
                    f.set(o, beanInterface.cast(proxy));  
                    
                    // perform some logs
                    LOG.log(Level.CONFIG, "Proxy initialized for bean {0}",f.getName());
                }
            }
        }
        catch (IllegalArgumentException | IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        
        // scan all @EJB and inject EJB implementations
        
        // manage @Stateless & @Statefull & @Singleton strategies -> do not arrange EJB pool
        
        // manage @PostConstruct
        
        // manage @PreDestroy
        
        // manage @PersistenceContext injection
        
        // manage @TransactionAttribute (Required and Required new) and strategies 
        
        // manage errors
    }
}