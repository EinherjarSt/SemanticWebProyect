package my.company.websemanticaoscar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

public class OscarsParser {
	Model model;
	String voc = "http://oscars.org/voc#";
	String awardURL = "http://oscars.org/awards/";
	String nomineeURL = "http://oscars.org/nominee/";

	private Property yearProperty;
	private Property nameProperty;
	private Property hasPrizeProperty;
	private Property hasNomineeProperty;
	private Property hasWinnerProperty;
	private Property isPrizeOfProperty;
	private Property isNomineeOfProperty;

	private Resource awardClass;
	private Resource categoryClass;
	private Resource nomineeClass;

	public OscarsParser() {
		this.model = ModelFactory.createDefaultModel();
		model.setNsPrefix("voc", voc);
		model.setNsPrefix("nominee", nomineeURL);
		model.setNsPrefix("awards", awardURL);
		// Properties
		yearProperty = model.createProperty(voc, "year");
		nameProperty = model.createProperty(voc, "name");
		hasPrizeProperty = model.createProperty(voc, "hasPrize");
		hasNomineeProperty = model.createProperty(voc, "hasNominee");
		hasWinnerProperty = model.createProperty(voc, "hasWinner");
		isPrizeOfProperty = model.createProperty(voc, "isPrizeOf");
		isNomineeOfProperty = model.createProperty(voc, "isNomineeOf");
		// Class
		awardClass = model.createProperty(voc, "Award");
		categoryClass = model.createProperty(voc, "Category");
		nomineeClass = model.createProperty(voc, "Nominee");

	}

	public void parseTokens(String[] tokens) {
		String award = awardURL + tokens[0];
		Resource awardResource = model.createResource(award);
		Resource categoryResource = model.createResource(award + "/" + convertCategoryToResourceName(tokens[1]));
		Resource nomineeResource = model.createResource(nomineeURL + convertNameToResourceName(tokens[3]));

		awardResource.addProperty(RDF.type, awardClass);
		awardResource.addProperty(yearProperty, tokens[0]);
		awardResource.addProperty(hasPrizeProperty, categoryResource);

		categoryResource.addProperty(RDF.type, categoryClass);
		categoryResource.addProperty(yearProperty, tokens[0]);
		categoryResource.addProperty(hasNomineeProperty, nomineeResource);
		if (tokens[2].equals("True")) {
			categoryResource.addProperty(hasWinnerProperty, nomineeResource);
		}		
		categoryResource.addProperty(isPrizeOfProperty, awardResource);

		nomineeResource.addProperty(RDF.type, nomineeClass);
		nomineeResource.addProperty(nameProperty, tokens[3]);
		nomineeResource.addProperty(isNomineeOfProperty, categoryResource);
	}

	public String convertNameToResourceName(String string) {
		return string.replaceAll("\\(|\\)", " ").trim().replace(" ", "_").replace(",", "").replace(".", "")
				.replace("\"", "").replace("\'", "");

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
