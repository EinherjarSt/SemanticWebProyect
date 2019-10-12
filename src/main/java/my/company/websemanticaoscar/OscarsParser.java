package my.company.websemanticaoscar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.RDFSyntax;

public class OscarsParser {
	Model model;
	String voc = "http://oscars.org/voc#";
	String awardURL = "http://oscars.org/awards/";
	String nomineeURL = "http://oscars.org/nominee/";

	private Property yearProperty;
	private Property prizeProperty;
	private Property nomineeProperty;
	private Property winnerProperty;
	private Property nameProperty;

	private Resource awardClass;
	private Resource categoryClass;
	private Resource entityClass;

	public OscarsParser() {
		this.model = ModelFactory.createDefaultModel();
		model.setNsPrefix("voc", voc);
		model.setNsPrefix("nominee", nomineeURL);
		model.setNsPrefix("awards", awardURL);
		// Properties
		yearProperty = model.createProperty(voc, "year");
		prizeProperty = model.createProperty(voc, "prize");
		nomineeProperty = model.createProperty(voc, "nominee");
		winnerProperty = model.createProperty(voc, "winner");
		nameProperty = model.createProperty(voc, "name");

		// Class
		awardClass = model.createProperty(voc, "Award");
		categoryClass = model.createProperty(voc, "Category");
		entityClass = model.createProperty(voc, "Entity");

	}

	public void parseTokens(String[] tokens) {
		String award = awardURL + tokens[0];
		Resource awardResource = model.createResource(award);
		Resource categoryResource = model.createResource(award +"/"+ convertCategoryToResourceName(tokens[1]));
		Resource entityResource = model.createResource(nomineeURL + convertNameToResourceName(tokens[3]));

		awardResource.addProperty(RDF.type, awardClass);
		awardResource.addProperty(yearProperty, tokens[0]);
		awardResource.addProperty(prizeProperty, categoryResource);

		categoryResource.addProperty(RDF.type, categoryClass);
		categoryResource.addProperty(yearProperty, tokens[0]);
		categoryResource.addProperty(nomineeProperty, entityResource);
		if (tokens[2].equals("True")) {
			categoryResource.addProperty(winnerProperty, entityResource);
		}
		entityResource.addProperty(nameProperty, tokens[3]);
	}

	public String convertNameToResourceName(String string) {
		return string.replaceAll("\\(|\\)", " ").trim().replace(" ", "_").replace(",", "").replace(".", "").replace("\"", "")
				.replace("\'", "");

	}

	public String convertCategoryToResourceName(String string) {
		return string.toLowerCase().replaceAll("\\([^)]*\\)", "").trim().replace(' ', '_');

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
