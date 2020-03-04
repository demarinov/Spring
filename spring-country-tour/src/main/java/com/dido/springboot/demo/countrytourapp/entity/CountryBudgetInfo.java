package com.dido.springboot.demo.countrytourapp.entity;

import java.util.Map;

public class CountryBudgetInfo {

	private Map<String, CurrencyRate> countryRates;

	private int numberOfTours;
	private String leftOverBudget;
	private Map<String, String> convertedCountryTourBudgets;

	public String getLeftOverBudget() {
		return leftOverBudget;
	}

	public void setLeftOverBudget(String leftOverBudget) {
		this.leftOverBudget = leftOverBudget;
	}

	public Map<String, CurrencyRate> getCountryRates() {
		return countryRates;
	}

	public void setCountryRates(Map<String, CurrencyRate> countryRates) {
		this.countryRates = countryRates;
	}

	public Map<String, String> getConvertedCountryTourBudgets() {
		return convertedCountryTourBudgets;
	}

	public void setConvertedCountryTourBudgets(Map<String, String> convertedCountryTourBudgets) {
		this.convertedCountryTourBudgets = convertedCountryTourBudgets;
	}

	public CountryBudgetInfo() {

	}

	public int getNumberOfTours() {
		return numberOfTours;
	}

	public void setNumberOfTours(int numberOfTours) {
		this.numberOfTours = numberOfTours;
	}

}
