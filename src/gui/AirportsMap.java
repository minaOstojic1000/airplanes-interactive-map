package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import airportClasses.Airport;
import airportClasses.AirportsBase;
import myExceptions.AirportAlreadyExistsException;
import myExceptions.CoordinateOutOfRangeException;
import myExceptions.IdCodeInvalidException;
import shapes.Square;

import java.awt.List;

public class AirportsMap extends Frame {

	private ArrayList<Airport> allAirports = new ArrayList<Airport>();
	private ArrayList<Airport> shownAirports = new ArrayList<Airport>();
	private ArrayList<Airport> newClearedAirports = new ArrayList<Airport>();
	private ArrayList<Checkbox> checkboxes = new ArrayList<>();
	private TextArea info = new TextArea();
	private Button selectAll = new Button("Select all");
	private Button deselectAll = new Button("Deselect all");
	private MapCanvas map;
	
	public AirportsMap() {
		
		setBounds(500, 200, 700, 500);
		setResizable(true);
		setTitle("Map of airports");
				
		//allAirports = AirportsBase.Instance().getAirList();
		testiranje();
		
		populateWindow();
		map.setShownAirports(shownAirports);
		map.setNewHiddenAirports(newClearedAirports);
				
		setVisible(true);
	}
	
	private void testiranje() {
		try {
			allAirports.add(Airport.createAirport("airport1", "ABC".toCharArray(), 22, 33));
			allAirports.add(Airport.createAirport("airport2", "MNO".toCharArray(), 44, -33));
			allAirports.add(Airport.createAirport("airport3", "ABA".toCharArray(), 0, 0));
			allAirports.add(Airport.createAirport("airport4", "ABB".toCharArray(), -90, 55));
			allAirports.add(Airport.createAirport("airport5", "ABM".toCharArray(), 0, 60));
			allAirports.add(Airport.createAirport("airport6", "ABN".toCharArray(), 80, -40));
			allAirports.add(Airport.createAirport("airport7", "ABP".toCharArray(), 90, 80));
		} catch (CoordinateOutOfRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IdCodeInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AirportAlreadyExistsException e) {
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
				for (Thread t : map.getThreads()) {
					if (t != null && t.isAlive()) {
						t.interrupt();
					}
				}
				dispose();
			}
		});
		
		Panel mapPanel = new Panel();
		Panel checkedAirports = checkAirPanel();
		Panel airportsInfo = createAirportsInfoPanel();
				
		content.add(checkedAirports, BorderLayout.EAST);
		content.add(airportsInfo, BorderLayout.WEST);
		
		int mapWidth = this.getWidth() - checkedAirports.getWidth() - airportsInfo.getWidth();
		int mapHeight = this.getHeight();

		map = new MapCanvas(mapWidth, mapHeight);
		map.setPreferredSize(new Dimension(map.getWidth(), map.getHeight()));
		mapPanel.add(map);
		
		content.add(mapPanel, BorderLayout.CENTER);
		
		this.add(content);
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
		scroll.setPreferredSize(new Dimension(120, 300));
		scroll.add(checkPanel);
		
		Panel content = new Panel(new BorderLayout(5, 5));
		content.add(new Panel(), BorderLayout.EAST);
		content.getComponent(0).setPreferredSize(new Dimension(10, 0));
		content.add(new Panel(), BorderLayout.WEST);
		content.getComponent(1).setPreferredSize(new Dimension(10, 0));

		content.add(scroll, BorderLayout.NORTH);
		
		Panel buttons = new Panel(new GridLayout(0, 1, 0, 10));

		clickDeSelect();
		
		buttons.add(new Panel());
		buttons.add(selectAll);
		buttons.add(new Panel());
		buttons.add(deselectAll);
		buttons.add(new Panel());
		buttons.getComponent(1).setPreferredSize(new Dimension(0, 10));
		buttons.getComponent(3).setPreferredSize(new Dimension(0, 10));
		
		content.add(buttons, BorderLayout.CENTER);
		
		return content;
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
	
	public static void main(String[] args) {
		new AirportsMap();
	}

}
