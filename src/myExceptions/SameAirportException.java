package myExceptions;

public class SameAirportException extends Exception {
	@Override
	public String getMessage() {
		return "Departure and arrival airport are same.";
	}
}
