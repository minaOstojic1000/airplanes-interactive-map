package readWrite;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import airportClasses.Flight;
import airportClasses.FlightsBase;
import myExceptions.AirportDoesntExistException;
import myExceptions.BadTimeFormatException;
import myExceptions.FileParseException;

public class FlightsReader extends CSVReader {
	
	public FlightsReader(String fileName) throws FileNotFoundException {
		super(fileName);
	}
	
	public void loadFlights() 
			throws IOException, FileParseException, AirportDoesntExistException, BadTimeFormatException {
		ArrayList<String[]> lines = readFile();
		try {
			for (String[] line : lines) {
				if (line == null || line.length < 5)
					throw new FileParseException();
				Flight.createFlight(line[0].toCharArray(), line[1].toCharArray(), Integer.parseInt(line[2]), 
						Integer.parseInt(line[3]), Integer.parseInt(line[4]));
			}
		}
		catch(NumberFormatException e) {
			throw new FileParseException();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
