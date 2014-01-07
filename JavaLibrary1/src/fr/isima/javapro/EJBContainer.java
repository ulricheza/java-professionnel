/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.isima.javapro;

// <editor-fold defaultstate="collapsed" desc="Imports">
import fr.isima.javapro.annotation.EJB;
import fr.isima.javapro.annotation.PersistenceContext;
import fr.isima.javapro.annotation.Stateless;
import fr.isima.javapro.ejb.FifthEJB;
import fr.isima.javapro.ejb.FifthEJBLocal;
import fr.isima.javapro.ejb.FirstEJB;
import fr.isima.javapro.ejb.FirstEJBLocal;
import fr.isima.javapro.ejb.FourthEJB;
import fr.isima.javapro.ejb.FourthEJBLocal;
import fr.isima.javapro.ejb.SecondEJB;
import fr.isima.javapro.ejb.SecondEJBLocal;
import fr.isima.javapro.ejb.ThirdEJB;
import fr.isima.javapro.ejb.ThirdEJBLocal;
import fr.isima.javapro.interceptor.Interceptor;
import fr.isima.javapro.interceptor.MethodInterceptor;
import fr.isima.javapro.interceptor.TransactionAttributeInterceptor;
import fr.isima.javapro.invocation.EJBInvocationHandler;
import fr.isima.javapro.invocation.MethodManager;
import fr.isima.javapro.persistence.Database;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
// </editor-fold>

public class EJBContainer {
    
    private static EJBContainer INSTANCE;
    private final Level LOG_LEVEL = Level.FINER;
    private final Map<Class<?>, Class<?>> registry;
    private final Map<Class<?>, Object> listProxys;
    private final List<EJBStatus> listEJBs;
    private final Interceptor[] interceptors;
    private final Deque<Object> transactions;
    private static final Logger LOG = Logger.getLogger(EJBContainer.class.getName());
    
    public static EJBContainer getInstance() {
        if (INSTANCE == null) INSTANCE = new EJBContainer();
        return INSTANCE;
    }
    
    private EJBContainer(){
        registry = new HashMap<>();
        listProxys = new HashMap<>();
        listEJBs = new ArrayList<>();
        transactions = new ArrayDeque<>();
        interceptors = new Interceptor[] {
            new TransactionAttributeInterceptor(),
            new MethodInterceptor()           
        };
        
        bootstrapInit();
        configureLogging();
    }
    
    private void bootstrapInit() {
        // scan all classes of the classLoader
        
        // find EJB interfaces map EJB to implementation
        registry.put(FirstEJBLocal.class, FirstEJB.class);
        registry.put(SecondEJBLocal.class,SecondEJB.class);
        registry.put(ThirdEJBLocal.class, ThirdEJB.class);
        registry.put(FourthEJBLocal.class, FourthEJB.class);
        registry.put(FifthEJBLocal.class, FifthEJB.class);
        
        // manage errors
    }
    
    private void configureLogging() {
        Logger root = Logger.getLogger("");
        root.setLevel(Level.ALL);
        for (Handler handler : root.getHandlers()) {          
            if (handler instanceof ConsoleHandler) {
                handler.setLevel(LOG_LEVEL);
            }
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Transactions Management">
    public boolean isTransactionOpened(){
        return !transactions.isEmpty();
    }
    
    public void openTransaction(){
        if(isTransactionOpened()) Database.getInstance().suspendTransaction();
        
        transactions.addFirst(new Object());
        Database.getInstance().openTransaction();
        
        LOG.log(Level.INFO, "Opening new transaction : T{0}", transactions.size());
    }
    
    public void closeTransaction(){
        LOG.log(Level.INFO, "Closing transaction T{0}", transactions.size());
        
        transactions.removeFirst();
        if(isTransactionOpened()) Database.getInstance().resumeTransaction();
    }
    
    public void commit(){
        Database.getInstance().commit();
        closeTransaction();
    }
    
    public void rollback(){
        Database.getInstance().rollback();
        closeTransaction();
    }
    // </editor-fold>
    
    private <T> Object createProxy(Class<T> beanInterface, Class<? extends T> beanClass){
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[] {beanInterface},
                new EJBInvocationHandler(beanClass,interceptors));
    }
    
    private <T> Object createProxy(Class<T> beanInterface){
        Class<? extends T> beanClass = (Class<? extends T>) registry.get(beanInterface);
               
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
                f.setAccessible(true);
                
                if (f.isAnnotationPresent(EJB.class)){                    
                    Class<?> beanInterface = (Class<?>) f.getGenericType();
                    
                    // proxy creation
                    Object proxy = createProxy(beanInterface);
                    
                    // set field value to proxy
                    f.set(o, beanInterface.cast(proxy));  
                    
                    // log info
                    LOG.log(Level.FINEST, "Injected local implementation of bean {0}",
                            o.getClass().getSimpleName()+"."+f.getName());
                }
                else if (f.isAnnotationPresent(PersistenceContext.class)){
                    Class<?> beanClass = (Class<?>) f.getGenericType();
                    
                    f.set(o, beanClass.newInstance());
                    
                    // log info
                    LOG.log(Level.FINEST, "Injected PersistenceContext {0}",
                            o.getClass().getSimpleName()+"."+f.getName());               
                }
                
                f.setAccessible(false);
            }
        }
        catch (IllegalArgumentException | IllegalAccessException | InstantiationException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        // scan all @EJB and inject EJB implementations
        
        // manage @Stateless & @Statefull & @Singleton strategies -> do not arrange EJB pool
        
        // manage @PostConstruct
        
        // manage @PreDestroy
        
        // manage @PersistenceContext injection
        
        // manage @TransactionAttribute (Required and Required new)
        
        // manage errors
    }
    
    public void close(){
        listProxys.clear();
        
        if (!listEJBs.isEmpty()){
            LOG.info("Releasing non-released EJBs...");
            for (EJBStatus s : listEJBs)
                if (!s.removed)
                    MethodManager.invokeMethodWithDeclaredAnnotation(s.ejb, PreDestroy.class, null);
            LOG.info("Releasing done");
        }
        
        listEJBs.clear();
    }
    
    public EJBStatus addEJB(Object ejb){
        EJBStatus ejbStatus = new EJBStatus(ejb);
        listEJBs.add(ejbStatus);
        return ejbStatus;
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