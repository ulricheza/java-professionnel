/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro;

import fr.isima.javapro.annotation.EJB;
import fr.isima.javapro.annotation.Stateless;
import fr.isima.javapro.ejb.FirstEJB;
import fr.isima.javapro.ejb.FirstEJBLocal;
import fr.isima.javapro.ejb.SecondEJB;
import fr.isima.javapro.ejb.SecondEJBLocal;
import fr.isima.javapro.ejb.ThirdEJB;
import fr.isima.javapro.ejb.ThirdEJBLocal;
import fr.isima.javapro.interceptor.Interceptor;
import fr.isima.javapro.interceptor.MethodInterceptor;
import fr.isima.javapro.invocation.EJBInvocationHandler;
import fr.isima.javapro.invocation.MethodManager;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;

/**
 *
 * @author Ulrich EZA
 */
public class EJBContainer {
    
    private static EJBContainer INSTANCE;
    private final Map<Class<?>, Class<?>> registry;
    private final Map<Class<?>, Object> listProxys;
    private final List<EJBStatus> listEJBs;
    private final Interceptor[] interceptors;
    private static final Logger LOG = Logger.getLogger(EJBContainer.class.getName());
    
    public static EJBContainer getInstance() {
        if (INSTANCE == null) INSTANCE = new EJBContainer();
        return INSTANCE;
    }
    
    private EJBContainer(){
        registry = new HashMap<>();
        listProxys = new HashMap<>();
        listEJBs = new ArrayList<>();
        interceptors = new Interceptor[] {
            new MethodInterceptor()           
        };
        
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
        
        LOG.log(Level.FINEST, "Logging set up");
    }
    
    private void bootstrapInit() {
        // scan all classes of the classLoader
        
        // find EJB interfaces map EJB to implementation
        registry.put(FirstEJBLocal.class, FirstEJB.class);
        registry.put(SecondEJBLocal.class,SecondEJB.class);
        registry.put(ThirdEJBLocal.class, ThirdEJB.class);
        
        // manage errors
        
        LOG.log(Level.FINEST, "EJBs' implementations mapping done.");
    }
    
    private Object createProxy(Class<?> beanInterface, Class<?> beanClass){
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[] {beanInterface},
                new EJBInvocationHandler(beanClass,interceptors));
    }
    
    private Object createProxy(Class<?> beanInterface){
        Class<?> beanClass = registry.get(beanInterface);
               
        Object proxy = listProxys.get(beanClass);        
        if (proxy == null){
            proxy = createProxy(beanInterface,beanClass);
            if(!beanClass.isAnnotationPresent(Stateless.class))  // Statetefull or Singleton
                listProxys.put(beanClass,proxy);
        }
        
        return proxy;
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
                    
                    // log info
                    LOG.log(Level.CONFIG, "Injected local implementation of bean {0}",f.getName());
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
    
    public void close(){
        listProxys.clear();
        
        if (!listEJBs.isEmpty()) LOG.config("Releasing non-released EJBs...");
        for (EJBStatus s : listEJBs)
            if (!s.removed)
                MethodManager.invokeMethodWithDeclaredAnnotation(s.ejb, PreDestroy.class, null);
        if (!listEJBs.isEmpty()) LOG.config("Releasing done");
        
        listEJBs.clear();
    }
    
    public void addEJB(EJBStatus ejb){
        listEJBs.add(ejb);
    }
    
    public static class EJBStatus{
        public boolean removed;
        Object ejb;
        
        public EJBStatus(Object ejb){
            this.ejb = ejb;
            this.removed = false;
        }
    }
}