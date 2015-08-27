package com.verizon.cfo.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.verizon.cfo.connection.ConnectionUtil;

@WebServlet("/MakeCall")
public class MakeCall extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Connection con;
	String status;
	String message;
	
    public MakeCall() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		con = ConnectionUtil.getConnection();
		response.setContentType("text/html");
		
		String accNo=request.getParameter("accountNumber");
		message=request.getParameter("description");
		try {
			PreparedStatement ps=con.prepareStatement("SELECT * FROM dlqtable WHERE ACCOUNT_NUMBER=? ");
			ps.setString(1, accNo);
			
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				status=rs.getString(4);
			}
			
			ps.close();
			
			java.util.Date utilDate = new Date();
			
			// Convert it to java.sql.Date
			java.sql.Date date = new java.sql.Date(utilDate.getTime());
			
			
			PreparedStatement ps1=con.prepareStatement("INSERT INTO action_taken VALUES(?,?,?,?,?)");
			ps1.setString(1, accNo);
			ps1.setString(2, status);
			ps1.setDate(3, date);
			ps1.setString(4, "Call");
			ps1.setString(5,""+message);
			
			ResultSet rs1=ps1.executeQuery();
			
			response.sendRedirect("http://localhost:8080/CFO/RepPage.html#menu2");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
