package uk.ac.abdn.socialprov.io.conversion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.sparql.vocabulary.DOAP;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.SKOS;

import au.com.bytecode.opencsv.CSVReader;

public class SocialMediaToolsToDoap {

	private static final String baseNs = "http://sj.abdn.ac.uk/socialprov/tools/";

	public List<String[]> readToolsCsv(String fileName) {
		List<String[]> contents = new ArrayList<String[]>();
		try {
			CSVReader reader = new CSVReader(new FileReader(fileName));
			contents = reader.readAll();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contents;
	}

	public Dataset convert(List<String[]> toolsCsv) {
		// required format: vendor, platform, media type, website, free/paid,
		// country
//		OntModel model = ModelFactory.createOntologyModel();
		Dataset model = DatasetFactory.create();

		// dictionary of organisations to avoid creating multiple individuals
		Map<String, Resource> organisations = new HashMap<String, Resource>();

		// skip first row - assume headers
		for (int i = 1, j = toolsCsv.size(); i < j; i++) {
			String[] row = toolsCsv.get(i);
			Resource org = organisations.get(row[0]);
			if (org == null) {
				org = buildResource(model, baseNs, row[0], FOAF.Organization);
				org.addLiteral(FOAF.name, row[0]);
				organisations.put(row[0], org);
			}
			Resource project = buildResource(model, baseNs, row[1], DOAP.Project);
			project.addLiteral(DOAP.name, row[1]);
			project.addLiteral(SKOS.prefLabel, String.format("%s - %s",  row[1], row[0]));
		}

		return model;
	}

	private Resource buildResource(Dataset model, String basebs, String id, Resource type) {
		String idClear = id.replaceAll("\\s", "");
		return model.getDefaultModel().createResource(basebs + idClear, type);
	}

	public void writeToFile(Model model, String destination, RDFFormat rdfFormat) throws IOException {
		FileOutputStream out = new FileOutputStream(new File(destination));
		RDFDataMgr.write(out, model, rdfFormat);
		out.flush();
		out.close();
	}

	public static void main(String[] args) throws IOException {
		SocialMediaToolsToDoap converter = new SocialMediaToolsToDoap();
		converter.writeToFile(converter.convert(converter.readToolsCsv("resources/tools/Tools.csv")).getDefaultModel(), "resources/tools/Tools.ttl",
				RDFFormat.TTL);
	}

}
