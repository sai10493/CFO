package com.verizon.cfo.connection;



public class BillDetails {
	long accNo;
	String billCycleDate;
	double billedAmount;
	double amountReceived;
	String paymentDate;
	public long getAccNo() {
		return accNo;
	}
	public void setAccNo(long accNo) {
		this.accNo = accNo;
	}
	public String getBillCycleDate() {
		return billCycleDate;
	}
	public void setBillCycleDate(String billCycleDate) {
		this.billCycleDate = billCycleDate;
	}
	public double getBilledAmount() {
		return billedAmount;
	}
	public void setBilledAmount(double billedAmount) {
		this.billedAmount = billedAmount;
	}
	public double getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(double amountReceived) {
		this.amountReceived = amountReceived;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	
}
