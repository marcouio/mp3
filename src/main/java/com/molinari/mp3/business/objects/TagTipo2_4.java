package com.molinari.mp3.business.objects;

import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.ID3v2_4;

public class TagTipo2_4 extends ID3v2_4 implements Tag {

	public TagTipo2_4(final AbstractID3v2 id3v4Tag) {
		super((ID3v2_4) id3v4Tag);
	}

	@Override
	public String getArtistaPrincipale() {
		return getLeadArtist();
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
		setLeadArtist(artistaPrincipale);
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
		return getTrackNumberOnAlbum();
	}

	@Override
	public void setTraccia(String traccia) {
		setTrackNumberOnAlbum(traccia);
	}

	@Override
	public boolean hasTitleAndArtist() {
		return TagUtil.hasTitleAndArtist(this);
	}
}
