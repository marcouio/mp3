package com.molinari.mp3.domain;

import com.molinari.utility.commands.beancommands.AbstractOggettoEntita;

public interface ISong extends AbstractOggettoEntita{

	String getComments();

	void setComments(String comments);

	String getGenre();

	void setGenre(String genre);

	Integer getIdsong();

	void setIdsong(Integer idsong);

	String getKey();

	void setKey(String key);

	String getName();

	void setName(String name);

	int getTracknumber();

	void setTracknumber(int tracknumber);

	IAlbum getAlbum();

	void setAlbum(IAlbum album);

	IArtist getArtist();

	void setArtist(IArtist artist);

}