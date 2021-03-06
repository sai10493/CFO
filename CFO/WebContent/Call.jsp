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
ResultSet rs=st.executeQuery("select d.account_number,f.firstname,f.lastname,(sysdate-d.days_elapsed) as duedate,d.days_elapsed,"
        +"d.due_amount,d.status,d.flag,d.p2p_days,f.contactnumber, d.p2p_count"
        +" from dlqtable d, fincustomerdata f WHERE d.account_number= f.customerid and (d.days_elapsed=1 or d.days_elapsed=4 or d.days_elapsed=11 or d.days_elapsed=25) ");
%>

<!-- Table creation to store the list fetched -->
<table class="TFtable" border=1>
<tr><th>Account Number</th>
	<th>First Name</th>
	<th>Last Name</th>
	<th>Due Date</th>
	<th>Days Elapsed</th>
	<th>Due Amount</th>
	<th>Status</th>
	<th>Live</th>
	<th>Promise Days</th>
	<th>Phone number</th>
	<th>Action</th>
	<th>p2p Submit</th></tr>
	<tr>
	

<% while(rs.next())
{int p2pCount=rs.getInt(11);
int p2p_days=rs.getInt(9);
int days_elapsed=rs.getInt(5);
int live=rs.getInt(8);%>
	<td><%= rs.getInt(1) %></td>
	<td><%= rs.getString(2) %></td>
	<td><%= rs.getString(3) %></td>
	<td><%= rs.getDate(4) %></td>
	<td><%= rs.getInt(5) %></td>
	<td><%= rs.getInt(6) %></td>
	<td><%= rs.getString(7) %></td>
	<%if(live==1){ %>
	<td>Yes</td>
	<%}else{ %>
	<td>No</td>
	<%}if((rs.getInt(9)-rs.getInt(5)) < 0){%>
	<td><%= 0 %></td>
	<% }
	else {%><td><%= (rs.getInt(9)-rs.getInt(5)) %></td><% ;}%>
	<td><%= rs.getLong(10) %></td>

	<td>
	<form id="formsub" action="MakeCall" method="get">
	<input type="text" name="description"/>
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<center><input type="submit" value="Submit"/></center>
	</form>
	</td>
	<%if(p2p_days>days_elapsed){ %>
	<td>Currently in p2p</td>
	<%}
	else if(p2pCount<2)
		{
		%><td>
	<form id="form2" action="AddP2P" method="get">
	<input type="text" name="p2p">
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<center><input type="submit" value="Add P2P"/></center>
	</form></td><%
		}else { %>
	<td>P2P limit exceeded</td>
	<%} %>
	</tr>
<%}

} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

%>

</table>
</body>
</html>