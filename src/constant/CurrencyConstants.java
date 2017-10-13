package constant;

public final class CurrencyConstants {
	
	private CurrencyConstants() {}
	
	/**
	 * Database commands constants
	 */
	public static final String INSERT_CURRENCY = "INSERT into CURRENCY values(?,?,?)";
	public static final String INSERT_COUNTRY = "INSERT into COUNTRY values(?,?,?,?)";
	
	
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
