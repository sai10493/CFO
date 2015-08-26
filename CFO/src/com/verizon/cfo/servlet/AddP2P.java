package com.verizon.cfo.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	int status;
	int p2pCount;
	
    public AddP2P() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		con=ConnectionUtil.getConnection();
		response.setContentType("text/html");
		
		String accNo=request.getParameter("accountNumber");
		String p2pDays=request.getParameter("p2p");
		
		try {
			
			PreparedStatement p=con.prepareStatement("SELECT p2p_count FROM dlqtable WHERE account_number=?");
			p.setString(1, accNo);
			
			
			ResultSet r=p.executeQuery();
			
			while(r.next())
			{
			p2pCount=r.getInt(1);
			//System.out.println(p2pCount);
			p2pCount++;
			}
			
			PreparedStatement ps=con.prepareStatement("UPDATE dlqtable SET p2p_days=?,p2p_count=? WHERE account_number=?");
			ps.setString(1, p2pDays);
			ps.setInt(2, p2pCount);
			ps.setString(3, accNo);
			
			ResultSet rs=ps.executeQuery();
			
			response.sendRedirect("http://localhost:8080/CFO/RepPage.html#menu2");
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
