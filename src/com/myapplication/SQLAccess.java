package com.myapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;



public class SQLAccess {
	
	private static String dbDriverClass;
	private static String dbUrl;
	private static String dbUserName;
	private static String dbPassWord;
	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	private volatile static UUID idOne;
	private volatile static String user;
	private static String nouser;
	public static String hash;
	
	//public static boolean genSumRep;
	
	 public volatile static boolean genSumRep;

	public synchronized static boolean getGenSumRep() {
	    boolean tmp = genSumRep;
	    if (tmp = false) {
	        tmp = genSumRep;
	        if (tmp = false) {
	          genSumRep = tmp = true;
	      }
	    }
	    return tmp; // Using tmp here instead of myField avoids an memory update
	  }
	
	public SQLAccess(String dbDriverClass, String dbUrl, String dbUserName, String dbPassWord) {

		SQLAccess.dbDriverClass = dbDriverClass;
		SQLAccess.dbUrl = dbUrl;
		SQLAccess.dbUserName = dbUserName;
		SQLAccess.dbPassWord = dbPassWord;

	}
	
	  public synchronized final static UUID uuId(){

	          if (idOne == null) {
		     SQLAccess.idOne = UUID.randomUUID();
	          	}
		  return idOne;
	         
	  }
	  
	  public synchronized final static String user() throws Exception{

          if (user == null) {
	     SQLAccess.user = SQLAccess.user_hash(HelloWorld.pass);
          	}
	  return user;
         
  }
	          
	public static void SetUpDataBase() throws Exception {

		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);			
			

		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);
		}

	}
	
	public static String user_hash(String pass) throws Exception {
						
				preparedStatement = connect.prepareCall("{call `user_hash`(?)}");
				preparedStatement.setString(3, pass);
			      
				ResultSet rs = preparedStatement.executeQuery();
								
				while (rs.next()) {
			        user = rs.getString("user_hash");
			        
			        if (user.isEmpty()) {
			        	nouser = "no user";
			        	return nouser;
			        }
				}

			return user;
		}
	
	public static boolean sessionId() throws Exception {

		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
					 
				long time = System.currentTimeMillis();
				java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
				
			String sql = "insert into  feedback.Sessions values (default, ?, ?, ?)";
				
			preparedStatement = connect.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1,SQLAccess.uuId().toString());
			preparedStatement.setLong(2, 2);
			preparedStatement.setTimestamp(3, timestamp);
			
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			while (rs.next()) {
			}
			preparedStatement.closeOnCompletion();
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			close();

		}
		return true;
	}
	
	public static boolean hash() throws Exception {

		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
				
			String sql = "select hash_ from login.logins";
				
			preparedStatement = connect.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				
				hash =rs.getString(2);
			}
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			close();

		}
		return true;
	}


	// You need to close the resultSet
	private static void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}
			
			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (connect != null) {
				connect.close();

			}
		} catch (Exception e) {

		}
	}
	
	  public static void printSQLException(SQLException ex) {
		    for (Throwable e : ex) {
		      if (e instanceof SQLException) {
		        if (ignoreSQLException(((SQLException)e).getSQLState()) == false) {
		          e.printStackTrace(System.err);
		          System.err.println("SQLState: " + ((SQLException)e).getSQLState());
		          System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
		          System.err.println("Message: " + e.getMessage());
		          Throwable t = ex.getCause();
		          while (t != null) {
		            System.out.println("Cause: " + t);
		            t = t.getCause();
		          }
		        }
		      }
		    }
		  }
	  
	  public static boolean ignoreSQLException(String sqlState) {
		    if (sqlState == null) {
		      System.out.println("The SQL state is not defined!");
		      return false;
		    }
		    // X0Y32: Jar file already exists in schema
		    if (sqlState.equalsIgnoreCase("X0Y32"))
		      return true;
		    // 42Y55: Table already exists in schema
		    if (sqlState.equalsIgnoreCase("42Y55"))
		      return true;
		    return false;
		  }

}
