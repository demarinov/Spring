package com.dido.springboot.demo.countrytourapp.services;

import java.util.List;
import java.util.Map;

import com.dido.springboot.demo.countrytourapp.entity.Country;
import com.dido.springboot.demo.countrytourapp.entity.CurrencyRate;

public interface CountryRateService {
	
	public List<Country> loadCountries(String code);
	public Country loadCountry(String countryCode);
	public List<Country> getNeighbours(String countryCodes);
	public Map<String,CurrencyRate> getNeighboursMap(String countryCode, String currencyCode);
	public CurrencyRate getCountryRates(String currencyCode,String targetCurrency);
}
