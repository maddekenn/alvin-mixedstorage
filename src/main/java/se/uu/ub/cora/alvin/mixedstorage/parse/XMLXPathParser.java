package se.uu.ub.cora.alvin.mixedstorage.parse;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class XMLXPathParser {
	private Document document;
	private XPath xpath;

	private XMLXPathParser(String xml)
			throws ParserConfigurationException, SAXException, IOException {
		document = createDocumentFromXML(xml);
		setupXPath();
	}

	public static XMLXPathParser forXML(String xml) {
		try {
			return new XMLXPathParser(xml);
		} catch (Exception e) {
			throw ParseException.withMessageAndException("Can not read xml: " + e.getMessage(), e);
		}
	}

	private void setupXPath() {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		xpath = xpathFactory.newXPath();
	}

	public Document createDocumentFromXML(String xml)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder dBuilder = createDocumentBuilder();
		return readXMLUsingBuilderAndXML(dBuilder, xml);
	}

	private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		ErrorHandler errorHandlerWithoutSystemOutPrinting = new DefaultHandler();
		dBuilder.setErrorHandler(errorHandlerWithoutSystemOutPrinting);
		return dBuilder;
	}

	private Document readXMLUsingBuilderAndXML(DocumentBuilder dBuilder, String xml)
			throws SAXException, IOException {
		StringReader reader = new StringReader(xml);
		InputSource in = new InputSource(reader);
		Document doc = dBuilder.parse(in);
		doc.getDocumentElement().normalize();
		return doc;
	}

	public String getStringFromDocumentUsingXPath(String xpathString) {
		try {
			XPathExpression expr = xpath.compile(xpathString);
			return (String) expr.evaluate(document, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			throw ParseException.withMessageAndException(composeMessage(e), e);
		}
	}

	private String composeMessage(XPathExpressionException e) {
		return "Unable to use xpathString: " + e.getMessage();
	}

	public String getStringFromNodeUsingXPath(Node node, String xpathString) {
		try {
			XPathExpression expr = xpath.compile(xpathString);
			return (String) expr.evaluate(node, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			throw ParseException.withMessageAndException(composeMessage(e), e);
		}
	}

	public NodeList getNodeListFromDocumentUsingXPath(String xpathString) {
		try {
			XPathExpression expr = xpath.compile(xpathString);
			return (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw ParseException.withMessageAndException(composeMessage(e), e);
		}
	}

	public void setStringInDocumentUsingXPath(String xpathString, String newValue) {
		try {
			Node nodeToSet = evaluateXPath(xpathString);
			nodeToSet.setTextContent(newValue);
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Error setting string value on node", e);
		}
	}

	private Node evaluateXPath(String xpathString) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(xpathString);
		return (Node) expr.evaluate(document, XPathConstants.NODE);
	}

	public void setOrCreateStringInDocumentUsingXPath(String xpathString, String newValue,
			String elementName, String parentPath) {
		try {
			tryToSetOrCreateStringInDocuement(xpathString, newValue, elementName, parentPath);
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Error setting or creating string value on node", e);
		}
	}

	private void tryToSetOrCreateStringInDocuement(String xpathString, String newValue,
			String elementName, String parentPath) throws XPathExpressionException {
		Node nodeToSet = evaluateXPath(xpathString);
		if (nodeToSet != null) {
			setStringInDocumentUsingXPath(xpathString, newValue);
		} else {
			createAndAppendElement(elementName, newValue, parentPath);
		}
	}

	private void createAndAppendElement(String elementName, String newValue, String parentPath)
			throws XPathExpressionException {
		Element newNode = document.createElement(elementName);
		newNode.appendChild(document.createTextNode(newValue));
		XPathExpression parentExpr = xpath.compile(parentPath);
		Node parentNode = (Node) parentExpr.evaluate(document, XPathConstants.NODE);
		parentNode.appendChild(newNode);
	}

	public String getDocumentAsString(String xpathString) {
		try {
			XPathExpression expr = xpath.compile(xpathString);
			Node nodeToExport = (Node) expr.evaluate(document, XPathConstants.NODE);
			StringWriter sw = new StringWriter();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			transformerFactory.setAttribute("indent-number", 4);

			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "no");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.transform(new DOMSource(nodeToExport), new StreamResult(sw));
			return sw.toString();
		} catch (Exception e) {
			throw new RuntimeException("Error converting node to String", e);
		}
	}

	public void removeNodeUsingElementNameAndParentPath(String xpathString, String parentPath) {
		try {
			Node node = evaluateXPath(xpathString);
			if (node != null) {
				XPathExpression parentExpr = xpath.compile(parentPath);
				Node parentNode = (Node) parentExpr.evaluate(document, XPathConstants.NODE);
				parentNode.removeChild(node);
			}
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Error removing node", e);
		}
	}
}