package shapes;

import java.awt.Color;
import java.awt.Graphics;

import gui.MapCanvas;

public class BlinkingSquare extends Shape implements Runnable {
	
	static final Color blinkingColor = Color.RED;
	static final Color defaultSquareColor = Color.LIGHT_GRAY;
	static final int defaultSquareWidth = 30;
	
	private boolean shown = false;
	private boolean blinking = false;
	private String sqText;
	private MapCanvas myCanvas;
	
	
	public BlinkingSquare(int x, int y, int width, Color boundColor, Color fillColor, String sqText, MapCanvas myCanvas) {
		super(x, y, width, boundColor, fillColor);
		this.sqText = sqText;
		this.myCanvas = myCanvas;
	}
	
	public BlinkingSquare(int x, int y, int width, String sqText, MapCanvas myCanvas) {
		this(x, y, width, defaultSquareColor, defaultSquareColor, sqText, myCanvas);
	}
	
	public BlinkingSquare(int x, int y, String sqText) {
		this(x, y, defaultSquareWidth, sqText, null);
	}
	
	@Override
	public synchronized void paint(Graphics g) {
		Color oldColor = g.getColor();
		g.setColor(fillColor);
		g.fillRect(x - width / 2, -y - width / 2, width, width);
		g.drawString(sqText, x + width / 2, -y - width / 2);
		g.setColor(oldColor);
	}
	
	public synchronized void reactToClick() {
		blinking = !blinking;
		if (blinking) {
			this.fillColor = blinkingColor;
			notify();
		}
		else {
			this.fillColor = defaultSquareColor;
		}
	}
	
	@Override
	public void run() {
			try {
				while(!Thread.interrupted()) {
					synchronized(this) {
						while(!blinking) {
							wait();
						}
					}
					fillColor = (fillColor == defaultSquareColor) ? blinkingColor : defaultSquareColor;
					Thread.sleep(250);
					myCanvas.repaint();
				}
			} catch (InterruptedException e) {}
	}

	public synchronized boolean getBlinking() {
		return blinking;
	}
	
	public synchronized void setShown(boolean shown) {
		this.shown = shown;
	}
	
	public synchronized boolean getShown() {
		return shown;
	}

	public synchronized String getSqText() {
		return sqText;
	}

	public synchronized void setSqText(String sqText) {
		this.sqText = sqText;
	}
	
	public void setMyCanvas(MapCanvas myCanvas) {
		this.myCanvas = myCanvas;
	}
	
	public MapCanvas getMyCanvas() {
		return myCanvas;
	}

	public boolean inBounds(int x, int y) {
		if (x > this.x - this.width / 2 && x < this.x + this.width / 2
			&& y > this.y - this.width / 2 && y < this.y + this.width / 2)
			return true;
		return false;
	}
}
