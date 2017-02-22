package com.uom.jirareport.consumers.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fotarik on 16/02/2017.
 */

public enum HibernateUtil implements HibernateAppenderSessionService{
    INSTANCE;

    public static ConcurrentHashMap<String,SessionFactory> FACTORY_MAP =
            new ConcurrentHashMap<String,SessionFactory>();
    public static final String EMP_HIBERNATE_SESSION_FACTORY = "emp:/hibernate/SessionFactory";
    public static final String EMP_HIBERNATE_SESSION_FACTORY_JUPITER_LOCAL = "emp:/hibernate/SessionFactory_Jupiter_Local";
    public static final String DEFAULT_FACTORY_NAME = EMP_HIBERNATE_SESSION_FACTORY;
    public static final String DEFAULT_LOCAL_FACTORY_NAME = EMP_HIBERNATE_SESSION_FACTORY_JUPITER_LOCAL;
    private static final Logger log = Logger.getLogger(HibernateUtil.class);

    public static Session currentSession() {
        return HibernateUtil.currentSession(DEFAULT_FACTORY_NAME);
    }

    public static Session currentSession(String factoryName) throws HibernateException {
        try {
            Context ctx = new InitialContext();
            SessionFactory sessionFactory = (SessionFactory) ctx.lookup(factoryName);
            return sessionFactory.openSession();
        } catch (NamingException e) {
            /* Hack Fallback Code.
             * All the session factories are bound under emp:/ this means that
             * JMX can't access them. So, we fall back to the map to provide them
             * until the configuration can be fixed properly.
             */
            SessionFactory sessionFactory = FACTORY_MAP.get(factoryName);
            if(sessionFactory == null) {
                log.error("Trying to find SessionFactory from JNDI. This is unrecoverable and means the Hibernate config is broke. factoryName=" + factoryName, e);
                return null;
            }
            return sessionFactory.openSession();
        }
    }

    public Session openSession() throws HibernateException {
        return currentSession();
    }
}
