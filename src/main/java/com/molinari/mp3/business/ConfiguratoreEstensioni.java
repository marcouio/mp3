package com.molinari.mp3.business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfiguratoreEstensioni {

	public static final String ESTENSIONI = "estensioni";
	private static final String RINOMINARE = "rinominare";
	private static final String SCOMPATTARE = "scompattare";
	private static final String CANCELLARE = "cancellare";
	private static final String VALORE = "valore";

	private File xml;
	private Document doc;
	private List<String> estensioneDaScompattare = new ArrayList<>();
	private List<String> estensioneRinominae = new ArrayList<>();
	private List<String> estensioneCancellare = new ArrayList<>();

	
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
	
	public boolean containEstensione(File file, List<String> listToCheck){
		if(listToCheck != null && file != null) {
			for (String estensione : listToCheck) {
				if(file.getAbsolutePath().endsWith(estensione)){
					return true;
				}
			}
		}
		return false;
	}

	private void riempiListeEstensioni(final Node nodo) {
		if (ESTENSIONI.equals(nodo.getNodeName())) {
			final NodeList figliNodoEstensioni = nodo.getChildNodes();
			for (int x = 0; x < figliNodoEstensioni.getLength(); x++) {
				final Node nodoEstensioni = figliNodoEstensioni.item(x);
				if (RINOMINARE.equals(nodoEstensioni.getNodeName())) {
					riempiLista(nodoEstensioni, estensioneRinominae);
				} else if (SCOMPATTARE.equals(nodoEstensioni.getNodeName())) {
					riempiLista(nodoEstensioni, estensioneDaScompattare);
				} else if (CANCELLARE.equals(nodoEstensioni.getNodeName())) {
					riempiLista(nodoEstensioni, estensioneDaScompattare);
				}
			}

		}
	}

	private void riempiLista(final Node nodoEstensioni, final List<String> lista) {
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

	public List<String> getEstensioniPerOperazione(final String elemento) {
		final ArrayList<String> estensioni = new ArrayList<>();
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

	public List<String> getEstensioneDaScompattare() {
		return estensioneDaScompattare;
	}

	public void setEstensioneDaScompattare(final List<String> estensioneDaScompattare) {
		this.estensioneDaScompattare = estensioneDaScompattare;
	}

	public List<String> getEstensioneRinominare() {
		return estensioneRinominae;
	}

	public void setEstensioneAccettate(final List<String> estensioneAccettate) {
		this.estensioneRinominae = estensioneAccettate;
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
	
	public List<String> getEstensioneEliminare() {
		return estensioneCancellare;
	}

	public void setEstensioneEliminare(List<String> estensioneEliminare) {
		this.estensioneCancellare = estensioneEliminare;
	}
}
