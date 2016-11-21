package uk.ac.abdn.socialprov.ui;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;

public class UiTemplateGenerator {

	/**
	 * Returns the JSON-LD encoding of all the information necessary to build
	 * the UI (i.e. the prov template, schema, and display details) for the given
	 * templateName
	 * 
	 * Assumes files are defined as: <templateName>-template.provn
	 * <templateName>-schema.ttl <templateName>-display.ttl
	 * 
	 * @param templateName
	 * @throws Throwable 
	 * @throws IOException 
	 */
	public String getUiTemplate(String templateName) throws IOException, Throwable {
		Dataset ds = RDFDataMgr.loadDataset(templateName + "-schema.ttl", Lang.TTL);
		RDFDataMgr.read(ds, templateName + "-display.ttl", Lang.TTL);
		RDFDataMgr.read(ds, IOUtils.toInputStream(new Utility().provnConvert(templateName+"-template.provn", ProvFormat.TURTLE)),
				Lang.TTL);

		StringWriter out = new StringWriter();
		RDFDataMgr.write(out, ds, Lang.JSONLD);
		System.out.println(out.toString());
		return out.toString();
	}

}
