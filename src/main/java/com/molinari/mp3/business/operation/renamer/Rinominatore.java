package com.molinari.mp3.business.operation.renamer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.farng.mp3.id3.ID3v2_4;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3Exception;
import com.molinari.mp3.business.acoustid.TagAudioTrack;
import com.molinari.mp3.business.lookup.LookUp;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagTipo2_4;
import com.molinari.mp3.business.operation.Assegnatore;
import com.molinari.mp3.business.operation.IOperazioni;
import com.molinari.mp3.business.operation.OperazioniBaseTagFile;

public class Rinominatore extends OperazioniBaseTagFile {
	
	private String key = "0B3qZnQc";
	private boolean forceFindTag = false;
	
	public Rinominatore(String key) {
		this.key = key;
	}

	private void valorizzaTag(final String pathFile, final File f, final Tag tag) {
		if(tag.hasTitleAndArtist() && !forceFindTag){
			safeRename(pathFile, f, tag);
		}else{
			logCallInternet(tag);
			
			Tag tagNew = findTag(f);
			if(tagNew != null){
				safeRename(pathFile, f, tagNew);
			}else{
				throw new Mp3Exception("Tag non trovato in rete per il file: " + f.getAbsolutePath());
			}
		}
	}

	public void logCallInternet(final Tag tag) {
		if(tag.hasTitleAndArtist() && forceFindTag){
			Controllore.getLog().info("-> Tag interno completo ma recupero i tag su internet");
		}else{
			Controllore.getLog().info("-> Tag interno non completo provo su internet");
		}
	}

	private void safeRename(final String pathFile, final File f, Tag tagNew) {
		if (tagNew.hasTitleAndArtist()) {
			try {
				rename(f, newName(pathFile, tagNew));
					
			} catch (final IOException e) {
				Controllore.getLog().log(Level.SEVERE, e.getMessage(), e);
			}
		} else {
			Controllore.getLog().info("Impossibile trovare tag per rinominare il file " + f.getName());
		}
	}

	public String newName(final String pathFile, Tag tagNew) {
		StringBuilder sb = new StringBuilder();
		sb.append(pathFile);
		sb.append(tagNew.getArtistaPrincipale());
		sb.append(" - ");
		sb.append(tagNew.getTitoloCanzone());
		sb.append(".mp3");
		return sb.toString();
	}

	private Tag findTag(final File f) {
		Tag result = null;
		LookUp lookUp = new LookUp(key);
		try {
			TagAudioTrack tagFromUrl = lookUp.lookup(f);
			Mp3 mp3 = new Mp3(f);
			result = mp3.getTag() != null ? mp3.getTag() : new TagTipo2_4(new ID3v2_4());
			if(tagFromUrl != null && tagFromUrl.getTrackName() != null && tagFromUrl.getArtist() != null){
				result.setTitoloCanzone(tagFromUrl.getTrackName());
				result.setArtistaPrincipale(tagFromUrl.getArtist());
				result.setNomeAlbum(tagFromUrl.getAlbum());
				mp3.setTag(result);
				mp3.save(f);
				
				Controllore.getLog().info("-> Memorizzazione tag");
			}else{
				Controllore.getLog().info("-> Tag da url non trovato o con informazioni mancanti");
			}
		} catch (Exception e) {
			Controllore.getLog().log(Level.SEVERE, "-> Eccezione durante la ricerca del tag", e);
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

	public boolean isForceFindTag() {
		return forceFindTag;
	}

	public void setForceFindTag(boolean forceFindTag) {
		this.forceFindTag = forceFindTag;
	}
}
