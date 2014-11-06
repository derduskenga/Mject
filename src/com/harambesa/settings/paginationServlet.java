package com.harambesa.settings;

// import javax.servlet.http.;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.HashMap;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class paginationServlet extends HttpServlet{
	PrintWriter out = null;
	Connection conn = null;


	public void init() throws ServletException{
	    try {
	        Class.forName("org.postgresql.Driver");
	        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mjet", "postgres", "");
	      } catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
	      }
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException,IOException{
		response.setContentType("text/html");
		out = response.getWriter();

		out.println("<h4>Hello</h4>");
		
	}

	public static ArrayList<HashMap> filter(Connection conn, String table, HashMap<String,String> conditions, String page) {
		ArrayList<HashMap> resultsArray= new ArrayList<HashMap>();
		System.out.print(conditions);
		// 	get table
		// 	get columns and values
		//  if any return number of records given

		// paginating vaariables
		// total records
		// divided by limit to get pages
		// start point and end point
		// limit e.g 20
		//  offset
		// String sql = "select * from "+table+" where "+column+"=";



		return resultsArray;
	}
}