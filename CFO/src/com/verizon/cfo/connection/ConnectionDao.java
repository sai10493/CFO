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
	public void insertData(long accNo, Date billCycleDate,double billedAmount,double amountReceived, Date paymentDate ){
		try {
			con=ConnectionUtil.getConnection();
			System.out.println("Connection established");
			
			ps = con.prepareStatement("insert into billreceived values(?,?,?,?,?)");
			ps.setLong(1, accNo);
			ps.setDate(2, new java.sql.Date(billCycleDate.getTime()));
			ps.setDouble(3, billedAmount);
			ps.setDouble(4, amountReceived);
			ps.setDate(5, new java.sql.Date(paymentDate.getTime()));
			ps.executeUpdate();
			ps.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		ps.setDate(4, new java.sql.Date(paymentDate.getTime()));
		ps.setLong(5, accNo);
		ps.executeUpdate();
		ps.close();
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	 }
    }
	
	
	
	public void updateDlqTable(long accNo, Date billCycleDate,double billedAmount,double amountReceived, Date paymentDate) throws ParseException, SQLException{
		
		con=ConnectionUtil.getConnection();
		PreparedStatement ps;
		int p2pDays =0, stage;
		
		String sysDate = new SimpleDateFormat("dd/MMM/YYYY").format(new Date());
		Date currDate = new SimpleDateFormat("dd/MMM/yyyy").parse(sysDate);
		
		int daysElapsed = (int) (((currDate.getTime() - billCycleDate.getTime()) / (24 * 60 * 1000 * 60))-15);
		
		String operation = classification(accNo,billedAmount,amountReceived,daysElapsed);
		//System.out.println(operation);
		switch (operation) { 
		case "delete": { 
			//System.out.println("Into Delete");
			String delQuery = "delete from dlqtable where account_number = ?";
			ps = con.prepareStatement(delQuery);
			ps.setLong(1, accNo);
			ps.execute();
			ps.close();
		}
			break;
			
		case "insert": {
			//System.out.println("Inserting data into dlqtable.");

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
			ps.setInt(4, stage);
			ps.setInt(5, flag);
			ps.execute();
			ps.close();

		}
			break;

		case "update": {

			//System.out.println("into update");

			ps = con.prepareStatement("select p2p_Days from dlqtable where account_number = ?");
			ps.setLong(1, accNo);
			ResultSet rs = ps.executeQuery();
			rs.next();
			p2pDays = rs.getInt("p2p_days");

			//System.out.println("P2P days: " + p2pDays);
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
				ps.setInt(3, stage);
				ps.setInt(4, flag);
				ps.setLong(5, accNo);
				ps.execute();
				ps.close();

			} else {

				String updateQuery = "update dlqtable set days_elapsed=?, due_amount = ?, flag=? where account_number=?";
				ps = con.prepareStatement(updateQuery);

				ps.setLong(1, daysElapsed);
				ps.setDouble(2, (billedAmount - amountReceived));

				ps.setInt(3, flag);
				ps.setLong(4, accNo);
				ps.execute();
				ps.close();

			}

		}
			break;

		}
		
	} 
	
	public String classification(long accno,double billedAmount,double amountReceived,int daysElapsed) { 																		
		//System.out.println("Entered update");
		con = ConnectionUtil.getConnection();
		String operation=" ";
		String query = "select * from dlqtable where account_number = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setLong(1, accno);
			ResultSet rs = ps.executeQuery();
			boolean st=rs.next();
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
	
	public int status(int daysElapsed) { 
		int stage = 0; 
		if (daysElapsed >= 1 && daysElapsed <= 3) {
			stage = 1;
		} else if (daysElapsed >= 4 && daysElapsed <= 6) {
			stage = 4;
		} else if (daysElapsed >= 7 && daysElapsed <= 10) {
			stage = 7;
		} else if (daysElapsed >= 11 && daysElapsed <= 17) {
			stage = 11;
		} else if (daysElapsed >= 18 && daysElapsed <= 20) {
			stage = 18;
		} else if (daysElapsed == 21) {
			stage = 21;
		} else if (daysElapsed >= 22 && daysElapsed <= 24) {
			stage = 22;
		} else if (daysElapsed >= 25 && daysElapsed <= 29) {
			stage = 25;
		} else if (daysElapsed == 30) {
			stage = 30;
		}

		return stage;
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
