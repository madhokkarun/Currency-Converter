package handler;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import constant.CurrencyConstants;
import dao.CurrencyDAO;
import model.ConversionHistory;
import model.Country;
import model.Currency;
import model.CurrencyExchange;
import utilities.CurrencyUtilities;

public class JSONHandler {
	
	public static final int HISTORY_MAX_ITERATIONS = 10;
	
	public static List<Currency> currencyList = null;
	public static List<Country> countryList = null;
	
	public static Deque<ConversionHistory> conversionQueue = new ArrayDeque<>();

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
	            	JOptionPane.showMessageDialog(null, "Connection problem");
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
		
		return String.valueOf(fromValue * Double.valueOf(String.valueOf(currencyInfo.get(CurrencyConstants.CURRENCY_VAL))));
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
	
	public static boolean writeCurrencies(String Location) throws IOException, SQLException
	{
		FileWriter currencyWriter = new FileWriter(Location + "\\" + "Currencies.txt");
		
		List<Currency> currencyList = new ArrayList<>();
		
		currencyList = CurrencyDAO.getAllCurrencies();
		
		if(currencyList.isEmpty())
			currencyWriter.write("No currencies");
		else
		{
			currencyWriter.write(String.format("%20s %31s \r\n", "CURRENCY ABBREVIATION", "CURRENCY NAME"));
			currencyWriter.write(System.getProperty("line.separator"));

			for(Currency currency: currencyList)
				currencyWriter.write(CurrencyUtilities.formatCurrency(currency));
		}
		
		currencyWriter.close();
		
		return true;
	}
	
	public static void addHistoryToQueue() throws SQLException
	{
		int counter = 0;
		conversionQueue.clear();
		
		List<ConversionHistory> conversionHistoryList = new ArrayList<>();
		
		conversionHistoryList = CurrencyDAO.getConversionHistory();
		
		if(!conversionHistoryList.isEmpty())
		{
			for(ConversionHistory conversionHistory: conversionHistoryList)
			{
				if(counter >= HISTORY_MAX_ITERATIONS)
					break;
				
				conversionQueue.addLast(conversionHistory);
				
				counter++;
			}
		}
		
	}
	
	public static boolean writeConversionHistory(String Location) throws IOException, SQLException
	{
		FileWriter conversionHistoryWriter = new FileWriter(Location + "\\" + "Conversion History.txt");
		
		List<ConversionHistory> conversionHistoryList = new ArrayList<>();
		
		conversionHistoryList = CurrencyDAO.getConversionHistory();
		
		if(conversionHistoryList.isEmpty())
			conversionHistoryWriter.write("No conversion History");
		else
		{
			conversionHistoryWriter.write(String.format("%17s %30s %34s \r\n", "DATE", "CONVERSION FROM", "CONVERSION TO"));
			conversionHistoryWriter.write(System.getProperty("line.separator"));
			
			for(ConversionHistory conversionHistory: CurrencyDAO.getConversionHistory())
				conversionHistoryWriter.write(CurrencyUtilities.formatConversionHistory(conversionHistory));
		}
		
		
		conversionHistoryWriter.close();
		
		return true;
	}
	
	
	public static List<CurrencyExchange> getAllCurrencyExchangePriceList(String toCurrency) throws SQLException, ParseException, IOException
	{
		List<Currency> currencyList = CurrencyDAO.getAllCurrencies();
		
		List<CurrencyExchange> currencyExchangeList = new ArrayList<>();
		
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
		
		if(!conversionQueue.isEmpty() && conversionQueue.size() > HISTORY_MAX_ITERATIONS)
			conversionQueue.removeLast();
		
		conversionQueue.addFirst(conversionHistory);
		
	}
	
	public static void addRecentConversionsToPanel(JPanel recentConversionsPanel)
	{
		recentConversionsPanel.removeAll();
		recentConversionsPanel.revalidate();
		recentConversionsPanel.repaint();
		
		JLabel lblRecentConversions = new JLabel("                                                            Recent Conversions                                                                 ", SwingConstants.CENTER);
		
		recentConversionsPanel.add(lblRecentConversions);
		
		Iterator<ConversionHistory> dqIterator = JSONHandler.conversionQueue.iterator();
		
		while(dqIterator.hasNext())
		{
			JLabel lblConversion = new JLabel(CurrencyUtilities.formatConversionHistory(dqIterator.next()));
			recentConversionsPanel.add(lblConversion);
		}
	}
	
	public static void addNoRecentConversionsToPanel(JPanel recentConversionsPanel)
	{
		recentConversionsPanel.removeAll();
		recentConversionsPanel.revalidate();
		recentConversionsPanel.repaint();
		
		JLabel lblNoHistory = new JLabel("        No recent conversions        ");
		recentConversionsPanel.add(lblNoHistory);
	}
	
	
	public static void resetApplication() throws SQLException, ParseException, IOException
	{
		CurrencyDAO.truncateTables();
		
		currencyList = addCurrencies();
		countryList = addCountries();
		
	}
}
