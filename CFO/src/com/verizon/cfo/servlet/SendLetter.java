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

@WebServlet("/SendLetter")
public class SendLetter extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	//Global Variable for creating connection
	Connection con;
	//for fetching the status of the customer
	int status;
	
    public SendLetter() {
        super();
    }
    
    

	



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		con=ConnectionUtil.getConnection();
		response.setContentType("text/html");
		
		String accNo=request.getParameter("accountNumber");
		
		try {
			PreparedStatement ps=con.prepareStatement("SELECT * FROM Dlqtable WHERE ACCOUNT_NUMBER=?");
			ps.setString(1, accNo);
			
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				status=rs.getInt(5);//fetching the status of the customer
			}
			
			ps.close();
			
			//Getting the current system date
			java.util.Date utilDate = new Date();
			
			// Convert it to java.sql.Date
			java.sql.Date date = new java.sql.Date(utilDate.getTime());
			
			
			PreparedStatement ps1=con.prepareStatement("INSERT INTO action_taken VALUES(?,?,?,?)");
			ps1.setString(1, accNo);
			ps1.setInt(2, status);
			ps1.setDate(3, date);
			ps1.setString(4, "Letter");
			
			ps1.executeQuery();
			
			response.sendRedirect("http://localhost:8080/CFO/RepPage.html#menu3");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}