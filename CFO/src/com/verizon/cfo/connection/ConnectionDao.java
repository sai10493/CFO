package com.verizon.cfo.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectionDao {
	Connection con;
	PreparedStatement ps;
	public void insertData(long accNo, String billCycleDate,double billedAmount,double amountReceived, String paymentDate ){
		try {
			con=ConnectionUtil.getConnection();
			System.out.println("Connection established");
			Date bcd = new SimpleDateFormat("dd-MMM-yyyy").parse(billCycleDate);
			Date pd = new SimpleDateFormat("dd-MMM-yyyy").parse(paymentDate);
			ps = con.prepareStatement("insert into billreceived values(?,?,?,?,?)");
			ps.setLong(1, accNo);
			ps.setDate(2, new java.sql.Date(bcd.getTime()));
			ps.setDouble(3, billedAmount);
			ps.setDouble(4, amountReceived);
			ps.setDate(5, new java.sql.Date(pd.getTime()));
			ps.executeUpdate();
			ps.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void updateData(long accNo, String billCycleDate,double billedAmount,double amountReceived, String paymentDate){
		try{
		con = ConnectionUtil.getConnection();
		Date bcd = new SimpleDateFormat("dd-MMM-yyyy").parse(billCycleDate);
		Date pd = new SimpleDateFormat("dd-MMM-yyyy").parse(paymentDate);
		ps = con.prepareStatement("update billreceived set bill_cycle_date=?, billed_amount=?, amount_received=?, payment_date=? where account_number=?");
		
		ps.setDate(1, new java.sql.Date(bcd.getTime()));
		ps.setDouble(2, billedAmount);
		ps.setDouble(3, amountReceived);
		ps.setDate(4, new java.sql.Date(pd.getTime()));
		ps.setLong(5, accNo);
		ps.executeUpdate();
		ps.close();
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	
	
	public boolean checkAccNo(long accNo){
		try {
			con = ConnectionUtil.getConnection();
			ps = con.prepareStatement("select * from billreceived where account_number=?");
			ps.setLong(1, accNo);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
	
	public void displayData(){
		try {
			con=ConnectionUtil.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("Select * from billreceived");
		
			while(rs.next()){
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
