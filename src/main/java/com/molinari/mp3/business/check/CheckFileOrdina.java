package com.molinari.mp3.business.check;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.molinari.mp3.business.ConfiguratoreEstensioni;

public class CheckFileOrdina extends CheckFile {

	public static final String OPERAZIONE_ORDINA = "ordinare";

	ArrayList<String> estensioniPerOrdinatore = new ArrayList<String>();

	public CheckFileOrdina() {
		try {
			estensioniPerOrdinatore = ConfiguratoreEstensioni.getSingleton().getEstensioniPerOperazione(OPERAZIONE_ORDINA);
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		} catch (final SAXException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
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
