package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.PriorityQueue;

import airportClasses.Airport;
import airportClasses.Flight;
import airportClasses.FlightsBase;
import shapes.BlinkingSquare;
import shapes.MovingCircle;

public class MapCanvas extends Canvas {
	
	public static final Color mapBackground = new Color(19, 61, 87);
	private ArrayList<Airport> shownAirports;
	private ArrayList<Airport> newHiddenAirports;
	private ArrayList<Flight> flights = new ArrayList<>();
	
	private boolean reset = false;
	
	private int squareWidth;
	private int circleWidth = 20; 
	private int width, height;
	private double squareWFactor = 0.07;
	private double circleWFactor = 0.04;
	ArrayList<Thread> airThreads = new ArrayList<>();
	ArrayList<Thread> flightThreads = new ArrayList<>();
	
	private boolean somethingFlies = false;
	
	AppTimer mainTimer;
	FlightsTimer flightTimer;
	
	AirportsMap owner;
	
	public MapCanvas(int width, int height, AirportsMap owner) {
		this.width = width;
		this.height = height;
		this.owner = owner;
		scaleSquare();
		scaleCircle();
		
		mainTimer = AppWindow.getTimer();
		flightTimer = new FlightsTimer(owner.getSimulationTime(), () -> startFly());
		
		this.setBackground(mapBackground);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				refreshAirportSquare(e);
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		if (reset) {
			g.clearRect(0, 0, width, height);
			reset = false;
		}
		g.translate(width / 2, height / 2);
		
		clearAirports(g);
		
		drawAirports(g);
				
		fly(g);
	}

	@Override
	public void update(Graphics g) {
	    paint(g);
	}

	
	private void fly(Graphics g) {
		if (flights == null) { return; }
		somethingFlies = false;
		for (Flight fl : flights) {
			MovingCircle mc = fl.getMyCircle();
			if (mc.getFlies()) {
				somethingFlies = true;
			}
			mc.paint(g);
		}
		if (mainTimer != null && somethingFlies && mainTimer.works()) {
			mainTimer.pause();
		}
	}
	
	public void startFly() {
	
		for (Airport air : owner.getAllAirports()) {
		PriorityQueue<Flight> airQueue = air.getFlights();
			Flight fl = airQueue.peek();
			if (fl == null)
				continue;
			
			MovingCircle mc = fl.getMyCircle();
			int fhour = fl.getStartHour();
			int fmin = fl.getStartMinute();
			// proveri da li treba da poleces
			if (flightTimer.getHours() >= fhour && 
					flightTimer.getMinutes() >= fmin) {
				if (mc != null) {
					mc.setMyCanvas(this); // podesi dodatne parametre - canvas, sirina
					mc.setWidth(circleWidth);
					flights.add(airQueue.poll());
					Thread t = new Thread(mc); // pokreni nit
					flightThreads.add(t);
					t.start();
					//System.out.println("Started");
				}
				mc.move();
			}
		}
	}
	
	public void stopFly() {
		for (Flight fl : flights) {
			MovingCircle mc = fl.getMyCircle();
			if (mc != null)
				mc.pause();
		}
	}
	
	public void continueFly() {
		for (Flight fl : flights) {
			MovingCircle mc = fl.getMyCircle();
			if (mc != null)
				mc.move();
		}
	}
	
	public void resetFly() {
		ArrayList<Airport> airports = owner.getAllAirports();
		for (int i = 0; i < flights.size(); i++) {
			Flight fl = flights.get(i);
			for (Airport air : airports) {
				if (air.equals(fl.getStartAirport())) {
					air.getFlights().add(fl);
					break;
				}
			}
			fl.getMyCircle().reset();
		}
		for (Thread t : flightThreads) {
			t.interrupt();
		}
		flights.clear();
		reset = true;
		repaint();
	}
	
	private void clearAirports(Graphics g) {
		if (newHiddenAirports == null) { return; }
		for (Airport air : newHiddenAirports) {
			BlinkingSquare sq = air.getMySquare();
			if (sq.getMyCanvas() != null) {
				int sqX = sq.getX(), sqY = sq.getY(), sqW = sq.getWidth();
				g.clearRect(sqX - sqW / 2, -sqY - sqW / 2, sqW, sqW);
		        FontMetrics fm = g.getFontMetrics();
		        int textWidth = fm.stringWidth(String.copyValueOf(air.getIdCode()));
		        int textHeight = fm.getHeight();
		        g.clearRect(sqX + sqW / 2, -sqY - sqW / 2 - fm.getAscent(), textWidth, textHeight); // brisanje teksta
			}
		}
		newHiddenAirports.clear();
	}
	
	private void drawAirports(Graphics g) {
		if (shownAirports == null) return;
		boolean somethingBlinks = false;
		for (Airport air : shownAirports) {
			BlinkingSquare sq = air.getMySquare();
			//sq.setWidth(squareWidth);
			if (sq.getMyCanvas() == null) {
				sq.setMyCanvas(this);
				sq.setWidth(squareWidth);
				Thread t = new Thread(sq);
				airThreads.add(t);
				t.start();
			}
			if (sq.getBlinking())
				somethingBlinks = true;
			sq.paint(g);
		}
		if (mainTimer != null) {
			if (somethingBlinks || somethingFlies) {
				mainTimer.pause();
			}
			else if (!mainTimer.works()) {
				mainTimer.go();
			}
		}
	}

	void refreshAirportSquare(MouseEvent e) {
		for (Airport air : shownAirports) {
			BlinkingSquare sq = air.getMySquare();
			int eX = e.getX(), eY = e.getY();
			int clickX = (eX < width / 2) ? -(width / 2 - eX) : eX - width / 2; 
			int clickY = (eY > height / 2) ? -(eY - height / 2) : (height / 2 - eY);
			if (sq.inBounds(clickX, clickY)) {
				sq.reactToClick();
			}
		}
	}
	
	private void scaleSquare() {
		this.squareWidth = (width < height) ? (int)(width * squareWFactor) : (int)(height * squareWFactor);
		for (Airport air : owner.getAllAirports()) {
			BlinkingSquare sq = air.getMySquare();
			if (sq != null) {
				sq.setWidth(squareWidth);
			}
		}
		repaint();
	}
	
	private void scaleCircle() {
		this.circleWidth = (width < height) ? (int)(width * circleWFactor) : (int)(height * circleWFactor);
		for (Flight fl : owner.getAllFlights()) {
			MovingCircle mc = fl.getMyCircle();
			if (mc != null) {
				mc.setWidth(circleWidth);
			}
		}
		repaint();
	}
	
	public ArrayList<Thread> getAllThreads() {
		ArrayList<Thread> allThreads = new ArrayList<Thread>(airThreads);
		allThreads.addAll(flightThreads);
		return allThreads;
	}
	
	public ArrayList<Thread> getAirThreads() {
		return airThreads;
	}
	
	public ArrayList<Thread> getFlightThreads() {
		return flightThreads;
	}
	
	public ArrayList<Airport> getShownAirports() {
		return shownAirports;
	}

	public void setShownAirports(ArrayList<Airport> airports) {
		this.shownAirports = airports;
	}
	
	public ArrayList<Airport> getNewHiddenAirports() {
		return newHiddenAirports;
	}

	public void setNewHiddenAirports(ArrayList<Airport> airports) {
		this.newHiddenAirports = airports;
	}

	public int getSquareWidth() {
		return squareWidth;
	}

	public void setSquareWidth(int squareWidth) {
		this.squareWidth = squareWidth;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		scaleCircle();
		scaleSquare();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		scaleCircle();
		scaleSquare();
	}
	
	public FlightsTimer getFlightTimer() {
		return flightTimer;
	}

	public void setFlights(ArrayList<Flight> flights) {
		this.flights = flights;
	}
	
	public ArrayList<Flight> getFlights() {
		return flights;
	}
	
}
