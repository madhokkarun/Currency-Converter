package handler;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import connection.SqlServerConnection;
import constant.CurrencyConstants;
import dao.CurrencyDAO;
import model.ConversionHistory;
import model.Country;
import model.Currency;
import model.CurrencyExchange;

public class JSONHandler {
	
	public static final int HISTORY_MAX_ITERATIONS = 10;
	
	public static List<Currency> currencyList = null;
	public static List<Country> countryList = null;
	public static Deque<ConversionHistory> conversionQueue = null;

	private static JSONObject getJSON(String url) throws ParseException, SQLException, IOException
	{
			URL jsonUrl = new URL(url);
			
			HttpURLConnection httpConn = (HttpURLConnection)jsonUrl.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			
			int responseCode = httpConn.getResponseCode();
			
			JSONObject results = null;
			
			if(responseCode != 200)
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
	            String msg;
	            while ((msg =reader.readLine()) != null)
	                System.out.println(msg);
			}
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
	
	private static String processConversionJSON(JSONObject currencyConversionJson, String fromToCurrency, Double fromValue)
	{
		return calculateConversion(currencyConversionJson, fromToCurrency, fromValue);
	}
	
	private static String calculateConversion(JSONObject json, String key, Double fromValue)
	{
		
		JSONObject currencyInfo = (JSONObject) json.get(key);
		
		return String.valueOf(fromValue * (Double)currencyInfo.get(CurrencyConstants.CURRENCY_VAL));
	}
	
	
	public static String getConversionRate(String fromCurrency, Double fromValue, String toCurrency) throws ParseException, SQLException, IOException
	{
		String fromToCurrencyKey = fromCurrency + "_" + toCurrency;
		
		String conversionURL = CurrencyConstants.CURRENCY_CONVERSION_URL + fromToCurrencyKey;
		
		String conversionValue = processConversionJSON(getJSON(conversionURL), fromToCurrencyKey, fromValue);
		
		addConversionHistory(fromCurrency, fromValue, toCurrency, Double.valueOf(conversionValue));
		
		return conversionValue;
		
	}
	
	public static List<Currency> addCurrencies() throws SQLException, ParseException, IOException
	{
		 currencyList =  processCurrencyJSON(getJSON(CurrencyConstants.CURRENCY_URL));
		 
		 return currencyList;
		
	}
	
	public static List<Country> addCountries() throws SQLException, ParseException, IOException
	{
		countryList = processCountryJSON(getJSON(CurrencyConstants.COUNTRY_URL));
		
		return countryList;
	}
	
	public static void writeCurrencies(String Location) throws IOException, SQLException
	{
		FileWriter currencyWriter = new FileWriter(Location + "Currencies.txt");
		
		
		currencyWriter.write(String.format("%20s %31s \r\n", "CURRENCY ABBREVIATION", "CURRENCY NAME"));
		currencyWriter.write(System.getProperty("line.separator"));

		for(Currency currency: CurrencyDAO.getAllCurrencies())
		{
			currencyWriter.write(String.format("%15s %-10s \r\n", currency.getId() + "\t\t\t\t", currency.getCurrencyName()));
		}
		
		currencyWriter.close();
	}
	
	public static void addHistoryToQueue() throws SQLException
	{
		int counter = 0;
		
		for(ConversionHistory conversionHistory: CurrencyDAO.getConversionHistory())
		{
			if(counter >= HISTORY_MAX_ITERATIONS)
				break;
			
			conversionQueue.addLast(conversionHistory);
			
			counter++;
		}
	}
	
	public static void writeConversionHistory(String Location) throws IOException, SQLException
	{
		FileWriter conversionHistoryWriter = new FileWriter(Location + "Conversion History.txt");
		
		conversionHistoryWriter.write(String.format("%17s %30s %34s \r\n", "DATE", "CONVERSION FROM", "CONVERSION TO"));
		conversionHistoryWriter.write(System.getProperty("line.separator"));
		
		for(ConversionHistory conversionHistory: CurrencyDAO.getConversionHistory())
		{
			conversionHistoryWriter.write(String.format("%20s %26s %35s \r\n", conversionHistory.getConversionDate(), String.format("%,.2f", conversionHistory.getFromValue()) + " " + conversionHistory.getFromCurrency(), String.format("%,.2f", conversionHistory.getToValue()) + " " + conversionHistory.getToCurrency()));
		}
		
		conversionHistoryWriter.close();
	}
	
	public static List<CurrencyExchange> getAllCurrencyExchangePriceList(String toCurrency) throws SQLException, ParseException, IOException
	{
		List<Currency> currencyList = CurrencyDAO.getAllCurrencies();
		
		List<CurrencyExchange> currencyExchangeList = new ArrayList();
		
		for(Currency currency: currencyList)
		{
			CurrencyExchange currencyExchange = new CurrencyExchange();
			
			currencyExchange.setToCurrency(toCurrency);
			currencyExchange.setFromCurrency(currency.getId());
			currencyExchange.setExchangePrice(getConversionRate(currency.getId(),1.0 , toCurrency));
			
			currencyExchangeList.add(currencyExchange);
			
		}
		
		return currencyExchangeList;
	}
	
	public static void addConversionHistory(String fromCurrency, Double fromValue, String toCurrency, Double toValue) throws SQLException
	{
		ConversionHistory conversionHistory = new ConversionHistory();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date currentDate = new Date();
		
		conversionHistory.setFromCurrency(fromCurrency);
		conversionHistory.setFromValue(fromValue);
		conversionHistory.setToCurrency(toCurrency);
		conversionHistory.setToValue(toValue);
		conversionHistory.setConversionDate(dateFormat.format(currentDate));
		
		CurrencyDAO.addConversionHistory(conversionHistory);
		
		conversionQueue.removeLast();
		conversionQueue.addFirst(conversionHistory);
	}
	
	public static void resetApplication() throws SQLException, ParseException, IOException
	{
		CurrencyDAO.truncateTables();
		
		currencyList = addCurrencies();
		countryList = addCountries();
	}
}
