package readWrite;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader extends FileReader {
	
	String fileName;
	ArrayList<String[]> lines = new ArrayList<>();
	
	protected CSVReader(String fileName) throws FileNotFoundException {
		super(fileName);
		this.fileName = fileName;
	}
	
	public ArrayList<String[]> readFile() throws IOException{
        String line;
        String delimiter = ","; // Or your specific delimiter
        BufferedReader br = null;
        try {
        	br = new BufferedReader(this); // optimization for reader
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);
                lines.add(values);
            }
        } 
        finally {
    	 if (br != null) {
    		 br.close();
    	 }
        }
        return lines;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public static void main(String[] args) {
       
    }
}
