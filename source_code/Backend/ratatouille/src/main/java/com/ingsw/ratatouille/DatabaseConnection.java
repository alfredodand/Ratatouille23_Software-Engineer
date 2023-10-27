package com.ingsw.ratatouille;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


import org.springframework.stereotype.Component;
@Component
public class DatabaseConnection{
	private Connection connection;
	private static DatabaseConnection databaseConnection = null;

	public static DatabaseConnection getInstance() {
		if (databaseConnection == null) {
			databaseConnection = new DatabaseConnection();
		}
		return databaseConnection;
	}
	
	public DatabaseConnection(){
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}  
		String dbName = "ratatouille";
		String username = "user";
		String password = "admin";
		
		String url = "jdbc:postgresql://ec2-13-39-47-160.eu-west-3.compute.amazonaws.com:54320/" + dbName;
//		String url = "jdbc:postgresql://host.docker.internal:54320/" + dbName;
		
		Properties props = new Properties();
		props.setProperty("user", username);
		props.setProperty("password", password);
		
		try {
			connection = DriverManager.getConnection(url, props);
			System.out.print("[v] Connected\n");
		}catch(SQLException e){
			System.out.print("Not connected");
		}
	}
	
    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
    	Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return statement;
    }
}