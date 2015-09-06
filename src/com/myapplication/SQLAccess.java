package com.myapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import org.apache.commons.io.IOUtils;

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
	public static String hash;
	public static String voucher;
	public static String token;


	
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
	
	public static boolean new_hash(String pass, String user) throws Exception {
		
		// This will load the MySQL driver, each DB has its own driver
		Class.forName(dbDriverClass);

		// Setup the connection with the DB
		connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
		
	String sql = "insert into  login.logins values (default, ?, ?, default)";

	InputStream ps = IOUtils.toInputStream(pass, "UTF-8");
    Reader readerP = new BufferedReader(new InputStreamReader(ps));
    
	InputStream us = IOUtils.toInputStream(user, "UTF-8");
    Reader readerU = new BufferedReader(new InputStreamReader(us));
    
			preparedStatement = connect.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setCharacterStream(1, readerP);
			preparedStatement.setCharacterStream(2, readerU);
		      
			preparedStatement.executeUpdate();
		
			preparedStatement.closeOnCompletion();
		
	readerP.close();
	readerU.close();
	
		return true;
}
	
	public static boolean sessionId() throws Exception {

		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
					 
				long time = System.currentTimeMillis();
				java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
				
			String sql = "insert into  login.logins values (default, ?)";
				
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
	
	public static boolean voucher(String voucher_) throws Exception {

		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
				
			InputStream in = IOUtils.toInputStream(voucher_, "UTF-8");
		    Reader reader = new BufferedReader(new InputStreamReader(in));
		    
		    connect.setCatalog("login");
			CallableStatement callableStatement = connect.prepareCall("{call `get_voucher`(?)}");

			callableStatement.setCharacterStream(1, reader);				
			ResultSet rs = callableStatement.executeQuery();
			callableStatement.closeOnCompletion();
			reader.close();
			
			while (rs.next()) {
				
				String voucher =rs.getString(1);

				if (voucher_.equals(voucher)) {
	
			InputStream in_ = IOUtils.toInputStream(voucher_, "UTF-8");
		    Reader reader_ = new BufferedReader(new InputStreamReader(in_));
		    
			connect.setCatalog("login");
			CallableStatement callableStatement_ = connect.prepareCall("{call `set_voucher`(?)}");
			callableStatement_.setCharacterStream(1, reader_);				
			callableStatement_.executeQuery();
			callableStatement_.closeOnCompletion();
			reader_.close();
				}
				
				close();
				return true;
			} 
			
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			close();

		}
		return false;
	}
	
	public static boolean insert_voucher(String voucher_, String user, String pass) throws Exception {
		
		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
				
			//InputStream in = IOUtils.toInputStream(voucher_, "UTF-8");
		    //Reader reader = new BufferedReader(new InputStreamReader(in));
		    
		    //connect.setCatalog("login");
			//CallableStatement callableStatement = connect.prepareCall("{call `check_voucher`(?)}");

			//callableStatement.setCharacterStream(1, reader);				
			//ResultSet rs = callableStatement.executeQuery();
			//reader.close();
			//while (rs.next()) {
				
			//	String voucher =rs.getString(1);

			//	if (voucher_.equals(voucher)) {
			 
			InputStream in_ = IOUtils.toInputStream(voucher_, "UTF-8");
		    Reader reader_ = new BufferedReader(new InputStreamReader(in_));
		    
			InputStream ins = IOUtils.toInputStream(pass, "UTF-8");
		    Reader readers = new BufferedReader(new InputStreamReader(ins));
		    
			connect.setCatalog("login");
			CallableStatement callableStatement_ = connect.prepareCall("{call `insert_voucher`(?, ?, ?)}");
			callableStatement_.setCharacterStream(1, reader_);
			callableStatement_.setString(2, user);
			callableStatement_.setCharacterStream(3, readers);
			callableStatement_.executeUpdate();
			callableStatement_.closeOnCompletion();
			reader_.close();
			readers.close();
				//}
				
				close();
				return true;
			//} 
			
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			close();

		}
		return false;
	}
	
	public static boolean insert_device(String deviceId, String user) throws Exception {
		
		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
			 
			InputStream in_ = IOUtils.toInputStream(deviceId, "UTF-8");
		    Reader reader_ = new BufferedReader(new InputStreamReader(in_));
		    
			InputStream ins = IOUtils.toInputStream(user, "UTF-8");
		    Reader readers = new BufferedReader(new InputStreamReader(ins));
		    
			connect.setCatalog("login");
			CallableStatement callableStatement_ = connect.prepareCall("{call `insert_device_`(?, ?)}");
			callableStatement_.setCharacterStream(1, reader_);
			callableStatement_.setCharacterStream(2, readers);
			callableStatement_.executeUpdate();
			callableStatement_.closeOnCompletion();
			reader_.close();
			readers.close();
				//}
				
				close();
				return true;
			//} 
			
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			close();

		}
		return false;
	}
	
	public static boolean insert_sessionCreated(String deviceId, long sessionCreated) throws ClassNotFoundException, IOException {
		
		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
			 
			InputStream in_ = IOUtils.toInputStream(deviceId, "UTF-8");
		    Reader reader_ = new BufferedReader(new InputStreamReader(in_));
		    
			connect.setCatalog("login");
			CallableStatement callableStatement_ = connect.prepareCall("{call `insert_sessionCreated`(?, ?)}");
			callableStatement_.setCharacterStream(1, reader_);
			callableStatement_.setLong(2, sessionCreated);
			callableStatement_.executeUpdate();
			callableStatement_.closeOnCompletion();
			reader_.close();
				//}
				
				close();
				return true;
			//} 
			
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			close();

		}
		return false;
	}
	
	public static boolean reset_voucher(String voucher) throws Exception {

		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
				
			InputStream in = IOUtils.toInputStream(voucher, "UTF-8");
		    Reader reader = new BufferedReader(new InputStreamReader(in));
		    
		    connect.setCatalog("login");
			CallableStatement callableStatement = connect.prepareCall("{call `reset_voucher`(?)}");

			callableStatement.setCharacterStream(1, reader);
							
			callableStatement.executeQuery();
			callableStatement.closeOnCompletion();
			reader.close();
	
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			close();

		}
		return true;
	}
	
	public static boolean register_voucher(String voucher) throws Exception {

		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
				
			InputStream in = IOUtils.toInputStream(voucher, "UTF-8");
		    Reader reader = new BufferedReader(new InputStreamReader(in));
		    
		    connect.setCatalog("login");
			CallableStatement callableStatement = connect.prepareCall("{call `register_voucher`(?)}");

			callableStatement.setCharacterStream(1, reader);
							
			callableStatement.executeQuery();
			callableStatement.closeOnCompletion();
			reader.close();
	
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			close();

		}
		return true;
	}
	
	public static String hash(String pass) throws Exception {

		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
								
			InputStream in = IOUtils.toInputStream(pass, "UTF-8");
		    Reader reader = new BufferedReader(new InputStreamReader(in));
		    
		    connect.setCatalog("login");
			CallableStatement callableStatement = connect.prepareCall("{call `get_hash`(?)}");

				callableStatement.setCharacterStream(1, reader);
							
			ResultSet rs = callableStatement.executeQuery();
			callableStatement.closeOnCompletion();
			reader.close();
			while (rs.next()) {
				
				hash =rs.getString(1);
				System.out.println(hash);
			}
			
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			close();

		}
		return hash;
	}
	
	public static String token(String deviceId) throws Exception {

		
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName(dbDriverClass);

			// Setup the connection with the DB
			connect = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
								
			InputStream in_ = IOUtils.toInputStream(deviceId, "UTF-8");
		    Reader reader_ = new BufferedReader(new InputStreamReader(in_));
		    
		    connect.setCatalog("login");
			CallableStatement callableStatement = connect.prepareCall("{call `get_token`(?)}");

				callableStatement.setCharacterStream(1, reader_);
							
			ResultSet rs = callableStatement.executeQuery();
			callableStatement.closeOnCompletion();
			reader_.close();
			while (rs.next()) {
				
				token =rs.getString(1);
			}
			
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			close();

		}
		return token;
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
		    // 70100: Unique key validation eror
		    if (sqlState.equalsIgnoreCase("70100"))
		      return true;
		    // 23000: Unique key validation eror
		    if (sqlState.equalsIgnoreCase("23000"))
		      return true;
		    // 42Y55: Table already exists in schema
		    if (sqlState.equalsIgnoreCase("42Y55"))
		      return true;		 
		    return false;
		  }

}
