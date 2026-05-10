package airportClasses;

import java.util.ArrayList;
import java.util.Arrays;

import myExceptions.AirportAlreadyExistsException;

public class AirportsBase{
	
	private ArrayList<Airport> airList = new ArrayList<>();
	
	private static AirportsBase instance;
	
	public static AirportsBase Instance() {
		if (instance == null)
			instance = new AirportsBase();
		return instance;
	}
	
	private AirportsBase() {}
	
	public void add(Airport element) throws AirportAlreadyExistsException {
		if (!exists(element))
			airList.add(element);
		else
			throw new AirportAlreadyExistsException(element.getIdCode());
	}
	
	public boolean exists(Airport element) {
		for (Airport a: airList) {
			if (a.equals(element))
				return true;
		}
		return false;
	}

	public void remove(int index) throws IndexOutOfBoundsException {
		airList.remove(index);
	}
	
	public void clear() {
		airList.clear();
	}

	public ArrayList<Airport> getAirList() {
		return airList;
	}
	
	public Airport getAirport(char[] idCode) {
		for (Airport a : airList) {
			if (Arrays.equals(a.getIdCode(), idCode)) 
				return a;
		}
		return null;
	}

	public void setAirList(ArrayList<Airport> airList) {
		airList.clear();
		airList.addAll(airList);
	}

	@Override
	public String toString() {
		return "AirportsBase [" + (airList != null ? "airList=" + airList : "") + "]";
	}

	public static void main(String[] args) {}

}
