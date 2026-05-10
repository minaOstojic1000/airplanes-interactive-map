package gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import airportClasses.Airport;
import shapes.Square;

public class MapCanvas extends Canvas {
	
	public static final Color mapBackground = Color.YELLOW;
	private ArrayList<Airport> shownAirports;
	private ArrayList<Airport> newHiddenAirports;
	private int squareWidth = 30; // promeni tako da bude relativno
	private int width, height;
	ArrayList<Thread> threads = new ArrayList<>();
	
	public MapCanvas(int width, int height) {
		this.width = width;
		this.height = height;
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
		g.translate(width / 2, height / 2);
		clearAirports(g);
		drawAirports(g);
	}

	@Override
	public void update(Graphics g) {
		//g.clearRect(0, 0, getWidth(), getHeight());
	    paint(g);
	}
	
	private void clearAirports(Graphics g) {
		if (newHiddenAirports == null) { return; }
		for (Airport air : newHiddenAirports) {
			System.out.println(air);
			Square sq = air.getMySquare();
			if (sq.getMyCanvas() != null) {
				int sqX = sq.getX(), sqY = sq.getY(), sqW = sq.getWidth();
				g.clearRect(sqX - sqW / 2, sqY - sqW / 2, sqW, sqW);
		        FontMetrics fm = g.getFontMetrics();
		        int textWidth = fm.stringWidth(String.copyValueOf(air.getIdCode()));
		        int textHeight = fm.getHeight();
		        int rectX =  sqX + sqW / 2;
		        int rectY = sqY - sqW / 2 - fm.getAscent();
		        g.clearRect(rectX, rectY, textWidth, textHeight); // brisanje teksta
			}
		}
		newHiddenAirports.clear();
	}
	
	private void drawAirports(Graphics g) {
		if (shownAirports == null) return;
		for (Airport air : shownAirports) {
			Square sq = air.getMySquare();
			if (sq.getMyCanvas() == null) {
				sq.setMyCanvas(this);
				sq.setWidth(squareWidth);
				Thread t = new Thread(sq);
				threads.add(t);
				t.start();
			}
			sq.paint(g);
		}
	}

	void refreshAirportSquare(MouseEvent e) {
		for (Airport air : shownAirports) {
			Square sq = air.getMySquare();
			if (sq.inBounds(e.getX() - this.width / 2, e.getY() - this.height / 2)) {
				sq.reactToClick();
			}
		}
	}
	
	public ArrayList<Thread> getThreads() {
		return threads;
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
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
}
