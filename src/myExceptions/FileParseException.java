package myExceptions;

public class FileParseException extends Exception {
	@Override
	public String getMessage() {
		return "Input data in file are not in good format.";
	}
}
