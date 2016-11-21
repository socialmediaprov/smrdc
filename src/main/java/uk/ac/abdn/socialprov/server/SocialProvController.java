package uk.ac.abdn.socialprov.server;

import java.io.IOException;

import org.openprovenance.prov.model.ProvFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.abdn.socialprov.ProvTemplateLibrary;

@RestController
public class SocialProvController {
	private ProvTemplateLibrary ptl;
	private ClassAndIndividualRetriever cir;
	private ProvTemplateExpander pte;

	public SocialProvController() {
		super();
		this.ptl = new ProvTemplateLibrary();
		this.cir = new ClassAndIndividualRetriever();
		this.pte = new ProvTemplateExpander();
	}

	@RequestMapping("/getTemplateForGui")
	@CrossOrigin()
	public String getTemplateFor(@RequestParam(value = "type") String type) throws IOException, Throwable {
		return this.ptl.getTemplateForGui(type);
	}

	@RequestMapping(value = "/generateProv", method = RequestMethod.POST)
	@CrossOrigin()
	public String generateProv(@RequestBody ProvTemplateMapping[] mappings) throws IOException, Throwable {
		ProvFactory pf =  new org.openprovenance.prov.xml.ProvFactory();
		StringBuilder sb = new StringBuilder();
		for (ProvTemplateMapping m : mappings) {
			
			sb.append(pte.expand(pf, ptl.getTemplateFor(m.getTemplateType(), pf), m.getMappings()));
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	@RequestMapping(value = "/getClassesAndIndividuals")
	@CrossOrigin()
	public String getClassesAndInDividualsFor(@RequestParam(value = "type") String type) {
		System.out.println("Getting type " + type);
		return cir.getClassesIndividual(type);
	}
	
	@RequestMapping(value = "/generateSummaryReport", method = RequestMethod.POST)
	@CrossOrigin()
	public String generateSummaryReport(@RequestBody String html) throws IOException, Throwable {
		return html;
	}
}
