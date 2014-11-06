package com.harambesa.settings;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Logger;

public class adminFilter implements Filter{

   FilterConfig filterConfig = null;
   Logger log = null;

   public void init(FilterConfig filterConfig) throws ServletException{
   	 this.filterConfig = filterConfig;
     log=Logger.getLogger(adminFilter.class.getName());
   }

   public void destroy(){

   }

   public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException{
          	HttpServletResponse response = (HttpServletResponse)res;
          	HttpServletRequest request = (HttpServletRequest)req;
            log.info(request.getRequestURI());

          	
            if(request.getParameter("tag").equals("log_in")){

          		filterChain.doFilter(req,res);

          	}else if(request.getParameter("tag").equals("set_admin_password")){

                 filterChain.doFilter(req,res);

           	}else if(request.getParameter("tag").equals("view_admins")){

                 filterChain.doFilter(req,res);

          	}else if(request.getParameter("tag").equals("suspend")){

          		filterChain.doFilter(req,res);
                   
          	}else if(request.getParameter("tag").equals("disable_admin")){

          		filterChain.doFilter(req,res);

          	}else if(request.getParameter("tag").equals("fetch_admins")){

          		filterChain.doFilter(req,res);

          	}else if (request.getParameter("tag").equals("fetch_admins")) {

          		filterChain.doFilter(req,res);
          		
          	}else if(request.getParameter("tag").equals("create_dpt")){

          		filterChain.doFilter(req,res);

          	}else if (request.getParameter("tag").equals("set_admin_password")) {

          		filterChain.doFilter(req,res);
          		
          	}else if (request.getParameter("tag").equals("create_admin")) {

          		   filterChain.doFilter(req,res);
          		   
          	}else if(request.getParameter("tag").equals("view_dpts")){

                 filterChain.doFilter(req,res);
                 
            }else if(request.getParameter("tag").equals("update_logs")){

                 filterChain.doFilter(req,res);

            }else if(request.getParameter("tag").equals("get_admin_logs")){

                 filterChain.doFilter(req,res);

            }else{

          		response.sendRedirect("/error");
          	}
   }
}