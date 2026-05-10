package readWrite;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import airportClasses.Airport;
import airportClasses.AirportsBase;
import myExceptions.AirportAlreadyExistsException;
import myExceptions.CoordinateOutOfRangeException;
import myExceptions.IdCodeInvalidException;
import myExceptions.FileParseException;

public class AirportsReader extends CSVReader {
	
	public AirportsReader(String fileName) throws FileNotFoundException {
		super(fileName);
	}
	
	public void loadAirports() 
			throws IOException, AirportAlreadyExistsException, 
			CoordinateOutOfRangeException, IdCodeInvalidException, FileParseException {
		ArrayList<String[]> lines = readFile();
		try {
			for (String[] line : lines) {
				if (line == null || line.length < 4)
					throw new FileParseException();
				Airport.createAirport(line[0], line[1].toCharArray(), Double.parseDouble(line[2]), Double.parseDouble(line[3]));
			}
		}
		catch(NumberFormatException e) {
			throw new FileParseException();
		}
	}

	public static void main(String[] args) {
		

	}

}
