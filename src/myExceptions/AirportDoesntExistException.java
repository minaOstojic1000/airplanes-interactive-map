package myExceptions;

public class AirportDoesntExistException extends Exception {

	char[] codeId;
	public AirportDoesntExistException(char[] codeId) {
		this.codeId = codeId;
	}
	
	@Override
	public String getMessage() {
		return "Airport with code: " + String.copyValueOf(codeId) + " doesn't exist in base.";
	}
}
