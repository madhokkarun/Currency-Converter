package utilities;

import java.sql.SQLException;

import javax.swing.JComboBox;

import dao.CurrencyDAO;
import model.ConversionHistory;
import model.Currency;

public class CurrencyUtilities {

	public static String formatCurrency(Currency currency)
	{
		return String.format("%15s %-10s \r\n", currency.getId() + "\t\t\t\t", currency.getCurrencyName());
	}
	
	public static String formatConversionHistory(ConversionHistory conversionHistory)
	{
		return String.format("%22s %28s %37s \r\n", conversionHistory.getConversionDate(), String.format("%,.2f", conversionHistory.getFromValue()) + " " + conversionHistory.getFromCurrency(), String.format("%,.2f", conversionHistory.getToValue()) + " " + conversionHistory.getToCurrency());
	}
	
	public static void addCurrenciesToComboBox(JComboBox<String> comboBox) throws SQLException
	{
		for(Currency currency: CurrencyDAO.getAllCurrencies())
			comboBox.addItem(currency.getId() + " - " + currency.getCurrencyName());
		
	}
	
	public static String getCurrencyFromComboBox(JComboBox<String> comboBox)
	{
		return comboBox.getSelectedItem().toString().substring(0, 3);
	}
}
