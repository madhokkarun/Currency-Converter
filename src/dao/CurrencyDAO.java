package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.json.simple.JSONObject;

import connection.SqlServerConnection;
import constant.CurrencyConstants;
import model.ConversionHistory;
import model.Country;
import model.Currency;
import model.CurrencyExchange;

public class CurrencyDAO {

	
	public static List<Currency> insertCurrency(JSONObject currencyResult) throws SQLException
	{
		List<Currency> currencyList = new ArrayList<Currency>();
		
		Connection conn = SqlServerConnection.getConnection();
		
		Set currencies = currencyResult.keySet();
		Iterator currencyIterator = currencies.iterator();
		
		PreparedStatement ps = conn.prepareStatement(CurrencyConstants.INSERT_CURRENCY);
		
		while(currencyIterator.hasNext())
		{
			int index = 1;
			
			String key = (String)currencyIterator.next();
			
			JSONObject value = (JSONObject) currencyResult.get(key);
			
			String currencyId = (String) value.get(CurrencyConstants.ID);
			String currencyName = (String) value.get(CurrencyConstants.CURRENCY_NAME);
			String currencySymbol = (String) value.get(CurrencyConstants.CURRENCY_SYMBOL);
			
			Currency currency = new Currency();
			
			currency.setId(currencyId);
			currency.setCurrencyName(currencyName);
			currency.setCurrencySymbol(currencySymbol);
			
			currencyList.add(currency);
			
			ps.setString(index++, currencyId);
			ps.setString(index++, currencyName);
			
			if(currencySymbol != null)
			{
				byte[] varBinary = currencySymbol.getBytes();
				ps.setBytes(index++, varBinary);
			}
			else
				ps.setBytes(index++, null);
			
			ps.addBatch();
		}
		
		ps.executeBatch();
		
		conn.close();
		
		return currencyList;
	}
	
	public static List<Country> insertCountry(JSONObject countryResult) throws SQLException
	{
		List<Country> countryList = new ArrayList<Country>();
		
		Connection conn = SqlServerConnection.getConnection();
		
		Set countries = countryResult.keySet();
		Iterator countryIterator = countries.iterator();
		
		PreparedStatement ps = conn.prepareStatement(CurrencyConstants.INSERT_COUNTRY);
		
		while(countryIterator.hasNext())
		{
			int index = 1;
			
			Country country = new Country();
			
			String key = (String) countryIterator.next();
			JSONObject value = (JSONObject) countryResult.get(key);
			
			String countryId = (String) value.get(CurrencyConstants.ID);
			String countryName = (String) value.get(CurrencyConstants.NAME);
			String countryAbbr = (String) value.get(CurrencyConstants.ABBREVIATION);
			String countryCurrency = (String) value.get(CurrencyConstants.CURRENCY);
			
			country.setId(countryId);
			country.setName(countryName);
			country.setAbbreviation(countryAbbr);
			country.setCurrencyId(countryCurrency);
			
			countryList.add(country);
			
			ps.setString(index++, countryId);
			ps.setString(index++, countryName);
			ps.setString(index++, countryAbbr);
			ps.setString(index++, countryCurrency);
			
			ps.addBatch();
			
		}
		
		ps.executeBatch();
		
		conn.close();
		
		return countryList;
	}
	
	
	public static List<Currency> getAllCurrencies() throws SQLException
	{
		Connection conn = SqlServerConnection.getConnection();
		
		List<Currency> currencyList = new ArrayList();
		
		PreparedStatement ps = conn.prepareStatement(CurrencyConstants.SELECT_CURRENCY);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next())
		{
			int index = 1;
			Currency currency = new Currency();
			
			currency.setId(rs.getString(index++));
			currency.setCurrencyName(rs.getString(index++));
			
			currencyList.add(currency);
		}
		
		conn.close();
		
		return currencyList;
	}
	
	public static void addConversionHistory(ConversionHistory conversionHistory) throws SQLException
	{
		Connection conn = SqlServerConnection.getConnection();
		
		PreparedStatement ps = conn.prepareStatement(CurrencyConstants.INSERT_CONVERSION);
		
		int index = 1;
		
		ps.setDouble(index++, conversionHistory.getFromValue());
		ps.setString(index++, conversionHistory.getFromCurrency());
		ps.setDouble(index++, conversionHistory.getToValue());
		ps.setString(index++, conversionHistory.getToCurrency());
		ps.setString(index++, conversionHistory.getConversionDate());
		
		ps.executeUpdate();
		
		conn.close();
	}
	
	public static List<ConversionHistory> getConversionHistory() throws SQLException
	{
		Connection conn = SqlServerConnection.getConnection();
		
		List<ConversionHistory> conversionHistoryList = new ArrayList();
		
		PreparedStatement ps = conn.prepareStatement(CurrencyConstants.SELECT_CONVERSION_HISTORY);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next())
		{
			ConversionHistory conversionHistory = new ConversionHistory();
			
			int index = 2;

			conversionHistory.setFromValue(rs.getDouble(index++));
			conversionHistory.setFromCurrency(rs.getString(index++));
			conversionHistory.setToValue(rs.getDouble(index++));
			conversionHistory.setToCurrency(rs.getString(index++));
			conversionHistory.setConversionDate(rs.getString(index++));
			
			conversionHistoryList.add(conversionHistory);
			
		}
		
		conn.close();
		
		return conversionHistoryList;
		
	}
	
	public static void truncateTables() throws SQLException
	{
		Connection conn = SqlServerConnection.getConnection();
		
		PreparedStatement psTruncateHistory = conn.prepareStatement(CurrencyConstants.TRUNCATE_CONVERSION_HISTORY);
		PreparedStatement psTruncateCountry = conn.prepareStatement(CurrencyConstants.TRUNCATE_COUNTRY);
		PreparedStatement psTruncateCurrency = conn.prepareStatement(CurrencyConstants.TRUNCATE_CURRENCY);
		
		psTruncateHistory.executeUpdate();
		psTruncateCountry.executeUpdate();
		psTruncateCurrency.executeUpdate();
		
		conn.close();
		
	}
}
