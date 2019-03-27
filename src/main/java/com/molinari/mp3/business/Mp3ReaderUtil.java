package com.molinari.mp3.business;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.molinari.utility.io.UtilIo;

import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;

public class Mp3ReaderUtil {

	private Mp3ReaderUtil() {
		//do nothing
	}
	
	public static void convert(String source, String target) throws JavaLayerException{
		Converter c = new Converter();
		c.convert(source, target);
	}
	
	public static String slash() {
		return UtilIo.slash();
	}

	public static boolean noNull(final String[] lista) {
		for (int i = 0; i < lista.length; i++) {
			final String string = lista[i];
			if (string == null || string.equals("")) {
				return false;
			}
		}
		return true;
	}

	public static void scriviFileSuPiuRighe(final File file, final List<String> righe) {
		UtilIo.scriviFileSuPiuRighe(file, righe);
	}

	public static Document createDocument(final File xml) throws ParserConfigurationException, SAXException, IOException {
		final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder.parse(xml);
	}
}
