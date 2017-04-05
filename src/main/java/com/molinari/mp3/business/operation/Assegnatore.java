package com.molinari.mp3.business.operation;

import java.io.File;
import java.io.IOException;

import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.MyTagException;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagTipo1;

public class Assegnatore {

	private Tag tag;
	private Mp3 file;

	public Assegnatore(final File f, final String regex) {
		final String[] info = f.getName().split(regex);
		try {
			dispatch(f, info, regex);
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final MyTagException e) {
			e.printStackTrace();
		}
	}

	private void dispatch(final File f, final String[] info, final String regex) throws IOException, MyTagException {
		final String nome = f.getName();
		Mp3 file = null;
		try {
			file = new Mp3(f);
		} catch (final Exception e1) {
			e1.printStackTrace();
			throw new MyTagException();
		}

		final Tag tag = file.getTag() == null ? new TagTipo1() : file.getTag();


		// verifico che il nome contiene il regex e il punto dell'estensione
		if (nome != null && nome.contains(regex) && nome.contains(".")) {

			// cerco l'indice del punto dell'estensione
			final int punto = info[info.length - 1].lastIndexOf(".");

			boolean hasNumTrack = hasNumberTrack(info);
			if(hasNumTrack){

				//la prima è la traccia e ha tre informazioni
				if (info.length == 3) {
					setTagConTreInfo(info, tag, punto);
		        // se la prima è la traccia e ha due informazioni
				} else if (info.length == 2) {
					setTagConDueInfoNTraccia(info, tag, punto);
				}
			}
		
			// la prima non è traccia e la seconda non ha info
			if (info.length == 2) {
				setTagConDueInfoAlbumSong(info, tag, punto);
			}

			if(tag.getArtistaPrincipale() != null && !tag.getArtistaPrincipale().equals("") &&
			   tag.getTitoloCanzone() != null && !tag.getTitoloCanzone().equals("")){
				this.setTag(tag);
			}
		}else if(nome != null && !nome.contains(regex) && nome.contains(".")){
			
			// cerco l'indice del punto dell'estensione
			final int punto = info[info.length - 1].lastIndexOf(".");
			String traccia = info[0].substring(0, punto);
			tag.setTitoloCanzone(traccia);
		}
		file.setTag(tag);
		file.save(f);
		this.setFile(file);

	}

	private boolean hasNumberTrack(final String[] info) {
		try{
			// se la prima info è lunga due potrebbe essere il numero della traccia sull'album
			if (info[0].length() == 2) {
	
				// provo a castare la prima info in integer
				Integer.parseInt(info[0]);
				return true;
			}
		}catch (Exception e) {
		}
		return false;
	}

	public void save(final File f) {
		this.getFile().save(f);
	}

	private void setFile(final Mp3 file) {
		this.file = file;
	}

	private void setTagConDueInfoAlbumSong(final String[] info, final Tag tag, final int punto) {
		String artista;
		String song;
		artista = info[0];
		song = info[1].substring(0, punto);

		if (Mp3ReaderUtil.noNull(new String[] { artista, song })) {
			tag.setArtistaPrincipale(artista);
			tag.setTitoloCanzone(song);
		}
	}

	private void setTagConDueInfoNTraccia(final String[] info, final Tag tag, final int punto) {
		String traccia;
		String song;
		traccia = info[0];
		song = info[1].substring(0, punto);

		if (Mp3ReaderUtil.noNull(new String[] { traccia, song })) {
			tag.setTraccia(traccia);
			tag.setTitoloCanzone(song);
		}
	}

	private void setTagConTreInfo(final String[] info, final Tag tag, final int punto) {
		String traccia;
		String artista;
		String song;
		traccia = info[0];
		artista = info[1];
		song = info[2].substring(0, punto);

		if (Mp3ReaderUtil.noNull(new String[] { traccia, artista, song })) {
			tag.setTraccia(traccia);
			tag.setArtistaPrincipale(artista);
			tag.setTitoloCanzone(song);
		}
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(final Tag tag) {
		this.tag = tag;
	}

	public Mp3 getFile() {
		return file;
	}
}
