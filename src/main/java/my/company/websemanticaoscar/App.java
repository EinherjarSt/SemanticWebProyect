package my.company.websemanticaoscar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
    	parseOscars();
    }
    
    public static void parseOscars () {
    	RDFSParser rdf = new RDFSParser();
    	System.out.println(System.getProperty("user.dir"));
    	try(BufferedReader reader = new BufferedReader(new FileReader("dataset/data_csv.csv"))){
    		String line;
    		reader.readLine();
    		while ((line = reader.readLine()) != null) {
    			String[] tokens = line.split(";");
    			rdf.parseTokens(tokens);
    			//System.out.println(rdf.convertNameToResourceName(tokens[3]));
    		}
    		rdf.printModel();
    		rdf.saveModelToFile(new File("output/oscar2.ttl"));
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
