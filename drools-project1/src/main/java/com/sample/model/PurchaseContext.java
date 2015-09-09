package com.sample.model;

import java.util.Calendar;

public class PurchaseContext {

	private Calendar purchaseDate;
	
	// calculated fields
	private int month;

	public Calendar getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Calendar purchaseDate) {
		this.purchaseDate = purchaseDate;
		this.month = purchaseDate.get(Calendar.MONTH);
	}

	@Override
	public String toString() {
		return "PurchaseContext [purchaseDate=" + purchaseDate + "]";
	}


	public int getMonth() {
		return month;
	}
	
}
