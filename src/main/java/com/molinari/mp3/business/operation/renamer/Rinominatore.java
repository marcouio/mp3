package com.molinari.mp3.business.operation.renamer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3Exception;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.operation.Assegnatore;
import com.molinari.mp3.business.operation.IOperazioni;
import com.molinari.mp3.business.operation.KeyHolder;
import com.molinari.mp3.business.operation.OperazioniBaseTagFile;

public class Rinominatore extends OperazioniBaseTagFile {
		
	public Rinominatore() {
		//do nothing
	}
	
	public Rinominatore(String key) {
		KeyHolder.getSingleton().setKey(key);
	}

	private void valorizzaTag(final String pathFile, final File f, final Tag tag) {
		if(tag.hasTitleAndArtist() && !isForceFindTag()){
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

	private void logCallInternet(final Tag tag) {
		if(tag.hasTitleAndArtist() && isForceFindTag()){
			Controllore.getLog().info("-> Tag interno completo ma recupero i tag su internet");
		}else{
			Controllore.getLog().info("-> Tag interno non completo provo su internet");
		}
	}

	private void safeRename(final String pathFile, final File f, Tag tagNew) {
		String newName = null;
		if (tagNew.hasTitleAndArtist()) {
			try {
				newName = newName(pathFile, tagNew);
				rename(f, newName);
					
			} catch (final IOException e) {
				Controllore.getLog().log(Level.SEVERE, e.getMessage(), e);
			}
		} else {
			Controllore.getLog().info("Impossibile trovare tag per rinominare il file " + f.getName());
		}
		if(newName != null){
			try {
				java.nio.file.Files.deleteIfExists(new File(newName.replaceAll(".mp3", ".original.mp3")).toPath());
				if(f.getAbsolutePath().contains(".mp3")){
					java.nio.file.Files.deleteIfExists(new File(f.getAbsolutePath().replaceAll(".mp3", ".original.mp3")).toPath());
				}
				if(f.getAbsolutePath().contains(".MP3")){
					java.nio.file.Files.deleteIfExists(new File(f.getAbsolutePath().replaceAll(".MP3", ".original.MP3")).toPath());
				}
			} catch (IOException e) {
				Controllore.getLog().log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
		

	public String newName(final String pathFile, Tag tagNew) {
		StringBuilder sb = new StringBuilder();
		sb.append(pathFile);
		sb.append(adjust(tagNew.getArtistaPrincipale()));
		sb.append(" - ");
		sb.append(adjust(tagNew.getTitoloCanzone()));
		sb.append(".mp3");
		return sb.toString();
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
