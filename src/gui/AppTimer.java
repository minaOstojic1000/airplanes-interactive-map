package gui;

import java.awt.Dialog;
import java.awt.Label;

public class AppTimer extends Thread {
	
	public static final int timerDuration = 60;
	
	private int seconds = timerDuration;
	private AppWindow.ActivityTimeDialog notif;
	private Label time;
	boolean work = false;
	
	public AppTimer(AppWindow.ActivityTimeDialog notif) {
		notif.setVisible(false);
		this.notif = notif;
		this.time = notif.getTime();
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
				seconds--;
				if (seconds <= 5) {
					time.setText(this.toString());
					time.revalidate();
					notif.pack();
					notif.setVisible(true);
					if (seconds == 0) {
						notif.getOwner().dispose();
						notif.dispose();
						interrupt();
					}
				}
			}
		} catch (InterruptedException e) {
			
		}	
	}
	
	public synchronized void go() {
		work = true;
		notify();
	}
	
	public synchronized void pause() {
		work = false;
	}

	public synchronized void reset() {
		seconds = timerDuration;
		notif.setVisible(false);
	}
	
	@Override
	public String toString() {
		return String.format("%02d", seconds);
	}
}
