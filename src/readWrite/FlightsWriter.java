package readWrite;

import java.io.IOException;
import java.util.ArrayList;

import airportClasses.Flight;
import airportClasses.FlightsBase;

public class FlightsWriter extends CSVWriter {

	public FlightsWriter(String fileName, boolean append) throws IOException {
		super(fileName, append);
	}

	public void writeFlights() throws IOException {
		ArrayList<String[]> lines = new ArrayList<>();
		for (Flight fl : FlightsBase.Instance().getFlightList()) {
			String[] line = new String[Flight.numOfParams];
			line[0] = ((Integer)fl.getFlightId()).toString();
			line[1] = String.copyValueOf(fl.getStartAirport().getIdCode());
			line[2] = String.copyValueOf(fl.getEndAirport().getIdCode());
			line[3] = ((Integer)fl.getStartHour()).toString();
			line[4] = ((Integer)fl.getStartMinute()).toString();
			line[5] = ((Integer)fl.getDuration()).toString();
			lines.add(line);
		}
		writeFile(lines);
	}
}
