package com.sample.model;

import java.math.BigDecimal;

public class Price {
	
	private BigDecimal price;     // the price without reduction
	private BigDecimal reduction; // the reduction amount
	

	public Price(BigDecimal price, BigDecimal reduction) {
		this.price = price;
		this.reduction = reduction;
	}
	public Price() {

	}
	public BigDecimal getNormalPrice() {
		return price;
	}
	public BigDecimal getReduction() {
		return reduction;
	}
	
	public BigDecimal getFinalPrice() {
		return price.subtract(reduction);
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public void setReduction(BigDecimal reduction) {
		this.reduction = reduction;
	}
	@Override
	public String toString() {
		return "Price [price=" + price + ", reduction=" + reduction + "]";
	}
	
	

}
