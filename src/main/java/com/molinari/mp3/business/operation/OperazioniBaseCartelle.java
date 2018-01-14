package com.molinari.mp3.business.operation;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.farng.mp3.TagException;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.objects.MyTagException;

public abstract class OperazioniBaseCartelle extends OperazioniBase {

	/**
	 * Questo metodo è solo una parte di codice ricorrente che scorre tutte le
	 * cartelle in profondità per tutti i file che raggiunge eseguo un metodo
	 * che va implementato a secondo dell'uso che se ne vuole fare
	 * 
	 * @param pathFile
	 * @return
	 */
	public boolean scorriEdEseguiSuTutteLeCartelle(String pathFile) {
		boolean ok = true;
		try {

			if (!pathFile.substring(pathFile.length() - 1, pathFile.length()).equals(Mp3ReaderUtil.slash())) {
				pathFile += Mp3ReaderUtil.slash();
			}
			final File dir = new File(pathFile);
			final String[] files = dir.list();

			for (int i = 0; i < files.length; i++) {
				final File f = new File(pathFile + files[i]);

				if (f.isDirectory() && f.canRead()) {
					try {
						operazioneDaEseguireSulFile(pathFile, f);
						cartelleDaScorrere.add(f.getAbsolutePath());
					} catch (final MyTagException e) {
						// tag non presente
						Controllore.log(Level.SEVERE, e.getMessage(), e);
					}
					// valorizzaTagGenerale(pathFile, f);
				}
			}
			if (cartelleDaScorrere != null && cartelleDaScorrere.size() > 0) {
				for (int i = 0; i < cartelleDaScorrere.size(); i++) {
					final String path = cartelleDaScorrere.get(i);
					cartelleDaScorrere.remove(i);
					scorriEdEseguiSuTutteLeCartelle(path);
				}
			}
			operazioneFinale();
		} catch (final IOException e) {
			// se il file non viene trovato o non è disponibile
			ok = false;
			Controllore.log(Level.SEVERE, e.getMessage(), e);
		}
		return ok;
	}

	protected abstract void operazioneFinale();

	/**
	 * metodo generico che prende il tag da un file e esegue delle operazioni su
	 * di esso tramite metodi astratti che vanno implementati nelle classi
	 * figlie a seconda dell'utilizzo che se ne vuole fare
	 * 
	 * @param pathFile
	 * @param f
	 * @throws IOException
	 * @throws TagException
	 */
	public void operazioniGenerica(final String pathFile, final File f) throws IOException, MyTagException {
		operazione(pathFile, f);
	}

	/**
	 * utilizzato per operazione su tag, nel caso di tag non presenti, specifica
	 * cosa fare
	 * 
	 * @param pathFile2
	 * @param f
	 */
	protected abstract void operazione(String pathFile2, File f);

	protected abstract void operazioneDaEseguireSulFile(final String pathFile, final File f) throws IOException, MyTagException;
	
}
