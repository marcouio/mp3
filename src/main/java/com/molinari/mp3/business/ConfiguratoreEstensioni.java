package main.java.com.molinari.mp3.business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfiguratoreEstensioni {

	public static final String ESTENSIONI = "estensioni";
	private static final String ACCETTATE = "accettate";
	private static final String SCOMPATTARE = "scompattare";
	private static final String VALORE = "valore";

	private File xml;
	private Document doc;
	ArrayList<String> estensioneDaScompattare = new ArrayList<String>();
	ArrayList<String> estensioneAccettate = new ArrayList<String>();

	public static void main(final String[] args) throws ParserConfigurationException, SAXException, IOException {
		// File xml = new
		// File("/home/kiwi/Documenti/Workspace/Mp3Reader/config.xml");
		// ConfiguratoreEstensioni config = new ConfiguratoreEstensioni();
	}

	private static ConfiguratoreEstensioni singleton;

	public static ConfiguratoreEstensioni getSingleton() throws ParserConfigurationException, SAXException, IOException {
		if (singleton == null) {
			singleton = new ConfiguratoreEstensioni();
		}
		return singleton;
	}

	private ConfiguratoreEstensioni() throws ParserConfigurationException, SAXException, IOException {
		setXml(new File("./config.xml"));
		this.doc = Mp3ReaderUtil.createDocument(xml);
		init(doc);
	}

	private void init(final Document doc2) {
		final Element root = doc.getDocumentElement();
		NodeList listaNodi = null;
		listaNodi = (root.hasChildNodes()) ? root.getChildNodes() : null;
		if (listaNodi != null) {
			for (int i = 0; i < listaNodi.getLength(); i++) {
				final Node nodo = listaNodi.item(i);
				riempiListeEstensioni(nodo);
			}
		}

	}

	private void riempiListeEstensioni(final Node nodo) {
		if (nodo.getNodeName().equals(ESTENSIONI)) {
			final NodeList figliNodoEstensioni = nodo.getChildNodes();
			for (int x = 0; x < figliNodoEstensioni.getLength(); x++) {
				final Node nodoEstensioni = figliNodoEstensioni.item(x);
				if (nodoEstensioni.getNodeName().equals(ACCETTATE)) {
					riempiLista(nodoEstensioni, estensioneAccettate);
				} else if (nodoEstensioni.getNodeName().equals(SCOMPATTARE)) {
					riempiLista(nodoEstensioni, estensioneDaScompattare);
				}
			}

		}
	}

	private void riempiLista(final Node nodoEstensioni, final ArrayList<String> lista) {
		final NodeList listaFigli = nodoEstensioni.getChildNodes();
		if (listaFigli != null) {
			for (int i = 0; i < listaFigli.getLength(); i++) {
				final Node nodo = listaFigli.item(i);
				Element elemento = null;
				if (nodo.getNodeType() == Node.ELEMENT_NODE) {
					elemento = (Element) nodo;
				}
				if (elemento != null) {
					final String estensione = elemento.getAttribute(VALORE);
					lista.add(estensione);
				}
			}
		}
	}

	public ArrayList<String> getEstensioniPerOperazione(final String elemento) {
		final ArrayList<String> estensioni = new ArrayList<String>();
		final Element root = doc.getDocumentElement();
		NodeList listaNodi = null;
		listaNodi = (root.hasChildNodes()) ? root.getChildNodes() : null;
		if (listaNodi != null) {
			for (int i = 0; i < listaNodi.getLength(); i++) {
				final Node nodo = listaNodi.item(i);
				riempiListAOperazione(nodo, elemento, estensioni);
			}
		}
		return estensioni;
	}

	private void riempiListAOperazione(final Node nodo, final String elemento, final ArrayList<String> estensioni2) {
		if (nodo.getNodeName().equals(ESTENSIONI)) {
			final NodeList figliNodoEstensioni = nodo.getChildNodes();
			for (int x = 0; x < figliNodoEstensioni.getLength(); x++) {
				final Node nodoEstensioni = figliNodoEstensioni.item(x);
				if (nodoEstensioni.getNodeName().equals(elemento)) {
					riempiLista(nodoEstensioni, estensioni2);
				}
			}

		}
	}

	public ArrayList<String> getEstensioneDaScompattare() {
		return estensioneDaScompattare;
	}

	public void setEstensioneDaScompattare(final ArrayList<String> estensioneDaScompattare) {
		this.estensioneDaScompattare = estensioneDaScompattare;
	}

	public ArrayList<String> getEstensioneAccettate() {
		return estensioneAccettate;
	}

	public void setEstensioneAccettate(final ArrayList<String> estensioneAccettate) {
		this.estensioneAccettate = estensioneAccettate;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(final Document doc) {
		this.doc = doc;
	}

	public File getXml() {
		return xml;
	}

	public void setXml(final File xml) {
		this.xml = xml;
	}
}
