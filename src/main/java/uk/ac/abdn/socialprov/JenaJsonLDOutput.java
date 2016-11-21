package uk.ac.abdn.socialprov;

import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.SKOS;

public class JenaJsonLDOutput {

	public static void main(String[] args) {
		OntModel model = ModelFactory.createOntologyModel();
	
		Dataset ds = DatasetFactory.create(model);
		OntModel dataModel = ModelFactory.createOntologyModel();
		OntClass software = dataModel.createClass("http://w3id.org/abdn/prism/Software");
		OntClass dataset = dataModel.createClass("http://dbpedia.org/resource/dataset");
		
		ObjectProperty collectedUsing = model.createObjectProperty("collectedUsing");
		collectedUsing.setPropertyValue(SKOS.prefLabel, model.createLiteral("which tool or API was used to collecte the data?"));
		ds.addNamedModel("http://w3id.org/abdn/prism/model", dataModel);
		dataset.addSuperClass(dataModel.createAllValuesFromRestriction(null, collectedUsing, software));
		
		RDFDataMgr.write(System.out, ds, Lang.JSONLD);
	}

}
