package com.verizon.cfo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.verizon.cfo.connection.BillDetails;
import com.verizon.cfo.connection.ConnectionDao;

@WebServlet("/DataServlet")
public class DataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public DataServlet() {
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url= " ";
		BillDetails bd = new BillDetails();
		JsonReader jr = new JsonReader();
		try {
			JSONObject json = jr.readJsonFromUrl(url);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		long accNo = bd.getAccNo();
		String billCycleDate = bd.getBillCycleDate();
		double billedAmount = bd.getBilledAmount();
		double amountReceived = bd.getAmountReceived();
		String paymentDate = bd.getPaymentDate();
		ConnectionDao cd = new ConnectionDao();
		if(cd.checkAccNo(bd.getAccNo())){
			cd.updateData(accNo, billCycleDate, billedAmount, amountReceived, paymentDate);
			
		}
		else{
			cd.insertData(accNo, billCycleDate, billedAmount, amountReceived, paymentDate);
		}
		
		
	}

}
