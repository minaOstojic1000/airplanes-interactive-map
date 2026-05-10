package myExceptions;

public class IdCodeInvalidException extends Exception {

	@Override
	public String getMessage() {
		return "ID Code of airport is in invalid format. It must be 3 letters long.";
	}
}