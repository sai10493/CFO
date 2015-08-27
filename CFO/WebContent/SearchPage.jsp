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
	Connection con = ConnectionUtil.getConnection();
    System.out.println("Connection established");
    
    

//Statement for getting the list of customers for whom a letter should be sent
//Statement st=con.createStatement();
PreparedStatement ps=con.prepareStatement("select d.account_number,f.firstname,f.lastname,f.streetname,f.zipcode,f.city,"
                                           +"f.state,f.country,f.contactnumber,"
                                           +"(sysdate-d.days_elapsed) as duedate,d.days_elapsed,"
                                           +"d.due_amount,d.status,d.flag from dlqtable d, fincustomerdata f"
                                           +" WHERE d.account_number= f.customerid and d.account_number=?");
		ps.setString(1,request.getParameter("search"));
		
		ResultSet rs=ps.executeQuery();
%>

<!-- Table to store the result fetched -->
<table border=2 class="TFtable">
<tr><th>Account Number</th>
    <th>First name</th>
    <th>Last name</th>
    <th>Street name</th>
    <th>Zipcode</th>
    <th>City</th>
    <th>State</th>
    <th>Country</th>
    <th>Contact</th>
    <th>Due date</th>
	<th>Days Elapsed</th>
	<th>Due Amount</th>
	<th>Status</th>
	<th>Live</th></tr>
	<tr>

<% while(rs.next()) //loop through theresult
{ %>
	<td><%= rs.getInt(1) %></td>
	<td><%= rs.getString(2) %></td>
	<td><%= rs.getString(3) %></td>
	<td><%= rs.getString(4) %></td>
	<td><%= rs.getInt(5) %></td>
	<td><%= rs.getString(6) %></td>
	<td><%= rs.getString(7) %></td>
	<td><%= rs.getString(8) %></td>
	<td><%= rs.getLong(9) %></td>
	<td><%= rs.getDate(10) %></td>
	<td><%= rs.getInt(11) %></td>
	<td><%= rs.getFloat(12) %></td>
	<td><%= rs.getString(13) %></td>
	<td><%= rs.getInt(14) %></td>
	</tr>
	<%}%>
	</table>
	<br/>
	<br/>
	<table border=2 class="TFtable" align="center">
	<tr>
	<th>Action Date</th>
	<th>Action Type</th>
	<th>Description</th>
	</tr>
	<% ps=con.prepareStatement("select * from action_taken where account_number=?");
	   ps.setString(1,request.getParameter("search"));
	   rs=ps.executeQuery();
	   
	  while(rs.next()) //loop through theresult
	   { %>
	   <tr>
	   	<td><%= rs.getDate(3) %></td>
	   	<td><%= rs.getString(4) %></td>
	   	<td><%= rs.getString(5) %></td>
	   </tr>
	
<%}
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}%>
</table>
<br/>
<br/>
<br/>
<form id="form2" action="AddP2PHome" method="get">
	<center><input type="text" name="p2p">
	<input type="hidden" id="accountNumber" name="accountNumber" value="<%=request.getParameter("search")%>"/>
	<input type="submit" value="Add P2P"/></center>
</form>
</body>
</html>