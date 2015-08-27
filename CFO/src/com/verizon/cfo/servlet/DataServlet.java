package com.verizon.cfo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;




import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.verizon.cfo.connection.ConnectionDao;
import com.verizon.cfo.connection.ConnectionUtil;

@WebServlet("/DataServlet")
public class DataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public DataServlet() {
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url= "http://www.json-generator.com/api/json/get/ckzeODIWBe?indent=2";             
		JSONObject jsonObj=null;
		JsonReader jr = new JsonReader();
		JSONArray jsonArr;
		ConnectionDao cd = new ConnectionDao();
		try {
		   jsonObj = jr.readJsonFromUrl(url);
		   jsonArr = jsonObj.getJSONArray("finance");
		   for(int i=0;i<jsonArr.length();i++){
			   long accNo = jsonArr.getJSONObject(i).getInt("accountNumber");
			   String bcd = jsonArr.getJSONObject(i).getString("billCycleDate");
			   double billedAmount = jsonArr.getJSONObject(i).getInt("billedAmount");
			   double amountReceived = jsonArr.getJSONObject(i).getInt("amountReceived");
			   String pd = jsonArr.getJSONObject(i).getString("paymentDate");
			   
			   Date billCycleDate = new SimpleDateFormat("dd-MMM-yyyy").parse(bcd);
			   Date paymentDate = new SimpleDateFormat("dd-MMM-yyyy").parse(pd);
			  
			   
			   
			   	
			   if(cd.checkAccNo(accNo)){
					cd.updateData(accNo, billCycleDate, billedAmount, amountReceived, paymentDate);
					
				}
				else{
					cd.insertData(accNo, billCycleDate, billedAmount, amountReceived, paymentDate);
				}
			   cd.updateDlqTable(accNo, billCycleDate, billedAmount, amountReceived, paymentDate);
		   }
		   response.setContentType("text/html");
		   PrintWriter out = response.getWriter();
		  
		   out.println("Billing details updated");
		   
		} catch (JSONException | ParseException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//response.sendRedirect("http://localhost:8080/CFO/CollectionsHome.html");
	
	}

	
	@Override
	public void destroy() {
		ConnectionUtil.closeConnection();
		System.out.println("Connection closed");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
	}

}
