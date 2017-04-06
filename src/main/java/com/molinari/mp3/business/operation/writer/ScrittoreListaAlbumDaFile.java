package com.molinari.mp3.business.operation.writer;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.check.CheckFile;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.operation.IOperazioni;
import com.molinari.mp3.business.operation.OperazioniBaseTagFile;
import com.molinari.utility.graphic.component.alert.Alert;

public class ScrittoreListaAlbumDaFile extends OperazioniBaseTagFile implements Scrittore {

	public ScrittoreListaAlbumDaFile() {
	}

	@Override
	protected void operazioneTagPresenti(final String pathFile2, final File f, final Tag tag) throws IOException {
		String riga = null;
		String artista = null;
		String album = null;
		if (tag != null) {
			artista = CheckFile.checkSingleTag(tag.getArtistaPrincipale());
			album = CheckFile.checkSingleTag(tag.getNomeAlbum());
			riga = artista.toUpperCase() + " - " + album.toUpperCase();
		}
		creaStringa(riga);
	}

	@Override
	protected void operazioneTagNonPresenti(final String pathFile2, final File f) {

	}

	private void creaStringa(final String riga) {
		boolean ok = true;
		if (riga != null) {
			for (int i = 0; i < righe.size(); i++) {
				final String r = righe.get(i);
				if (r.equals(riga)) {
					ok = false;
				}
			}
			if (ok) {
				righe.add(riga);
			}
		}
	}

	@Override
	protected void operazioneFinale() {
		Mp3ReaderUtil.scriviFileSuPiuRighe(file, righe);
	}

	@Override
	public void write(final String pathFileInput, final String pathFileOutput) {
		final File file = new File(pathFileOutput);
		this.setFile(file);

		try {
			if (scorriEdEseguiSuTuttiIFile(pathFileInput)) {
				Alert.info("Lista Creata", Alert.TITLE_OK);
			}
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		} catch (final SAXException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setTipoOperazione() {
		Controllore.setOperazione(IOperazioni.SCRIVI);
	}

}
