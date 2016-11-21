package uk.ac.abdn.socialprov;

import static org.openprovenance.prov.template.Expand.VAR_NS;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.Bindings;
import org.openprovenance.prov.template.Expand;
import org.openprovenance.prov.template.Groupings;

import static org.openprovenance.prov.template.Expand.VAR_NS;
import static org.openprovenance.prov.template.Expand.TMPL_NS;
import junit.framework.TestCase;

public class ProvTemplateTesting extends TestCase {

	static final String EX_NS = "http://example.org/";

	protected void setUp() throws Exception {
		super.setUp();
	}

	// String base = "resources/test/prov-templates/official/";
	String base = "resources/test/prov-templates/prism/";

	public void test() throws IOException, Throwable {
		// expandTemplate(base + "template.provn", base + "bindings.provn", base
		// + "out.provn");
		expandTemplate(base + "dataset-template-2d.provn", base + "dataset-test-bindings.provn", base + "dataset-test-output-2d-b.provn");
		// testExpand21();
	}

	ProvFactory pf = new org.openprovenance.prov.xml.ProvFactory();
	QualifiedName var_a = pf.newQualifiedName(VAR_NS, "a", "var");
	QualifiedName var_b = pf.newQualifiedName(VAR_NS, "b", "var");
	QualifiedName var_c = pf.newQualifiedName(VAR_NS, "c", "var");
	QualifiedName var_d = pf.newQualifiedName(VAR_NS, "d", "var");
	QualifiedName var_e = pf.newQualifiedName(VAR_NS, "e", "var");
	QualifiedName var_ag = pf.newQualifiedName(VAR_NS, "ag", "var");
	QualifiedName var_pl = pf.newQualifiedName(VAR_NS, "pl", "var");
	QualifiedName var_label = pf.newQualifiedName(VAR_NS, "label", "var");
	QualifiedName var_start = pf.newQualifiedName(VAR_NS, "start", "var");
	QualifiedName var_end = pf.newQualifiedName(VAR_NS, "end", "var");

	public void expandTemplate(String in, String inBindings, String out) throws IOException, Throwable {
		System.err.println("expander 2 ==========================================> " + in);

		Document doc = new Utility().readDocument(in, pf);
		Document docBindings = new Utility().readDocument(inBindings, pf);
		Bindings bindings1 = Bindings.fromDocument(docBindings, pf);

		Bundle bun = (Bundle) doc.getStatementOrBundle().get(0);

		Groupings grp1 = Groupings.fromDocument(doc);

		System.err.println("Found groupings " + grp1);
		boolean addOrderp = true;
		Bundle bun1 = (Bundle) new Expand(pf, addOrderp, false).expand(bun, bindings1, grp1).get(0);
		Document doc1 = pf.newDocument();
		doc1.getStatementOrBundle().add(bun1);

		bun1.setNamespace(Namespace.gatherNamespaces(bun1));

		// doc1.setNamespace(bun1.getNamespace());
		doc1.setNamespace(new Namespace());

		new Utility().writeDocument(doc1, out, pf);
		// InteropFramework inf=new InteropFramework();
		// inf.writeDocument(out, doc1);

		System.err.println("expander 2 ==========================================> ");

	}

	public void testConvert() throws IOException, Throwable {
		InteropFramework interop = new InteropFramework();
		String in = base + "dataset-template.provn";
		Document doc = new Utility().readDocument(in, pf);

		interop.writeDocument(System.out, ProvFormat.TURTLE, doc);
	}

	public void testExpand21() throws IOException, Throwable {

		Bindings bindings1 = new Bindings(pf);

		bindings1.addVariable(var_a, pf.newQualifiedName(EX_NS, "av1", "ex"));
		bindings1.addVariable(var_a, pf.newQualifiedName(EX_NS, "av2", "ex"));
		bindings1.addVariable(var_a, pf.newQualifiedName(EX_NS, "av3", "ex"));

		bindings1.addVariable(var_b, pf.newQualifiedName(EX_NS, "bv1", "ex"));
		bindings1.addVariable(var_b, pf.newQualifiedName(EX_NS, "bv2", "ex"));

		bindings1.addVariable(var_b, pf.newQualifiedName(EX_NS, "bv3", "ex"));

		List<TypedValue> ll = new LinkedList<TypedValue>();
		ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), "me1@example", pf.getName().XSD_STRING));
		bindings1.addAttribute(var_d, ll);

		ll = new LinkedList<TypedValue>();
		ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), "me2@example", pf.getName().XSD_STRING));
		bindings1.addAttribute(var_d, ll);

		ll = new LinkedList<TypedValue>();
		ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), "me3@example", pf.getName().XSD_STRING));
		bindings1.addAttribute(var_d, ll);

		ll = new LinkedList<TypedValue>();
		ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), "Luc1@example", pf.getName().XSD_STRING));
		bindings1.addAttribute(var_e, ll);

		ll = new LinkedList<TypedValue>();
		ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), "Luc2@example", pf.getName().XSD_STRING));
		bindings1.addAttribute(var_e, ll);

		ll = new LinkedList<TypedValue>();
		ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), "Luc6@example", pf.getName().XSD_STRING));
		ll.add(pf.newOther(pf.newQualifiedName(TMPL_NS, "ignore", "app"), "Luc7@example", pf.getName().XSD_STRING));
		bindings1.addAttribute(var_e, ll);

		expander(base + "template21.provn", base + "expanded21.provn", bindings1, base + "bindings21.provn");

	}

	public void expander(String template, String out, Bindings bindings1, String bindings)
			throws IOException, Throwable {
		System.err.println("expander ==========================================> " + template);

		ProvFactory pf = new org.openprovenance.prov.xml.ProvFactory();
		Document doc = new Utility().readDocument(template, pf);

		Bundle bun = (Bundle) doc.getStatementOrBundle().get(0);

		Groupings grp1 = Groupings.fromDocument(doc);

		System.err.println("Found groupings " + grp1);
		boolean addOrderp = false;
		Bundle bun1 = (Bundle) new Expand(pf, addOrderp, false).expand(bun, bindings1, grp1).get(0);
		Document doc1 = pf.newDocument();
		doc1.getStatementOrBundle().add(bun1);

		bun1.setNamespace(Namespace.gatherNamespaces(bun1));

		// doc1.setNamespace(bun1.getNamespace());
		doc1.setNamespace(new Namespace());

		new Utility().writeDocument(bindings1.toDocument(), bindings, pf);
		Namespace.withThreadNamespace(doc1.getNamespace());
		new Utility().writeDocument(doc1, out, pf);
		// InteropFramework inf=new InteropFramework();
		// inf.writeDocument(out, doc1);

		System.err.println("expander ==========================================> ");

	}

	public void testExpanderBindings() throws IOException, Throwable {
		pf = new org.openprovenance.prov.xml.ProvFactory();

		Bindings bindings1 = new Bindings(pf);
		QualifiedName var_a = pf.newQualifiedName(VAR_NS, "a", "var");
		QualifiedName var_b = pf.newQualifiedName(VAR_NS, "b", "var");
		
		bindings1.addVariable(var_a, pf.newQualifiedName(EX_NS, "av1", "ex"));
//		bindings1.addVariable(var_a, pf.newQualifiedName(EX_NS, "av2", "ex"));
//		bindings1.addVariable(var_a, pf.newQualifiedName(EX_NS, "av3", "ex"));

		bindings1.addVariable(var_b, pf.newQualifiedName(EX_NS, "bv1", "ex"));
//		bindings1.addVariable(var_b, pf.newQualifiedName(EX_NS, "bv2", "ex"));

		String base = "resources/test/prov-templates/official/";
		expander(base+"template1.provn", base+"expanded1.provn", bindings1, base+"bindings1.provn");
	}

	public void testExpand3() throws IOException, Throwable {
		
		pf = new org.openprovenance.prov.xml.ProvFactory();

		QualifiedName var_a = pf.newQualifiedName(VAR_NS, "a", "var");
		QualifiedName var_b = pf.newQualifiedName(VAR_NS, "b", "var");
		QualifiedName var_c = pf.newQualifiedName(VAR_NS, "c", "var");
		
	 	Bindings bindings1=new Bindings(pf);

	 	bindings1.addVariable(var_a,
	 	                      pf.newQualifiedName(EX_NS, "av1", "ex"));
	 	bindings1.addVariable(var_a,
	 	                      pf.newQualifiedName(EX_NS, "av2", "ex"));
	 	bindings1.addVariable(var_a,
	 	                      pf.newQualifiedName(EX_NS, "av3", "ex"));
	 	
	 	bindings1.addVariable(var_b,
	 	                      pf.newQualifiedName(EX_NS, "bv1", "ex"));
	 	bindings1.addVariable(var_b,
	 	                      pf.newQualifiedName(EX_NS, "bv2", "ex"));
	 	
	 	bindings1.addVariable(var_c,
	 	                      pf.newQualifiedName(EX_NS, "cv1", "ex"));
	 	bindings1.addVariable(var_c,
	 	                      pf.newQualifiedName(EX_NS, "cv2", "ex"));
	 	
	  	
	 	String base = "resources/test/prov-templates/official/";
		expander(base+"template3.provn", base+"expanded3.provn", bindings1, base+"bindings3.provn");
	 	
	 	
	}
	
	public void testExpander() throws IOException, Throwable {
		new ProvTemplateTesting().expander("resources/test/prov-templates/prism/dataset-template.provn_1",
				"resources/test/prov-templates/prism/dataset-test-bindings.provn",
				"resources/test/prov-templates/prism/dataset-test-output.provn");
	}

	public void expander(String in, String inBindings, String out) throws IOException, Throwable {
		System.err.println("expander ==========================================> " + in);
		ProvFactory pf = new org.openprovenance.prov.xml.ProvFactory();
		Document doc = new Utility().readDocument(in, pf);
		Document docBindings = new Utility().readDocument(inBindings, pf);
		Bindings bindings1 = Bindings.fromDocument(docBindings, pf);

		Bundle bun = (Bundle) doc.getStatementOrBundle().get(0);

		Groupings grp1 = Groupings.fromDocument(doc);

		System.err.println("Found groupings " + grp1);

		Bundle bun1 = (Bundle) new Expand(pf, false, false).expand(bun, bindings1, grp1).get(0);
		Document doc1 = pf.newDocument();
		doc1.getStatementOrBundle().add(bun1);

		bun1.setNamespace(Namespace.gatherNamespaces(bun1));

		// doc1.setNamespace(bun1.getNamespace());
		doc1.setNamespace(new Namespace());

		new Utility().writeDocument(doc1, out, pf);
		// InteropFramework inf=new InteropFramework();
		// inf.writeDocument(out, doc1);

		System.err.println("expander ==========================================> ");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
