<%@page import="com.verizon.cfo.connection.ConnectionUtil"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*" %>
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
<script>
function Call()
{
var x=$(accountNumber).val();
window.op
}
</script>
</head>

<body>
<%

//Establish Connection

try
{
	Connection con = ConnectionUtil.getConnection();
    System.out.println("Connection established");
    
    

//Statement for getting the list of customers for whom an emailshould be sent
Statement st=con.createStatement();
ResultSet rs=st.executeQuery("SELECT * FROM dlqtable where status = 7 or status = 18");
%>

<!-- Table to store the result fetched -->
<table class="TFtable">
<tr><th>Account Number</th>
	<th>Days Elapsed</th>
	<th>Due Amount</th>
	<th>Status</th>
	<th>Flag</th>
	<th>Promise Days</th>
	<th>Action</th></tr>
	<tr>

<% while(rs.next()) //loop through theresult
{ 
	int emailCount=rs.getInt(8);%>
	
	<td><%= rs.getInt(1) %></td>
	<td><%= rs.getInt(2) %></td>
	<td><%= rs.getInt(3) %></td>
	<td><%= rs.getInt(4) %></td>
	<td><%= rs.getInt(5) %></td>
	<td><%= rs.getString(6) %></td>
	<%if(emailCount==0) {%>
	<td>		
	<form id="formsub" action="SendEmail" method="get"> <!-- form to take the accountNumber to the servlet -->
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="submit" value="Send Mail"/>
	</form>
	</td>
	<% } 
	else {%>
	
	<td>
	<form id="formsub2" action="DisplayMail.jsp">
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="button" value="View Mail" onclick="Call()"/>
	</form>
	</td>
	<% } %>
	</tr>
	<% }
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}%>
</table>
</body>
</html>