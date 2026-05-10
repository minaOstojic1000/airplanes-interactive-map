package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import airportClasses.Airport;
import gui.FlightsTimer;
import gui.MapCanvas;

public class MovingCircle extends Shape implements Runnable {

	static final Color defaultCircleColor = Color.BLUE;
	static final int defaultCircleWidth = 10;
	static final int stepMS = 200;
	
	private boolean flies = false, flightOver = false, flightStarted = false;
	private int startX, startY;
	private int endX, endY;
	private int oldX = Integer.MIN_VALUE, oldY = Integer.MIN_VALUE;
	private int durationInMIN = 50;
	private Color clearColor = MapCanvas.mapBackground;
	private double stepX, stepY;
	private MapCanvas myCanvas;
	
	public MovingCircle(int startX, int startY, int endX, int endY, int duration, int width, 
			Color boundColor, Color fillColor, MapCanvas myCanvas) {
		super(startX, startY, width, boundColor, fillColor);
		
		this.startX = startX; this.startY = startY;
		this.endX = endX;this.endY = endY;
		this.durationInMIN = duration;
		
		setMyCanvas(myCanvas);
		//System.out.println("x: " + x + "y: " + y + "stepX: " + stepX + "stepY: " + stepY);
	}

	public MovingCircle(int startX, int startY, int endX, int endY, int duration, int width, MapCanvas myCanvas) {
		this(startX, startY, endX, endY, duration, width, defaultCircleColor, defaultCircleColor, myCanvas);
	}
	
	public MovingCircle(int startX, int startY, int endX, int endY, int duration) {
		this(startX, startY, endX, endY, duration, defaultCircleWidth, defaultCircleColor, defaultCircleColor, null);
	}
	
	@Override
	public synchronized void paint(Graphics g) {
		Color oldColor = g.getColor();
		
		if (oldX != Integer.MIN_VALUE && oldY != Integer.MIN_VALUE) {
			clearColor = MapCanvas.mapBackground;
			g.setColor(clearColor);
			g.fillRect(oldX - width / 2, -oldY - width / 2, width, width);
			fliesOverSomeAirport(oldX, oldY, g);
		}
		
		g.setColor(fillColor);
		g.fillOval(x - width / 2, -y - width / 2, width, width);
		g.setColor(oldColor);
	}

	@Override
	public void run() {
		try {
			flightStarted = true;
			while(!Thread.interrupted()) {
				synchronized(this) {
					while(!flies) {
						wait();
					}
				}
				Thread.sleep(stepMS);
				oldX = x; oldY = y;
				x += stepX;
				y += stepY;
				myCanvas.repaint();
				
				int sqW = myCanvas.getSquareWidth();
				if (inAirportBounds(x, y, endX, endY, sqW * 1. / (sqW / 3.0 + this.width / 2.0))) {
					flies = false;
					flightOver = true;
				}
			}
			
		}
		catch (InterruptedException e) {}
	}
	
	public synchronized boolean inAirportBounds(int x, int y, double xA, double yA, double surfaceProp) {
		int partSq = (int)(myCanvas.getSquareWidth() * 1. / surfaceProp);
		if (x >= xA - partSq && x <= xA + partSq && y >= yA - partSq && y <= yA + partSq)
			return true;
		return false;
	}
	
	private synchronized void fliesOverSomeAirport(int x, int y, Graphics g) {
		ArrayList<Airport> airports = myCanvas.getShownAirports();
		int sqW = myCanvas.getSquareWidth();
		double surfaceProp = sqW * 1. / (sqW / 2.0 + this.width / 2.0);
		for (Airport air : airports) {
			if (inAirportBounds(x, y, air.getX(), air.getY(), surfaceProp)) {
				clearColor = BlinkingSquare.defaultSquareColor;
				BlinkingSquare sq = air.getMySquare();
				if (sq.getBlinking()) {
					clearColor = BlinkingSquare.blinkingColor;
				}
				else {
					g.setColor(clearColor);
					g.fillRect(sq.getX() - sqW / 2, -sq.getY() - sqW / 2, sqW, sqW);
					g.drawString(sq.getSqText(), sq.getX() + sqW / 2, -sq.getY() - sqW / 2);
				}
			}
		}
	}
	
	private void calculateXYSteps(int durationInMS) {
		stepX = (endX - startX) * 1. / (durationInMS * 1. / stepMS);
		if (stepX > 0 && stepX < 1) stepX = 1;
		if (stepX == 0) {
			stepY = (endY - startY) * 1. / (durationInMS * 1. / stepMS);
		}
		else {
			stepY = ((endY - startY) * 1. / (endX - startX)) * stepX;
		}
		if (stepY > 0 && stepY < 1)
			stepY = 1;
	}
	
	public synchronized void move() {
		if (flightOver)
			return;
		flies = true;
		notify();
	}
	
	public synchronized void pause() {
		flies = false;
	}
	
	public synchronized void reset() {
		x = startX; y = startY;
		oldX = oldY = Integer.MIN_VALUE;
		flightOver = false;
		flies = false;
		flightStarted = false;
		clearColor = MapCanvas.mapBackground;
	}
	
	public synchronized boolean getFlies() {
		return flies;
	}
	
	public synchronized boolean getFlightStarted() {
		return flightStarted;
	}
	
	public synchronized MapCanvas getMyCanvas() {
		return myCanvas;
	}
	
	public synchronized void setMyCanvas(MapCanvas myCanvas) {
		this.myCanvas = myCanvas;
		int durationInMS;
		if (myCanvas != null) {
			myCanvas.getFlightTimer();
			durationInMS = (int)(durationInMIN * 1. / FlightsTimer.getMinutesincrement() * 1000);
		}
		else
			durationInMS = (int)(durationInMIN * 1. / 10 * 1000);
		calculateXYSteps(durationInMS);
	}
	
	public synchronized Color getClearColor() {
		return clearColor;
	}
	
	public synchronized void setClearColor(Color clearColor) {
		this.clearColor = clearColor;
	}

}
