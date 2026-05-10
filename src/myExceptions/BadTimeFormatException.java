package myExceptions;

public class BadTimeFormatException extends Exception {
	
	@Override
	public String getMessage() {
		return "Bad input for time or duration.";
	}
}
