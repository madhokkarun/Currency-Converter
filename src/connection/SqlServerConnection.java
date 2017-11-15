package connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import org.json.simple.parser.ParseException;

import dao.CurrencyDAO;
import handler.JSONHandler;
import model.CurrencyExchange;

public class SqlServerConnection {
	
	private static String connectionUrl = "jdbc:sqlserver://localhost:1433;" +  
	         "databaseName=CurrencyConverter;user=sa;password=km201294";
	
	
	public static Connection getConnection() throws SQLException
	{
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return DriverManager.getConnection(connectionUrl);
	}
	
	public static void main(String args[]) throws ParseException, SQLException, IOException
	{
		//System.out.println(JSONHandler.addCurrencies());
		//System.out.println(JSONHandler.addCountries());
		
		JOptionPane.showMessageDialog(null, JSONHandler.getConversionRate("LKR",20.0, "INR"));
		
		/*List<CurrencyExchange> currencyExchangeList = JSONHandler.getAllCurrencyExchangePriceList("INR");
		
		for(CurrencyExchange currencyExchange: currencyExchangeList)
		{
			System.out.println(currencyExchange.getFromCurrency() + "\t" + currencyExchange.getExchangePrice() + " " + currencyExchange.getToCurrency());
		}*/
		
		//JSONHandler.writeCurrencies("");
		//JSONHandler.writeConversionHistory("");
		
		//JSONHandler.resetApplication();
		
	}
} 

	