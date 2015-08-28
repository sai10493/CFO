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
<script>
function Call()
{
$(function(){
	$(document).on("click","#check",function(){
		alert(this.name);
		window.open('ViewLetter.jsp'+'?accountnumber='+this.name);
	});
});
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

//Statement for getting the list of customers for whom a letter should be sent
Statement st=con.createStatement();
ResultSet rs=st.executeQuery("select d.account_number,f.firstname,f.lastname,f.streetname,f.zipcode,f.city,"
                               +"f.state,f.country,f.contactnumber,"
                              +"(sysdate-d.days_elapsed) as duedate,d.days_elapsed,"
                              +"d.due_amount,d.status,d.flag,d.letter_count"
                              +" from dlqtable d, fincustomerdata f WHERE d.account_number= f.customerid and d.days_elapsed=21 ");
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
	<th>Flag</th>
	<th width="20%">Action</th></tr>
	<tr>

<% while(rs.next()) //loop through theresult
{ int letterCount=rs.getInt(15);
	int daysElapsed=rs.getInt(11);%>
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
	<!-- Column Condition -->
	<%if(letterCount==0 && daysElapsed==21 ) {%>
	<td>		
	<form id="formsub" action="SendLetter" method="get"> <!-- form to take the accountNumber to the servlet -->
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="submit" value="Send Letter"/>
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="button" name="<%=rs.getInt(1) %>" id="check" value="View Letter" onclick="Call()" disabled/>
	</form>
	</td>
	</tr>
	<% } else if(letterCount==1 && daysElapsed==21) {%>
	<td>		
	<form id="formsub" action="SendLetter" method="get"> <!-- form to take the accountNumber to the servlet -->
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="submit" value="Send Letter" disabled/>
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="button" name="<%=rs.getInt(1) %>" id="check" value="View Letter" onclick="Call()"/>
	</form>
	</td>
	</tr>
	<%}
	
	}
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}%>
</table>
</body>
</html>