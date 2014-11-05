package com.harambesa.DBConnection;

import java.sql.*;
import java.util.logging.Logger;

public class DBConnection{
  
  //fields 
  
  Connection db=null;
  String message="";
  Logger log=null;
  String lastErrorMsg="";
  
  //db setter and getter
  
  //setter
  public void setConnection (Connection db_){
  
      this.db = db_;
  
  }
  
  public void setMessage(String msg){
    
      this.message = msg;
  
  }
  
  //getter
  public Connection _getConnection(){
      
      return this.db;
  
  }
  
  
  public String _getMessage(){
    return this.message;
  }
  
  //constructor.
  public DBConnection(){
		
		log = Logger.getLogger(DBConnection.class.getName());
		this.connectDB();
  }
  
  public Connection connectDB(){
 
		try {
 	    
			Class.forName("org.postgresql.Driver");
			db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mjet", "postgres","");
			return db;

		} catch (SQLException e) {
 
			lastErrorMsg="An exception occurred: "+e.getMessage().toString();
			return null;
 
		}
		catch (ClassNotFoundException e) {
 
			//e.printStackTrace();
			lastErrorMsg="Where is your PostgreSQL JDBC Driver? Include in your library path! Error : " + e.getMessage().toString();
			return null;
		}
		catch (Exception ex){
			lastErrorMsg="An exception occurred : " + ex.getMessage().toString();
			return null;
		}
  }
  
  public ResultSet readQuery(String mysql) {
	 return readQuery(mysql, -1);
  }

	public ResultSet readQuery(String mysql, int limit) {

			ResultSet rs = null;
			try {
				Statement st = db.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				if(limit > 0) st.setFetchSize(limit);
				rs = st.executeQuery(mysql);
			} catch (SQLException ex) {
				log.severe("Database readQuery error : " + ex);
			}

			return rs;
	}
  
  	public String executeUpdate(String updsql) {
		String rst = null;

		try {
			Statement stUP = db.createStatement();
			stUP.executeUpdate(updsql);
			stUP.close();
		} catch (SQLException ex) {
			rst = ex.toString();
			lastErrorMsg = ex.getMessage();
			System.err.println("Database transaction get data error : " + ex);
		}

		return rst;
	}

	public String executeBatch(String mysql) {
		String rst = null;

		try {
			Statement st = db.createStatement();
			String[] lines = mysql.split(";");
			for(String line : lines) {
				if(!"".equals(line.trim()))
					st.addBatch(line);
			}
			st.executeBatch();
			st.close();
		} catch (SQLException ex) {
			rst = ex.toString();
			log.severe("Database executeBatch error : " + ex);
		}

		return rst;
	}
	
	public String executeQuery(String mysql) {
		String rst = null;
		try {
			Statement st = db.createStatement();
			st.execute(mysql);
			st.close();
		} catch (SQLException ex) {
			
			rst = ex.toString();
			lastErrorMsg = ex.toString();
			log.severe("Database executeQuery error : " + ex);
		}

		return rst;
	}
	
	
	public ResultSet executeQuery(String mysql,String keys) {
		ResultSet keyset = null;
		String rst = null;
		try {
			Statement st = db.createStatement();
			st.execute(mysql, Statement.RETURN_GENERATED_KEYS);
			keyset = st.getGeneratedKeys();
			//st.close();
		} catch (SQLException ex) {
			rst = ex.toString();
			lastErrorMsg = ex.toString();
			log.severe("Database executeQuery error : " + ex);
		}
		return keyset;
	}
	
	
	public String getLastErrorMsg() { return lastErrorMsg; }
  
	public void closeDB(){
			try{
				if(db != null){
					db.close();
					
				}
			}catch (SQLException sqle){
				lastErrorMsg = "Could not close DB: " + sqle.getMessage().toString();
				log.severe("Database close error : " + sqle);
			}
	}
}