package com.molinari.mp3.domain;

import java.util.List;

import com.molinari.utility.commands.beancommands.AbstractOggettoEntita;

public interface IAlbum extends AbstractOggettoEntita{

	String getName();

	void setName(String name);

	IArtist getArtist();

	void setArtist(IArtist artist);

	List<Song> getSongs();

	void setSongs(List<Song> songs);

	ISong addSong(Song song);

	ISong removeSong(ISong song);

	Integer getIdAlbum();

	void setIdAlbum(Integer idAlbum);

}