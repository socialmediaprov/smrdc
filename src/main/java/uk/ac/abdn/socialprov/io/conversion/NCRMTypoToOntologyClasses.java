package uk.ac.abdn.socialprov.io.conversion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.RDFS;


public class NCRMTypoToOntologyClasses {

	public static void main(String[] args) throws IOException {
		Reader in = new FileReader("resources/ncrm/NCRM-methods-Typology-extractedfrompdf.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
		OntModel model = ModelFactory.createOntologyModel();
		OntClass level1 = null, level2 = null, level3 = null;
		
		for (CSVRecord record : records) {
		    String one = record.get(0);
		    if (!"".equals(one)){
		    	level1 = createOntClass(one, model);
		    }
		    String two = record.get(1);
		    if (!"".equals(two)){
		    	level2 = createOntClass(two, model);
		    	level2.addSuperClass(level1);
		    }
		    String three = record.get(2);
		    if (!"".equals(three)){
		    	level3 = createOntClass(three, model);
		    	level3.addSuperClass(level2);
		    }
		}
		FileOutputStream out = new FileOutputStream(new File("resources/ncrm/ncrm-methods.ttl"));
		model.write(out, "TTL");
		out.flush();
		out.close();

	}
	private static int count =0;
	private static OntClass createOntClass(String label, OntModel model) {
		OntClass cls = model.createClass("http://www.ncrm.ac.uk/typology/" + count++);
		cls.setPropertyValue(RDFS.label, model.createLiteral(label));
		return cls;
	}
	
}
