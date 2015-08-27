package com.verizon.cfo.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SendToOMServlet
 */
@WebServlet("/SendToOMServlet")
public class SendToOMServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		if(request.getParameter("collSendOM")!=null){
			SendToOM.sendToOM();
			System.out.println("Success!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			response.sendRedirect("/CFO/CollectionsHome.html");
		}
		
		
		
	}

}
