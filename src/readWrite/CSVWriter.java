package readWrite;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class CSVWriter extends FileWriter {

    private final String fileName;

    protected CSVWriter(String fileName, boolean append) throws IOException {
        super(fileName, append);
        this.fileName = fileName;
    }

    public void writeFile(ArrayList<String[]> lines) throws IOException {
        StringBuilder sb = new StringBuilder();
     
        for (int i = 0; i < lines.size(); i++) {
            String[] line = lines.get(i);
            for (int j = 0; j < line.length; j++) {
                sb.append(line[j]);
                if (j < line.length - 1)
                    sb.append(',');
            }
            sb.append(System.lineSeparator());
        }
        if (isEmpty()) {
        	RandomAccessFile fw = new RandomAccessFile(fileName, "rw" );
        	fw.seek(0);
        	fw.writeChars(sb.toString());
        	fw.close();
        }
        else
        	this.write(sb.toString());
    }
    
    private boolean isEmpty() {
    	RandomAccessFile fr;
    	try {
			fr = new RandomAccessFile(fileName, "r" );
			String buff = fr.readLine();
			fr.close();
			if (buff == null || buff.length() == 0 || buff.equals("\n")) {
				return true;
			}
		} 
    	catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} 
    	catch (IOException e) {
    		e.printStackTrace();
		}
    	return false;
    }

    public String getFileName() {
        return fileName;
    }

    public static void main(String[] args) {
        try {
            CSVWriter cv = new CSVWriter("data6.csv", true);
            ArrayList<String[]> list = new ArrayList<>();
            String[] reci1 = {"sunce1", "mesec1", "zvezda1", "oblak1"};
            String[] reci2 = {"sunce2", "mesec2", "zvezda2", "oblak2"};
            list.add(reci1);
            list.add(reci2);
            cv.writeFile(list);
            cv.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
