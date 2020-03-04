package com.dido.springboot.demo.countrytourapp.rest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dido.springboot.demo.countrytourapp.entity.Country;
import com.dido.springboot.demo.countrytourapp.entity.CountryBudgetInfo;
import com.dido.springboot.demo.countrytourapp.entity.CurrencyRate;
import com.dido.springboot.demo.countrytourapp.services.CountryRateService;

@RestController
public class CountryTourRestController {

	private Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	CountryRateService crService;

	@GetMapping("/")
	public String sayHello() {

		return "Hello world! It is time to do some REST Country tour... " + LocalDateTime.now()
				+ " <a href=\"/help\">Help</a>";
	}

	@GetMapping("/help")
	public String help() {
		String urlPattern = "http://localhost:[serverPort]/getCountryTourBudgetInfo/[startCountryCode]&amp[budgetPerCountry]&amp[totalBudget]&amp[baseCurrency]";

		String info = "<strong>Url pattern:</strong> '"+urlPattern+"' <br/><br/> <strong>Example:</strong> 'http://localhost:8585/getCountryTourBudgetInfo/BG&100&1200&EUR'"
		+"<br/><br/> <strong>All url parameters are mandatory!!!</strong>";

		return info;
	}

	@GetMapping("/getCountry/{countryCode}")
	public Country getCountry(@PathVariable String countryCode) {

		return crService.loadCountry("alpha/" + countryCode);

	}

	@GetMapping("/getCountries")
	public List<Country> getAllCountres() {

		return crService.loadCountries("all");

	}

	@GetMapping("/getCurrency/{currencyCode}")
	public CurrencyRate getCurrency(@PathVariable String currencyCode) {
		CurrencyRate rate = crService.getCountryRates(currencyCode, "GBP");

		return rate;

	}

	@GetMapping("/getNCountry/{countryCode}")
	public List<Country> getNCountry(@PathVariable String countryCode) {
		List<Country> countries = crService.getNeighbours("alpha/" + countryCode);

		return countries;

	}

	@GetMapping("/getRates/{currencyCode}&{countryCode}")
	public Map<String, CurrencyRate> getRates(@PathVariable String currencyCode, @PathVariable String countryCode) {
		Map<String, CurrencyRate> currencyRateMap = crService.getNeighboursMap("alpha/" + countryCode, currencyCode);

		return currencyRateMap;

	}

	@GetMapping("/getCountryTourBudgetInfo/{countryCode}&{budgetPerCountry}&{totalBudget}&{currency}")
	public Map<String, CountryBudgetInfo> getCountryBudgetInfo(@PathVariable String countryCode,
			@PathVariable String budgetPerCountry, @PathVariable String totalBudget, @PathVariable String currency) {

		logger.info(getClass() + ".getCountryBudgetInfo():property currency" + currency);
		Map<String, CountryBudgetInfo> countryTourBudget = new HashMap<>();
		countryTourBudget.put("countryTourBudget",
				calculateCountryBudgetInfo(budgetPerCountry, totalBudget, countryCode, currency));

		return countryTourBudget;

	}

	private CountryBudgetInfo calculateCountryBudgetInfo(String budgetPerCountry, String totalBudget,
			String countryCode, String currency) {
		CountryBudgetInfo budgetInfo = new CountryBudgetInfo();

		if (currency.length() < 3 || currency == null) {
			logger.info(getClass() + ".calculateCountryBudgetInfo(): Provided base currency code is invalid " + currency
					+ ",use default base currency EUR");
			currency = "EUR";
		}

		Map<String, CurrencyRate> currencyRateMap = crService.getNeighboursMap("alpha/" + countryCode, currency);
		budgetInfo.setCountryRates(currencyRateMap);
		Double numberOfToursWithLeftOver = (Double.parseDouble(totalBudget)
				/ (Double.parseDouble(budgetPerCountry) * currencyRateMap.size()));
		int numberOfTours = (int) (Double.parseDouble(totalBudget)
				/ (Double.parseDouble(budgetPerCountry) * currencyRateMap.size()));
		budgetInfo.setNumberOfTours(numberOfTours);
		Double leftOver = (double) Math.round((numberOfToursWithLeftOver - numberOfTours)
				* Double.parseDouble(budgetPerCountry) * currencyRateMap.size() * 100.0) / 100.0;

		budgetInfo.setLeftOverBudget(leftOver + " " + currency);
		Map<String, String> convertedBudgets = new HashMap<>();

		for (Entry<String, CurrencyRate> entry : currencyRateMap.entrySet()) {
			CurrencyRate value = entry.getValue();
			Double rate = value.getRates().values().stream().collect(Collectors.toList()).get(0);

			Double convertedBudget = (rate * Double.parseDouble(budgetPerCountry) * numberOfTours);
			if (rate == 1.00D) {
				convertedBudgets.put(entry.getKey(), String.format("%.2f", convertedBudget) + " " + currency);
			} else {
				String realCurrency = value.getRates().keySet().iterator().next();
				convertedBudgets.put(entry.getKey(), String.format("%.2f", convertedBudget) + " " + realCurrency);
			}
		}

		budgetInfo.setConvertedCountryTourBudgets(convertedBudgets);

		return budgetInfo;
	}
}
