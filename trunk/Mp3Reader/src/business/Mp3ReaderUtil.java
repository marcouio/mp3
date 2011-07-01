package business;

import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Mp3ReaderUtil {

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

	public static void scriviFileSuPiuRighe(final File file, final ArrayList<String> righe) {
		try {
			final BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for (final Iterator<String> iterator = righe.iterator(); iterator.hasNext();) {
				final String type = iterator.next();
				out.write(type);
				out.newLine();
			}
			out.close();
		} catch (final IOException e) {
		}
	}

	public static Document createDocument(final File xml) throws ParserConfigurationException, SAXException, IOException {
		final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		final Document doc = dBuilder.parse(xml);
		return doc;
	}
}
