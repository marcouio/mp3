package com.molinari.mp3.domain;

import java.util.List;

import com.molinari.utility.commands.beancommands.AbstractOggettoEntita;

public interface IArtist extends AbstractOggettoEntita{

	String getName();

	void setName(String name);

	List<Album> getAlbums();

	void setAlbums(List<Album> albums);

	IAlbum addAlbum(Album album);

	IAlbum removeAlbum(IAlbum album);

	List<Song> getSongs();

	void setSongs(List<Song> songs);

	ISong addSong(Song song);

	ISong removeSong(ISong song);

	Integer getIdArtist();

	void setIdArtist(Integer idArtist);

}