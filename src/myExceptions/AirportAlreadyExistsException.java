package myExceptions;

public class AirportAlreadyExistsException extends Exception {
	
	char[] codeId;
	public AirportAlreadyExistsException(char[] codeId) {
		this.codeId = codeId;
	}
	@Override
	public String getMessage() {
		return "Airport with code: " + String.copyValueOf(codeId) + " already exists in base.";
	}
}
