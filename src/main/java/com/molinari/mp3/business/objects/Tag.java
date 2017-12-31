package com.molinari.mp3.business.objects;

public interface Tag {
	
	String getArtistaPrincipale();

	String getTitoloCanzone();

	String getNomeAlbum();

	String getGenere();

	String getCommenti();

	String getTraccia();

	void setArtistaPrincipale(String artistaPrincipale);

	void setTitoloCanzone(String titoloCanzone);

	void setNomeAlbum(String nomeAlbum);

	void setGenere(String genere);

	void setCommenti(String commenti);

	void setTraccia(String traccia);

	boolean hasTitleAndArtist();
}
