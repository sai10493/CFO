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
		String url= "http://192.168.1.42:8080/BillingSystem/rest/finance";             
		JSONObject jsonObj=null;
		JsonReader jr = new JsonReader();
		JSONArray jsonArr;
		ConnectionDao cd = new ConnectionDao();
		try {
		   jsonObj = jr.readJsonFromUrl(url);
		   jsonArr = jsonObj.getJSONArray("payments");
		   for(int i=0;i<jsonArr.length();i++){
			   long accNo = jsonArr.getJSONObject(i).getJSONObject("accountNumber").getInt("num");
			   String bcd = jsonArr.getJSONObject(i).getJSONObject("billCycleDate").getString("value");;
			   double billedAmount = jsonArr.getJSONObject(i).getJSONObject("billedAmount").getInt("num");
			   double amountReceived = jsonArr.getJSONObject(i).getJSONObject("amountReceived").getInt("num");
			   String pd = jsonArr.getJSONObject(i).getJSONObject("paymentDate").getString("value");
			   
			   Date billCycleDate = new SimpleDateFormat("dd/MMM/yyyy").parse(bcd);
			   Date paymentDate=null;
			   if(!pd.equalsIgnoreCase("not paid")){
				   paymentDate= new SimpleDateFormat("dd/MMM/yyyy").parse(pd);
			   }
			   
			   System.out.println("reading data "+accNo);
			   if(cd.checkAccNo(accNo)){
					cd.updateData(accNo, billCycleDate, billedAmount, amountReceived, paymentDate);
					
				}
				else{
					cd.insertData(accNo, billCycleDate, billedAmount, amountReceived, paymentDate);
				}
			   cd.updateDlqTable(accNo, billCycleDate, billedAmount, amountReceived);
		   }
		   response.setContentType("text/html");
		   PrintWriter out = response.getWriter();
		  
		   out.println("Billing details updated");
		   
		} catch (JSONException | ParseException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
	}

}
