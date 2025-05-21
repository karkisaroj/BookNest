package com.booknest.model;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentModel {
	private int paymentID;
	private int orderID;
	private Date paymentDate;
	private BigDecimal paymentAmount;
	private String paymentMethod;
	private String paymentStatus;

	// Constructor
	public PaymentModel(int paymentID, int orderID, Date paymentDate, BigDecimal paymentAmount, String paymentMethod,
			String paymentStatus) {
		this.paymentID = paymentID;
		this.orderID = orderID;
		this.paymentDate = paymentDate;
		this.paymentAmount = paymentAmount;
		this.paymentMethod = paymentMethod;
		this.paymentStatus = paymentStatus;
	}

	// Default constructor
	public PaymentModel() {
	}

	// Getters and setters
	public int getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(int paymentID) {
		this.paymentID = paymentID;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	@Override
	public String toString() {
		return "PaymentModel [paymentID=" + paymentID + ", orderID=" + orderID + ", payment_date=" + paymentDate
				+ ", payment_amount=" + paymentAmount + ", payment_method=" + paymentMethod + ", payment_status="
				+ paymentStatus + "]";
	}
}