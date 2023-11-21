package com.dido.springboot.demo.countrytourapp.services;

import java.util.List;
import java.util.Map;

import com.dido.springboot.demo.countrytourapp.entity.Country;
import com.dido.springboot.demo.countrytourapp.entity.CountryBudgetInfo;
import com.dido.springboot.demo.countrytourapp.entity.CurrencyRate;

public interface CountryRateService {
	
	List<Country> loadCountries(String code);
	Country loadCountry(String countryCode);
	List<Country> getNeighbours(String countryCodes);
	Map<String,CurrencyRate> getNeighboursMap(String countryCode, String currencyCode);
	CurrencyRate getCountryRates(String currencyCode,String targetCurrency);

	CountryBudgetInfo calculateCountryBudgetInfo(String budgetPerCountry, String totalBudget, String countryCode, String currency);
}
