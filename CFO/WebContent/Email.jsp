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
$(function(){
	$(document).on("click","#check",function(){
		
		window.open('ViewMail.jsp'+'?accountnumber='+this.name);
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
    
 
//Statement for getting the list of customers for whom an emailshould be sent
Statement st=con.createStatement();
ResultSet rs=st.executeQuery("select d.account_number,f.firstname,f.lastname,(sysdate-d.days_elapsed) as duedate,d.days_elapsed,"
		                      +"d.due_amount,d.status,d.flag,d.p2p_days,f.email,d.email_count"
		                      +" from dlqtable d, fincustomerdata f WHERE d.account_number= f.customerid and"
		                      +" (d.days_elapsed=7 or d.days_elapsed=18)");
%>

<!-- Table to store the result fetched -->
<table class="TFtable">
<tr><th>Account Number</th>
	<th>First Name</th>
	<th>Last Name</th>
	<th width="10%">Due Date</th>
	<th>Days Elapsed</th>
	<th>Due Amount</th>
	<th>Status</th>
	<th>Live</th>
	<th>Promise Days</th>
	<th>Email Id</th>
	<th width="15%">Action</th></tr>
	<tr>

<% while(rs.next()) //loop through theresult
{ 
	int emailCount=rs.getInt(11);
	int daysElapsed=rs.getInt(5);
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
    <%} %>
	<td><%= rs.getInt(9) %></td>
	<td><%= rs.getString(10) %></td>
	<!-- Column Condition -->
	<%if(emailCount==0 && daysElapsed==7 ) {%>
	<td>		
	<form id="formsub" action="SendEmail" method="get"> <!-- form to take the accountNumber to the servlet -->
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="submit" value="Send Mail"/>
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="button" name="<%=rs.getInt(1) %>" id="check" value="View Mail" onclick="Call()" disabled/>
	</form>
	</td>
	</tr>
	<% } else if(emailCount==1 && daysElapsed==7) {%>
	<td>		
	<form id="formsub" action="SendEmail" method="get"> <!-- form to take the accountNumber to the servlet -->
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="submit" value="Send Mail" disabled/>
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="button" name="<%=rs.getInt(1) %>" id="check" value="View Mail" onclick="Call()"/>
	</form>
	</td>
	</tr>
	<%} else if(emailCount==1 && daysElapsed==18) {%>
	<td>		
	<form id="formsub" action="SendEmail" method="get"> <!-- form to take the accountNumber to the servlet -->
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="submit" value="Send Mail"/>
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="button" name="<%=rs.getInt(1) %>" id="check" value="View Mail" onclick="Call()"/>
	</form>
	</td>
	</tr>
	<%} else if(emailCount==2 && daysElapsed==18){%>
	<td>		
	<form id="formsub" action="SendEmail" method="get"> <!-- form to take the accountNumber to the servlet -->
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="submit" value="Send Mail" disabled/>
	<input type="hidden" id="accountNumber" name="accountNumber" value='<%=rs.getInt(1) %>'/>
	<input type="button" value="View Mail" name="<%=rs.getInt(1) %>" id="check" onclick="Call()"/>
	</form>
	</td>
	</tr>
	<% }
	
	}
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}%>
</table>
</body>
</html>