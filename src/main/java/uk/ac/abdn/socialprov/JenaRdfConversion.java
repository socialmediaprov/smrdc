package uk.ac.abdn.socialprov;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public class JenaRdfConversion {

	public static void main(String[] args) {
		Model m = RDFDataMgr.loadModel("resources/web/js/dataset-template.ttl", Lang.TTL);
		RDFDataMgr.write(System.out, m, Lang.JSONLD);
	}
	
}
