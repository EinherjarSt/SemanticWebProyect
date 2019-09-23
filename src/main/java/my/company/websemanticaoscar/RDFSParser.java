package my.company.websemanticaoscar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.RDFSyntax;

public class RDFSParser {
	Model model;
	String voc = "http://oscars.org/voc#";
	String movieURL = "http://oscars.org/";
	private Property yearProperty;
	private Property instanceProperty;
	private Property categoryProperty;
	private Property veredictProperty;
	private Property nomineeProperty;

	public RDFSParser() {
		this.model = ModelFactory.createDefaultModel();
		model.setNsPrefix("voc", voc);
		yearProperty = model.createProperty(voc, "year");
		instanceProperty = model.createProperty(voc, "instance");
		categoryProperty = model.createProperty(voc, "category");
		veredictProperty = model.createProperty(voc, "veredict");
		nomineeProperty = model.createProperty(voc, "nominee");
	}

	public void parseTokens(String[] tokens) {
		Resource subject = model.createResource(movieURL + convertNameToResourceName(tokens[3]));
		Resource classRdfs = model.createResource(voc + "Nominee");
		Resource blankInstance = model.createResource();
		
		subject.addProperty(RDF.type, classRdfs);
		subject.addProperty(yearProperty, tokens[0]);
		// subject.addProperty(categoryProperty, tokens[1]);
		// subject.addProperty(veredictProperty, tokens[2]);		
		subject.addProperty(nomineeProperty, tokens[3]);
		
		subject.addProperty(instanceProperty, blankInstance);
		blankInstance.addProperty(categoryProperty, tokens[1]);
		blankInstance.addProperty(veredictProperty, tokens[2].compareTo("True") == 0? "Winner": "Loser");
	}

	public String convertNameToResourceName(String string) {
		return string.replace(" ", "_").replace(",", "").replace(".", "");

	}

	public void saveModelToFile(File file) {
		try {
			this.model.write(new FileOutputStream(file), "Turtle");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printModel() {
		this.model.write(System.out, "Turtle");
	}
}
