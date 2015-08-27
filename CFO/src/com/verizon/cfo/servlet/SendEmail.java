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
import javax.servlet.http.HttpSession;

import com.verizon.cfo.connection.ConnectionUtil;

@WebServlet("/SendEmail")
public class SendEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Connection con;
	String status;
	int email_count;

	public SendEmail() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		con = ConnectionUtil.getConnection();
		response.setContentType("text/html");

		String accNo = request.getParameter("accountNumber");
		int dueAmount = 0;
		Date dueDate=null;
		String emailId=null, firstname=null, email=null;

		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT (sysdate-days_elapsed), due_amount, status, email_count FROM dlqtable WHERE ACCOUNT_NUMBER=?");
			ps.setString(1, accNo);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				status = rs.getString(3);
				dueAmount = rs.getInt(2);
				dueDate = rs.getDate(1);
				email_count=rs.getInt(4);

			}
			email_count++;

			ps = con.prepareStatement("select email, firstname from fincustomerdata where customerid = ?");
			ps.setString(1, accNo);
			ResultSet rs2 = ps.executeQuery();
			while (rs2.next()) {
				emailId = rs2.getString(1);
				firstname = rs2.getString(2);
				email = "From: rep@twentyfifteen.com<br/>To: "+emailId+"<br/>Subject: Bill Pay Reminder<br/>Dear "+firstname
						+", <br>  Your Account Number: "+accNo+" has a bill due amount of "+dueAmount+" with Due Date: "+dueDate
						+".<br>Kindly pay the bill.<br>Thank you,<br>TwentyFifteen Corporation.";
			}
			ps.close();

			java.util.Date utilDate = new Date();

			// Convert it to java.sql.Date
			java.sql.Date date = new java.sql.Date(utilDate.getTime());

			PreparedStatement ps1 = con
					.prepareStatement("INSERT INTO action_taken VALUES(?,?,?,?,?)");
			ps1.setString(1, accNo);
			ps1.setString(2, status);
			ps1.setDate(3, date);
			ps1.setString(4, "email");
			ps1.setString(5, email);
			
			ps1.executeQuery();

			ps1=con.prepareStatement("UPDATE dlqtable SET email_count=? WHERE account_number=?");
			ps1.setInt(1, email_count);
			ps1.setString(2, accNo);
			ps1.executeQuery();
			response.sendRedirect("http://localhost:8080/CFO/RepPage.html#menu1");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
