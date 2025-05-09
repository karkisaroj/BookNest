package com.booknest.model;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentModel {
	private int paymentID;
	private int orderID;
	private Date payment_date;
	private BigDecimal payment_amount;
	private String payment_method;
	private String payment_status;

	// Constructor
	public PaymentModel(int paymentID, int orderID, Date payment_date, BigDecimal payment_amount, String payment_method,
			String payment_status) {
		this.paymentID = paymentID;
		this.orderID = orderID;
		this.payment_date = payment_date;
		this.payment_amount = payment_amount;
		this.payment_method = payment_method;
		this.payment_status = payment_status;
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

	public Date getPayment_date() {
		return payment_date;
	}

	public void setPayment_date(Date payment_date) {
		this.payment_date = payment_date;
	}

	public BigDecimal getPayment_amount() {
		return payment_amount;
	}

	public void setPayment_amount(BigDecimal payment_amount) {
		this.payment_amount = payment_amount;
	}

	public String getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	public String getPayment_status() {
		return payment_status;
	}

	public void setPayment_status(String payment_status) {
		this.payment_status = payment_status;
	}

	@Override
	public String toString() {
		return "PaymentModel [paymentID=" + paymentID + ", orderID=" + orderID + ", payment_date=" + payment_date
				+ ", payment_amount=" + payment_amount + ", payment_method=" + payment_method + ", payment_status="
				+ payment_status + "]";
	}
}