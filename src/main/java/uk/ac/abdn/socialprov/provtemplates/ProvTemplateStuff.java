package uk.ac.abdn.socialprov.provtemplates;

import org.apache.jena.query.Dataset;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;


public class ProvTemplateStuff {
	public static void main(String[] args) {
		Dataset model = RDFDataMgr.loadDataset("resources/templates/dataset-template.ttl", Lang.TTL);
		System.out.println(model.getDefaultModel().size());
		
		RDFDataMgr.write(System.out, model, Lang.JSONLD);
	}
}
