package constant;

public final class CurrencyConstants {
	
	private CurrencyConstants() {}
	
	/**
	 * Database commands constants
	 */
	public static final String INSERT_CURRENCY = "INSERT into CURRENCY values(?,?,?)";
	public static final String INSERT_COUNTRY = "INSERT into COUNTRY values(?,?,?,?)";
	public static final String SELECT_CURRENCY = "SELECT id, currency_name from CURRENCY";
	public static final String INSERT_CONVERSION = "INSERT into CONVERSION_HISTORY values(?,?,?,?,?)";
	public static final String SELECT_CONVERSION_HISTORY = "SELECT * from CONVERSION_HISTORY";
	
	public static final String TRUNCATE_CURRENCY = "DELETE from CURRENCY";
	public static final String TRUNCATE_COUNTRY = "TRUNCATE TABLE COUNTRY";
	public static final String TRUNCATE_CONVERSION_HISTORY = "TRUNCATE TABLE CONVERSION_HISTORY";
	
	/**
	 * Currency constants
	 */
	public static final String ID = "id";
	public static final String CURRENCY_SYMBOL = "currencySymbol";
	public static final String CURRENCY_NAME = "currencyName";
	public static final String CURRENCY_VAL = "val";
	
	/**
	 * COUNTRY constants
	 */
	public static final String NAME = "name";
	public static final String ABBREVIATION = "alpha3";
	public static final String CURRENCY = "currencyId";
	
	public static final String RESULTS = "results";
	
	/**
	 * URLs
	 */
	public static final String CURRENCY_URL = "http://free.currencyconverterapi.com/api/v3/currencies";
	public static final String COUNTRY_URL = "http://free.currencyconverterapi.com/api/v3/countries";
	public static final String CURRENCY_CONVERSION_URL = "http://free.currencyconverterapi.com/api/v3/convert?q=";
}
