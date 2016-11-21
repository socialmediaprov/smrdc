package uk.ac.abdn.socialprov.io.conversion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.RDFS;


public class NCRMTypoToOntologyIndividuals {

	public static void main(String[] args) throws IOException {
		Reader in = new FileReader("resources/ncrm/NCRM-methods-Typology-extractedfrompdf.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
		OntModel model = ModelFactory.createOntologyModel();
		OntClass dataAnalysisMethodCls = model.createClass("http://w3id.org/abdn/prism/ontology/DataAnalysisMethod");
		
		for (CSVRecord record : records) {
		    String one = record.get(0);
		    if (!"".equals(one)){
		    	 createIndividual(one, model, dataAnalysisMethodCls);
		    }
		    String two = record.get(1);
		    if (!"".equals(two)){
		    	createIndividual(two, model, dataAnalysisMethodCls);
		    }
		    String three = record.get(2);
		    if (!"".equals(three)){
		    	 createIndividual(three, model, dataAnalysisMethodCls);
		    }
		}
		FileOutputStream out = new FileOutputStream(new File("resources/ncrm/ncrm-methods-individuals.ttl"));
		model.write(out, "TTL");
		out.flush();
		out.close();

	}
	private static int count =0;
	private static Individual  createIndividual(String label, OntModel model, OntClass type) {
		Individual i = model.createIndividual("http://www.ncrm.ac.uk/typology/" + count++, type);
		i.setPropertyValue(RDFS.label, model.createLiteral(label));
		return i;
	}
	
}
