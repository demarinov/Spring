package com.dido.springboot.demo.countrytourapp.rest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dido.springboot.demo.countrytourapp.entity.Country;
import com.dido.springboot.demo.countrytourapp.entity.CountryBudgetInfo;
import com.dido.springboot.demo.countrytourapp.entity.CurrencyRate;
import com.dido.springboot.demo.countrytourapp.services.CountryRateService;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class CountryTourRestController {

    private final Logger logger = Logger.getLogger(getClass().getName());

    public static final String countryCodePrefix = "alpha/";

    private final CountryRateService crService;

    public CountryTourRestController(CountryRateService crService) {
        this.crService = crService;
    }

    @GetMapping("/")
    public ModelAndView sayHello(ModelAndView modelAndView) {

        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/help")
    public String help() {
        String urlPattern = "http://localhost:[serverPort]/getCountryTourBudgetInfo/[startCountryCode]&amp[budgetPerCountry]&amp[totalBudget]&amp[baseCurrency]";

        return "<strong>Url pattern:</strong> '" + urlPattern + "' <br/><br/> <strong>Example:</strong> 'http://localhost:8585/getCountryTourBudgetInfo/BG&100&1200&EUR'"
                + "<br/><br/> <strong>All url parameters are mandatory!!!</strong>";
    }

    @GetMapping("/getCountry/{countryCode}")
    public Country getCountry(@PathVariable String countryCode) {

        return crService.loadCountry(countryCodePrefix + countryCode);

    }

    @GetMapping("/getCountries")
    public List<Country> getAllCountries() {

        return crService.loadCountries("all");

    }

    @GetMapping("/getCurrency/{currencyCode}")
    public CurrencyRate getCurrency(@PathVariable String currencyCode) {
        return crService.getCountryRates(currencyCode, "GBP");

    }

    @GetMapping("/getNCountry/{countryCode}")
    public List<Country> getNCountry(@PathVariable String countryCode) {
        return crService.getNeighbours(countryCodePrefix + countryCode);

    }

    @GetMapping("/getRates/{currencyCode}&{countryCode}")
    public Map<String, CurrencyRate> getRates(@PathVariable String currencyCode, @PathVariable String countryCode) {
        return crService.getNeighboursMap(countryCodePrefix + countryCode, currencyCode);

    }

    @GetMapping("/getCountryTourBudgetInfo/{countryCode}&{budgetPerCountry}&{totalBudget}&{currency}")
    public Map<String, CountryBudgetInfo> getCountryBudgetInfo(@PathVariable String countryCode,
                                                               @PathVariable String budgetPerCountry, @PathVariable String totalBudget, @PathVariable String currency) {

        logger.info(String.format("%s%s%s", getClass(), ".getCountryBudgetInfo():property currency", currency));
        Map<String, CountryBudgetInfo> countryTourBudget = new HashMap<>();
        countryTourBudget.put("countryTourBudget",
                crService.calculateCountryBudgetInfo(budgetPerCountry, totalBudget, countryCode, currency));

        return countryTourBudget;

    }
}
