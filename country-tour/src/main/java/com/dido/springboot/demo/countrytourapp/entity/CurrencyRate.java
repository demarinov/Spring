package com.dido.springboot.demo.countrytourapp.entity;

import java.util.Map;

public class CurrencyRate {

	private Map<String,Double> rates;
	private String base;
	
	public CurrencyRate() {
		
	}

	public CurrencyRate(Map<String, Double> rates, String base) {
		this.rates = rates;
		this.base = base;
	}

	public Map<String, Double> getRates() {
		return rates;
	}

	public void setRates(Map<String, Double> rates) {
		for(String key : rates.keySet()) {
			Double rateVal = rates.get(key);
			rates.put(key, (double)Math.round(rateVal * 100.0)/100.0);
		}
		this.rates = rates;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}
	
	
	
	
}
