package business.operazioni.rinominatore;

import java.io.File;
import java.io.IOException;

import oggettiWrapper.Tag;
import business.Controllore;
import business.check.CheckFile;
import business.operazioni.Assegnatore;
import business.operazioni.IOperazioni;
import business.operazioni.OperazioniBaseTagFile;

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
