package uk.ac.abdn.socialprov.ui;

import java.io.IOException;

import junit.framework.TestCase;

public class UiTemplateGeneratorTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetUiTemplate() throws IOException, Throwable {
		System.out.println(new UiTemplateGenerator().getUiTemplate("resources/test/prov-templates/prism/dataset"));
	}

}
