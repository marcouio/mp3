package com.molinari.mp3.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the ALBUM database table.
 * 
 */
@Entity
@Table(name="ALBUM")
@NamedQuery(name="Album.findAll", query="SELECT a FROM Album a")
public class Album implements Serializable, com.molinari.utility.commands.beancommands.AbstractOggettoEntita, IAlbum {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable=false)
	private Integer idAlbum;
	
	@Column(nullable=false, length=2000000000)
	private String name;

	//bi-directional many-to-one association to Artist
	@ManyToOne
	@JoinColumn(name="IDARTIST", referencedColumnName="IDARTIST", nullable=false)
	private IArtist artist;

	//bi-directional many-to-one association to Song
	@OneToMany(mappedBy="album")
	private List<Song> songs;

	public Album() {
		//do nnothing
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IAlbum#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IAlbum#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IAlbum#getArtist()
	 */
	@Override
	public IArtist getArtist() {
		return this.artist;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IAlbum#setArtist(com.molinari.mp3.domain.Artist)
	 */
	@Override
	public void setArtist(IArtist artist) {
		this.artist = artist;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IAlbum#getSongs()
	 */
	@Override
	public List<Song> getSongs() {
		return this.songs;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IAlbum#setSongs(java.util.List)
	 */
	@Override
	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IAlbum#addSong(com.molinari.mp3.domain.Song)
	 */
	@Override
	public ISong addSong(Song song) {
		getSongs().add(song);
		song.setAlbum(this);

		return song;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IAlbum#removeSong(com.molinari.mp3.domain.Song)
	 */
	@Override
	public ISong removeSong(ISong song) {
		getSongs().remove(song);
		song.setAlbum(null);

		return song;
	}

	@Override
	public String getIdEntita() {
		return Integer.toString(getIdAlbum());
	}

	@Override
	public void setIdEntita(String idEntita) {
		this.setIdAlbum(Integer.valueOf(idEntita));
	}

	@Override
	public String getNome() {
		return getName();
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IAlbum#getIdAlbum()
	 */
	@Override
	public Integer getIdAlbum() {
		return idAlbum;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IAlbum#setIdAlbum(java.lang.Integer)
	 */
	@Override
	public void setIdAlbum(Integer idAlbum) {
		this.idAlbum = idAlbum;
	}

}