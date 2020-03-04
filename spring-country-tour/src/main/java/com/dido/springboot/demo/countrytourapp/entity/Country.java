package com.dido.springboot.demo.countrytourapp.entity;

import java.util.List;

public class Country {

	private String name;
	private String alpha2Code;
	private List<String> borders;
	private List<Currency> currencies;
	
	public Country() {
		
	}

	public Country(String name, String alpha2Code, List<String> borders, List<Currency> currencies) {
		this.name = name;
		this.alpha2Code = alpha2Code;
		this.borders = borders;
		this.currencies = currencies;
	}

	public String getName() {
		return name;
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

	public List<Currency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<Currency> currencies) {
		this.currencies = currencies;
	}
	
	
}
