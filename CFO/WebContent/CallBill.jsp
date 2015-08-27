<%@page import="com.verizon.cfo.connection.ConnectionUtil"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<% 
try
{
	Connection con = ConnectionUtil.getConnection();
    System.out.println("Connection established");
    
 
//Statement for getting the list of customers for whom an emailshould be sent
Statement st=con.createStatement();
ResultSet rs=st.executeQuery("SELECT * FROM billreceived");
%>
<!-- Table to store the result fetched -->
<table class="TFtable">
<tr><th>Account Number</th>
	<th>Bill Cycle Date</th>
	<th>Bill Amount</th>
	<th>Amount Received</th>
	<th>Payment Date</th>
	</tr>
	
<% while(rs.next()) //loop through theresult
{ %>
	<tr>
	<td><%= rs.getInt(1) %></td>
	<td><%= rs.getDate(2) %></td>
	<td><%= rs.getFloat(3) %></td>
	<td><%= rs.getFloat(4) %></td>
	<td><%= rs.getDate(5) %></td></tr>
<%}
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}%>
</table>
</body>
</html>