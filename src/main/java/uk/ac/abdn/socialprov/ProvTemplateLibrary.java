package uk.ac.abdn.socialprov;

import java.io.IOException;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.notation.Utility;

import antlr.Utils;
import uk.ac.abdn.socialprov.ui.UiTemplateGenerator;

public class ProvTemplateLibrary {

	public static final String TEMPLATE_PRISM_DATASET = "resources/test/prov-templates/prism/dataset";
	public static final String TEMPLATE_PRISM_DATAPREP = "resources/test/prov-templates/prism/prep";
	public static final String TEMPLATE_PRISM_DATAANALYSIS = "resources/test/prov-templates/prism/analysis";
	private UiTemplateGenerator templateGen;

	public ProvTemplateLibrary() {
		super();
		this.templateGen = new UiTemplateGenerator();
	}

	public Document getTemplateFor(String type, ProvFactory pf) throws IOException, Throwable{
		if ("dbr:dataset".equals(type) || "http://dbpedia.org/resource/dataset".equals(type)){
			return new Utility().readDocument(TEMPLATE_PRISM_DATASET+"-template.provn", pf);
		} else if ("prism:DataPreparationActivity".equals(type) || "http://w3id.org/abdn/prism/ontology/DataPreparationActivity".equals(type)){
			return new Utility().readDocument(TEMPLATE_PRISM_DATAPREP+"-template.provn", pf);
		}else if ("prism:DataAnalysisActivity".equals(type) || "http://w3id.org/abdn/prism/ontology/DataAnalysisActivity".equals(type)){
			return new Utility().readDocument(TEMPLATE_PRISM_DATAANALYSIS+"-template.provn", pf);
		}
		else return null;
	}
	
	public String getTemplateForGui(String type) throws IOException, Throwable{
		if ("dbr:dataset".equals(type) || "http://dbpedia.org/resource/dataset".equals(type)){
			return templateGen.getUiTemplate(TEMPLATE_PRISM_DATASET);
		} else if ("prism:DataPreparationActivity".equals(type) || "http://w3id.org/abdn/prism/ontology/DataPreparationActivity".equals(type)){
			return templateGen.getUiTemplate(TEMPLATE_PRISM_DATAPREP);
		} else if ("prism:DataAnalysisActivity".equals(type) || "http://w3id.org/abdn/prism/ontology/DataAnalysisActivity".equals(type)){
			return templateGen.getUiTemplate(TEMPLATE_PRISM_DATAANALYSIS);
		} else if ("http://sj.abdn.ac.uk/socialprov/tools/TwitterSearch".equals(type)){
			return templateGen.getUiTemplate("resources/test/prov-templates/prism/dataset-collectedFrom-twitter-search");
		}
		return "{}";
	}

}
