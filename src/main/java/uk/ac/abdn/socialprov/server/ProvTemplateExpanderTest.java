package uk.ac.abdn.socialprov.server;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.Bindings;
import org.openprovenance.prov.xml.ProvFactory;

import static org.openprovenance.prov.template.Expand.VAR_NS;
import static uk.ac.abdn.socialprov.ProvTemplateLibrary.TEMPLATE_PRISM_DATASET;

import java.io.IOException;

import junit.framework.TestCase;
import uk.ac.abdn.socialprov.ProvTemplateTesting;

public class ProvTemplateExpanderTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testExpand1() throws IOException, Throwable {
		org.openprovenance.prov.model.ProvFactory pf = new ProvFactory();
		Document template = new Utility().readDocument(TEMPLATE_PRISM_DATASET+"-template.provn_1", pf);

		ProvTemplateExpander pte = new ProvTemplateExpander();
		ProvTemplateMappingItem[] mappings = new ProvTemplateMappingItem[]{
				new ProvTemplateMappingItem("prism:collectedFromService", "Twitter Coffee","var:collectedFromService", "vargen:rawDatasetId","http://coffee.com/Twitter" )
		};
				
		String expanded = pte.expand(pf, template, mappings);
		System.out.println(expanded);
		assertTrue(expanded != null && !("".equals(expanded)));
	}

}
