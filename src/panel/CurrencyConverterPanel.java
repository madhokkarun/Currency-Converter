package panel;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;

import org.json.simple.parser.ParseException;

import handler.JSONHandler;
import model.ConversionHistory;

import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import java.awt.SystemColor;
import javax.swing.JLabel;

public class CurrencyConverterPanel {

	private JFrame frmCurrencyConverter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
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
	 */
	public CurrencyConverterPanel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCurrencyConverter = new JFrame();
		frmCurrencyConverter.setFont(new Font("Dialog", Font.BOLD, 14));
		frmCurrencyConverter.setBackground(Color.GRAY);
		frmCurrencyConverter.setTitle("Currency Converter");
		frmCurrencyConverter.setIconImage(Toolkit.getDefaultToolkit().getImage("E:\\Tools\\eclipse-workspace\\Currency Converter\\res\\forex.png"));
		frmCurrencyConverter.setBounds(100, 100, 720, 480);
		frmCurrencyConverter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel recentConversionsPanel = new JPanel();
		recentConversionsPanel.setBackground(SystemColor.window);
		recentConversionsPanel.setBounds(10, 181, 684, 249);
		frmCurrencyConverter.getContentPane().add(recentConversionsPanel);
		
		if(JSONHandler.conversionQueue.isEmpty())
			JSONHandler.addNoRecentConversionsToPanel(recentConversionsPanel);
		else
			JSONHandler.addRecentConversionsToPanel(recentConversionsPanel);
			
		
		JButton btnConvertCurrency = new JButton("Convert Currency");
		btnConvertCurrency.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnConvertCurrency.setVerticalAlignment(SwingConstants.BOTTOM);
		btnConvertCurrency.setBounds(10, 53, 142, 74);
		btnConvertCurrency.setForeground(Color.BLACK);
		btnConvertCurrency.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frmCurrencyConverter.getContentPane().setLayout(null);
		btnConvertCurrency.setBackground(Color.LIGHT_GRAY);
		frmCurrencyConverter.getContentPane().add(btnConvertCurrency);
		
		JButton btnExportCurrencies = new JButton("Export Currencies");
		btnExportCurrencies.setVerticalAlignment(SwingConstants.BOTTOM);
		btnExportCurrencies.setForeground(Color.BLACK);
		btnExportCurrencies.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExportCurrencies.setBackground(Color.LIGHT_GRAY);
		btnExportCurrencies.setBounds(192, 53, 142, 74);
		frmCurrencyConverter.getContentPane().add(btnExportCurrencies);
		
		JButton btnExportConversionHistory = new JButton("Export Conversion History");
		btnExportConversionHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnExportConversionHistory.setVerticalAlignment(SwingConstants.BOTTOM);
		btnExportConversionHistory.setForeground(Color.BLACK);
		btnExportConversionHistory.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExportConversionHistory.setBackground(Color.LIGHT_GRAY);
		btnExportConversionHistory.setBounds(373, 53, 142, 74);
		frmCurrencyConverter.getContentPane().add(btnExportConversionHistory);
		
		JButton btnResetApplication = new JButton("Reset Application");
		btnResetApplication.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				try {
					frmCurrencyConverter.setCursor(Cursor.WAIT_CURSOR);
					recentConversionsPanel.removeAll();
					recentConversionsPanel.revalidate();
					recentConversionsPanel.repaint();
					
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
		btnResetApplication.setVerticalAlignment(SwingConstants.BOTTOM);
		btnResetApplication.setForeground(Color.BLACK);
		btnResetApplication.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnResetApplication.setBackground(Color.LIGHT_GRAY);
		btnResetApplication.setBounds(552, 53, 142, 74);
		frmCurrencyConverter.getContentPane().add(btnResetApplication);
		
		
		
	}
}
