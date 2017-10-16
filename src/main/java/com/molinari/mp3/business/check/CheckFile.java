package com.molinari.mp3.business.check;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.molinari.mp3.business.objects.Mp3;

public class CheckFile {

	public static final String ESTENSIONE_MP3 = ".mp3";

	public CheckFile() {
	}

	public boolean checkFile(final File f, final boolean correggi) {
		final String estensione = ESTENSIONE_MP3;
		if (correggi && !f.isDirectory()) {
			return checkEcorreggi(estensione, f);
		} else {
			return check(estensione, f);
		}
	}

	public static boolean checkCartelle(final String input2, final String output2) {
		final File input = new File(input2);
		final File output = new File(output2);
		if (!input.exists() || !input.isDirectory()) {
			return false;
		}
		if (output.exists() && !output.isDirectory()) {
			return false;
		} else if (!output.exists()) {
			if (!output.mkdir()) {
				return false;
			}
		}
		return true;
	}

	public boolean checkCorreggiERiposiziona(final String estensione, final File file, final String pathFile) throws IOException {
		Boolean ok = new Boolean(true);
		try {
			ok = correggi(estensione, file);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return ok;
	}

	public boolean checkEcorreggi(final String estensione, final File file) {
		Boolean ok = new Boolean(true);
		if (file.isFile()) {
			try {
				ok = correggi(estensione, file);
			} catch (final IOException e) {
				e.printStackTrace();
			} catch (final ParserConfigurationException e) {
				e.printStackTrace();
			} catch (final SAXException e) {
				e.printStackTrace();
			}
		} else {
			ok = true;
		}
		return ok;
	}

	private boolean correggi(final String estensione, final File file) throws IOException, ParserConfigurationException, SAXException {
		boolean checkEstensione = true;
		boolean checkAssenzaParentesi = true;
		final String cartella = Mp3.getCartella(file);
		if (checkLunghezzaNome(estensione, file)) {
			if (!checkEstensione(estensione, file)) {
				checkEstensione = false;

				/*
				 * da scommentare se si vuole attivare il rinomina su file con
				 * estensione errata
				 * 
				 * final ConfiguratoreEstensioni config =
				 * ConfiguratoreEstensioni.getSingleton(); ok = rename(file,
				 * cartella + file.getName() +
				 * config.estensioneAccettate.get(0));
				 */
			}
			if (!checkAssenzaParentesi(estensione, file)) {
				String senzaParentesi = "";
				senzaParentesi = file.getName().replace("(", "");
				senzaParentesi = senzaParentesi.replace(")", "");
				checkAssenzaParentesi = rename(file, cartella + senzaParentesi);
			}
		} else {
			return false;
		}
		return checkEstensione && checkAssenzaParentesi;
	}

	public boolean check(final String estensione, final File file) {
		return file.exists() && file.isFile() ? checkEstensione(estensione, file) && checkLunghezzaNome(estensione, file) && checkAssenzaParentesi(estensione, file) : true;
	}

	public boolean checkLunghezzaNome(final String estensione, final File file) {
		boolean ok = true;
		final String nomeFile = file.getName();
		if (nomeFile.length() < estensione.length()) {
			ok = false;
		}
		return ok;
	}

	public boolean checkEstensione(final String estensione, final File file) {
		if (file.getName().toLowerCase().endsWith(estensione)) {
			return true;
		}
		return false;

	}

	public boolean checkAssenzaParentesi(final String estensione, final File file) {
		boolean ok = true;
		if (file.getName().contains("(") || file.getName().contains(")")) {
			ok = false;
		}
		return ok;
	}

	public static String checkSingleTag(final String tag) {
		final String nuovoTag = tag.replaceAll("�", "");
		return nuovoTag;

	}

	protected static boolean rename(final File mp3, final String nome_dopo) throws IOException {
		final File file2 = new File(nome_dopo);
		final boolean success = mp3.renameTo(file2);
		return success;
	}
}
