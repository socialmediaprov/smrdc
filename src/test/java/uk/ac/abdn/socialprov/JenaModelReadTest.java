package uk.ac.abdn.socialprov;

import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public class JenaModelReadTest {

	public static void main(String[] args) {
		Model model = ModelFactory.createDefaultModel();
		RDFDataMgr.read(model, "file://users/csc316/Programming/socialprov/resources/prism-dataprepmethods.ttl");
		
		RDFDataMgr.write(System.out, model, Lang.RDFXML);
	}
}
