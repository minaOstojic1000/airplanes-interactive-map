package gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import myExceptions.AirportAlreadyExistsException;
import myExceptions.AirportDoesntExistException;
import myExceptions.BadTimeFormatException;
import myExceptions.CoordinateOutOfRangeException;
import myExceptions.FileParseException;
import myExceptions.IdCodeInvalidException;
import readWrite.AirportsReader;
import readWrite.FlightsReader;

public class LoadFromFile extends ChooseFileBaseDialog {

	protected LoadFromFile(Frame owner) {
		super(owner, "Load from file", "LOAD AIRPORTS", "LOAD FLIGHTS");
	}

	@Override
	protected String processAirportData(String fileName) 
			throws IOException, AirportAlreadyExistsException, CoordinateOutOfRangeException, 
			IdCodeInvalidException, FileParseException {
		AirportsReader ar = new AirportsReader(fileName);
		ar.loadAirports();
		ar.close();
		return "Airports are successfully loaded.";
	}

	@Override
	protected String processFlightData(String fileName) throws IOException, FileParseException, AirportDoesntExistException, BadTimeFormatException {
		FlightsReader fr = new FlightsReader(fileName);
		fr.loadFlights();
		fr.close();
		return "Flights are successfully loaded.";
	}

	
		
}
