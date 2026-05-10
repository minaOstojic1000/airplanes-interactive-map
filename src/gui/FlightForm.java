package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Dialog.ModalityType;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import airportClasses.Airport;
import airportClasses.Flight;
import myExceptions.AirportAlreadyExistsException;
import myExceptions.AirportDoesntExistException;
import myExceptions.BadTimeFormatException;
import myExceptions.CoordinateOutOfRangeException;
import myExceptions.IdCodeInvalidException;

public class FlightForm extends InputDataForm {
	
	private TextField departureAirport = new TextField(3);
	private TextField arrivalAirport = new TextField(3);
	private TextField hourInput = new TextField(2);
	private TextField minInput = new TextField(2);
	private TextField durationInput = new TextField(2);
	
	private Label AirportValidLabel = new Label("");
	private Label TimeValidLabel = new Label("");
	
	private boolean invalidAir = false;
	private boolean invalidTimeDur = false;
	
	private class SuccessDialog extends Dialog {
		
		private Button ok = new Button("OK");
		private String message;
		private String[] info;
		
		public SuccessDialog(Frame owner, String message, String[] info) {
			super(owner);
			
			this.message = message;
			this.info = info;
			
			setTitle("Flight input result");
			setLocation(owner.getX() + owner.getWidth()/4,
					owner.getY() + owner.getHeight()/4);
			setResizable(false);
			setModalityType(ModalityType.APPLICATION_MODAL);
			
			populateWindow();
			
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
		
		private void populateWindow() {
			Panel content = new Panel(new BorderLayout());
			
			Panel messagePanel = new Panel(new FlowLayout());
			messagePanel.add(new Label(message));
			
			ok.setPreferredSize(new Dimension(60, 25));
			Panel okPanel = new Panel();
			okPanel.add(ok);
			
			content.add(new Panel(), BorderLayout.EAST);
		    content.add(new Panel(), BorderLayout.WEST);
		    content.getComponent(0).setPreferredSize(new Dimension(10, 0));
	        content.getComponent(1).setPreferredSize(new Dimension(10, 0));
	        content.add(messagePanel, BorderLayout.NORTH);
	        content.add(okPanel, BorderLayout.SOUTH);
			content.add(makeInfoPanel(), BorderLayout.CENTER);
			
	        add(content);
		}
		
		private Panel makeInfoPanel() {
			Panel infoPanel = new Panel(new GridLayout(3, 2, 5, 5));
			infoPanel.add(new Label("Flight ID: " + info[0]));
			infoPanel.add(new Label("Dep. time:  " + info[3] + ":" + info[4]));
			infoPanel.add(new Label("Dep. airport: " + info[1]));
			infoPanel.add(new Label("Duration: " + info[5] + " min"));
			infoPanel.add(new Label("Arr. airport: " + info[2]));
			Panel margin = new Panel(new BorderLayout());
			margin.add(new Panel(), BorderLayout.WEST);
			margin.getComponent(0).setPreferredSize(new Dimension(30, 0));
			margin.add(infoPanel, BorderLayout.CENTER);
			return margin;
		}
	}
	
	public FlightForm() {
		
		setLocation(700, 100);
		setResizable(true);
		setTitle("Flight input data");
				
		populateWindow();
		
		pack();
		this.setBounds(this.getLocation().x, this.getLocation().y, 500, this.getHeight());
		
		setVisible(true);
	}
	
	private void populateWindow() {
		
		Panel content = new Panel(new GridLayout(0, 1));
		
		Panel infoPanel1 = makeLabelMessagePanel(new Label("Input departure and arrival airport ID code (3 letters)"), 
				AppWindow.defaultInstructionLabelFont, null, Color.BLACK);
		
		Panel infoPanel2 = makeLabelMessagePanel(new Label("Input departure time and flight duration"), 
				AppWindow.defaultInstructionLabelFont, null, Color.BLACK);
	
		Panel departurePanel = makeTextPanel(departureAirport, "Departure airport ID: ", null);
		
		Panel arrivalPanel = makeTextPanel(arrivalAirport, "Arrival airport ID: ", null);
		
		Panel AirportValidPanel = makeLabelMessagePanel(AirportValidLabel, AppWindow.defaultErrorLabelFont, null, errorColor);

		listenAirport(departureAirport, AirportValidLabel);
		listenAirport(arrivalAirport, AirportValidLabel);
		
		Panel HourPanel = makeTextPanel(hourInput, "Departure hour (0-24): ", null);

		Panel MinPanel = makeTextPanel(minInput, "Departure minute (0-59): ", null);
		
		Panel DurationPanel = makeTextPanel(durationInput, "Flight duration (in minutes): ", null);
		
		Panel TimeValidPanel = makeLabelMessagePanel(TimeValidLabel, AppWindow.defaultErrorLabelFont, null, errorColor);
		
		Panel submitPanel = makeButtonPanel(submit);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		enableDisableSubmit();
		
		listenTimeDur(hourInput, TimeValidLabel);
		listenTimeDur(minInput, TimeValidLabel);
		listenTimeDur(durationInput, TimeValidLabel);

		content.add(infoPanel1);
		content.add(departurePanel);
		content.add(arrivalPanel);
		content.add(AirportValidPanel);
		content.add(infoPanel2);
		content.add(HourPanel);
		content.add(MinPanel);
		content.add(DurationPanel);
		content.add(TimeValidPanel);
		content.add(submitPanel);
		
		clickSubmit();
		
		this.add(content, BorderLayout.CENTER);
		}
	
	private void listenAirport(TextField tf, Label message) {
	
		message.setVisible(false);
		tf.addTextListener((te)->{
			int len = tf.getText().length();
			int fi = -1;
			if (len >= 1 && !Character.isLetter(tf.getText().charAt(len - 1))) {
				fi = len - 1;
			}
			else if (len >= 1 && fi < len && fi >= 0 &&
					Character.isLetter(tf.getText().charAt(fi))) {
				fi = -1;
			}
			if (fi >= 0 && len > 0) {
				message.setText("*Airport ID can't include numbers or special characters.");
				tf.setForeground(errorColor);
				invalidAir = true;
				message.setVisible(true);
			}
			else if (len != 3 && len >= 1) {
				message.setText("*Airport ID code must be 3 characters long.");
				tf.setForeground(errorColor);
				invalidAir = true;
				message.setVisible(true);
			}
			else {
				tf.setForeground(defaultTextFColor);
				invalidAir = false;
				message.setVisible(false);
			}
			message.revalidate();
			enableDisableSubmit();
		});
		
		tf.addKeyListener(new KeyListener() {
	        @Override
	        public void keyTyped(KeyEvent e) {}
	
	        @Override
	        public void keyPressed(KeyEvent e) {}
	
	        @Override
	        public void keyReleased(KeyEvent e) {
	        	int caretPos = tf.getCaretPosition();
	            tf.setText(tf.getText().toUpperCase());
	            tf.setCaretPosition(caretPos);
	        }
	    });
		
	}

	private void listenTimeDur(TextField tf, Label message) {

		tf.addKeyListener(new KeyListener() {
	        @Override
	        public void keyTyped(KeyEvent e) {}
	
	        @Override
	        public void keyPressed(KeyEvent e) {}
	
	        @Override
	        public void keyReleased(KeyEvent e) {
	        	message.setVisible(false);
	        	invalidTimeDur = false;
	        	int fi = -1;
	        	if (!Character.isDigit(e.getKeyChar()) && 
	        			!(e.getKeyChar() == '\b')) {
	        		fi = tf.getText().length() - 1;
	        	}
	        	else {	
	        		for (int i = 0; i < tf.getText().length(); i++) {
	        			if (!Character.isDigit(tf.getText().toCharArray()[i]))
	        			{ fi = i; break; }
	        		}
	        	}
	        	if (fi != -1) {
	        		invalidTimeDur = true;
	        		tf.setForeground(errorColor);
	        	}
	        	else {
	        		invalidTimeDur = false;
	        		tf.setForeground(defaultTextFColor);
	        	}
	        }
	    });
		
		tf.addTextListener((te)->{
			enableDisableSubmit();
		});
	}

	@Override
	protected boolean checkToSubmit() {
		if (departureAirport.getText().length() != 3 || arrivalAirport.getText().length() != 3 ||
				hourInput.getText().length() == 0 || minInput.getText().length() == 0 || durationInput.getText().length() == 0 ||
				invalidAir || invalidTimeDur)
			return false;
		return true;
	}

	@Override
	protected void clickSubmit() {
		submit.addActionListener((ae)->{
			try {
				Flight.createFlight(getIdCodeStart(), getIdCodeEnd(), Integer.parseInt(getHours()), 
						Integer.parseInt(getMinutes()), Integer.parseInt(getDuration()));
				String[] info = {((Integer)(Flight.getNextId() - 1)).toString(), String.copyValueOf(getIdCodeStart()), 
						String.copyValueOf(getIdCodeEnd()),getHours().toString(), getMinutes().toString(),
						getDuration().toString()};
				new FlightForm.SuccessDialog(this, "Flight successfully added!", info);
			} 
			catch (NumberFormatException e) {} 
			catch (BadTimeFormatException e) {
				TimeValidLabel.setText(e.getMessage());
				TimeValidLabel.setForeground(errorColor);
				TimeValidLabel.setVisible(true);
				TimeValidLabel.revalidate();
			} 
			catch (AirportDoesntExistException e) {
				
				AirportValidLabel.setText(e.getMessage());
				AirportValidLabel.setForeground(errorColor);
				AirportValidLabel.setVisible(true);
				AirportValidLabel.revalidate();
			} 
		});
	}

	public char[] getIdCodeStart() {
		return departureAirport.getText().toCharArray();
	}
	
	public char[] getIdCodeEnd() {
		return arrivalAirport.getText().toCharArray();
	}
	
	public String getHours() {
		return hourInput.getText();
	}
	
	public String getMinutes() {
		return minInput.getText();
	}
	
	public String getDuration() {
		return durationInput.getText();
	}
	
	public static void main(String[] args) {
		new FlightForm();
	}
}
