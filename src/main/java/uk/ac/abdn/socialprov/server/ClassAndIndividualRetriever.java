package uk.ac.abdn.socialprov.server;

import java.io.ByteArrayOutputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class ClassAndIndividualRetriever {

	public String getClassesIndividual(String type) {
		System.out.println("Getting individuals of type " + type);
		String query = "";
		if ("prism:DataAnalysisMethod".equals(type) || "http://w3id.org/abdn/prism/ontology/DataAnalysisMethod".equals(type)) {
			query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX ncrm: <http://www.ncrm.ac.uk/typology/> PREFIX skos: <http://www.w3.org/2004/02/skos/core#> PREFIX prism: <http://w3id.org/abdn/prism/ontology/>"
					+ "SELECT ?individual ?label WHERE {SERVICE <http://localhost:3031/socialprovkb> { graph <http://localhost:3031/socialprovkb/data/ncrm-individuals> {  ?individual a prism:DataAnalysisMethod; rdfs:label ?label.} } } Order by ASC(?label)";

			
			// analysis activity - this is the query for using classes version of NVRM, which we need to work out how to handle in the tool.
//			query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX ncrm: <http://www.ncrm.ac.uk/typology/>"
//					+ "SELECT Distinct ?class ?label WHERE {SERVICE <http://localhost:3031/socialprovkb> { graph <http://localhost:3031/socialprovkb/data/ncrm> {  {?class rdfs:subClassOf  ncrm:177; rdfs:label ?label.} union {?class rdfs:subClassOf  ncrm:209; rdfs:label ?label} union {?class rdfs:subClassOf  ncrm:310; rdfs:label ?label} } } } Order by  ?label";
		} else if ("http://www.wikidata.org/entity/Q3220391".equals(type)) {
			// social media platform
			query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX entity: <http://www.wikidata.org/entity/> PREFIX wdt: <http://www.wikidata.org/prop/direct/>"
					+ "SELECT Distinct ?individual ?label WHERE {  SERVICE <https://query.wikidata.org/sparql> {?individual wdt:P31  entity:Q3220391.  ?individual rdfs:label ?label.  FILTER(LANG(?label) = \"en\")}} Order by  ?label";
		} else if ("prism:DataCollectionTool".equals(type) || "http://w3id.org/abdn/prism/ontology/DataCollectionTool".equals(type)){
			query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX skos: <http://www.w3.org/2004/02/skos/core#> PREFIX doap: <http://usefulinc.com/ns/doap#> select DISTINCT ?individual ?label WHERE { SERVICE <http://localhost:3031/socialprovkb> { graph <http://localhost:3031/socialprovkb/data/tools> {?individual a doap:Project; skos:prefLabel ?label.} } } ORDER BY ASC(?label)";
		} else if ("prism:DataPreparationMethod".equals(type) || "http://w3id.org/abdn/prism/ontology/DataPreparationMethod".equals(type)){
			// graoph <http://localhost:3031/socialprovkb/data/preparationActivities>
			query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX prism: <http://w3id.org/abdn/prism/ontology/> PREFIX skos: <http://www.w3.org/2004/02/skos/core#> PREFIX doap: <http://usefulinc.com/ns/doap#> select DISTINCT ?individual ?label WHERE { SERVICE <http://localhost:3031/socialprovkb> { graph <http://localhost:3031/socialprovkb/data/preparationActivities> {?individual a prism:DataPreparationMethod; skos:prefLabel ?label.} } } ORDER BY ASC(?label)";
			System.out.println(query);
		} else {
			// unknown type so send a null response
			return "";
		}
		return executeSparl(query);
	}

	private String executeSparl(String query) {
		Model model = ModelFactory.createDefaultModel();
		Query q = QueryFactory.create(query);
		QueryExecution qexec = QueryExecutionFactory.create(q, model);
		ResultSet results = qexec.execSelect();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ResultSetFormatter.outputAsJSON(os, results);
		System.out.println("   returning results");
		return os.toString();
	}

}
