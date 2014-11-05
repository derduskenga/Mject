package com.harambesa.admin; 

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.logging.Logger;
import com.harambesa.settings.settingsUtil;


public class HarambesaListener implements ServletContextListener, HttpSessionListener {
		ServletContext currentContext;
		Logger logger = Logger.getLogger(HarambesaListener.class.getName());
		Integer count=0;


	public void contextInitialized(ServletContextEvent sce){ 
        logger.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"Context Initialized"));
		currentContext = sce.getServletContext();
		currentContext.setAttribute("totalSessionsActive",count); 
	}
 
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"Context Destroyed"));
    	currentContext = sce.getServletContext();
		currentContext.removeAttribute("totalSessionsActive");
    }
 
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        logger.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"Session Initialized"));
    	sessionsCounterIncrease();
    }
 
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        logger.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,"Session Destroyed"));
        sessionsCounterReduce();
        logger.info("session was destroyed");
    }
    
    /**
    *
    *	adds to the count variable, safely i.e. 
    *	no two threads can write to this variable at the same time.
    *
    *
    */
    synchronized private void sessionsCounterIncrease(){
        logger.info("increase counter variable"); 
        count++;   	
        logger.info("the count variable: ====== "+count);
        currentContext.setAttribute("totalSessionsActive",count);
        logger.info((String)currentContext.getAttribute("totalSessionsActive"));
         
    }
    
    
    /**
    *
    *	reduce to the count variable, safely i.e. 
    *	no two threads can write to this variable at the same time.
    *
    *
    */
    synchronized private void sessionsCounterReduce(){
        logger.info("decrease counter variable"); 
        if(count>0){
        	count--;
        }
           	
        logger.info("the count variable has : ====== "+count);
        currentContext.setAttribute("totalSessionsActive",count);
        logger.info("the totalSessionsActive:"+(String)currentContext.getAttribute("totalSessionsActive"));
    }
}