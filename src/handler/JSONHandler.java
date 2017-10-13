package handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import connection.SqlServerConnection;
import constant.CurrencyConstants;
import dao.CurrencyDAO;
import model.Country;
import model.Currency;

public class JSONHandler {

	private static JSONObject getJSON(String url) throws ParseException, SQLException, IOException
	{
			URL jsonUrl = new URL(url);
			
			HttpURLConnection httpConn = (HttpURLConnection)jsonUrl.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			
			int responseCode = httpConn.getResponseCode();
			
			JSONObject results = null;
			
			if(responseCode != 200)
				throw new RuntimeException();
			else
			{
				Scanner sc = new Scanner(jsonUrl.openStream());
				String jsonString = "";
				
				while(sc.hasNext())
				{
					jsonString += sc.nextLine();
				}
				sc.close();
			
				
				JSONParser jsonParser = new JSONParser();
				
				JSONObject jObj = (JSONObject) jsonParser.parse(jsonString);
				results = (JSONObject) jObj.get(CurrencyConstants.RESULTS);
				
			}
				
			return results;
	}
	
	private static List<Currency> processCurrencyJSON(JSONObject json) throws SQLException, ParseException, IOException
	{
		return CurrencyDAO.insertCurrency(json);
	}
	
	private static List<Country> processCountryJSON(JSONObject json) throws SQLException, ParseException, IOException
	{
		return CurrencyDAO.insertCountry(json);
	}
	
	private static String processConversionJSON(JSONObject currencyConversionJson, String fromToCurrency)
	{
		return calculateConversion(currencyConversionJson, fromToCurrency);
	}
	
	private static String calculateConversion(JSONObject json, String key)
	{
		
		JSONObject currencyInfo = (JSONObject) json.get(key);
		
		return String.valueOf(currencyInfo.get(CurrencyConstants.CURRENCY_VAL));
	}
	
	
	public static String getConversionRate(String fromCurrency, String toCurrency) throws ParseException, SQLException, IOException
	{
		String fromToCurrencyKey = fromCurrency + "_" + toCurrency;
		
		String conversionURL = CurrencyConstants.CURRENCY_CONVERSION_URL + fromToCurrencyKey;
		
		return processConversionJSON(getJSON(conversionURL), fromToCurrencyKey);
		
	}
	
	public static List<Currency> addCurrencies() throws SQLException, ParseException, IOException
	{
		 return processCurrencyJSON(getJSON(CurrencyConstants.CURRENCY_URL));
		
	}
	
	public static List<Country> addCountries() throws SQLException, ParseException, IOException
	{
		return processCountryJSON(getJSON(CurrencyConstants.COUNTRY_URL));
	}
}
