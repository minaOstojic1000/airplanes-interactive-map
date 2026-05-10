package readWrite;

import java.io.IOException;
import java.util.ArrayList;

import airportClasses.Airport;
import airportClasses.AirportsBase;

public class AirportsWriter extends CSVWriter {
	
	public AirportsWriter(String fileName, boolean append) throws IOException {
		super(fileName, append);
	}
	
	public void writeAirports() throws IOException {
		ArrayList<String[]> lines = new ArrayList<>();
		for (Airport air : AirportsBase.Instance().getAirList()) {
			String[] line = new String[Airport.numOfParams];
			line[0] = air.getName();
			line[1] = String.copyValueOf(air.getIdCode());
			line[2] = ((Double)air.getX()).toString();
			line[3] = ((Double)air.getY()).toString();
			lines.add(line);
		}
		writeFile(lines);
	}

	public static void main(String[] args) {
		
	}

}
