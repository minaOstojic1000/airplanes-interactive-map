package gui;

import java.awt.Frame;
import java.io.IOException;

import readWrite.AirportsWriter;
import readWrite.FlightsWriter;

public class SaveInFile extends ChooseFileBaseDialog{

	
	public SaveInFile(Frame owner) {
		super(owner, "Save in file", "SAVE AIRPORTS", "SAVE FLIGHTS");
	}

	@Override
	protected String processAirportData(String fileName) throws IOException {
		AirportsWriter aw = new AirportsWriter(fileName, true);
		aw.writeAirports();
		aw.close();
		return "Airports are successfully saved.";
	}

	@Override
	protected String processFlightData(String fileName) throws IOException {
		FlightsWriter fw = new FlightsWriter(fileName, true);
		fw.writeFlights();
		fw.close();
		return "Flights are successfully saved.";
	}

}
