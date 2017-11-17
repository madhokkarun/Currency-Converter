package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
} 

	