package com.sample;

import java.math.BigDecimal;

import com.sample.model.Price;
import com.sample.model.Product;

public class PriceCalculator {

	public static Price calculate(Product product) {
		// just return the product Price without any reduction
		return new Price(product.getBasicPrice(),new BigDecimal(0));
	}

}
