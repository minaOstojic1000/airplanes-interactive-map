package myExceptions;

public class CoordinateOutOfRangeException extends Exception {

	char coordinate;
	double low = -90, high = 90;
	
	public CoordinateOutOfRangeException(char coordinate) {
		super();
		this.coordinate = coordinate;
	}

	@Override
	public String getMessage() {
		return "Coordinate " + coordinate + " is out of range [" + low +", " + high + "].";
	}
}
