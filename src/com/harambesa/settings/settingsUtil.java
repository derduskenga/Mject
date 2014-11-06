package com.harambesa.settings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;  

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import com.harambesa.DBConnection.DBConnection; 

public class settingsUtil{ 
	/**
	*
	*	Check if entry exists in the database
	*
	*	@param connection 	the database connection object
	*	@param id 			the value to use to look up entry
	*	@param table		the table to lookup the entry in 
	*	@param column		the table column to lookup the value in
	*
	*
	*	@return ResultSet	Returns a resultset with cursor at the first item
	*/
	public static ResultSet entryExists(Connection conn, String value, String table, String column) 
	throws SQLException{
		ResultSet results=null;
		// sql string
		String sql = "select * from "+table+" where "+column+"= ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,value);
		// System.out.println(pstmt.toString());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){ 
			results=rs;
		}else{
			rs = null;
			System.out.println("No record exists");
		}

		return results;
	}


	/**
	*
	*	Returns an array list for records in the database based on the arguements given
	*
	*	@param connection the connection object
	*	@param value the value to lookup for
	*	@param table the table to lookup the value in
	*	@param column the column to looukup the value in
	*
	*	@return ArrayList containing hash maps, hash maps contain individual rows in the table
	*
	*/
	public static ArrayList<HashMap> entryExistsMap(Connection conn, String value, String column, String table) {
		
		ArrayList<HashMap> resultsArray= new ArrayList<HashMap>();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try{
			// sql string
			String sql = "select * from "+table+" where "+column+"= ? ORDER BY "+column+" DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,Integer.parseInt(value));
			// System.out.println(pstmt.toString());
			rs = pstmt.executeQuery();
			// get the metadata
				ResultSetMetaData rsm = rs.getMetaData();
				// get column count in the db
				Integer columnsTotal = rsm.getColumnCount();
			// List to store the column names
				ArrayList<String> columnList = new ArrayList<String>();
				for(int i=1;i<=columnsTotal;i++){ 
					columnList.add(rsm.getColumnName(i)); 
				} 

			while(rs.next()){
				HashMap<String, String> results= new HashMap<String, String>();
			// get iterator for the columnlist
				Iterator it = columnList.iterator();
				while(it.hasNext()){
					String columnName = (String) it.next();
					results.put(columnName,rs.getString(columnName));
				} 
				resultsArray.add(results);
			}


		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{

				if(pstmt!=null){
					pstmt.close();
				}

				if(rs!=null){
					rs.close();
				}

			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return resultsArray;
	}


	/**
	*
	*	Returns the current line of the loger
	*
	*	@param Thread the current thread to get the line from
	*	@param Object the object of the current class
	*	
	*	@return Interger the line number for the source
	*/
	public static String getLoggerLine(Thread thread, Object obj, String message){
		// get the class of the object
		String objClassName = obj.getClass().getName();
		Integer lineNumber = null;

		// loop through the threads stack trace
		for(StackTraceElement ste : thread.getStackTrace()){
			String strackTraceClassName = ste.getClassName(); 

			// if an item on the stack matches the class of the object
			// get the current line number
			if(objClassName.equals(strackTraceClassName)){
				lineNumber = ste.getLineNumber();
			}

		}

		
		return "( At Line : "+lineNumber+ " ) =======>>"+message;

	}


	/**
	*
	*	Returns an array list for records in the database based on the arguements given
	*	can also return paginated records
	*
	*	@param connection the connection object
	*	@param value the value to lookup for
	*	@param condation an array of HashMap containig column/value pair 
	*
	*	@return ArrayList containing hash maps, hash maps contain individual rows in the table
	*
	*/
		
	// public static ArrayList<HashMap> filter(Connection conn, String table, HashMap<String,String> conditions, String page) {
	// 	ArrayList<HashMap> resultsArray= new ArrayList<HashMap>();
	// 	System.out.print(conditions);
	// 	// 	get table
	// 	// 	get columns and values
	// 	//  if any return number of records given

	// 	// paginating vaariables
	// 	// total records
	// 	// divided by limit to get pages
	// 	// start point and end point
	// 	// limit e.g 20
	// 	//  offset
	// 	String sql = "select * from "+table+" where "+column+"=";



	// 	return resultsArray;
	// }


}