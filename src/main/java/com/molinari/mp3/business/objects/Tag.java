package com.molinari.mp3.business.objects;

public interface Tag {
	
	public String getArtistaPrincipale();

	public String getTitoloCanzone();

	public String getNomeAlbum();

	public String getGenere();

	public String getCommenti();

	public String getTraccia();

	void setArtistaPrincipale(String artistaPrincipale);

	void setTitoloCanzone(String titoloCanzone);

	void setNomeAlbum(String nomeAlbum);

	void setGenere(String genere);

	void setCommenti(String commenti);

	void setTraccia(String traccia);

	boolean hasTitleAndArtist();
}
