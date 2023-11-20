package com.dido.springboot.demo.countrytourapp.entity;

import java.util.List;
import java.util.Map;

public class Country {

	private String name;
	private String alpha2Code;
	private List<String> borders;
	private Map<String, Currency> currencies;
	
	public Country() {
		
	}

	public Country(String name, String alpha2Code, List<String> borders, Map<String, Currency> currencies) {
		this.name = name;
		this.alpha2Code = alpha2Code;
		this.borders = borders;
		this.currencies = currencies;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlpha2Code() {
		return alpha2Code;
	}

	public void setAlpha2Code(String alpha2Code) {
		this.alpha2Code = alpha2Code;
	}

	public List<String> getBorders() {
		return borders;
	}

	public void setBorders(List<String> borders) {
		this.borders = borders;
	}

	public Map<String, Currency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Map<String, Currency> currencies) {
		this.currencies = currencies;
	}
	
	
}
