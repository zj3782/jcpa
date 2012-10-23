package com.jcpa.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {
	
	private final static SessionFactory sessionFactory = buildSessionFactory();
	private final static ThreadLocal<Session> session = new ThreadLocal<Session>();
	
	private HibernateUtil(){}
	
	private static SessionFactory buildSessionFactory(){
		System.out.println("SessionFactory Buliding");
		Configuration configuration = new Configuration(); 
	    configuration.configure(); 
	    ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();         
	    return configuration.buildSessionFactory(serviceRegistry); 
	}

	public static SessionFactory getSessionfactory() {
		return sessionFactory;
	}
	
	public static Session openSession(){
		Session sn = (Session)session.get();
		if(sn == null){
			sn = sessionFactory.openSession();
			session.set(sn);
		}
		return sn;
	}
	
	public static Session getSession(){
		return openSession();
	}
	
	public static void closeSession(){
		Session sn = (Session)session.get();
		if(sn != null){
			sn.close();
		}
		session.set(null);
	}
}
