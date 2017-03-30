package main.java.com.molinari.mp3.business.operation.renamer;

import java.io.File;
import java.io.IOException;

import main.java.com.molinari.mp3.business.Controllore;
import main.java.com.molinari.mp3.business.check.CheckFile;
import main.java.com.molinari.mp3.business.objects.Tag;
import main.java.com.molinari.mp3.business.operation.Assegnatore;
import main.java.com.molinari.mp3.business.operation.IOperazioni;
import main.java.com.molinari.mp3.business.operation.OperazioniBaseTagFile;

public class Rinominatore extends OperazioniBaseTagFile {

	private void valorizzaTag(final String pathFile, final File f, final Tag tag) {
		final String artista = CheckFile.checkSingleTag(tag.getArtistaPrincipale());
		final String title = CheckFile.checkSingleTag(tag.getTitoloCanzone());
		if (title != null && artista != null && title != "" && artista != "") {
			try {
				rename(f, pathFile + artista + " - " + title + ".mp3");
			} catch (final IOException e) {
				e.printStackTrace();
			}
		} else {
			// TODO LOG
		}
	}

	@Override
	protected void operazioneTagNonPresenti(final String pathFile2, final File f) {
		final Assegnatore assegna = new Assegnatore(f, "-");
		assegna.save(f);
		// assegnaTagDaNome(f);

	}

	@Override
	protected void operazioneFinale() {
		// non fa nulla
	}

	@Override
	protected void operazioneTagPresenti(final String pathFile2, final File f, final Tag tag) throws IOException {
		valorizzaTag(pathFile2, f, tag);
	}

	@Override
	protected void setTipoOperazione() {
		Controllore.setOperazione(IOperazioni.RINOMINA);

	}
}
