package com.verizon.cfo.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	
	static Connection con;
	
	public static Connection getConnection(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			if(con==null){
				con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","CFO","finance");
				return con;
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		con.close();
		super.finalize();
	}
}
