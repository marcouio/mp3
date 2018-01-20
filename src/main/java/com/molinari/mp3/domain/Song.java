package com.molinari.mp3.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the SONG database table.
 * 
 */
@Entity
@Table(name="SONG")
@NamedQuery(name="Song.findAll", query="SELECT s FROM Song s")
public class Song implements Serializable, com.molinari.utility.commands.beancommands.AbstractOggettoEntita, ISong {
	private static final long serialVersionUID = 1L;

	@Column(nullable=false, length=2000000000)
	private String album;

	@Column(nullable=false, length=2000000000)
	private String artist;

	@Column(nullable=false, length=2000000000)
	private String comments;

	@Column(nullable=false)
	private int fpduration;

	@Column(nullable=false, length=2000000000)
	private String genre;

	@Id
	@Column(nullable=false)
	private Integer idsong;

	@Column(nullable=false, length=2000000000)
	private String key;

	@Column(nullable=false, length=2000000000)
	private String name;

	@Column(nullable=false)
	private int tracknumber;

	public Song() {
		//do nothing
	}

	@Override
	public String getAlbum() {
		return this.album;
	}

	@Override
	public void setAlbum(String album) {
		this.album = album;
	}

	@Override
	public String getArtist() {
		return this.artist;
	}

	@Override
	public void setArtist(String artist) {
		this.artist = artist;
	}

	@Override
	public String getComments() {
		return this.comments;
	}

	@Override
	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public int getFpduration() {
		return this.fpduration;
	}

	@Override
	public void setFpduration(int fpduration) {
		this.fpduration = fpduration;
	}

	@Override
	public String getGenre() {
		return this.genre;
	}

	@Override
	public void setGenre(String genre) {
		this.genre = genre;
	}

	@Override
	public Integer getIdsong() {
		return this.idsong;
	}

	@Override
	public void setIdsong(Integer idsong) {
		this.idsong = idsong;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getTracknumber() {
		return this.tracknumber;
	}

	@Override
	public void setTracknumber(int tracknumber) {
		this.tracknumber = tracknumber;
	}

	@Override
	public String getIdEntita() {
		return Integer.toString(getIdsong());
	}

	@Override
	public void setIdEntita(String idEntita) {
		setIdsong(Integer.valueOf(idEntita));
	}

	@Override
	public String getNome() {
		return getName();
	}

}