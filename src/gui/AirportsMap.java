package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.Dialog.ModalityType;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import airportClasses.Airport;
import airportClasses.AirportsBase;
import airportClasses.Flight;
import airportClasses.FlightsBase;
import myExceptions.AirportAlreadyExistsException;
import myExceptions.AirportDoesntExistException;
import myExceptions.BadTimeFormatException;
import myExceptions.CoordinateOutOfRangeException;
import myExceptions.IdCodeInvalidException;
import shapes.BlinkingSquare;

import java.awt.List;

public class AirportsMap extends Frame {

	private ArrayList<Airport> allAirports = new ArrayList<Airport>();
	private ArrayList<Airport> shownAirports = new ArrayList<Airport>();
	private ArrayList<Airport> newClearedAirports = new ArrayList<Airport>();
	
	private ArrayList<Flight> allFlights = new ArrayList<>();
	
	private ArrayList<Checkbox> checkboxes = new ArrayList<>();
	private TextArea info = new TextArea();
	private Label simulationTime = new Label();
	
	private Button selectAll = new Button("Select all");
	private Button deselectAll = new Button("Deselect all");
	private Button startSimulation = new Button("Start simulation");
	private Button resetSimulation = new Button("Reset simulation");
	
	private MapCanvas map;
	
	private class UnexpectedErrorDialog extends Dialog {
		
		private Button okClose = new Button("Exit application");
		
		public UnexpectedErrorDialog(Frame owner) {
			super(owner);
			
			setTitle("Unexpected error");
			setBounds(owner.getX() + owner.getWidth()/4,
					owner.getY() + owner.getHeight()/4, 200, 150);
			setResizable(false);
			// blokiraj rad app dok se ne zatvori dijalog
			setModalityType(ModalityType.APPLICATION_MODAL);
			
			okClose.addActionListener((ae)->{
				AirportsMap.this.dispose();
				AppWindow.Instance().dispose();
			});
			
			Panel buttons = new Panel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 5, 5, 5); // margine izmedju komponenti
			gbc.weightx = 1.0;
			gbc.gridx = 0;
			
			buttons.add(okClose, gbc);
			
			Panel messagePanel = new Panel();
			messagePanel.add(new Label("Unexpected error occured..."), BorderLayout.CENTER);
			
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
	
	public AirportsMap() {
		
		try {
			setBounds(500, 200, 700, 500);
			setResizable(true);
			setTitle("Map of airports");
					
			allAirports = AirportsBase.Instance().getAirList();
			allFlights = FlightsBase.Instance().getFlightList();
			//testiranje();
			
			populateWindow();
			map.setShownAirports(shownAirports);
			map.setNewHiddenAirports(newClearedAirports);
			
			
		setResizable(false);
			/*
		  this.addComponentListener(new ComponentAdapter() {
	            @Override
	            public void componentResized(ComponentEvent e) {
	            	int mapWidth = getWidth() - 150 - 150;
	        		int mapHeight = getHeight();
	        		if (map != null) {
	        			map.setWidth(mapWidth);
	        			map.setHeight(mapWidth);
	        		}
	                repaint();
	            }
	        });
			*/
			setVisible(true);
		}
		catch (Exception e) {
			new UnexpectedErrorDialog(this);
		}
	}
	
	private void testiranje() {
		try {/*
			allAirports.add(Airport.createAirport("airport1", "ABC".toCharArray(), -90, 90));
			allAirports.add(Airport.createAirport("airport2", "MNO".toCharArray(), 90, -90));
			allAirports.add(Airport.createAirport("airport3", "ABA".toCharArray(), 0, 0));
			allAirports.add(Airport.createAirport("airport4", "ABB".toCharArray(), -90, 55));
			allAirports.add(Airport.createAirport("airport5", "ABM".toCharArray(), 0, 60));
			allAirports.add(Airport.createAirport("airport6", "ABN".toCharArray(), 80, -40));
			allAirports.add(Airport.createAirport("airport7", "ABP".toCharArray(), 90, 80));
			
			allFlights.add(Flight.createFlight(allAirports.get(0), allAirports.get(1), 0, 20, 40));
			allFlights.add(Flight.createFlight(allAirports.get(0), allAirports.get(1), 0, 20, 40));
			allFlights.add(Flight.createFlight(allAirports.get(2), allAirports.get(4), 1, 00, 120));
			allFlights.add(Flight.createFlight(allAirports.get(1), allAirports.get(6), 1, 30, 70));
			allFlights.add(Flight.createFlight(allAirports.get(0), allAirports.get(5), 2, 00, 65));
			allFlights.add(Flight.createFlight(allAirports.get(3), allAirports.get(2), 2, 20, 90));
			*/
			allAirports.add(Airport.createAirport("airport1", "ABC".toCharArray(), 33, 10));
			allAirports.add(Airport.createAirport("airport2", "MNO".toCharArray(), 90, 90));
			allAirports.add(Airport.createAirport("airport2", "BBB".toCharArray(), -30, 0));
			allFlights.add(Flight.createFlight(allAirports.get(0), allAirports.get(1), 0, 20, 120));
			allFlights.add(Flight.createFlight(allAirports.get(1), allAirports.get(2), 2, 30, 90));
		} 
		catch (CoordinateOutOfRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IdCodeInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AirportAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AirportDoesntExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadTimeFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void populateWindow() {
		
		Panel content = new Panel(new BorderLayout(5, 5));
		content.setBackground(AppWindow.defaultWindowBackColor);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				for (Thread t : map.getAllThreads()) {
					if (t != null && t.isAlive()) {
						t.interrupt();
					}
				}
				Thread t;
				if (map != null && (t = map.getFlightTimer()) != null && t.isAlive())
					map.getFlightTimer().interrupt();
				dispose();
			}
		});
		
		Panel mapPanel = new Panel();
		Panel checkedAirports = checkAirPanel();
		Panel airportsInfo = createASInfoPanel();
				
		content.add(checkedAirports, BorderLayout.EAST);
		content.add(airportsInfo, BorderLayout.WEST);
		
		int mapWidth = this.getWidth() - checkedAirports.getWidth() - airportsInfo.getWidth();
		int mapHeight = this.getHeight();

		map = new MapCanvas(mapWidth, mapHeight, this);
		map.setPreferredSize(new Dimension(map.getWidth(), map.getHeight()));
		mapPanel.add(map);
		
		content.add(mapPanel, BorderLayout.CENTER);
		
		this.add(content);
	}
	
	private Panel createASInfoPanel() {
		Panel content = new Panel(new BorderLayout());
		
		Label simLab = new Label("Simulation time", Label.CENTER);
		simLab.setPreferredSize(new Dimension(0, 30));
		Panel simTimePanel = new Panel(new BorderLayout());
		simulationTime.setAlignment(Label.CENTER);
		simTimePanel.add(simLab, BorderLayout.NORTH);
		simTimePanel.add(simulationTime, BorderLayout.CENTER);
		
		content.add(createAirportsInfoPanel(), BorderLayout.CENTER);
		content.add(simTimePanel, BorderLayout.SOUTH);
		
		return content;
	}
	
	private Panel createAirportsInfoPanel() {
		Panel content = new Panel(new GridLayout(0, 1));
		info.setPreferredSize(new Dimension(150, 100));
		info.setEditable(false);
		
		List lst = new List(5, false); // selektovan samo jedan
		
		for (Airport a : allAirports) {
			lst.add(a.getName());
		}
		
		lst.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				selectInfoList(e);
			}
		});
		
		Label lab = new Label("Airport informations", Label.CENTER);
		lab.setPreferredSize(new Dimension(0, 30));
		Panel infoText = new Panel(new BorderLayout());
		infoText.add(lab, BorderLayout.NORTH);
		infoText.add(info, BorderLayout.CENTER);
		
		content.add(lst);
		content.add(infoText);
		
		return content;
	}
	
	private Panel checkAirPanel() {
		Panel checkPanel = new Panel(new GridLayout(0, 1));
		
		for (Airport a : allAirports) {
			Checkbox cb = new Checkbox(a.getName());
			checkboxes.add(cb);
			checkPanel.add(cb);
			cb.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == 1 && !shownAirports.contains(a)) {
						shownAirports.add(a);
					}
					else {
						shownAirports.remove(a);
						newClearedAirports.add(a);
					}
					showOnMap();
				}

			});
		}
		
		ScrollPane scroll = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
		scroll.setPreferredSize(new Dimension(150, 300));
		scroll.add(checkPanel);
		
		Panel content = new Panel(new BorderLayout(5, 5));
		content.add(new Panel(), BorderLayout.EAST);
		content.getComponent(0).setPreferredSize(new Dimension(10, 0));
		content.add(new Panel(), BorderLayout.WEST);
		content.getComponent(1).setPreferredSize(new Dimension(10, 0));

		content.add(scroll, BorderLayout.NORTH);
		
		Panel buttons = new Panel(new GridLayout(0, 1));

		clickDeSelect();
		clickStartSimulation();
		clickResetSimulation();
		
		buttons.add(new Panel());
		buttons.add(selectAll);
		buttons.add(new Panel());
		buttons.add(deselectAll);
		buttons.add(new Panel());
		buttons.add(startSimulation);
		buttons.add(new Panel());
		buttons.add(resetSimulation);
		buttons.add(new Panel());
		buttons.getComponent(1).setPreferredSize(new Dimension(0, 15));
		buttons.getComponent(3).setPreferredSize(new Dimension(0, 15));
		buttons.getComponent(5).setPreferredSize(new Dimension(0, 15));
		buttons.getComponent(7).setPreferredSize(new Dimension(0, 15));
		
		content.add(buttons, BorderLayout.CENTER);
		
		return content;	
	}

	private void clickResetSimulation() {
		resetSimulation.addActionListener((ae)->{
			if (map != null) {
				map.resetFly();
				map.getFlightTimer().reset();
				startSimulation.setLabel("Start simulation");
			}
		});
	}

	private void clickStartSimulation() {
		startSimulation.addActionListener((ae)->{
			FlightsTimer ft;
			if (map == null || (ft = map.getFlightTimer()) == null)
				return;
			if (!ft.works()) {
				if (!ft.isAlive())
					ft.start();
				ft.go();
				startSimulation.setLabel("Pause simulation");
				map.continueFly();
			}
			else {
				ft.pause();
				startSimulation.setLabel("Start simulation");
				map.stopFly();
			}
		});
	}

	private void clickDeSelect() {
		selectAll.addActionListener((ae)->{
			for (int i = 0; i < checkboxes.size(); i++) {
				checkboxes.get(i).setState(true);
				Airport a = allAirports.get(i);
				if (!shownAirports.contains(a))
					shownAirports.add(a);
			}
			showOnMap();
		});
		
		deselectAll.addActionListener((ae)->{
			for (int i = 0; i < checkboxes.size(); i++) {
				checkboxes.get(i).setState(false);
			}
			newClearedAirports.addAll(shownAirports);
			shownAirports.clear();
			showOnMap();
		});
	}
	
	private void selectInfoList(ItemEvent e) {
		int index = (Integer)e.getItem();
		Airport airport = allAirports.get(index);
		info.setText("");
		info.append("Name: " + airport.getName() + "\n");
		info.append("ID Code: " + String.copyValueOf(airport.getIdCode()) + "\n");
		info.append("X coordinate: " + airport.getX() + "\n");
		info.append("Y coordinate: " + airport.getY());
	}
	
	private void showOnMap() {
		for (Airport air : newClearedAirports)
			System.out.println(air);
		if (map != null)
			map.repaint();
	}
	
	public Label getSimulationTime() {
		return simulationTime;
	}
	
	public ArrayList<Airport> getAllAirports() {
		return allAirports;
	}
	
	public ArrayList<Flight> getAllFlights() {
		return allFlights;
	}
	
	public static void main(String[] args) {
		new AirportsMap();
	}

}
