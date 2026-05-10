package airportClasses;

import java.util.List;
import java.util.Vector;

public class FlightsBase {

	private List<Flight> flightList = new Vector<>();

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

	public List<Flight> getFlightList() {
		return flightList;
	}

	public void setAirList(List<Flight> flightList) {
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
