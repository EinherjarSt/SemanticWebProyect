
package my.company.websemanticaoscar;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import com.opencsv.*;

public class CustomCSVReader {
	private String filePath;
	
	public CustomCSVReader(String filePath) {
		this.filePath = filePath;
	}
	
	
	public List<String[]> readCSV() throws Exception {
	    Reader reader = Files.newBufferedReader(Paths.get(
	      ClassLoader.getSystemResource(this.filePath).toURI()));
	    return this.readAllLines(reader);
	}
	
	public List<String[]> readAllLines(Reader reader) throws Exception {
	    CSVReader csvReader = new CSVReader(reader);
	    List<String[]> list = new ArrayList<>();
	    list = csvReader.readAll();
	    reader.close();
	    csvReader.close();
	    return list;
	}
	
	
}