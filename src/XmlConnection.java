
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.SAXException;

public class XmlConnection {
	
	private static Document xmlDoc;
	private static DocumentBuilder docReader;
	private static XPath xPath;
	
	public XmlConnection() throws ParserConfigurationException, SAXException, IOException {
		docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		xmlDoc = docReader.parse(new File("D:\\AndroidDevelopment\\JAVA DEVELOPMENT\\lab.3.2.15\\Files\\users.xml"));
		
		xPath = XPathFactory.newInstance().newXPath();
		
	}
	
	public static int userLogin(String userName, String password) throws TransformerException, NoSuchAlgorithmException{
		
			//MessageDigest md=MessageDigest.getInstance("MD5");
			
		String expression = "//user[@name=\""+userName + "\" and @password=\"" + password + "\"]"; 
		System.out.println(expression);
		
		try {
			Node user = (Node) xPath.compile(expression).evaluate(xmlDoc, XPathConstants.NODE);
			
			if (user == null){
				String expression2 = "//user[@name=\""+userName + "\"]"; 
				System.out.println(expression2);
				Node user2 = (Node) xPath.compile(expression2).evaluate(xmlDoc, XPathConstants.NODE);
				
				if (user2 == null){
					Element newUser = xmlDoc.createElement("user");
					newUser.setAttribute("name", userName);
					newUser.setAttribute("password", password);
					xmlDoc.getElementsByTagName("users").item(0).appendChild(newUser);
					StreamResult file=new StreamResult(new File("D:\\AndroidDevelopment\\JAVA DEVELOPMENT\\lab.3.2.15\\Files\\users.xml"));
					Transformer transformer;
					try {
						transformer=TransformerFactory.newInstance().newTransformer();
						DOMSource source =new DOMSource(xmlDoc);
						transformer.transform(source, file);
						return 0;
					} catch (TransformerConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TransformerFactoryConfigurationError e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return -4;
					}
							
				} else {
					return -1;
				}
				
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return -3;
		}
		
		return 0;
	}
	
	/*public static void main(String[] args) throws TransformerException, NoSuchAlgorithmException {
		
		try {
			
			XmlConnection test = new XmlConnection();
			int result = XmlConnection.userLogin("someone", "mad");
			System.out.println(result);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}*/

}