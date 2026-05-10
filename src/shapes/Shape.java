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
	
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getX() {
		return x;
	}



	public int getY() {
		return y;
	}



	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public Color getBoundColor() {
		return boundColor;
	}

	public void setBoundColor(Color boundColor) {
		this.boundColor = boundColor;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public abstract void paint(Graphics g);
}
