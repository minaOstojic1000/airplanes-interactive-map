package gui;

import airportClasses.Airport;
import myExceptions.AirportAlreadyExistsException;
import myExceptions.CoordinateOutOfRangeException;
import myExceptions.IdCodeInvalidException;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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


public class AirportForm extends InputDataForm{
	
	private TextField nameInput = new TextField(10);
	private TextField idCodeInput = new TextField(3);
	private TextField xInput = new TextField(5);
	private TextField yInput = new TextField(5);
	
	private Label idCodeValidLabel = new Label("");
	private Label XYValidLabel = new Label("");
	
	private boolean invalidCode = false;
	private boolean invalidXY = false;
	
	private class SuccessDialog extends Dialog {
			
		private Button ok = new Button("OK");
		private String message;
		private String[] info;
		
		public SuccessDialog(Frame owner, String message, String[] info) {
			super(owner);
			
			this.message = message;
			this.info = info;
			
			setTitle("Airport input result");
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
			Panel infoPanel = new Panel(new GridLayout(2, 2, 5, 5));
			infoPanel.add(new Label("Name: " + info[0]));
			infoPanel.add(new Label("X: " + info[2]));
			infoPanel.add(new Label("ID code: " + info[1]));
			infoPanel.add(new Label("Y: " + info[3]));
			Panel margin = new Panel(new BorderLayout());
			margin.add(new Panel(), BorderLayout.WEST);
			margin.getComponent(0).setPreferredSize(new Dimension(30, 0));
			margin.add(infoPanel, BorderLayout.CENTER);
			return margin;
		}
	}
	
	public AirportForm() {
	
		setLocation(700, 200);
		setResizable(true);
		setTitle("Airport input data");
				
		populateWindow();
		
		pack();
		this.setBounds(this.getLocation().x, this.getLocation().y, 400, this.getHeight());
		
		setVisible(true);
	}
	
	private void populateWindow() {
		
		Panel content = new Panel(new GridLayout(0, 1));
		
		Panel infoPanel1 = makeLabelMessagePanel(new Label("Input airport name and ID code (3 letters)"), 
				AppWindow.defaultInstructionLabelFont, null, Color.BLACK);
		
		Panel infoPanel2 = makeLabelMessagePanel(new Label("Input airport x and y coordinates"), 
				AppWindow.defaultInstructionLabelFont, null, Color.BLACK);
	
		Panel namePanel = makeTextPanel(nameInput, "Airport name: ", null);
		
		Panel idCodePanel = makeTextPanel(idCodeInput, "Airport ID: ", null);
		
		Panel idCodeValidPanel = makeLabelMessagePanel(idCodeValidLabel, AppWindow.defaultErrorLabelFont, null, errorColor);

		listenIdCode(idCodeValidLabel);
		
		Panel coorXPanel = makeTextPanel(xInput, "Coordinate X: ", null);

		Panel coorYPanel = makeTextPanel(yInput, "Coordinate Y: ", null);
		
		Panel XYValidPanel = makeLabelMessagePanel(XYValidLabel, AppWindow.defaultErrorLabelFont, null, errorColor);
		
		Panel submitPanel = makeButtonPanel(submit);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		enableDisableSubmit();
		listenName();
		listenCoordinate(xInput);
		listenCoordinate(yInput);
		
		content.add(infoPanel1);
		content.add(namePanel);
		content.add(idCodePanel);
		content.add(idCodeValidPanel);
		content.add(infoPanel2);
		content.add(coorXPanel);
		content.add(coorYPanel);
		content.add(XYValidPanel);
		content.add(submitPanel);
		
		clickSubmit();
		
		this.add(content, BorderLayout.CENTER);
	}
	
	private void listenIdCode(Label message) {
		
		idCodeInput.addTextListener((te)->{
			int len = idCodeInput.getText().length();
			int firstInvalidCode = -1;
			if (len >= 1 && !Character.isLetter(idCodeInput.getText().charAt(len - 1))) {
				firstInvalidCode = len - 1;
			}
			else if (len >= 1 && firstInvalidCode < len && firstInvalidCode >= 0 &&
					Character.isLetter(idCodeInput.getText().charAt(firstInvalidCode))) {
				firstInvalidCode = -1;
			}
			if (firstInvalidCode >= 0 && len > 0) {
				message.setText("*Airport ID can't include numbers or special characters.");
				idCodeInput.setForeground(errorColor);
				invalidCode = true;
				message.setVisible(true);
			}
			else if (len != 3 && len >= 1) {
				message.setText("*Airport ID code must be 3 characters long.");
				idCodeInput.setForeground(errorColor);
				invalidCode = true;
				message.setVisible(true);
			}
			else {
				idCodeInput.setForeground(defaultTextFColor);
				invalidCode = false;
				message.setVisible(false);
			}
			message.revalidate();
			enableDisableSubmit();
		});
		
		idCodeInput.addKeyListener(new KeyListener() {
	        @Override
	        public void keyTyped(KeyEvent e) {}
	
	        @Override
	        public void keyPressed(KeyEvent e) {}
	
	        @Override
	        public void keyReleased(KeyEvent e) {
	        	int caretPos = idCodeInput.getCaretPosition();
	            idCodeInput.setText(idCodeInput.getText().toUpperCase());
	            idCodeInput.setCaretPosition(caretPos);
	        }
	    });
	}
	
	private void listenName() {
		nameInput.addTextListener((te)->{
			enableDisableSubmit();
		});
	}
	
	private void listenCoordinate(TextField tf) {
		tf.addKeyListener(new KeyListener() {
	        @Override
	        public void keyTyped(KeyEvent e) {}
	
	        @Override
	        public void keyPressed(KeyEvent e) {}
	
	        @Override
	        public void keyReleased(KeyEvent e) {
	        	XYValidLabel.setVisible(false);
	        	int fi = -1;
	        	if (!Character.isDigit(e.getKeyChar()) && 
	        			!(e.getKeyChar() == '\b') && !(e.getKeyChar() == '.')) {
	        		fi = tf.getText().length() - 1;
	        	}
	        	else {	
	        		for (int i = 0; i < tf.getText().length(); i++) {
	        			if (!Character.isDigit(tf.getText().toCharArray()[i])
	        					&& !(tf.getText().toCharArray()[i] == '.') && !(tf.getText().toCharArray()[i] == '-'))
	        			{ fi = i; break; }
	        		}
	        	}
	        	if (fi != -1) {
	        		invalidXY = true;
	        		tf.setForeground(errorColor);
	        	}
	        	else {
	        		invalidXY = false;
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
		if (nameInput.getText().length() == 0 || 
				idCodeInput.getText().length() != 3 || invalidCode || 
			xInput.getText().length() == 0 || yInput.getText().length() == 0
			|| invalidXY)
			return false;
		return true;
	}
	
	
	@Override
	protected void clickSubmit() {

		submit.addActionListener((ae)->{
			try {
				Airport.createAirport(getNameInput(), getIdCode(), Double.parseDouble(getXInput()), Double.parseDouble(getYInput()));
				String[] info = {getNameInput(), String.copyValueOf(getIdCode()), getXInput(), getYInput()};
				new AirportForm.SuccessDialog(this, "Airport successfully added!", info);
			} 
			catch (NumberFormatException e) {} 
			catch (CoordinateOutOfRangeException e) {
	
				XYValidLabel.setText(e.getMessage());
				XYValidLabel.setForeground(errorColor);
				XYValidLabel.setVisible(true);
				XYValidLabel.revalidate();
			} 
			catch (IdCodeInvalidException e) {} 
			catch (AirportAlreadyExistsException e) {
				XYValidLabel.setText(e.getMessage());
				XYValidLabel.setForeground(errorColor);
				XYValidLabel.setVisible(true);
				XYValidLabel.revalidate();
			}
		});
	}
	
	public String getNameInput() {
		return nameInput.getText();
	}
	
	public char[] getIdCode() {
		return idCodeInput.getText().toCharArray();
	}

	public String getXInput() {
		return xInput.getText();
	}

	public String getYInput() {
		return yInput.getText();
	}

	public static void main(String[] args) {
		new AirportForm();
	}

}
