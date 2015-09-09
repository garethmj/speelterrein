package com.sample.model;

import java.math.BigDecimal;

public class Product {

	private String name;
	private BigDecimal basicPrice; // the price of this product before any reductions 
	
	
	public Product() {
	}
	
	public Product(String name, BigDecimal basicPrice) {
		this.name = name;
		this.basicPrice = basicPrice;
	}


	public String getName() {
		return name;
	}

	public BigDecimal getBasicPrice() {
		return basicPrice;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBasicPrice(BigDecimal basicPrice) {
		this.basicPrice = basicPrice;
	}
}
