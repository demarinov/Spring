package com.dido.springboot.demo.countrytourapp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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

		logger.info(getClass()+".CountryRateServiceImpl(): Loaded property:  rest.country.url=" + restCountryUrl + " ,rest.rates.url=" + restRatesUrl);
	}

	@Override
	public Country loadCountry(String countryCode) {

		// make REST call
		ResponseEntity<Country> responseEntity = restTemplate.exchange(restCountryUrl + countryCode, HttpMethod.GET,
				null, Country.class);
		logger.info(getClass()+".loadCountry(): country" + responseEntity.getBody());

		startCountry = responseEntity.getBody();

		return startCountry;
	}

	@Override
	public List<Country> loadCountries(String code) {

		// make REST call
		ResponseEntity<List<Country>> responseEntity = restTemplate.exchange(restCountryUrl + code, HttpMethod.GET,
				null, new ParameterizedTypeReference<List<Country>>() {
				});
		logger.info(getClass()+".loadCountries(): countries" + responseEntity.getBody());

		return responseEntity.getBody();
	}

	@Override
	public List<Country> getNeighbours(String countryCode) {

		List<Country> countries = new ArrayList<>();
		Map<String, CurrencyRate> rateMap = new HashMap<>();

		loadCountry(countryCode);

		List<String> countryCodes = startCountry.getBorders();

		for (String cCode : countryCodes) {
			// make REST call
			ResponseEntity<Country> responseEntity = restTemplate.exchange(restCountryUrl + "alpha/" + cCode,
					HttpMethod.GET, null, Country.class);
			countries.add(responseEntity.getBody());

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
			ResponseEntity<Country> responseEntity = restTemplate.exchange(restCountryUrl + "alpha/" + cCode,
					HttpMethod.GET, null, Country.class);
			countries.add(responseEntity.getBody());
			CurrencyRate rate = null;
			
			try {
				rate = getCountryRates(currencyCode, responseEntity.getBody().getCurrencies().get(0).getCode());
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
		String addUrl = restRatesUrl + currencyBase + "&symbols=" + targetCurrency;
		ResponseEntity<CurrencyRate> responseEntity = restTemplate.exchange(addUrl, HttpMethod.GET, null,
				CurrencyRate.class);
		logger.info(getClass()+".getCountryRates(): currencies" + responseEntity.getBody());

		return responseEntity.getBody();
	}

}
