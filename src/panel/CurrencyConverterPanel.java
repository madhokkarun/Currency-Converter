package panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileSystemView;

import org.json.simple.parser.ParseException;

import constant.CurrencyConstants;
import handler.JSONHandler;
import utilities.CurrencyUtilities;

public class CurrencyConverterPanel {

	private JFrame frmCurrencyConverter;
	private JTextField inputFromValue;
	private JTextField outputToValue;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Adds conversion history from database to deque
					JSONHandler.addHistoryToQueue();
					
					CurrencyConverterPanel window = new CurrencyConverterPanel();
					
					window.frmCurrencyConverter.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public CurrencyConverterPanel() throws SQLException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		frmCurrencyConverter = new JFrame();
		frmCurrencyConverter.setResizable(false);
		frmCurrencyConverter.setFont(new Font("Dialog", Font.BOLD, 14));
		frmCurrencyConverter.setBackground(Color.GRAY);
		frmCurrencyConverter.setTitle("Currency Converter");
		frmCurrencyConverter.setIconImage(Toolkit.getDefaultToolkit().getImage("E:\\Tools\\eclipse-workspace\\Currency Converter\\res\\forex.png"));
		frmCurrencyConverter.setBounds(100, 100, 720, 506);
		frmCurrencyConverter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
			
		
		JButton btnConvertCurrency = new JButton("Convert Currency");
		btnConvertCurrency.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnConvertCurrency.setBounds(10, 53, 155, 74);
		btnConvertCurrency.setForeground(Color.BLACK);
		
		frmCurrencyConverter.getContentPane().setLayout(null);
		
		JPanel conversionInputPanel = new JPanel();
		conversionInputPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		conversionInputPanel.setBackground(Color.LIGHT_GRAY);
		conversionInputPanel.setBounds(145, 138, 410, 302);
		frmCurrencyConverter.getContentPane().add(conversionInputPanel);
		conversionInputPanel.setLayout(null);
		conversionInputPanel.setVisible(false);
		
		JComboBox<String> fromCurrencyOptions = new JComboBox<>();
		fromCurrencyOptions.setBounds(10, 41, 268, 33);
		
		//Adds currencies from database to Combo Box
		CurrencyUtilities.addCurrenciesToComboBox(fromCurrencyOptions);
		
		conversionInputPanel.add(fromCurrencyOptions);
		
		inputFromValue = new JTextField();
		inputFromValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		inputFromValue.setToolTipText("Enter the currency value to convert");
		inputFromValue.setBounds(288, 41, 112, 33);
		inputFromValue.setText("");
		conversionInputPanel.add(inputFromValue);
		inputFromValue.setColumns(10);
		
		JLabel lblFromCurrency = new JLabel("From currency");
		lblFromCurrency.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblFromCurrency.setBounds(10, 11, 155, 19);
		conversionInputPanel.add(lblFromCurrency);
		
		JLabel lblToCurrency = new JLabel("To currency");
		lblToCurrency.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblToCurrency.setBounds(10, 97, 155, 20);
		conversionInputPanel.add(lblToCurrency);
		
		JComboBox<String> toCurrencyOptions = new JComboBox<>();
		toCurrencyOptions.setBounds(10, 128, 268, 33);
		
		//Adds currencies from database to Combo Box
		CurrencyUtilities.addCurrenciesToComboBox(toCurrencyOptions);
		
		conversionInputPanel.add(toCurrencyOptions);
		
		JLabel lblConversionValue = new JLabel("Conversion Value");
		lblConversionValue.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblConversionValue.setBounds(137, 172, 141, 20);
		conversionInputPanel.add(lblConversionValue);
		
		outputToValue = new JTextField();
		outputToValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		outputToValue.setEditable(false);
		outputToValue.setText("");
		outputToValue.setColumns(10);
		outputToValue.setBounds(144, 203, 134, 33);
		conversionInputPanel.add(outputToValue);
		
		JButton btnConvertConversionPanel = new JButton("Convert");
		
		btnConvertConversionPanel.setBounds(10, 268, 89, 23);
		conversionInputPanel.add(btnConvertConversionPanel);
		
		JButton btnCloseConversionPanel = new JButton("Close");
		btnCloseConversionPanel.setBounds(311, 268, 89, 23);
		conversionInputPanel.add(btnCloseConversionPanel);
		
		JButton btnClearValues = new JButton("Clear");
		
		btnClearValues.setBounds(109, 268, 89, 23);
		conversionInputPanel.add(btnClearValues);
		
		JPanel recentConversionsPanel = new JPanel();
		recentConversionsPanel.setBackground(SystemColor.window);
		recentConversionsPanel.setBounds(10, 181, 684, 275);
		frmCurrencyConverter.getContentPane().add(recentConversionsPanel);
		btnConvertCurrency.setBackground(Color.LIGHT_GRAY);
		frmCurrencyConverter.getContentPane().add(btnConvertCurrency);
		
		JButton btnExportCurrencies = new JButton("Export Currencies");
		btnExportCurrencies.setForeground(Color.BLACK);
		btnExportCurrencies.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExportCurrencies.setBackground(Color.LIGHT_GRAY);
		btnExportCurrencies.setBounds(192, 53, 155, 74);
		frmCurrencyConverter.getContentPane().add(btnExportCurrencies);
		
		JButton btnExportConversionHistory = new JButton("Export Conversion History");
		btnExportConversionHistory.setForeground(Color.BLACK);
		btnExportConversionHistory.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExportConversionHistory.setBackground(Color.LIGHT_GRAY);
		btnExportConversionHistory.setBounds(373, 53, 155, 74);
		frmCurrencyConverter.getContentPane().add(btnExportConversionHistory);
		
		JButton btnResetApplication = new JButton("Reset Application");
		btnResetApplication.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			
			//Resets the application, removing conversion history and resetting the database
			public void actionPerformed(ActionEvent e) {
				try {
					frmCurrencyConverter.setCursor(Cursor.WAIT_CURSOR);
					
					JSONHandler.addNoRecentConversionsToPanel(recentConversionsPanel);
					JSONHandler.resetApplication();
					frmCurrencyConverter.setCursor(Cursor.DEFAULT_CURSOR);
					
					JOptionPane.showMessageDialog(frmCurrencyConverter, "Application reset complete");
					
				} catch (SQLException | ParseException | IOException e1) {
					//LIGHT_ TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		btnResetApplication.setForeground(Color.BLACK);
		btnResetApplication.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnResetApplication.setBackground(Color.LIGHT_GRAY);
		btnResetApplication.setBounds(549, 53, 155, 74);
		frmCurrencyConverter.getContentPane().add(btnResetApplication);
		
		//If conversion history queue is empty, shows no recent conversions message else displays the recent conversions
		if(JSONHandler.conversionQueue.isEmpty())
			JSONHandler.addNoRecentConversionsToPanel(recentConversionsPanel);
		else
			JSONHandler.addRecentConversionsToPanel(recentConversionsPanel);
		
		btnConvertCurrency.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conversionInputPanel.setVisible(true);
			}
		});
		
		btnCloseConversionPanel.addActionListener(new ActionListener() {
			
			//Closes the conversion panel and resets the input and combo box values
			public void actionPerformed(ActionEvent arg0) {
				
				conversionInputPanel.setVisible(false);
				
				JSONHandler.addRecentConversionsToPanel(recentConversionsPanel);
				
				inputFromValue.setText("");
				outputToValue.setText("");
				fromCurrencyOptions.setSelectedIndex(0);
				toCurrencyOptions.setSelectedIndex(0);
				
			}
		});
		
		btnClearValues.addActionListener(new ActionListener() {
			
			//Resets the input and combo box values
			public void actionPerformed(ActionEvent e) {
				inputFromValue.setText("");
				outputToValue.setText("");
				fromCurrencyOptions.setSelectedIndex(0);
				toCurrencyOptions.setSelectedIndex(0);
			}
		});
		
		btnConvertConversionPanel.addActionListener(new ActionListener() {
			
			//Takes input from user and converts the value
			public void actionPerformed(ActionEvent arg0) {
				
				String currencyInputText = inputFromValue.getText();
				
				if(currencyInputText.isEmpty())
					JOptionPane.showMessageDialog(conversionInputPanel, "Please enter amount");
				else if(!currencyInputText.matches(CurrencyConstants.CURRENCY_INPUT_PATTERN))
					JOptionPane.showMessageDialog(conversionInputPanel, "Please enter a valid amount");
				else
					try {
						outputToValue.setText(String.format("%,.2f", Double.valueOf(JSONHandler.getConversionRate(CurrencyUtilities.getCurrencyFromComboBox(fromCurrencyOptions), Double.valueOf(currencyInputText), CurrencyUtilities.getCurrencyFromComboBox(toCurrencyOptions)))));
						
						JSONHandler.addHistoryToQueue();
						
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
		
		//Opens file chooser dialog to prompt user to select location for exporting conversion history
		btnExportConversionHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				fileChooser.setDialogTitle("Select a folder to save...");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int userChoice = fileChooser.showSaveDialog(frmCurrencyConverter);
				
				
				if(userChoice == JFileChooser.APPROVE_OPTION)
				{
					if(fileChooser.getSelectedFile().isDirectory())
					{
						try 
						{
							boolean success = JSONHandler.writeConversionHistory(fileChooser.getSelectedFile().getAbsolutePath());
							
							if(success)
								JOptionPane.showMessageDialog(frmCurrencyConverter, "Conversion history exported successfully");
							else
								JOptionPane.showMessageDialog(frmCurrencyConverter, "Some error occured");
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			}
		});
		
		//Opens file chooser dialog to prompt user to select location for exporting currencies
		btnExportCurrencies.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				fileChooser.setDialogTitle("Select a folder to save...");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int userChoice = fileChooser.showSaveDialog(frmCurrencyConverter);
				
				
				if(userChoice == JFileChooser.APPROVE_OPTION)
				{
					if(fileChooser.getSelectedFile().isDirectory())
					{
						try 
						{
							boolean success = JSONHandler.writeCurrencies(fileChooser.getSelectedFile().getAbsolutePath());
							
							if(success)
								JOptionPane.showMessageDialog(frmCurrencyConverter, "Currencies exported successfully");
							else
								JOptionPane.showMessageDialog(frmCurrencyConverter, "Some error occured");
							
						} catch (IOException exp) {
							// TODO Auto-generated catch block
							exp.printStackTrace();
						} catch (SQLException exp) {
							// TODO Auto-generated catch block
							exp.printStackTrace();
						}
					}
					
				}
			}
		});
		
	}
}
