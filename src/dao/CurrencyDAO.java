package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
import model.Country;
import model.Currency;

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
}
