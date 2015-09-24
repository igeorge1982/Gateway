package com.myapplication;

/**
 * @author George Gaspar
 * @email: igeorge1982@gmail.com 
 * 
 * @Year: 2015
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;

public class SQLAccess {
	
	private static volatile Connection connect = null;
	private static volatile PreparedStatement preparedStatement = null;
	private volatile static UUID idOne;
	public static volatile String hash;
	public static volatile String token;
	private static volatile int isActivated;
	private static volatile String Response = null;
	private static volatile ResultSet rs;
	private static volatile CallableStatement callableStatement = null;

	  public synchronized final static UUID uuId(){

          if (idOne == null) {
	     SQLAccess.idOne = UUID.randomUUID();
          	}
	  return idOne;         
	  }
	  
	  public synchronized static Connection connect(ServletContext context) throws ClassNotFoundException, SQLException{

          if (connect == null) {
      		DBConnectionManager dbManager = (DBConnectionManager) context.getAttribute("DBManager");
      		SQLAccess.connect = dbManager.getConnection();
          	}
	  return connect;         
	  }
	
	/**
	 * Inserts user and password.
	 * 
	 * @param pass
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public synchronized static boolean new_hash(String pass, String user, ServletContext context) throws Exception {
	
		// Setup the connection with the DB
		DBConnectionManager dbManager = (DBConnectionManager) context.getAttribute("DBManager");
		
		try {

		connect = dbManager.getConnection();
		
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
	
			} catch (SQLException ex) {
				SQLAccess.printSQLException(ex);
	
				} finally {
			
			dbManager.closeConnection();
	
		}
			return true;
	}
	
	public synchronized static boolean sessionId(ServletContext context) throws Exception {
		
		// Setup the connection with the DB
		DBConnectionManager dbManager = (DBConnectionManager) context.getAttribute("DBManager");
		
		try {
			
    		connect = dbManager.getConnection();
					 
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
			
			dbManager.closeConnection();

		}
		return true;
	}
	
	/**
	 * First checks the voucher state, and depending on the state of the voucher a @return will be set. 
	 * It returns always true, if the voucher is available.
	 * 
	 * @param voucher_
	 * @return
	 * @throws Exception
	 */
	public synchronized static boolean voucher(String voucher_, ServletContext context) throws Exception {

		// Setup the connection with the DB
		DBConnectionManager dbManager = (DBConnectionManager) context.getAttribute("DBManager");
		
		try {

			connect = dbManager.getConnection();
				
			InputStream in = IOUtils.toInputStream(voucher_, "UTF-8");
		    Reader reader = new BufferedReader(new InputStreamReader(in));
		    
			callableStatement = connect.prepareCall("{call `get_voucher`(?)}");

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
				
				dbManager.closeConnection();
				return true;
			} 
			
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			dbManager.closeConnection();

		}
		return false;
	}
	
	/**
	 * Initial voucher insert in the first step of registration process.
	 * 
	 * @param voucher_
	 * @param user
	 * @param pass
	 * @return true on success, otherwise false
	 * @throws Exception
	 */
	public synchronized static boolean insert_voucher(String voucher_, String user, String pass, ServletContext context) throws Exception {
		
		// Setup the connection with the DB
		DBConnectionManager dbManager = (DBConnectionManager) context.getAttribute("DBManager");
		
		try {
    		connect = dbManager.getConnection();
			 
			InputStream in_ = IOUtils.toInputStream(voucher_, "UTF-8");
		    Reader reader_ = new BufferedReader(new InputStreamReader(in_));
		    
			InputStream ins = IOUtils.toInputStream(pass, "UTF-8");
		    Reader readers = new BufferedReader(new InputStreamReader(ins));
		    
			connect.setCatalog("login");
			callableStatement = connect.prepareCall("{call `insert_voucher`(?, ?, ?)}");
			callableStatement.setCharacterStream(1, reader_);
			callableStatement.setString(2, user);
			callableStatement.setCharacterStream(3, readers);
			callableStatement.executeUpdate();
			callableStatement.closeOnCompletion();
			reader_.close();
			readers.close();
				
				dbManager.closeConnection();
				return true; 
			
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			dbManager.closeConnection();

		}
		return false;
	}
	
	/**
	 * Inserts a deviceId for the current user.
	 * 
	 * @param deviceId
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public synchronized static boolean insert_device(String deviceId, String user, ServletContext context) throws Exception {
		
		// Setup the connection with the DB
		DBConnectionManager dbManager = (DBConnectionManager) context.getAttribute("DBManager");
		
		try {
    		connect = dbManager.getConnection();
			 
			InputStream in_ = IOUtils.toInputStream(deviceId, "UTF-8");
		    Reader reader_ = new BufferedReader(new InputStreamReader(in_));
		    
			InputStream ins = IOUtils.toInputStream(user, "UTF-8");
		    Reader readers = new BufferedReader(new InputStreamReader(ins));
		    
			connect.setCatalog("login");
			callableStatement = connect.prepareCall("{call `insert_device_`(?, ?)}");
			callableStatement.setCharacterStream(1, reader_);
			callableStatement.setCharacterStream(2, readers);
			callableStatement.executeUpdate();
			callableStatement.closeOnCompletion();
			reader_.close();
			readers.close();
								
				dbManager.closeConnection();
				
				return true; 
			
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			dbManager.closeConnection();

		}
		return false;
	}
	
	
	/**
	 * Inserts session creation time for deviceId. 
	 * This insert also triggers tokens into the Tokens table for the device (user), that can be used for API calls.
	 * 
	 * @param deviceId
	 * @param sessionCreated
	 * @return true on success, otherwise false
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SQLException 
	 */
	public synchronized static boolean insert_sessionCreated(String deviceId, long sessionCreated, String sessionID, ServletContext context) throws ClassNotFoundException, IOException, SQLException {
		
		// Setup the connection with the DB
		DBConnectionManager dbManager = (DBConnectionManager) context.getAttribute("DBManager");
		
		try {
    		//connect(context);
			connect = dbManager.getConnection();
			 
			InputStream in_ = IOUtils.toInputStream(deviceId, "UTF-8");
		    Reader reader_ = new BufferedReader(new InputStreamReader(in_));
		    
			InputStream in = IOUtils.toInputStream(sessionID, "UTF-8");
		    Reader reader = new BufferedReader(new InputStreamReader(in));
		    
			connect.setCatalog("login");
			callableStatement = connect.prepareCall("{call `insert_sessionCreated`(?, ?, ?)}");
			callableStatement.setCharacterStream(1, reader_);
			callableStatement.setLong(2, sessionCreated);
			callableStatement.setCharacterStream(3, reader);
			callableStatement.executeUpdate();
			callableStatement.closeOnCompletion();
			reader_.close();
				
				
			dbManager.closeConnection();
			
				return true;
			
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			dbManager.closeConnection();

		}
		return false;
	}
	
	/**
	 * Resets the voucher to the first enum state.
	 * 
	 * @param voucher
	 * @return true
	 * @throws Exception
	 */
	public synchronized static boolean reset_voucher(String voucher, ServletContext context) throws Exception {

		DBConnectionManager dbManager = (DBConnectionManager) context.getAttribute("DBManager");
		
		try {
    		
    		connect = dbManager.getConnection();
				
			InputStream in = IOUtils.toInputStream(voucher, "UTF-8");
		    Reader reader = new BufferedReader(new InputStreamReader(in));
		    
		    connect.setCatalog("login");
			callableStatement = connect.prepareCall("{call `reset_voucher`(?)}");

			callableStatement.setCharacterStream(1, reader);
							
			callableStatement.executeQuery();
			callableStatement.closeOnCompletion();
			reader.close();
	
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			dbManager.closeConnection();

		}
		return true;
	}
	
	/**
	 *  Updates (finalizes) the voucher state in the last step of registration process.
	 * 
	 * @param voucher
	 * @return true
	 * @throws Exception
	 */
	public synchronized static boolean register_voucher(String voucher_, ServletContext context) throws Exception {

		// Setup the connection with the DB
		DBConnectionManager dbManager = (DBConnectionManager) context.getAttribute("DBManager");
		
		try {
    		connect = dbManager.getConnection();
			
			InputStream in = IOUtils.toInputStream(voucher_, "UTF-8");
		    Reader reader = new BufferedReader(new InputStreamReader(in));
		    
		    connect.setCatalog("login");
			callableStatement = connect.prepareCall("{call `get_processing_voucher`(?)}");

			callableStatement.setCharacterStream(1, reader);				
			ResultSet rs = callableStatement.executeQuery();
			callableStatement.closeOnCompletion();
			reader.close();
	
			while (rs.next()) {
				
				String voucher =rs.getString(1);

				if (voucher_.equals(voucher)) {
			
			InputStream in_ = IOUtils.toInputStream(voucher, "UTF-8");
		    Reader reader_ = new BufferedReader(new InputStreamReader(in_));
		    
		    connect.setCatalog("login");
			callableStatement = connect.prepareCall("{call `register_voucher`(?)}");

			callableStatement.setCharacterStream(1, reader_);
							
			callableStatement.executeQuery();
			callableStatement.closeOnCompletion();
			reader.close();
	
				}
				
				dbManager.closeConnection();

				return true;
			} 
			
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			dbManager.closeConnection();

		}
		return false;
	}
	
	/**
	 * Checks user password.
	 * 
	 * @param pass
	 * @return hash
	 * @throws Exception
	 */
	public synchronized static String hash(String pass, ServletContext context) throws Exception {

		// Setup the connection with the DB
		DBConnectionManager dbManager = (DBConnectionManager) context.getAttribute("DBManager");
		
		try {
    		connect = dbManager.getConnection();
								
			InputStream in = IOUtils.toInputStream(pass, "UTF-8");
		    Reader reader = new BufferedReader(new InputStreamReader(in));
		    
		    connect.setCatalog("login");
			callableStatement = connect.prepareCall("{call `get_hash`(?)}");

				callableStatement.setCharacterStream(1, reader);
							
			ResultSet rs = callableStatement.executeQuery();
			callableStatement.closeOnCompletion();
			reader.close();
			while (rs.next()) {
				
				hash =rs.getString(1);
			}
			
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			dbManager.closeConnection();

		}
		return hash;
	}
	
	
	public synchronized static String isActivated(String user, ServletContext context) throws Exception {

		// Setup the connection with the DB
		DBConnectionManager dbManager = (DBConnectionManager) context.getAttribute("DBManager");
		
		try {
    		connect = dbManager.getConnection();
								
			InputStream in = IOUtils.toInputStream(user, "UTF-8");
		    Reader reader = new BufferedReader(new InputStreamReader(in));
		    
		    connect.setCatalog("login");
			callableStatement = connect.prepareCall("{call `isActivated`(?)}");

				callableStatement.setCharacterStream(1, reader);
							
			rs = callableStatement.executeQuery();
			callableStatement.closeOnCompletion();
			reader.close();
			while (rs.next()) {
				
				isActivated =rs.getInt(1);
			}
			
			if (isActivated != 1) {
				Response = "S";
			} 	else {
				Response = "";
			}
			
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			dbManager.closeConnection();

		}
		return Response;
	}
	
	
	/**
	 * Get token1 for current device. 
	 * 
	 * @param deviceId
	 * @return token
	 * @throws Exception
	 */
	public synchronized static String token(String deviceId, ServletContext context) throws Exception {

		// Setup the connection with the DB
		DBConnectionManager dbManager = (DBConnectionManager) context.getAttribute("DBManager");
		
		try {
    		connect = dbManager.getConnection();
								
			InputStream in_ = IOUtils.toInputStream(deviceId, "UTF-8");
		    Reader reader_ = new BufferedReader(new InputStreamReader(in_));
		    
		    connect.setCatalog("login");
			callableStatement = connect.prepareCall("{call `get_token`(?)}");

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
			
			dbManager.closeConnection();

		}
		return token;
	}
	
	public synchronized static boolean logout(String sessionID, ServletContext context) throws Exception {

		// Setup the connection with the DB
		DBConnectionManager dbManager = (DBConnectionManager) context.getAttribute("DBManager");
		
		try {
    		connect = dbManager.getConnection();
								
			InputStream in_ = IOUtils.toInputStream(sessionID, "UTF-8");
		    Reader reader_ = new BufferedReader(new InputStreamReader(in_));
		    
		    connect.setCatalog("login");
			callableStatement = connect.prepareCall("{call `logout_device`(?)}");

			callableStatement.setCharacterStream(1, reader_);
							
			callableStatement.executeQuery();
			callableStatement.closeOnCompletion();
			reader_.close();

			
		} catch (SQLException ex) {
		      SQLAccess.printSQLException(ex);

		} finally {
			
			dbManager.closeConnection();

		}
		return true;
	}
	
	/**
	 * Prints SQL errors in catalina.out.
	 * 
	 * @param ex
	 */
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
