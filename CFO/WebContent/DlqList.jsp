<%@page import="com.verizon.cfo.connection.ConnectionUtil"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<!-- jQuery script loader -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	
<!-- style for the generated table -->	
<style>
table, th, td {
    
    border-collapse: collapse;
}
th, td {
    padding: 15px;
}
</style>
</head>
<body>

<%

//Establish Connection

try
{
  Connection con=ConnectionUtil.getConnection();
  System.out.println("Connection established");

//Statement for getting the list of customers for whom a call should be made
Statement st=con.createStatement();
ResultSet rs=st.executeQuery("select b.account_number,b.bill_cycle_date,b.billed_amount,b.amount_received,"
		                      + "d.due_amount,d.days_elapsed,(sysdate-d.days_elapsed) from dlqtable d,billreceived b where b.account_number=d.account_number");
%>

<!-- Table creation to store the list fetched -->
<table class="TFtable" border=1>
<tr><th>Account Number</th>
	<th>Bill Cycle Date</th>
	<th>Billed Amount</th>
	<th>Amount received</th>
	<th>Due Amount</th>
	<th>Due Date</th>
	<th>Days Elapsed</th>
	</tr>
	<tr>
	

<% while(rs.next())
{%>
	<td><%= rs.getInt(1) %></td>
	<td><%= rs.getDate(2) %></td>
	<td><%= rs.getFloat(3) %></td>
	<td><%= rs.getFloat(4) %></td>
	<td><%= rs.getFloat(5) %></td>
	<td><%= rs.getDate(7) %></td>
	<td><%= rs.getInt(6) %></td>
	</tr>
<%}

} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}%>

</table>
</body>
</html>