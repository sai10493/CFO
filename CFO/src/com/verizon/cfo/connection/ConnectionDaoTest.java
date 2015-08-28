package com.verizon.cfo.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConnectionDaoTest {
ConnectionDao pst;
	@Before
	public void setUp() throws Exception {
	pst = new ConnectionDao();
	
	}

	@After
	public void tearDown() throws Exception {
 
		//pst.deleteData(100003 );
		Connection con = ConnectionUtil.getConnection();
		System.out.println("Connection established TEAR");
		PreparedStatement ps = con.prepareStatement("delete from billreceived where account_number = 100003");
		PreparedStatement pd = con.prepareStatement("delete from DlqTable where account_number IN (100903,100904)");
		ps.executeUpdate();
		pd.executeUpdate();
	}

	@Test
	public void testInsertData() {
		
		java.util.Date date=new java.util.Date("12-jan-2015");
		pst.insertData(100003,date, 5000,4500,date);	
		Assert.assertEquals(1, pst.r);
		}

	@Test
	public void testUpdateData() {
		
		java.util.Date date=new java.util.Date("14-jan-2015");
		pst.insertData(100003,date, 5000,4500,date);
		pst.updateData(100003,date, 6000,5500,date);	
		Assert.assertEquals(1, pst.r);
		
	}

	@Test
	public void testUpdateDlqTable() throws ParseException, SQLException {
	
		Connection con1=ConnectionUtil.getConnection();
		System.out.println("Connection established");
		PreparedStatement sl = con1.prepareStatement("insert into dlqtable values(100903,10,10000,,1,10");
		//sl.executeUpdate();
		
	java.util.Date date1=new java.util.Date("14-jan-2015");
	java.util.Date date2=new java.util.Date("28-feb-2015");
		
	pst.classification(100904, 10000, 8000, 10);
	if (pst.st==false)
	{
		//for insert	
        pst.updateDlqTable(100904, date1, 10000, 8000);
		Assert.assertEquals(false, pst.s);	
		
	}
	else
	{
	    //for update
	    pst.classification(100903, 10000, 8000, 10);
	    java.util.Date date3=new java.util.Date("30-Mar-2015");
	    pst.updateDlqTable(100903, date1, 5000, 4500);
	    Assert.assertEquals(true, pst.s);	
		
	    //for delete
	    java.util.Date date4=new java.util.Date("09-Apr-2015");
	    pst.updateDlqTable(100903, date1, 5000, 5000);
	    Assert.assertEquals(true, pst.s);	
	}
}

	@Test
	public void testClassification() throws SQLException, ParseException {
		java.util.Date date11=new java.util.Date("30-Mar-2015");
		pst.updateDlqTable(100904, date11, 10000, 8000);
		pst.classification(100904, 60000, 5000 , 10);
		Assert.assertEquals(true, pst.st);
	}


	@Test
	public void testCheckAccNo() {
		java.util.Date date8=new java.util.Date("09-Apr-2015");
		pst.insertData(100003,date8, 5000,4500,date8);
		pst.checkAccNo(100003);
		Assert.assertEquals(true, pst.s);
	}

	
}
