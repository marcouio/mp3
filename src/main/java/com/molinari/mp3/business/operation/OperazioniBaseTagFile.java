package com.molinari.mp3.business.operation;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.farng.mp3.TagException;
import org.xml.sax.SAXException;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.check.CheckFile;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.MyTagException;
import com.molinari.mp3.business.objects.Tag;

public abstract class OperazioniBaseTagFile extends OperazioniBase {

	protected CheckFile checkFile;

	public OperazioniBaseTagFile() {
		initCheckFile();
	}

	protected void initCheckFile() {
		checkFile = new CheckFile();
	}

	/**
	 * Questo metodo è solo una parte di codice ricorrente che scorre tutte le
	 * cartelle in profondità per tutti i file che raggiunge eseguo un metodo
	 * che va implementato a secondo dell'uso che se ne vuole fare
	 * 
	 * @param pathFile
	 * @return
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public boolean scorriEdEseguiSuTuttiIFile(String pathFile) throws ParserConfigurationException, SAXException {
		setTipoOperazione();
		boolean ok = true;
		try {

			if (!pathFile.substring(pathFile.length() - 1, pathFile.length()).equals(Mp3ReaderUtil.slash())) {
				pathFile += Mp3ReaderUtil.slash();
			}
			final File dir = new File(pathFile);
			final String[] files = dir.list();

			for (int i = 0; i < files.length; i++) {
				final File f = new File(pathFile + files[i]);

				if (checkFile(f, true)) {
					if (f.isFile() && f.canRead()) {
						try {
							operazioneDaEseguireSulFile(pathFile, f);
						} catch (final MyTagException e) {
							// tag non presente
							e.printStackTrace();
						}
					} else if (f.isDirectory()) {
						cartelleDaScorrere.add(f.getAbsolutePath());
					}
				}
			}
			if (cartelleDaScorrere != null && cartelleDaScorrere.size() > 0) {
				for (int i = 0; i < cartelleDaScorrere.size(); i++) {
					final String path = cartelleDaScorrere.get(i);
					cartelleDaScorrere.remove(i);
					scorriEdEseguiSuTuttiIFile(path);
				}
			}
			operazioneFinale();
			Controllore.setOperazione(IOperazioni.DEFAULT);
		} catch (final IOException e) {
			// se il file non viene trovato o non è disponibile
			ok = false;
			e.printStackTrace();
		}
		return ok;
	}

	private boolean checkFile(final File f, final boolean b) {
		return checkFile.checkFile(f, b);
	}

	protected abstract void setTipoOperazione();

	protected abstract void operazioneFinale();

	/**
	 * Metodo generico che prende il tag da un file ed esegue delle operazioni
	 * su di esso tramite metodi astratti che vanno implementati nelle classi
	 * figlie a seconda dell'utilizzo che se ne vuole fare
	 * 
	 * @param pathFile
	 * @param f
	 * @throws IOException
	 * @throws TagException
	 */
	public void operazioniGenerica(final String pathFile, final File f) throws IOException, Exception {
		final Mp3 file = new Mp3(f);
		final Tag tag = file.getTag();
		if (tag != null) {
			operazioneTagPresenti(pathFile, f, tag);
		} else {
			operazioneTagNonPresenti(pathFile, f);
		}
	}

	/**
	 * utilizzato per operazione su tag, nel caso di tag non presenti, specifica
	 * cosa fare da implementare nelle sottoclassi
	 * 
	 * @param pathFile2
	 * @param f
	 */
	protected abstract void operazioneTagNonPresenti(String pathFile2, File f);

	/**
	 * utilizzato per operazione su tag, nel caso di tag non presenti, specifica
	 * cosa fare da implementare nelle sottoclassi
	 * 
	 * @param pathFile2
	 * @param f
	 */
	protected abstract void operazioneTagPresenti(String pathFile2, File f, Tag tag) throws IOException;

	protected void operazioneDaEseguireSulFile(final String pathFile, final File f) throws IOException, MyTagException {
		try {
			operazioniGenerica(pathFile, f);
		} catch (final Exception e) {
			System.out.println("Mi manda in negative offset il file: " + f.getAbsolutePath());
			e.printStackTrace();
		}
	}

}
