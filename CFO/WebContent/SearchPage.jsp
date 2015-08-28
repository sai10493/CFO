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
		int p2p_days=0,days_elapsed=0,p2p_count=0;
		PreparedStatement ps1 = con.prepareStatement("select p2p_days,p2p_count from dlqtable where account_number=?");
		ps1.setString(1,request.getParameter("search"));
		
		ResultSet rs=ps.executeQuery();
		ResultSet rs1=ps1.executeQuery();
		while(rs1.next()){
			p2p_days=rs1.getInt(1);
			p2p_count=rs1.getInt(2);
		}
		if(!rs.isBeforeFirst()){
			out.println("<br><br><div><center>Record not found</center></div>");
		}else{
		
%>

<!-- Table to store the result fetched -->
<br>
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
{ days_elapsed=rs.getInt(11);%>
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
	<br>
	<br>
	<%if(p2p_days>days_elapsed){ %>
	<form id="form2" action="AddP2PHome" method="get">
	<center><input type="text" name="p2p">
	<input type="hidden" id="accountNumber" name="accountNumber" value="<%=request.getParameter("search")%>"/>
	<input type="submit" value="Add P2P" disabled/>  <b>Currently in p2p</b> </center>
    </form>
    <% }else if(p2p_count>=2){%>
	<br/>
	<br/>
	<form id="form2" action="AddP2PHome" method="get">
	<center><input type="text" name="p2p">
	<input type="hidden" id="accountNumber" name="accountNumber" value="<%=request.getParameter("search")%>"/>
	<input type="submit" value="Add P2P" disabled/>  <b>P2P limit exceeded</b></center>
    </form>
    <br>
    <br>
    <%} else{ %>
	<br/>
	<br/>
	<form id="form2" action="AddP2PHome" method="get">
	<center><input type="text" name="p2p">
	<input type="hidden" id="accountNumber" name="accountNumber" value="<%=request.getParameter("search")%>"/>
	<input type="submit" value="Add P2P"/></center>
    </form>
    <br>
    <br>
    <%} %>
	<table border=2 class="TFtable" align="center">
	<tr>
	<th>Action Date</th>
	<th>Action Type</th>
	<th>Description</th>
	</tr>
	<% }ps=con.prepareStatement("select * from action_taken where account_number=? order by action_date desc");
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

</body>
</html>