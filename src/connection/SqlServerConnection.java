package connection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.json.simple.parser.ParseException;

import handler.JSONHandler;

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
		/*System.out.println(JSONHandler.addCurrencies());
		System.out.println(JSONHandler.addCountries());
		*/
		
		System.out.println(JSONHandler.getConversionRate("CAD", "INR"));
	}
} 

	