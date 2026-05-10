package airportClasses;

import myExceptions.AirportDoesntExistException;
import myExceptions.BadTimeFormatException;
import shapes.BlinkingSquare;
import shapes.MovingCircle;

public class Flight implements Comparable<Flight>{
	
	private static int idAll = 1;
	
	private static FlightsBase flights = FlightsBase.Instance();
	public static final int numOfParams = 6;

	private Airport startAirport, endAirport;
	private int startHour, startMinute;
	private int duration;
	private int flightId = idAll++;
	
	private MovingCircle myCircle;
	
	public static Flight createFlight(char[] idCodeStart, char[] idCodeEnd, int startHour, int startMinute, int duration) 
			throws AirportDoesntExistException, BadTimeFormatException {
		checkTime(startHour, startMinute, duration);
		return createFlight(checkAirport(idCodeStart), checkAirport(idCodeEnd), startHour, startMinute, duration);
	}
	
	public static Flight createFlight(Airport startAirport, Airport endAirport, int startHour, int startMinute, int duration) 
			throws AirportDoesntExistException, BadTimeFormatException {
		Flight newInstance = new Flight(startAirport, endAirport, startHour, startMinute, duration);
		newInstance.myCircle = new MovingCircle((int)startAirport.getX(), (int)startAirport.getY(), 
				(int)endAirport.getX(), (int)endAirport.getY(), duration);
		flights.add(newInstance);
		startAirport.getFlights().add(newInstance);
		return newInstance;
	}
	
	protected Flight(Airport startAirport, Airport endAirport, int startHour, int startMinute, int duration)
	throws AirportDoesntExistException, BadTimeFormatException {
		
		checkTime(startHour, startMinute, duration);
		checkAirport(startAirport);
		checkAirport(endAirport);
		
		this.startAirport = startAirport;
		this.endAirport = endAirport;
		this.startHour = startHour;
		this.startMinute = startMinute;
		this.duration = duration;
	}

	private void checkAirport(Airport air) throws AirportDoesntExistException {
		AirportsBase ab = AirportsBase.Instance();
		if (!ab.exists(air)) throw new AirportDoesntExistException(air.getIdCode());
	}
	
	private static Airport checkAirport(char[] idCode) throws AirportDoesntExistException {
		AirportsBase ab = AirportsBase.Instance();
		Airport air;
		if ((air = ab.getAirport(idCode)) == null)
			throw new AirportDoesntExistException(idCode);
		return air;
	}
	
	private static void checkTime(int h, int m, int dur) throws BadTimeFormatException {
		if (h > 23 || h < 0 || m >= 60 || m < 0 || dur <= 0) 
			throw new BadTimeFormatException();
	}
	
	public Airport getStartAirport() {
		return startAirport;
	}

	public Airport getEndAirport() {
		return endAirport;
	}

	public int getStartHour() {
		return startHour;
	}

	public int getStartMinute() {
		return startMinute;
	}

	public int getDuration() {
		return duration;
	}

	public int getFlightId() {
		return flightId;
	}
	
	static public int getNextId() {
		return idAll;
	}
	
	public MovingCircle getMyCircle() {
		return myCircle;
	}


	@Override
	public int compareTo(Flight f) {
		if (f == null)
			return -1;
		if (f.startHour == this.startHour && f.startMinute == this.startMinute)
			return 0;
		if (this.startHour < f.startHour)
			return -1;
		if (this.startHour == f.startHour && this.startMinute < f.startMinute)
			return -1;
		return 1;
	}

	@Override
	public String toString() {
		return "Flight " + flightId + ": [" + (startAirport != null ? "startAirport=" + String.copyValueOf(startAirport.getIdCode()) + ", " : "")
				+ (endAirport != null ? "endAirport=" + String.copyValueOf(endAirport.getIdCode()) + ", " : "") + "hour=" + startHour
				+ ", minute=" + startMinute + ", duration=" + duration + "]";
	}

}
