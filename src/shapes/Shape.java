package shapes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

public abstract class Shape {
	
	protected int x, y; // koordinate centra oblika
	protected int width; // jedna dimenzija oblika
	protected Color boundColor;
	protected Color fillColor;
	
	public Shape(int x, int y, int width, Color boundColor, Color fillColor) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.boundColor = boundColor;
		this.fillColor = fillColor;
	}
	
	
	public synchronized int getWidth() {
		return width;
	}

	public synchronized void setWidth(int width) {
		this.width = width;
	}

	public synchronized int getX() {
		return x;
	}



	public synchronized int getY() {
		return y;
	}



	public synchronized void setX(int x) {
		this.x = x;
	}

	public synchronized void setY(int y) {
		this.y = y;
	}
	
	public synchronized Color getBoundColor() {
		return boundColor;
	}

	public synchronized void setBoundColor(Color boundColor) {
		this.boundColor = boundColor;
	}

	public synchronized Color getFillColor() {
		return fillColor;
	}

	public synchronized void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public abstract void paint(Graphics g);
}
