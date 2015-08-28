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

//Establish Connection

try
{
	Connection con = ConnectionUtil.getConnection();
    System.out.println("Connection established");
 
	//Statement for getting the list of customers for whom an emailshould be sent
	PreparedStatement ps=con.prepareStatement("SELECT description FROM action_taken WHERE account_number=? and status='Send letter'");
	ps.setString(1,request.getParameter("accountnumber"));
	
	ResultSet rs=ps.executeQuery();
	while(rs.next())
	{%>
	<p><%= rs.getString(1) %></p><br/><hr><br/>
	<%}
	}catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}System.out.println("Into View Letter");%>


</body>
</html>