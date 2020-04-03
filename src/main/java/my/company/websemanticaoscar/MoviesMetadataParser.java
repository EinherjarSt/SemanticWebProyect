package my.company.websemanticaoscar;

import java.util.HashMap;

public class MoviesMetadataParser {
	
	private HashMap<String, Integer> dataIndices = new HashMap<String, Integer>();
	private String[] attributesNames;
	
	public MoviesMetadataParser(String[] attributesNames) {
		this.attributesNames = attributesNames;
		initializeDataIndices();
	}
	
    public void initializeDataIndices() {
		for(int i = 0; i < this.attributesNames.length; i++) {
			dataIndices.put(this.attributesNames[i], i);
		}
	}
    
    public String getValue(String[] line, String attributeName) {
    	return line[this.dataIndices.get(attributeName)];
    }
}
