package com.molinari.mp3.business.check;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.molinari.mp3.business.ConfiguratoreEstensioni;
import com.molinari.mp3.business.Controllore;

public class CheckFileOrdina extends CheckFile {

	public static final String OPERAZIONE_ORDINA = "ordinare";

	List<String> estensioniPerOrdinatore = new ArrayList<String>();

	public CheckFileOrdina() {
		try {
			estensioniPerOrdinatore = ConfiguratoreEstensioni.getSingleton().getEstensioniPerOperazione(OPERAZIONE_ORDINA);
		} catch (final ParserConfigurationException e) {
			Controllore.log(Level.SEVERE, e.getMessage(), e);
		} catch (final SAXException e) {
			Controllore.log(Level.SEVERE, e.getMessage(), e);
		} catch (final IOException e) {
			Controllore.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public boolean checkEstensione(final String estensione, final File file) {
		if (file.getName().endsWith(estensione)) {
			return true;
		}
		for (int i = 0; i < estensioniPerOrdinatore.size(); i++) {
			final String est = estensioniPerOrdinatore.get(i);
			if (file.getName().endsWith(est)) {
				return true;
			}
		}
		return false;
	}
}
