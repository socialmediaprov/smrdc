package uk.ac.abdn.socialprov.server;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.Bindings;
import org.openprovenance.prov.template.Expand;
import org.openprovenance.prov.template.Groupings;
import static org.openprovenance.prov.template.Expand.VAR_NS;

public class ProvTemplateExpander {

	public String expand(ProvFactory pf, Document template, ProvTemplateMappingItem[] mappings) {
//		ProvFactory pf = new org.openprovenance.prov.xml.ProvFactory();
		Bundle bun = (Bundle) template.getStatementOrBundle().get(0);

		Bindings bindings = new Bindings(pf);
		for (ProvTemplateMappingItem mapping : mappings) {
			String nsUri = mapping.getSelectedValue().lastIndexOf('#') > -1
					? mapping.getSelectedValue().substring(0, mapping.getSelectedValue().lastIndexOf("#")+1)
					: mapping.getSelectedValue().substring(0, mapping.getSelectedValue().lastIndexOf("/")+1);
					
			bindings.addVariable(pf.newQualifiedName(VAR_NS, mapping.getVariable().substring(4), "var"),
					pf.newQualifiedName(nsUri, mapping.getSelectedValue().substring(nsUri.length()), "eg"));

			
			List<TypedValue> attributeValues = new ArrayList<TypedValue>();
			attributeValues.add(pf.newValue(mapping.getSelectedValue(), null));
			bindings.addAttribute(pf.newQualifiedName(VAR_NS, mapping.getVariable().substring(4), "var"),attributeValues);
//					pf.newQualifiedName(nsUri, mapping.getSelectedResourceUri().substring(nsUri.length()), "eg"));
		}

		Bundle expanded = (Bundle) new Expand(pf, false, false).expand(bun, bindings, Groupings.fromDocument(template))
				.get(0);
		Document doc1 = pf.newDocument();
		doc1.getStatementOrBundle().add(expanded);
		expanded.setNamespace(Namespace.gatherNamespaces(expanded));
		doc1.setNamespace(new Namespace());

		ByteArrayOutputStream os3 = new ByteArrayOutputStream();
		new Utility(). writeDocument(template, os3, pf);
		System.out.println(os3);
		ByteArrayOutputStream os2 = new ByteArrayOutputStream();
		new Utility(). writeDocument(bindings.toDocument(), os2, pf);
		System.out.println(os2);
		
		Namespace.withThreadNamespace(doc1.getNamespace());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new Utility().writeDocument(doc1, os, pf);
		return os.toString();
	}

}
