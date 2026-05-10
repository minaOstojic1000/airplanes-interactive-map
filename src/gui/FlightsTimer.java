package gui;

import java.awt.Label;

public class FlightsTimer extends Thread {
	
	@FunctionalInterface
	interface tickReaction {
		void reactOnTick();
	}
	
	static final int minutesIncrement = 10;
	
	private int hours;
	private int minutes;
	private Label time;
	private boolean work = false;
	private tickReaction TR;
	
	public FlightsTimer(Label time, tickReaction TR) {
		this.time = time;
		time.setText(this.toString());
		this.TR = TR;
	}
	
	@Override
	public void run() {
		try {
			while(!isInterrupted()) {
				
				synchronized (this) {
					while(!work) {
						wait();
					}
				}
		
				sleep(1000);
				if (work) {
					minutes += minutesIncrement;
					if (minutes == 60) {
						minutes = 0;
						hours += 1;
						if (hours == 24) {
							hours = 0;
						}
					}
					time.setText(this.toString());
					TR.reactOnTick();
				}
			}
		} catch (InterruptedException e) {}	
		
	}
	
	public synchronized void go() {
		work = true;
		notify();
	}
	
	public synchronized boolean works() {
		return work;
	}
	
	public synchronized void pause() {
		work = false;
	}

	public synchronized void reset() {
		hours = 0;
		minutes = 0;
		time.setText("00:00");
		work = false;
	}
	
	public synchronized int getHours() {
		return hours;
	}
	
	public synchronized int getMinutes() {
		return minutes;
	}
	
	public static int getMinutesincrement() {
		return minutesIncrement;
	}
	
	@Override
	public String toString() {
		return String.format("%02d:%02d", hours, minutes);
	}
}
