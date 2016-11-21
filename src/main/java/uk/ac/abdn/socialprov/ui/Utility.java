package uk.ac.abdn.socialprov.ui;

import static org.openprovenance.prov.template.Expand.VAR_NS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.jena.query.Dataset;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

public class Utility {

	ProvFactory pf;
	public Utility(){
		pf = new org.openprovenance.prov.xml.ProvFactory();
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
	}
	
	public String convert(String rdfFile, Lang sourceLang, Lang targetLang){
		Dataset model = RDFDataMgr.loadDataset(rdfFile, sourceLang);
		StringWriter writer = new StringWriter();
		RDFDataMgr.write(writer, model, targetLang);
		return writer.toString();
	}
	
	public String provnConvert(String source, ProvFormat destination) throws IOException, Throwable{
		InteropFramework interop = new InteropFramework();
		Document doc = new  org.openprovenance.prov.notation.Utility().readDocument(source, pf);
		StringWriter writer = new StringWriter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		interop.writeDocument(out, destination, doc);
		return out.toString();
	}
	
}
