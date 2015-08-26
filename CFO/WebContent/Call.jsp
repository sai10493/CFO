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
ResultSet rs=st.executeQuery("SELECT * FROM Dlqtable where status = 1 or status = 4 or status = 11 or status=25");
%>

<!-- Table creation to store the list fetched -->
<table class="TFtable" border=1>
<tr><th>Account Number</th>
	<th>Days Elapsed</th>
	<th>Due Amount</th>
	<th>Status</th>
	<th>Flag</th>
	<th>Promise days</th>
	<th>Action</th>
	<th>p2p Submit</th></tr>
	<tr>
	

<% while(rs.next())
{int p2pCount=rs.getInt(7);%>
	<td><%= rs.getInt(1) %></td>
	<td><%= rs.getInt(2) %></td>
	<td><%= rs.getInt(3) %></td>
	<td><%= rs.getInt(4) %></td>
	<td><%= rs.getInt(5) %></td>
	<td><%= rs.getString(6) %></td>
	<td>
	<form id="formsub" action="MakeCall" method="get">
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="submit" value="Make Call"/>
	</form>
	</td>
	<%if(p2pCount<2){ %>
	<td>
	<form id="form2" action="AddP2P" method="get">
	<input type="text" name="p2p">
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="submit" value="Add P2P"/>
	</form></td><%}
	else
		{
		%><td>p2p limit exceeded</td><%
		}%></tr>
<%}

} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}%>

</table>
</body>
</html>