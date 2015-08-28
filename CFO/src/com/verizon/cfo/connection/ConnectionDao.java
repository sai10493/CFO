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
	//ConnectionDAO
	Connection con;
	int r=0;
	boolean s,st;
	PreparedStatement ps;
	public void insertData(long accNo, Date billCycleDate,double billedAmount,double amountReceived, Date paymentDate ){
		try {
			con=ConnectionUtil.getConnection();
			System.out.println("Connection established");
			if(paymentDate!=null){
			ps = con.prepareStatement("insert into billreceived values(?,?,?,?,?)");
			ps.setLong(1, accNo);
			ps.setDate(2, new java.sql.Date(billCycleDate.getTime()));
			ps.setDouble(3, billedAmount);
			ps.setDouble(4, amountReceived);
			ps.setDate(5, new java.sql.Date(paymentDate.getTime()));
			r =ps.executeUpdate();
			ps.close();
			}
			else{
				ps = con.prepareStatement("insert into billreceived values(?,?,?,?,?)");
				ps.setLong(1, accNo);
				ps.setDate(2, new java.sql.Date(billCycleDate.getTime()));
				ps.setDouble(3, billedAmount);
				ps.setDouble(4, amountReceived);
				ps.setString(5, null);
				r =ps.executeUpdate();
				ps.close();
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public void updateData(long accNo, Date billCycleDate,double billedAmount,double amountReceived, Date paymentDate){
		try{
		con = ConnectionUtil.getConnection();
		
        
		ps = con.prepareStatement("update billreceived set bill_cycle_date=?, billed_amount=?, amount_received=?, payment_date=? where account_number=?");
		
		ps.setDate(1, new java.sql.Date(billCycleDate.getTime()));
		ps.setDouble(2, billedAmount);
		ps.setDouble(3, amountReceived);
		if(paymentDate!=null){
		  ps.setDate(4, new java.sql.Date(paymentDate.getTime()));
		}
		else{
			ps.setString(4, null);
		}
			
		ps.setLong(5, accNo);
		
		r = ps.executeUpdate();
		ps.close();
        
	} catch (SQLException e) {
		e.printStackTrace();
	 }
    }
	
	
	
	public void updateDlqTable(long accNo, Date billCycleDate,double billedAmount,double amountReceived) throws ParseException, SQLException{
		
		con=ConnectionUtil.getConnection();
		PreparedStatement ps;
		int p2pDays =0;
		String stage="";
		
		String sysDate = new SimpleDateFormat("dd/MMM/YYYY").format(new Date());
		Date currDate = new SimpleDateFormat("dd/MMM/yyyy").parse(sysDate);
		
		int daysElapsed = (int) (((currDate.getTime() - billCycleDate.getTime()) / (24 * 60 * 1000 * 60))-15);
		
		String operation = classification(accNo,billedAmount,amountReceived,daysElapsed);
		
		switch (operation) { 
		case "delete": { 
			
			String delQuery = "delete from dlqtable where account_number = ?";
			ps = con.prepareStatement(delQuery);
			ps.setLong(1, accNo);
			s = ps.execute();
			ps.close();
		}
			break;
			
		case "insert": {

			stage = status(daysElapsed);

			int flag = 1;
			if (daysElapsed >= 30) {
				flag = 0;
			}
			String insertQuery = "insert into dlqtable (account_number,days_elapsed, due_amount, status, flag)"
					+ "values(?,?,?,?,?)";
			ps = con.prepareStatement(insertQuery);
			ps.setLong(1, accNo);
			ps.setLong(2, daysElapsed);
			ps.setDouble(3, (billedAmount - amountReceived));
			ps.setString(4, stage);
			ps.setInt(5, flag);
			s = ps.execute();
			ps.close();

		}
			break;

		case "update": {

			ps = con.prepareStatement("select p2p_Days from dlqtable where account_number = ?");
			ps.setLong(1, accNo);
			ResultSet rs = ps.executeQuery();
			rs.next();
			p2pDays = rs.getInt("p2p_days");

			ps.close();
			
			
			int flag = 1;
			if (daysElapsed >= 30) {
				flag = 0;
			}

			if (daysElapsed > p2pDays) {
				stage = status(daysElapsed);

				String updateQuery = "update dlqtable set days_elapsed=?, due_amount = ?, status=?, flag=? where account_number=?";
				ps = con.prepareStatement(updateQuery);

				ps.setLong(1, daysElapsed);
				ps.setDouble(2, (billedAmount - amountReceived));
				ps.setString(3, stage);
				ps.setInt(4, flag);
				ps.setLong(5, accNo);
				s = ps.execute();
				ps.close();

			} else {

				String updateQuery = "update dlqtable set days_elapsed=?, due_amount = ?, flag=? where account_number=?";
				ps = con.prepareStatement(updateQuery);

				ps.setLong(1, daysElapsed);
				ps.setDouble(2, (billedAmount - amountReceived));

				ps.setInt(3, flag);
				ps.setLong(4, accNo);
				s = ps.execute();
				ps.close();

			}

		}
			break;

		}
		
	} 
	
	public String classification(long accno,double billedAmount,double amountReceived,int daysElapsed) { 																		
		con = ConnectionUtil.getConnection();
		String operation=" ";
		String query = "select * from dlqtable where account_number = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setLong(1, accno);
			ResultSet rs = ps.executeQuery();
			st=rs.next();
			if (st == false && (billedAmount - amountReceived) != 0 && (daysElapsed>0)) {
				operation="insert";
			} else if (st == true && (billedAmount - amountReceived) == 0) {
				operation = "delete";
			} else if (st == true && (billedAmount - amountReceived) != 0 && (daysElapsed>0)) {
				operation="update";
			}
			ps.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return operation;
	}
	
	public String status(int daysElapsed) { 
		String stage = ""; 
		if (daysElapsed >= 1 && daysElapsed <= 3) {
			stage = "Reminder call";
		} else if (daysElapsed >= 4 && daysElapsed <= 6) {
			stage = "Reminder call";
		} else if (daysElapsed >= 7 && daysElapsed <= 10) {
			stage = "Send email";
		} else if (daysElapsed >= 11 && daysElapsed <= 17) {
			stage = "Reminder call";
		} else if (daysElapsed >= 18 && daysElapsed <= 20) {
			stage = "Send email";
		} else if (daysElapsed == 21) {
			stage = "Send letter";
		} else if (daysElapsed >= 22 && daysElapsed <= 24) {
			stage = "Send to next level for review";
		} else if (daysElapsed >= 25 && daysElapsed <= 29) {
			stage = "Reminder call";
		} else if (daysElapsed == 30) {
			stage = "Disconnect";
		}

		return stage;
	}
	
	public boolean checkAccNo(long accNo){
		try {
			con = ConnectionUtil.getConnection();
			ps = con.prepareStatement("select * from billreceived where account_number=?");
			ps.setLong(1, accNo);
			ResultSet rs = ps.executeQuery();
			s = rs.next();
			if(s){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
	
}
