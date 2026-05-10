package airportClasses;

import java.util.ArrayList;

public class FlightsBase {

	private ArrayList<Flight> flightList = new ArrayList<>();

	private static FlightsBase instance;
	
	public static FlightsBase Instance() {
		if (instance == null)
			instance = new FlightsBase();
		return instance;
	}
	
	private FlightsBase() {}
	
	public void add(Flight element){
		if (!exists(element))
			flightList.add(element);
	}
	
	public boolean exists(Flight element) {
		for (Flight a: flightList) {
			if (a.equals(element))
				return true;
		}
		return false;
	}

	public void remove(int index) throws IndexOutOfBoundsException {
		flightList.remove(index);
	}
	
	public void clear() {
		flightList.clear();
	}

	public ArrayList<Flight> getFlightList() {
		return flightList;
	}

	public void setAirList(ArrayList<Flight> flightList) {
		flightList.clear();
		flightList.addAll(flightList);
	}

	@Override
	public String toString() {
		return "FlightsBase [" + (flightList != null ? "flightList=" + flightList : "") + "]";
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
