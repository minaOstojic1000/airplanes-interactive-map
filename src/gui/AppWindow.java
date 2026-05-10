package gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.Dialog.ModalityType;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dialog;

public class AppWindow extends Frame {
	
	static private AppWindow instance;
	
	static private AppTimer userActivity;
	
	private Button inputAirport = new Button("Enter airport information");
	private Button inputFlight = new Button("Enter flight information");
	private Button loadData = new Button("Load from file");
	private Button saveData = new Button("Save in file");
	private Button exitApplication = new Button("Exit application");
	private Button showAirports = new Button("Show airports map");
	
	static final Color defaultWindowBackColor = new Color(240, 244, 248);
	
	static final int defaultButtonHeight = 50;
	static final int defaultButtonWidth = 200;
	static final Color defaultButtonBackColor = new Color(25, 118, 210);
	static final Color defaultButtonForeColor = Color.WHITE;
	static final Font defaultButtonFont = new Font("Segoe UI", Font.BOLD, 14);
	
	static final Font defaultInputLabelFont = new Font("Poppins", Font.PLAIN, 13);
	static final Font defaultErrorLabelFont = new Font("Monospaced", Font.PLAIN, 12);
	static final Font defaultInstructionLabelFont = new Font("Monospaced", Font.PLAIN, 12);
	
	private class QuitDialog extends Dialog {
		
		private Button okClose = new Button("OK");
		private Button cancelClose = new Button("Cancel");
		
		public QuitDialog(Frame owner) {
			super(owner);
			
			setTitle("Exit application");
			setBounds(owner.getX() + owner.getWidth()/4,
					owner.getY() + owner.getHeight()/4, 200, 150);
			setResizable(false);
			// blokiraj rad app dok se ne zatvori dijalog
			setModalityType(ModalityType.APPLICATION_MODAL);
			
			okClose.addActionListener((ae)->{
				if (userActivity != null) {
					userActivity.interrupt();
				}
				AppWindow.this.dispose();
			});
			
			cancelClose.addActionListener((ae)->{
				QuitDialog.this.dispose();
			});
			
			Panel buttons = new Panel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 5, 5, 5); // margine izmedju komponenti
			gbc.weightx = 1.0;
			gbc.gridx = 0;
			
			buttons.add(okClose, gbc);
			
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1.0;
			gbc.gridx = 1;

			buttons.add(cancelClose, gbc);
			
			Panel messagePanel = new Panel();
			messagePanel.add(new Label("Are you sure you want to exit?"), BorderLayout.CENTER);
			
			Panel content = new Panel(new GridBagLayout());
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.gridy = 0;
			content.add(messagePanel, gbc);
			gbc.anchor = GridBagConstraints.PAGE_END;
			gbc.gridy = 1;
			content.add(buttons, gbc);
			
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dispose();
				}
			});
			
			this.add(content);
			setVisible(true);
		}
	}
	
	class ActivityTimeDialog extends Dialog {

		private Label info = new Label("Inactivity timeout in...");
		private Label time = new Label("");
		private Button stayActive = new Button("Stay active");
		
		public ActivityTimeDialog(Frame owner) {
			super(owner);
			setLocation(owner.getX() + owner.getWidth()/4,
					owner.getY() + owner.getHeight()/4);
			Panel timePanel = new Panel(new FlowLayout());
			timePanel.add(info);
			timePanel.add(time);
			Panel content = new Panel(new GridLayout(0, 1));
			content.add(timePanel);
			content.add(stayActive);
			
			this.setLayout(new BorderLayout());
			this.add(new Panel(), BorderLayout.NORTH); // margina
			this.add(new Panel(), BorderLayout.SOUTH); // margina
			this.add(new Panel(), BorderLayout.EAST); // margina
			this.add(new Panel(), BorderLayout.WEST); // margina
			this.getComponent(0).setPreferredSize(new Dimension(0, 20));
			this.getComponent(1).setPreferredSize(new Dimension(0, 20));
			this.getComponent(2).setPreferredSize(new Dimension(20, 0));
			this.getComponent(3).setPreferredSize(new Dimension(20, 0));
			this.add(content, BorderLayout.CENTER);
		
			pack();
			this.setAlwaysOnTop(true);
			
			stayActive.addActionListener((ae)->{
				this.setVisible(false);
				userActivity.reset();
			});
			
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					ActivityTimeDialog.this.setVisible(false);
					userActivity.reset();
				}
			
			});
			
			setVisible(false);
		}
		
		public Label getTime() {
			return time;
		}
		
		public void setTime(Label time) {
			this.time = time;
		}
	}
	
	static public AppWindow Instance() {
		if (instance == null)
			instance = new AppWindow();
		return instance;
	}
	
	protected AppWindow() {
		
		setLocation(400, 100);
		setResizable(true);
		setTitle("Airport application");
				
		this.setBounds(this.getLocation().x, this.getLocation().y, 500, 500);
		
		populateWindow();
		
		ActivityTimeDialog activityDialog = new ActivityTimeDialog(this);
		
		userActivity = new AppTimer(activityDialog);
		userActivity.start(); // pokrece nit, tj. izvrsava run() u zasebnom toku kontrole; moze se pozvati samo jednom! ("ozivljava objekat")
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (userActivity != null) {
					userActivity.interrupt();
				}
				new QuitDialog(AppWindow.this);
			}
		});

		Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
		    if ((event instanceof MouseEvent || event instanceof KeyEvent) && !activityDialog.isVisible()) {
		        userActivity.reset();
		    }
		}, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);

		setVisible(true);
		userActivity.go(); // regulise kada nit krece a kada staje
	}
	
	private void populateWindow() {
		
		Panel content = new Panel(new GridBagLayout());
		content.setBackground(defaultWindowBackColor);
		
		Panel buttonsPanel = new Panel(new GridLayout(0, 1, 0, 20));

		formatButton(inputAirport, null, null, null, null, null);
		formatButton(inputFlight, null, null, null, null, null);
		formatButton(loadData, null, null, null, null, null);
		formatButton(exitApplication, null, null, null, null, null);
		formatButton(showAirports, null, null, null, null, null);
		formatButton(saveData, null, null, null, null, null);
		
		clickInputAirport();
		clickInputFlight();
		clickLoadData();
		clickSaveData();
		clickExitApplication();
		clickShowAirports();
		
		buttonsPanel.add(inputAirport);
		buttonsPanel.add(inputFlight);
		buttonsPanel.add(loadData);
		buttonsPanel.add(saveData);
		buttonsPanel.add(exitApplication);
		buttonsPanel.add(showAirports);

		content.add(buttonsPanel);

		this.add(content);
	}

	static void formatButton(Button button, Integer height, Integer width, Color backColor, Color foreColor, Font font) {
		if (height == null) height = defaultButtonHeight;
		if (width == null) width = defaultButtonWidth;
		if (backColor == null) backColor = defaultButtonBackColor;
		if (foreColor == null) foreColor = defaultButtonForeColor;
		
		if (font == null) button.setFont(defaultButtonFont);
		
		button.setPreferredSize(new Dimension(width, height));
		button.setBackground(backColor);
		button.setForeground(foreColor);
	}
	
	private void clickInputAirport() {
		inputAirport.addActionListener((ae)->{
			new AirportForm();
		});
	}
	
	private void clickInputFlight() {
		inputFlight.addActionListener((ae)->{
			 new FlightForm();
		});
	}
	
	private void clickLoadData() {
		loadData.addActionListener((ae)->{
			 new LoadFromFile(this);
		});
	}
	
	private void clickSaveData() {
		saveData.addActionListener((ae)->{
			 new SaveInFile(this);
		});
	}
	
	private void clickExitApplication() {
		exitApplication.addActionListener((ae)->{
			new QuitDialog(AppWindow.this);
		});
	}
	

	private void clickShowAirports() {
		showAirports.addActionListener((ae)->{
			new AirportsMap();
		});
	}
	
	static public AppTimer getTimer() {
		return userActivity;
	}
	
	public static void main(String[] args) {
		AppWindow aw = Instance();
	}

}
