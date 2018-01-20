package com.molinari.mp3.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	private String comments;

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

	//bi-directional many-to-one association to Album
	@ManyToOne
	@JoinColumn(name="IDALBUM", referencedColumnName="IDALBUM", nullable=false)
	private IAlbum album;

	//bi-directional many-to-one association to Artist
	@ManyToOne
	@JoinColumn(name="IDARTIST", referencedColumnName="IDARTIST", nullable=false)
	private IArtist artist;

	public Song() {
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#getComments()
	 */
	@Override
	public String getComments() {
		return this.comments;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#setComments(java.lang.String)
	 */
	@Override
	public void setComments(String comments) {
		this.comments = comments;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#getGenre()
	 */
	@Override
	public String getGenre() {
		return this.genre;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#setGenre(java.lang.String)
	 */
	@Override
	public void setGenre(String genre) {
		this.genre = genre;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#getIdsong()
	 */
	@Override
	public Integer getIdsong() {
		return this.idsong;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#setIdsong(java.lang.Integer)
	 */
	@Override
	public void setIdsong(Integer idsong) {
		this.idsong = idsong;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#getKey()
	 */
	@Override
	public String getKey() {
		return this.key;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#setKey(java.lang.String)
	 */
	@Override
	public void setKey(String key) {
		this.key = key;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#getTracknumber()
	 */
	@Override
	public int getTracknumber() {
		return this.tracknumber;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#setTracknumber(int)
	 */
	@Override
	public void setTracknumber(int tracknumber) {
		this.tracknumber = tracknumber;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#getAlbum()
	 */
	@Override
	public IAlbum getAlbum() {
		return this.album;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#setAlbum(com.molinari.mp3.domain.IAlbum)
	 */
	@Override
	public void setAlbum(IAlbum album) {
		this.album = album;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#getArtist()
	 */
	@Override
	public IArtist getArtist() {
		return this.artist;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.ISong#setArtist(com.molinari.mp3.domain.IArtist)
	 */
	@Override
	public void setArtist(IArtist artist) {
		this.artist = artist;
	}

	@Override
	public String getIdEntita() {
		return Integer.toString(getIdsong());
	}

	@Override
	public void setIdEntita(String idEntita) {
		this.setIdsong(Integer.valueOf(idEntita));
	}

	@Override
	public String getNome() {
		return getName();
	}

}