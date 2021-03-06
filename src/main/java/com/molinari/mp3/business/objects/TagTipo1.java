package com.molinari.mp3.business.objects;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;

import org.farng.mp3.AbstractMP3Tag;
import org.farng.mp3.TagNotFoundException;
import org.farng.mp3.id3.ID3v1;

import com.molinari.mp3.business.Controllore;

public class TagTipo1 extends ID3v1 implements Tag {

	
	public TagTipo1() {
		super();

	}

	public TagTipo1(final AbstractMP3Tag id3) {
		super(id3);
	}

	public TagTipo1(final ID3v1 id3v1) {
		super(id3v1);
	}

	public TagTipo1(final RandomAccessFile raf) throws TagNotFoundException, IOException {
		super(raf);
	}

	public TagTipo1(final TagTipo1 id3v1Tag) {
		super(id3v1Tag);
	}

	@Override
	public String getArtistaPrincipale() {
		return getArtist();
	}

	@Override
	public String getTitoloCanzone() {
		return getSongTitle();
	}

	@Override
	public String getNomeAlbum() {
		return getAlbumTitle();
	}

	@Override
	public String getGenere() {
		return getSongGenre();
	}

	@Override
	public String getCommenti() {
		return getSongComment();
	}

	@Override
	public void setArtistaPrincipale(final String artistaPrincipale) {
		setArtist(artistaPrincipale);
	}

	@Override
	public void setTitoloCanzone(final String titoloCanzone) {
		setSongTitle(titoloCanzone);
	}

	@Override
	public void setNomeAlbum(final String nomeAlbum) {
		setAlbumTitle(nomeAlbum);
	}

	@Override
	public void setGenere(final String genere) {
		setSongGenre(genere);
	}

	@Override
	public void setCommenti(final String commenti) {
		setSongComment(commenti);
	}

	@Override
	public String getTraccia() {
		try{
			return getTrackNumberOnAlbum();
		}catch (Exception e) {
			Controllore.getLog().log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	@Override
	public void setTraccia(final String traccia) {
		try{
			setTrackNumberOnAlbum(traccia);
		}catch (Exception e) {
			Controllore.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
		
	}

	@Override
	public boolean hasTitleAndArtist() {
		return TagUtil.hasTitleAndArtist(this);
	}
}
