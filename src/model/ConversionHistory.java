package model;

public class ConversionHistory {
	
	private Double fromValue;
	private String fromCurrency;
	
	private Double toValue;
	private String toCurrency;
	private String conversionDate;
	
	public Double getFromValue() {
		return fromValue;
	}
	public void setFromValue(Double fromValue) {
		this.fromValue = fromValue;
	}
	
	public String getFromCurrency() {
		return fromCurrency;
	}
	public void setFromCurrency(String fromCurrency) {
		this.fromCurrency = fromCurrency;
	}
	
	public Double getToValue() {
		return toValue;
	}
	public void setToValue(Double toValue) {
		this.toValue = toValue;
	}
	
	public String getToCurrency() {
		return toCurrency;
	}
	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}
	
	public String getConversionDate() {
		return conversionDate;
	}
	public void setConversionDate(String conversionDate) {
		this.conversionDate = conversionDate;
	}
	
}
