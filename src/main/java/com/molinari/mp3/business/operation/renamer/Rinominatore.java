package com.molinari.mp3.business.operation.renamer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.LogFactory;
import org.farng.mp3.id3.ID3v2_4;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.acoustid.TagAudioTrack;
import com.molinari.mp3.business.lookup.LookUp;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagTipo2_4;
import com.molinari.mp3.business.operation.Assegnatore;
import com.molinari.mp3.business.operation.IOperazioni;
import com.molinari.mp3.business.operation.OperazioniBaseTagFile;

public class Rinominatore extends OperazioniBaseTagFile {
	
	String key = "0B3qZnQc";
	
	public Rinominatore(String key) {
		this.key = key;
	}

	private void valorizzaTag(final String pathFile, final File f, final Tag tag) {
		Tag tagNew = findTag(f);
	
		safeRename(pathFile, f, tagNew);
	}

	private void safeRename(final String pathFile, final File f, Tag tagNew) {
		if (tagNew.getArtistaPrincipale() != null && tagNew.getTitoloCanzone() != null) {
			try {
				rename(f, pathFile + tagNew.getArtistaPrincipale() + " - " + tagNew.getTitoloCanzone() + ".mp3");
					
			} catch (final IOException e) {
				LogFactory.getLog("mp3").error(null, e);
			}
		} else {
			LogFactory.getLog("mp3").info("Impossibile trovare tag per rinominare il file " + file.getName());
		}
	}

	private Tag findTag(final File f) {
		Tag result = null;
		LookUp lookUp = new LookUp(key);
		try {
			TagAudioTrack tagFromUrl = lookUp.lookup(f);
			Mp3 mp3 = new Mp3(f);
			result = mp3.getTag() != null ? mp3.getTag() : new TagTipo2_4(new ID3v2_4());
			result.setTitoloCanzone(tagFromUrl.getTrackName());
			result.setArtistaPrincipale(tagFromUrl.getArtist());
			result.setNomeAlbum(tagFromUrl.getAlbum());
			mp3.setTag(result);
			mp3.save(f);
		} catch (Exception e) {
			LogFactory.getLog("mp3").error(null, e);
		}
		return result;
	}

	@Override
	protected void operazioneTagNonPresenti(final String pathFile2, final File f) {
		Tag tag = findTag(f);
		if(tag != null){
			safeRename(pathFile2, f, tag);
		}
		else{
			final Assegnatore assegna = new Assegnatore(f, "-");
			assegna.save(f);
		}
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
