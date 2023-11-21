package com.dido.springboot.demo.countrytourapp.services;

import java.util.*;
import java.util.logging.Logger;

import com.dido.springboot.demo.countrytourapp.entity.CountryBudgetInfo;
import com.dido.springboot.demo.countrytourapp.entity.Currency;
import com.dido.springboot.demo.countrytourapp.rest.CountryTourRestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dido.springboot.demo.countrytourapp.entity.Country;
import com.dido.springboot.demo.countrytourapp.entity.CurrencyRate;

@Service
public class CountryRateServiceImpl implements CountryRateService {

	private RestTemplate restTemplate;
	private String restCountryUrl;
	private String restRatesUrl;
	private Logger logger = Logger.getLogger(getClass().getName());
	private Country startCountry;
	private List<Country> neighbourCountries;

	public CountryRateServiceImpl(RestTemplate restTemplate, @Value("${rest.country.url}") String restCountryUrl,
			@Value("${rest.rates.url}") String restRatesUrl) {
		this.restTemplate = restTemplate;
		this.restCountryUrl = restCountryUrl;
		this.restRatesUrl = restRatesUrl;

		logger.finer(getClass()+".CountryRateServiceImpl(): Loaded property:  rest.country.url=" + restCountryUrl + " ,rest.rates.url=" + restRatesUrl);
	}

	@Override
	public Country loadCountry(String countryCode) {

		// make REST call
		ResponseEntity<Object[]> responseEntity = restTemplate.exchange(restCountryUrl + countryCode, HttpMethod.GET,
				null, Object[].class);
		logger.finer(getClass()+".loadCountry(): country" + responseEntity.getBody());

//		startCountry = responseEntity.getBody();
		Map<String, Object> resultBody = (Map<String, Object>) Objects
				.requireNonNull(responseEntity.getBody())[0];

		startCountry = mapCountryBodyToCountry(resultBody);

		return startCountry;
	}

	private Country mapCountryBodyToCountry(Map<String, Object> resultBody) {

		Country country = new Country();

		country.setBorders((List<String>) resultBody.get("borders"));
		country.setName(((Map<String, String>)resultBody.get("name")).get("common"));
		country.setCurrencies((Map<String, Currency>) resultBody.get("currencies"));

		return country;
	}

	@Override
	public List<Country> loadCountries(String code) {

		// make REST call
		ResponseEntity<List<Object[]>> responseEntity = restTemplate.exchange(restCountryUrl + code, HttpMethod.GET,
				null, new ParameterizedTypeReference<List<Object[]>>() {
				});
		logger.finer(getClass()+".loadCountries(): countries" + responseEntity.getBody());

		List<Country> countries = new ArrayList<>();

		for(Object country : Objects.requireNonNull(responseEntity.getBody()).get(0)) {
			countries.add(mapCountryBodyToCountry((Map<String, Object>) country));
		}

		return countries;
	}

	@Override
	public List<Country> getNeighbours(String countryCode) {

		List<Country> countries = new ArrayList<>();
		Map<String, CurrencyRate> rateMap = new HashMap<>();

		loadCountry(countryCode);

		List<String> countryCodes = startCountry.getBorders();

		for (String cCode : countryCodes) {
			// make REST call
			ResponseEntity<Object[]> responseEntity = restTemplate.exchange(restCountryUrl + "alpha/" + cCode,
					HttpMethod.GET, null, Object[].class);
			countries.add(mapCountryBodyToCountry((Map<String, Object>) Objects
					.requireNonNull(responseEntity.getBody())[0]));


			rateMap.put(cCode, getCountryRates(countryCode, cCode));

		}

		neighbourCountries = countries;

		return neighbourCountries;
	}

	@Override
	public Map<String, CurrencyRate> getNeighboursMap(String countryCode, String currencyCode) {

		List<Country> countries = new ArrayList<>();
		Map<String, CurrencyRate> rateMap = new HashMap<>();

		loadCountry(countryCode);

		List<String> countryCodes = startCountry.getBorders();

		for (String cCode : countryCodes) {
			// make REST call
			ResponseEntity<Object[]> responseEntity = restTemplate.exchange(restCountryUrl + "alpha/" + cCode,
					HttpMethod.GET, null, Object[].class);
			Country country = mapCountryBodyToCountry((Map<String, Object>) Objects
					.requireNonNull(responseEntity.getBody())[0]);
			countries.add(country);
			CurrencyRate rate = null;
			
			try {
				rate = getCountryRates(currencyCode, country.getCurrencies().keySet().toArray()[0].toString());
			} catch (Exception exc) {
				Map<String, Double> rates = new HashMap<>();
				rates.put(currencyCode,1.00D);
				rate = new CurrencyRate(rates,currencyCode);
			}
			rateMap.put(cCode, rate);

		}

		neighbourCountries = countries;

		return rateMap;
	}

	@Override
	public CurrencyRate getCountryRates(String currencyBase, String targetCurrency) {

		// make REST call
		currencyBase = currencyBase.toUpperCase();
		String addUrl = restRatesUrl + currencyBase;
		ResponseEntity<CurrencyRate> responseEntity = restTemplate.exchange(addUrl, HttpMethod.GET, null,
				CurrencyRate.class);
		logger.finer(getClass()+".getCountryRates(): currencies" + responseEntity.getBody());
		CurrencyRate currencyRate = responseEntity.getBody();
		Objects.requireNonNull(currencyRate).setTarget(targetCurrency);
		return currencyRate;
	}

	public CountryBudgetInfo calculateCountryBudgetInfo(String budgetPerCountry, String totalBudget,
														 String countryCode, String currency) {
		CountryBudgetInfo budgetInfo = new CountryBudgetInfo();

		if (currency == null || currency.length() < 3) {
			logger.finer(String.format("%s%s%s%s",getClass(), ".calculateCountryBudgetInfo(): Provided base currency code is invalid ", currency
					, ",use default base currency EUR"));
			currency = "EUR";
		}

		Map<String, CurrencyRate> currencyRateMap = getNeighboursMap(CountryTourRestController.countryCodePrefix + countryCode, currency);
		budgetInfo.setCountryRates(currencyRateMap);
		double numberOfToursWithLeftOver = (Double.parseDouble(totalBudget)
				/ (Double.parseDouble(budgetPerCountry) * currencyRateMap.size()));
		int numberOfTours = (int) (Double.parseDouble(totalBudget)
				/ (Double.parseDouble(budgetPerCountry) * currencyRateMap.size()));
		budgetInfo.setNumberOfTours(numberOfTours);
		Double leftOver = (double) Math.round((numberOfToursWithLeftOver - numberOfTours)
				* Double.parseDouble(budgetPerCountry) * currencyRateMap.size() * 100.0) / 100.0;

		budgetInfo.setLeftOverBudget(leftOver + " " + currency);
		Map<String, String> convertedBudgets = new HashMap<>();

		for (Map.Entry<String, CurrencyRate> entry : currencyRateMap.entrySet()) {
			CurrencyRate value = entry.getValue();
			Double rate = value.getRates().get(value.getTarget());

			Double convertedBudget = (rate * Double.parseDouble(budgetPerCountry));
			if (rate == 1.00D) {
				convertedBudgets.put(entry.getKey(), String.format("%.2f", convertedBudget) + " " + currency);
			} else {
				String realCurrency = value.getTarget();
				convertedBudgets.put(entry.getKey(), String.format("%.2f", convertedBudget) + " " + realCurrency);
			}
		}

		budgetInfo.setConvertedCountryTourBudgets(convertedBudgets);

		return budgetInfo;
	}

}
