package com.verizon.cfo.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConnectionDao {
	
	public ConnectionDao(){
		
		
	}
	
	public void insertData(long accNo, String billCycleDate,double billedAmount,double amountReceived, String paymentDate ){
		try {
			Connection con=ConnectionUtil.getConnection();;
			Date bcd = new SimpleDateFormat("dd-MMM-yyyy").parse(billCycleDate);
			Date pd = new SimpleDateFormat("dd-MMM-yyyy").parse(paymentDate);
			PreparedStatement ps = con.prepareStatement("insert into billreceived values(?,?,?,?,?)");
			ps.setLong(1, accNo);
			ps.setDate(2, new java.sql.Date(bcd.getTime()));
			ps.setDouble(3, billedAmount);
			ps.setDouble(4, amountReceived);
			ps.setDate(5, new java.sql.Date(pd.getTime()));
			ps.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{ConnectionUtil.closeConnection();}
	}
	
	public void updateData(long accNo, String billCycleDate,double billedAmount,double amountReceived, String paymentDate){
		try{
		Connection con = ConnectionUtil.getConnection();
		Date bcd = new SimpleDateFormat("dd-MMM-yyyy").parse(billCycleDate);
		Date pd = new SimpleDateFormat("dd-MMM-yyyy").parse(paymentDate);
		PreparedStatement ps = con.prepareStatement("update billreceived set bill_cycle_date=?, billed_amount=?, amount_received=?, payment_date=? where account_number=?");
		
		ps.setDate(1, new java.sql.Date(bcd.getTime()));
		ps.setDouble(2, billedAmount);
		ps.setDouble(3, amountReceived);
		ps.setDate(4, new java.sql.Date(pd.getTime()));
		ps.setLong(5, accNo);
		ps.close();
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally{ConnectionUtil.closeConnection();}
	}
	
	public boolean checkAccNo(long accNo){
		
		try {
			Connection con = ConnectionUtil.getConnection();
			PreparedStatement ps = con.prepareStatement("select * from billreceived where accno=?");
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
}
