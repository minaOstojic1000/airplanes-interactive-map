package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import myExceptions.AirportAlreadyExistsException;
import myExceptions.AirportDoesntExistException;
import myExceptions.BadTimeFormatException;
import myExceptions.CoordinateOutOfRangeException;
import myExceptions.FileParseException;
import myExceptions.IdCodeInvalidException;

public abstract class ChooseFileBaseDialog extends Dialog{
	
	private class ErrorSuccessDialog extends Dialog {
		
		private Button ok = new Button("OK");
		public ErrorSuccessDialog(Dialog owner, String message) {
			super(owner);
			
			setTitle("File reading result");
			setLocation(owner.getX() + owner.getWidth()/4,
					owner.getY() + owner.getHeight()/4);
			setResizable(false);
			setModalityType(ModalityType.APPLICATION_MODAL);
			
			Panel messagePanel = new Panel(new GridLayout(0, 1));
			messagePanel.add(new Label(message));
			
			ok.setPreferredSize(new Dimension(60, 25));
			Panel okPanel = new Panel();
			okPanel.add(ok);
			messagePanel.add(okPanel);
			
			add(messagePanel);
			
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dispose();
				}
			});
			
			ok.addActionListener((ae)->{
				dispose();
			});
			pack();
			setVisible(true);
		}
	}
	
	private String dialogTitle = new String();
	private Button airportButton = new Button();
	private Button flightButton = new Button();
	private TextField fileText = new TextField(20);
	
	// returns success message
	protected abstract String processAirportData(String fileName) 
			throws IOException, AirportAlreadyExistsException, CoordinateOutOfRangeException, 
			IdCodeInvalidException, FileParseException;
	
	// returns success message
	protected abstract String processFlightData(String fileName) 
			throws IOException, FileParseException, 
			AirportDoesntExistException, BadTimeFormatException;
		
	protected ChooseFileBaseDialog(Frame owner, String dialogTitle, String buttonAirportLabel, String buttonFlightLabel) {
		super(owner);
		
		setLocation(owner.getX() + owner.getWidth()/4,
				owner.getY() + owner.getHeight()/4);
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		this.setDialogTitle(dialogTitle);
		this.setAirportButton(buttonAirportLabel);
		this.setFlightButton(buttonFlightLabel);
		
		populateWindow();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		airportButton.addActionListener((ae)->{
			buttonAction(fFileName->processAirportData(fFileName));
		});
		
		flightButton.addActionListener((ae)->{
			buttonAction(fFileName->processFlightData(fFileName));
		});
	
		pack();
		setVisible(true);
	}
		
	private void populateWindow() {
		Panel buttons = new Panel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.weightx = 1.0;
		gbc.gridx = 0;
		
		buttons.add(airportButton, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.gridx = 1;

		buttons.add(flightButton, gbc);
		
		Panel messagePanel = new Panel(new GridLayout(0, 1));
		messagePanel.add(new Label("Enter file name"));
		messagePanel.add(fileText);
		
		Panel content = new Panel(new GridBagLayout());
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridy = 0;
		content.add(messagePanel, gbc);
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.gridy = 1;
		content.add(buttons, gbc);
		this.add(content);
	}
	
	@FunctionalInterface
	interface PD {String processData(String fileName) throws 
		IOException, AirportAlreadyExistsException, CoordinateOutOfRangeException, 
		IdCodeInvalidException, FileParseException,
		AirportDoesntExistException, BadTimeFormatException;}
	
	private void buttonAction(PD process) {
		if (fileText.getText().length() == 0) {
			new ErrorSuccessDialog(this, "Enter name of the file.");
			return;
		}
		try {
			String successMessage = process.processData(fileText.getText());
			new ErrorSuccessDialog(this, successMessage);
		} 
		catch(Exception e) {
			new ErrorSuccessDialog(this, e.getMessage());
		}
	}
	

	public String getDialogTitle() {
		return dialogTitle;
	}

	public Button getAirportButton() {
		return airportButton;
	}

	public Button getFlightButton() {
		return flightButton;
	}

	public TextField getFileText() {
		return fileText;
	}

	protected void setDialogTitle(String title) {
		this.setTitle(title);
	}
	
	protected void setAirportButton(String text) {
		airportButton.setLabel(text);
	}
	
	protected void setFlightButton(String text) {
		flightButton.setLabel(text);
	}

}
