package model;

public class CurrencyExchange {
	
	private String fromCurrency;
	private String ToCurrency;
	private String exchangePrice;
	
	
	public String getFromCurrency() {
		return fromCurrency;
	}
	public void setFromCurrency(String fromCurrency) {
		this.fromCurrency = fromCurrency;
	}
	public String getToCurrency() {
		return ToCurrency;
	}
	public void setToCurrency(String toCurrency) {
		ToCurrency = toCurrency;
	}
	public String getExchangePrice() {
		return exchangePrice;
	}
	public void setExchangePrice(String exchangePrice) {
		this.exchangePrice = exchangePrice;
	}
	
}
