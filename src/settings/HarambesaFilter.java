package com.harambesa.settings; 
// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
import java.util.*;

import java.util.logging.Logger; 
import com.harambesa.settings.settingsUtil;


// Implements Filter class
public class HarambesaFilter implements Filter  {
  Logger log = null;
  public void  init(FilterConfig config) 
  throws ServletException{ 
    log = Logger.getLogger(HarambesaFilter.class.getName());
  }
  public void  doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
  throws java.io.IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest)request;
    HttpServletResponse res = (HttpServletResponse)response;    
    String path  = null;
    HttpSession session = req.getSession(false); 
    try{
      log.info("session duration"+session.getMaxInactiveInterval());

      ServletContext cont = req.getServletContext(); 
      String openSessions = "the counter : "+cont.getAttribute("totalSessionsActive");
      log.info(settingsUtil.getLoggerLine(Thread.currentThread(),this,openSessions));

    }catch(NullPointerException e){
        log.info("session expired");
        log.info(e.getMessage());
    }
    String baseDir = req.getServletContext().getContextPath();
    String ref = req.getRequestURI();
    log.info(ref); 
    log.info("base file ############# "+baseDir); 
    System.out.println(ref.matches("/mjet/$"));
      if(!ref.equals(baseDir+"/request_handler") && 
        !ref.matches(baseDir+"/login/") &&  
        !ref.matches(baseDir+"/signup/") && 
        !ref.matches(baseDir+"/logout") && 
        !ref.equals(baseDir+"/re-login") && 
        !ref.matches(baseDir+"/$") &&
        !ref.matches(".*[css|jpg|png|gif|js]")){
        // ensure that a session exists
        if(session != null){
          String entity_id = (String)session.getAttribute("entity_id");
          // ensure user id exists in the session 
          log.info("the entity_id is "+entity_id);
          if(entity_id != null){
            session.setAttribute("BASEDIR",baseDir);
            chain.doFilter(request,response);
          }else{        
            log.info("User Not Logged In: Trying To Access: "+req.getRequestURI());
            log.info(req.getContextPath());
            path = req.getContextPath().toString()+"/login";
            // set tag to indicate action being done
            request.setAttribute("tag", "re_login"); 
            // set the page user was trying to access
            request.setAttribute("referer", req.getRequestURI()); 
            // 
            log.info("the referer is ="+(String)request.getAttribute("referer"));
            // res.sendRedirect(path);
            RequestDispatcher rd = request.getRequestDispatcher("/login/index.jsp"); 
            rd.forward(request,response);
          }
          // Pass request back down the filter chain
          
        }else{       
            log.info("User Not Logged In: Trying To Access: "+req.getRequestURI());
            log.info(req.getContextPath());
            path = req.getContextPath().toString()+"/login";
            // set tag to indicate action being done
            request.setAttribute("tag", "re_login"); 
            // set the page user was trying to access
            request.setAttribute("referer", req.getRequestURI()); 
            // 
            log.info("the referer is ="+(String)request.getAttribute("referer"));
            // res.sendRedirect(path);
            RequestDispatcher rd = request.getRequestDispatcher("/login/index.jsp"); 
            rd.forward(request,response);
        }
      }else{  
            log.info("accessing /login");
            chain.doFilter(request,response);
      }
      
   }
   public void destroy( ){
      /* Called before the Filter instance is removed 
      from service by the web container*/
   }
}