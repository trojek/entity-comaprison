package cc.rojek.ec;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Application domain model class
 */
public class ADM {

	private Document doc;

	public ADM(String filename) throws SAXException, IOException {

		try {
			File fXmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void printroot(){
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	}
}
