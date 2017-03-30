package com.molinari.mp3.business.operation.writer;

import java.io.File;
import java.io.IOException;

import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.objects.MyTagException;
import com.molinari.mp3.business.operation.OperazioniBaseCartelle;
import com.molinari.mp3.views.Alert;

public class ScrittoreListaAlbumDaCartelle extends OperazioniBaseCartelle implements Scrittore {

	public ScrittoreListaAlbumDaCartelle() {
	}

	@Override
	protected void operazioneFinale() {
		Mp3ReaderUtil.scriviFileSuPiuRighe(file, righe);
	}

	@Override
	protected void operazioneTagNonPresenti(final String pathFile2, final File f) {
		String artista = null;
		String album = null;
		if (f != null && pathFile2 != null) {
			final String nomeFile = f.getName();
			if (nomeFile.contains("-")) {
				final int trattino = nomeFile.indexOf("-");

				artista = nomeFile.substring(0, trattino).trim();
				album = nomeFile.substring(trattino + 1, nomeFile.length()).trim();
			}
			final String riga = artista + " - " + album;
			creaStringa(riga);
		}
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
	protected void operazioneDaEseguireSulFile(final String pathFile, final File f) throws IOException, MyTagException {
		operazioniGenerica(pathFile, f);
	}

	@Override
	public void write(final String pathFileInput, final String pathFileOutput) {
		final File file = new File(pathFileOutput);
		this.setFile(file);
		if (scorriEdEseguiSuTutteLeCartelle(pathFileInput)) {
			Alert.info("Lista Creata", Alert.TITLE_OK);
		}
	}
}
