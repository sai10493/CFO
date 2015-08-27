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
	String status;
	int letter_count;
	
    public SendLetter() {
        super();
    }
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		con=ConnectionUtil.getConnection();
		response.setContentType("text/html");
		
		String accNo=request.getParameter("accountNumber");
		String firstName=null, lastName=null, streetName=null, city=null, state=null, country=null,letter=null;
		int zipcode=0;
		float dueAmount=0;
		
		
		try {
			PreparedStatement ps=con.prepareStatement("SELECT * FROM Dlqtable WHERE ACCOUNT_NUMBER=?");
			ps.setString(1, accNo);
			
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				status=rs.getString(5);//fetching the status of the customer
				letter_count=rs.getInt(9);
			}
			letter_count++;
			
			ps = con.prepareStatement("select firstname, lastname, streetname, city, state, country, zipcode, dueamount from fincustomerdata where customerid = ?");
			ps.setString(1, accNo);
			ResultSet rs2 = ps.executeQuery();
			while (rs2.next()) {
				firstName = rs2.getString(1);
				lastName = rs2.getString(2);
				streetName = rs2.getString(3);
				city = rs2.getString(4);
				state = rs2.getString(5);
				country = rs2.getString(6);
				zipcode = rs2.getInt(7);
				dueAmount = rs2.getFloat(8);
			
				letter = "Twenty Fifteen Corporation<br/>Address<br/>Pincode<br/><br/><br/>"+"Mr./Ms. "+lastName+"<br/><br/>"+streetName+"<br/>"+city+" - "
							+zipcode+"<br/><br/>"+"Dear Sir/Madam,<br/><br/>Subject: Bill Payment Due.<br/><br/>You have a pending bill of Rs. "+dueAmount
							+". <br/>Kindly pay the due amount.<br/><br/>Thank you,<br/>Twenty Fifteen Corporation.";
				
			}
			
			ps.close();
			
			//Getting the current system date
			java.util.Date utilDate = new Date();
			
			// Convert it to java.sql.Date
			java.sql.Date date = new java.sql.Date(utilDate.getTime());
			
			
			PreparedStatement ps1=con.prepareStatement("INSERT INTO action_taken VALUES(?,?,?,?,?)");
			ps1.setString(1, accNo);
			ps1.setString(2, status);
			ps1.setDate(3, date);
			ps1.setString(4, "Letter");
			ps1.setString(5, letter);
			ps1.executeQuery();
			
			ps1=con.prepareStatement("UPDATE dlqtable SET letter_count=? WHERE account_number=?");
			ps1.setInt(1, letter_count);
			ps1.setString(2, accNo);
			ps1.executeQuery();
			ps1.close();
			
			response.sendRedirect("http://localhost:8080/CFO/RepPage.html#menu3");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
