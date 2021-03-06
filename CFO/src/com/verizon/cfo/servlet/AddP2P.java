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

@WebServlet("/AddP2P")
public class AddP2P extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Connection con;
	int p2pCount;
	String status;
	
    public AddP2P() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		con=ConnectionUtil.getConnection();
		response.setContentType("text/html");
		
		String accNo=request.getParameter("accountNumber");
		int p2pDays=Integer.parseInt(request.getParameter("p2p"));
		int daysElapsed=0;
		
		
		try {
			
			PreparedStatement p=con.prepareStatement("SELECT p2p_count, days_elapsed FROM dlqtable WHERE account_number=?");
			p.setString(1, accNo);
			
			
			ResultSet r=p.executeQuery();
			
			while(r.next())
			{
			p2pCount=r.getInt(1);
			daysElapsed = r.getInt(2);
			//System.out.println(p2pCount);
			p2pCount++;
			}
			
			PreparedStatement ps=con.prepareStatement("UPDATE dlqtable SET p2p_days=?,p2p_count=? WHERE account_number=?");
			p2pDays = p2pDays+daysElapsed;
			ps.setInt(1, p2pDays);
			ps.setInt(2, p2pCount);
			ps.setString(3, accNo);
			
			ResultSet rs=ps.executeQuery();
			
			java.util.Date utilDate = new Date();
			
			// Convert it to java.sql.Date
			java.sql.Date date = new java.sql.Date(utilDate.getTime());
			
			
			PreparedStatement ps1=con.prepareStatement("INSERT INTO action_taken VALUES(?,?,?,?,?)");
			ps1.setString(1, accNo);
			ps1.setString(2, status);
			ps1.setDate(3, date);
			ps1.setString(4, "P2P");
			ps1.setString(5,request.getParameter("p2p")+" p2p days added");
			
			ResultSet rs1=ps1.executeQuery();
			
			response.sendRedirect("http://localhost:8080/CFO/RepPage.html#menu2");
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
