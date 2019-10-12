package my.company.websemanticaoscar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import org.json.*;

import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.RDFSyntax;

public class MoviesMetadataRDFModel {

	Model model;
	String voc = "http://movies.org/voc#";
	String movieURL = "http://movies.org/";
	String actorURL = movieURL + "cast_members/";
	private Property full_age_restricted, budget, genre, imdbID, 
	original_lang, overview ,popularity, release_date, revenue, runtime, status, title,
	vote_avg, vote_count;
	
	private Property character, gender, name;
	
	private Property role;
	private Property movie;
	
	public MoviesMetadataRDFModel() {
		this.model = ModelFactory.createDefaultModel();
		model.setNsPrefix("voc", voc);
		this.createModelProperties();
		
	}
	
	private void createModelProperties() {
		this.full_age_restricted = model.createProperty(voc,"full_age_restricted");
		this.budget = model.createProperty(voc,"budget");
		this.genre = model.createProperty(voc,"genre");
		this.imdbID = model.createProperty(voc,"imdbID");
		this.original_lang = model.createProperty(voc,"original_lang");
		this.overview = model.createProperty(voc,"overview");
		this.popularity = model.createProperty(voc,"popularity");
		this.release_date = model.createProperty(voc,"release_date");
		this.revenue = model.createProperty(voc,"revenue");
		this.runtime = model.createProperty(voc,"runtime");
		this.status = model.createProperty(voc,"status");
		this.title = model.createProperty(voc,"title");
		this.vote_avg = model.createProperty(voc,"vote_avg");
		this.vote_count = model.createProperty(voc,"vote_count");
		
		this.character = model.createProperty(voc,"character");
		this.gender = model.createProperty(voc,"gender");
		this.name = model.createProperty(voc,"name");
		
		this.role = model.createProperty(voc,"role");
		this.movie = model.createProperty(voc, "movie");
	}
	
	public void addCSVLineAsModelStatements(String[] csvLine, MoviesMetadataParser lineParser) {
		String movieURI = this.movieURL + convertNameToResourceName(lineParser.getValue(csvLine, "title"));
		Resource subject = model.createResource(movieURI);
		Resource classRdfs = model.createResource(voc+"Movie");
		
		
		subject.addProperty(RDF.type, classRdfs);
		subject.addProperty(this.full_age_restricted, lineParser.getValue(csvLine, "adult"));
		subject.addProperty(this.budget, lineParser.getValue(csvLine, "budget"));
		subject.addProperty(this.imdbID, lineParser.getValue(csvLine, "imdb_id"));
		subject.addProperty(this.original_lang, lineParser.getValue(csvLine, "original_language"));
		subject.addProperty(this.overview, lineParser.getValue(csvLine, "overview"));
		subject.addProperty(this.popularity, lineParser.getValue(csvLine, "popularity"));
		subject.addProperty(this.release_date, lineParser.getValue(csvLine, "release_date"));
		subject.addProperty(this.revenue, lineParser.getValue(csvLine, "revenue"));
		subject.addProperty(this.runtime, lineParser.getValue(csvLine, "runtime"));
		subject.addProperty(this.status, lineParser.getValue(csvLine, "status"));
		subject.addProperty(this.title, lineParser.getValue(csvLine, "title"));
		subject.addProperty(this.vote_avg, lineParser.getValue(csvLine, "vote_average"));
		subject.addProperty(this.vote_count, lineParser.getValue(csvLine, "vote_count"));
		JSONArray genres = new JSONArray(lineParser.getValue(csvLine, "genres"));
		for (int i = 0; i < genres.length(); i++) {
		    JSONObject genreObject = genres.getJSONObject(i);
		    subject.addProperty(this.genre,genreObject.getString("name"));
		}
		
		try {
			JSONArray cast = new JSONArray(lineParser.getValue(csvLine, "cast"));
			Resource actor;
			String actorURI;
			Resource actorClass = model.createResource(voc+"actor");
			Resource movieRole;
			
			for (int i = 0; i < cast.length(); i++) {
			    JSONObject castObject = cast.getJSONObject(i);
			    actorURI = this.actorURL + convertNameToResourceName(castObject.getString("name"));
				actor = model.createResource(actorURI);
				
				actor.addProperty(RDF.type, actorClass);
				actor.addProperty(this.gender, castObject.getNumber("gender").toString());
			    actor.addProperty(this.name, castObject.getString("name"));
			    movieRole = model.createResource();
			    movieRole.addProperty(this.character, castObject.getString("character"));
			    movieRole.addProperty(this.movie, subject);
			    actor.addProperty(this.role, movieRole);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		
		
		
		
	}
	
	public String convertNameToResourceName(String string) {
		return string.replace(" ", "_").replace(",", "").replace(".", "").replace("\"", "").replace("\'", "");		
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


