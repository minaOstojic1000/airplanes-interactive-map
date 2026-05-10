package airportClasses;

import shapes.Square;
import java.util.Arrays;

import myExceptions.AirportAlreadyExistsException;
import myExceptions.CoordinateOutOfRangeException;
import myExceptions.IdCodeInvalidException;

public class Airport {

	private static AirportsBase airports = AirportsBase.Instance();
	public static final int numOfParams = 4;
	
	private static final int codeLength = 3; 
	private String name;
	private char[] idCode = new char[codeLength];
	private double x, y;
	private double low = -90, high = 90;
	
	private Square mySquare;
	
	public static Airport createAirport(String name, char[] idCode, double x, double y) 
			throws CoordinateOutOfRangeException, IdCodeInvalidException, AirportAlreadyExistsException {
		Airport newInstance = new Airport(name, idCode, x, y);
		airports.add(newInstance);
		newInstance.mySquare = new Square((int)x, (int)y, String.copyValueOf(idCode));
		return newInstance;
	}
	
	protected Airport(String name, char[] idCode, double x, double y) 
			throws CoordinateOutOfRangeException, IdCodeInvalidException {
		super();
		this.name = name;
		validIdCode(idCode);
		this.idCode = idCode;
		validCoordinate(x, 'x');
		validCoordinate(y, 'y');
		this.x = x;
		this.y = y;
	}
	
	private void validCoordinate(double coor, char c) throws CoordinateOutOfRangeException {
		if (coor < low || coor > high)
			throw new CoordinateOutOfRangeException(c);
	}
	
	private void validIdCode(char[] idCode) throws IdCodeInvalidException {
		if (idCode.length != 3)
			throw new IdCodeInvalidException();
		
		for (char c: idCode) {
			if (!Character.isLetter(c) || !Character.isUpperCase(c)) {
				throw new IdCodeInvalidException();
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Airport other = (Airport) obj;
		return Arrays.equals(this.idCode, other.idCode);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char[] getIdCode() {
		return idCode;
	}

	public void setIdCode(char[] idCode) {
		this.idCode = idCode;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}
	
	public Square getMySquare() {
		return mySquare;
	}

	@Override
	public String toString() {
		return "Airport [" + (name != null ? "name=" + name + ", " : "")
				+ (idCode != null ? "idCode=" + String.copyValueOf(idCode) + ", " : "") + "x=" + x + ", y=" + y + "]";
	}

	public static void main(String[] args) {
		
	}

}
