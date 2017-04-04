package com.molinari.mp3.business.operation.renamer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.LogFactory;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.acoustid.TagAudioTrack;
import com.molinari.mp3.business.check.CheckFile;
import com.molinari.mp3.business.lookup.LookUp;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.operation.Assegnatore;
import com.molinari.mp3.business.operation.IOperazioni;
import com.molinari.mp3.business.operation.OperazioniBaseTagFile;

public class Rinominatore extends OperazioniBaseTagFile {

	private void valorizzaTag(final String pathFile, final File f, final Tag tag) {
		String artista = CheckFile.checkSingleTag(tag.getArtistaPrincipale());
		String title = CheckFile.checkSingleTag(tag.getTitoloCanzone());
		if(title == null || artista == null || title == "" || artista == ""){
			
			LookUp lookUp = new LookUp("");
			try {
				TagAudioTrack tagFromUrl = lookUp.lookup(f);
				title = tagFromUrl.getTrackName();
				artista = tagFromUrl.getArtist();
			} catch (Exception e) {
				LogFactory.getLog("mp3").error(null, e);
			}
		}
		if (title != null && artista != null && title != "" && artista != "") {
			try {
				rename(f, pathFile + artista + " - " + title + ".mp3");
			} catch (final IOException e) {
				LogFactory.getLog("mp3").error(null, e);
			}
		} else {
			LogFactory.getLog("mp3").info("Impossibile trovare tag per rinominare il file " + file.getName());
		}
	}

	@Override
	protected void operazioneTagNonPresenti(final String pathFile2, final File f) {
		final Assegnatore assegna = new Assegnatore(f, "-");
		assegna.save(f);

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
