package shapes;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Square extends Shape implements Runnable {
	
	static final Color blinkingColor = Color.RED;
	static final Color defaultColor = Color.LIGHT_GRAY;
	static final int defaultWidth = 30;
	
	private boolean shown = false;
	private boolean blinking = false;
	private String sqText;
	private Canvas myCanvas;
	
	public Square(int x, int y, String sqText) {
		super(x, y, defaultWidth, defaultColor, defaultColor);
		this.sqText = sqText;
	}
	
	public Square(int x, int y, int width, String sqText, Canvas myCanvas) {
		super(x, y, width, defaultColor, defaultColor);
		this.sqText = sqText;
		this.myCanvas = myCanvas;
	}
	
	@Override
	public synchronized void paint(Graphics g) {
		Color oldColor = g.getColor();
		g.setColor(fillColor);
		g.fillRect(x - width / 2, y - width / 2, width, width);
		g.drawString(sqText, x + width / 2, y - width / 2);
		g.setColor(oldColor);
	}
	
	public synchronized void reactToClick() {
		blinking = !blinking;
		if (blinking) {
			this.fillColor = blinkingColor;
			notify();
		}
		else {
			this.fillColor = defaultColor;
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
					fillColor = (fillColor == defaultColor) ? blinkingColor : defaultColor;
					Thread.sleep(250);
					myCanvas.repaint();
				}
			} catch (InterruptedException e) {}
	}

	public synchronized boolean getBlinking() {
		return blinking;
	}
	
	public void setShown(boolean shown) {
		this.shown = shown;
	}
	
	public boolean getShown() {
		return shown;
	}

	public String getSqText() {
		return sqText;
	}

	public void setSqText(String sqText) {
		this.sqText = sqText;
	}
	
	public void setMyCanvas(Canvas myCanvas) {
		this.myCanvas = myCanvas;
	}
	
	public Canvas getMyCanvas() {
		return myCanvas;
	}

	public boolean inBounds(int x, int y) {
		if (x > this.x - this.width / 2 && x < this.x + this.width / 2
			&& y > this.y - this.width / 2 && y < this.y + this.width / 2)
			return true;
		return false;
	}
}
