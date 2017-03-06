package com.uom.jirareport.consumers.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Created by fotarik on 16/02/2017.
 */
public interface HibernateAppenderSessionService {
    /**
     * <P>Returns a reference to a Hibernate session instance.</P>
     *
     * <P>This interface gives applications the ability to open a session
     * using their existing infrastructure, which may include registering
     * audit interceptors if required.</P>
     *
     * @return An open Hibernate session
     * @throws HibernateException
     */
    Session openSession() throws HibernateException;
}