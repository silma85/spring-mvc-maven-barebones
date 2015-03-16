package alive.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

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

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import alive.stream.XMLOutputStream;

@Service
public class XMLService {

  public XMLOutputStream parseAndTransform(String raw, OutputStream out) throws ParserConfigurationException,
          SAXException, IOException, TransformerConfigurationException, TransformerFactoryConfigurationError,
          TransformerException {
    XMLOutputStream xo = new XMLOutputStream(out);

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document d = db.parse(new ByteArrayInputStream(raw.getBytes()));

    StreamResult sr = new StreamResult(xo);
    DOMSource ds = new DOMSource(d);
    Transformer tf = TransformerFactory.newInstance().newTransformer();

    tf.transform(ds, sr);

    return xo;
  }
}
