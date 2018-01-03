package com.molinari.mp3.business;

import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.molinari.utility.controller.ControlloreBase;

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
		String slash = "";
		final String os = System.getProperty("os.name");
		if (os.startsWith("Win")) {
			slash = "\\";
		} else {
			slash = "/";
		}
		return slash;
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

	public static ImageIcon resizeImage(final int nuovaLarghezza, final int nuovaAltezza, final ImageIcon imageicon) {
		final Image newimg = imageicon.getImage().getScaledInstance(nuovaLarghezza, nuovaAltezza, Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}

	public static void scriviFileSuPiuRighe(final File file, final List<String> righe) {
		try (final BufferedWriter out = new BufferedWriter(new FileWriter(file));){
			
			for (final Iterator<String> iterator = righe.iterator(); iterator.hasNext();) {
				final String type = iterator.next();
				out.write(type);
				out.newLine();
			}
		} catch (final IOException e) {
			ControlloreBase.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public static Document createDocument(final File xml) throws ParserConfigurationException, SAXException, IOException {
		final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder.parse(xml);
	}
}
