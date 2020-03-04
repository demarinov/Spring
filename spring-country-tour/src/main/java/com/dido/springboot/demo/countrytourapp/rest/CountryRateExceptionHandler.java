package com.dido.springboot.demo.countrytourapp.rest;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dido.springboot.demo.countrytourapp.entity.CountryRateErrorResponse;

@RestControllerAdvice
public class CountryRateExceptionHandler {

	@ExceptionHandler(value = { IOException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	public CountryRateErrorResponse handleException(Exception exc) {

		String message = exc.getMessage()+" .May be try with different base currency.";
		CountryRateErrorResponse error = new CountryRateErrorResponse(HttpStatus.BAD_REQUEST.value(), message,
				System.currentTimeMillis());
		
		return error;
	}
	
	@ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
	public CountryRateErrorResponse unknownException(Exception exc) {

		String message = exc.getMessage()+" .Check your url.";
		CountryRateErrorResponse error = new CountryRateErrorResponse(HttpStatus.NOT_FOUND.value(), message,
				System.currentTimeMillis());
		
		return error;
	}
}
