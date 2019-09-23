package my.company.websemanticaoscar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.RDFSyntax;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	//moviesMetadataToRDFModel();
    	if(args.length != 2) {
			printHelp();
			printArgs(args);
    	}
    	else if(args[0].equals("oscars")) {
    		parseOscars(args[1]);
    	}
    	else if(args[0].equals("movies")) {
    		moviesMetadataToRDFModel(args[1]);
    	}
    	else {
    		printHelp();
    	}
    	
    	
    }
    
    public static void printHelp() {
    	System.out.println("Usage: java <oscars|movies> dataToTurtle");
	}

	public static void printArgs(String[] args){
		System.out.println("Los argumentos detectados fueron:");
		int i = 0;
		if(args.length == 0){
			System.out.println("No se detecto ningun argumento");
			return;
		}
		for (String arg : args) {
			System.out.println("args["+ i++ +"]: " + arg);
		}
	}
    
    public static void moviesMetadataToRDFModel(String path) {
		System.out.println("El archivo a leer es: " + new File(path).getAbsolutePath());

    	CustomCSVReader csvReader = new CustomCSVReader(path);
    	MoviesMetadataRDFModel model = new MoviesMetadataRDFModel();
    	List<String[]> csvLines;
    	
    	try {
    		csvLines = csvReader.readCSV();
    		String[] csvHeader = csvLines.get(0);
    		MoviesMetadataParser parser = new MoviesMetadataParser(csvHeader);
    		
    		String [] line;
    		//Data
    		for(int i = 1 ; i < csvLines.size(); i++) {
    			
    			line = csvLines.get(i);
    			
    			if(line.length == 26) {
    				model.addCSVLineAsModelStatements(csvLines.get(i), parser);
    			}
    		}
    		
    		model.saveModelToFile(new File("movies.ttl"));
    		model.printModel();
    		
		} catch(Exception e) {
			e.printStackTrace();
		}
    	
    	
    }

   
    
    public static void parseOscars (String path) {
		System.out.println("El archivo a leer es: " + new File(path).getAbsolutePath());
    	RDFSParser rdf = new RDFSParser();
    	try(BufferedReader reader = new BufferedReader(new FileReader(path))){
    		String line;
    		reader.readLine();
    		while ((line = reader.readLine()) != null) {
    			String[] tokens = line.split(";");
    			rdf.parseTokens(tokens);
    			//System.out.println(rdf.convertNameToResourceName(tokens[3]));
    		}
    		rdf.printModel();
    		rdf.saveModelToFile(new File("oscar.ttl"));

    	}
    	catch (IOException e) {
    		e.printStackTrace();
		}
    }
    
    public static void printTokens(String[] tokens) {
    	for (String string : tokens) {
    		System.out.println(string);
			//System.out.print(string + " | ");
		}
    	System.out.println(tokens.length);
    	System.out.println();
    }
}
