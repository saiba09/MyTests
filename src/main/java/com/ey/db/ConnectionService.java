package com.ey.db;
import java.sql.*;
import java.util.logging.Logger;

public class ConnectionService {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.GoogleDriver";
	private static final String DB_URL = System.getProperty("ae-cloudsql.cloudsql-database-url");
	private static final String USER_NAME = "root" ;
	private static final String PASSWORD = "root" ;
	private static final Logger log = Logger.getLogger(ConnectionService.class.getName());
	private static Connection connection = null;
	protected static Connection getConnection() {
		// TODO Auto-generated method stub
		try {
			Class.forName(JDBC_DRIVER);

			connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			log.info("connection Established :" + connection);
			return connection ;
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			log.severe("exception :" + e);
			e.printStackTrace();
		}
		return null ;
	}
	protected static void closeConnection(){
		try {
			connection.close();
			log.info("connection closed");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.severe("exception closing connection" + e);
			e.printStackTrace();
		}

	}
	
}
