package com.example.expensetracker.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyConverterService {

  @Value("${exchangerate.api.key}")
  private String apiKey;

  private final RestTemplate restTemplate = new RestTemplate();

  public double convert(String fromCurrency, String toCurrency, double amount){
    String urlString= String.format(
        "https://v6.exchangerate-api.com/v6/%s/pair/%s/%s",
        apiKey,
        fromCurrency,
        toCurrency
    );

    Map<String, Object> response = restTemplate.getForObject(urlString, Map.class);

    if( response != null
          &&
        response.get("conversion_rate") != null
    ){
      double conversionRate = (double) response.get("conversion_rate");
      return amount * conversionRate;
    }
    throw new RuntimeException("Failed to getz conversion rate");
  }

}
