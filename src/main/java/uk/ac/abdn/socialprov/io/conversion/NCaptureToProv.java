/**
 * 
 */
package uk.ac.abdn.socialprov.io.conversion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author David Corsar
 *
 */
public class NCaptureToProv extends DefaultHandler {

	private enum Stage {
		SOURCENAME, DESCRIPTION, METADATA, KEY, ACCESSDATE, URL, TITLE, DATAORIGIN, DATATYPE, DATAIDENTIFIER, DATASET, HEADINGS, HEADING, ROWS, ROW, COLUMN
	}

	private Stage currentStage;
	private List<String> headings;
	private List<Map<String, Object>> data;
	private Map<String, Object> currentObject;

	@Override
	public void startDocument() throws SAXException {
		this.headings = new ArrayList<String>();
		this.data = new ArrayList<Map<String, Object>>();
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	private int columnIndex = -1;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch (localName) {
		case "SourceName":

			break;
		case "Description":

			break;
		case "Metadata":

			break;
		case "Key":
			// Key's are part of the metadata describing the dataset
			switch (attributes.getValue("name")) {
			case "accessDate":
				break;
			case "url":
				break;
			case "title":
				break;
			case "dataOrigin":
				break;
			case "dataType":
				break;
			case "dataIdentifier":
				break;
			}
			break;
		case "Dataset":

			break;
		case "Headings":

			break;
		case "Heading":
			this.headings.add(attributes.getValue("columnIdentifier"));
			break;
		case "Rows":

			break;
		case "Row":
			this.currentObject = new HashMap<String, Object>();
			break;
		case "Column":
			this.columnIndex++;
			this.currentStage = Stage.COLUMN;
			break;

		default:
			break;
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (localName) {
		case "SourceName":

			break;
		case "Description":

			break;
		case "Metadata":

			break;
		case "Key":
			break;
		case "Dataset":

			break;
		case "Headings":

			break;
		case "Heading":
			break;
		case "Rows":

			break;
		case "Row":
			this.columnIndex = -1;
			this.data.add(this.currentObject);
			break;
		case "Column":
			this.currentStage = null;
			break;

		default:
			break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (currentStage == Stage.COLUMN)
			this.currentObject.put(this.headings.get(columnIndex), new String(ch, start, length));
	}

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		String filename = "file:///Users/davidcorsar/Box Sync/Work/ESRCSocialMedia/ExampleDataSets/VoteLeaveTweets/Live Tweets VoteLeave on Twitter 2016-04-21 10-41-01Z.nvcx";
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		NCaptureToProv parser = new NCaptureToProv();
		xmlReader.setContentHandler(parser);
		xmlReader.parse(filename);
		
		StringBuilder sb = new StringBuilder();
		for (Map<String, Object> dataItem : parser.data){
			for (String key : dataItem.keySet()){
				sb.append(key).append(" : ").append(dataItem.get(key)).append(" ");
			}
			sb.append("\n");
		}
		System.out.println(sb);
		return;
	}

}
