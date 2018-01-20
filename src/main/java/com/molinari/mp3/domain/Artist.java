package com.molinari.mp3.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the ARTIST database table.
 * 
 */
@Entity
@Table(name="ARTIST")
@NamedQuery(name="Artist.findAll", query="SELECT a FROM Artist a")
public class Artist implements Serializable, com.molinari.utility.commands.beancommands.AbstractOggettoEntita, IArtist {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable=false)
	private Integer idArtist;
	
	@Column(nullable=false, length=2000000000)
	private String name;

	//bi-directional many-to-one association to Album
	@OneToMany(mappedBy="artist")
	private List<Album> albums;

	//bi-directional many-to-one association to Song
	@OneToMany(mappedBy="artist")
	private List<Song> songs;

	public Artist() {
		//do nothing
	}
	
	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IArtist#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IArtist#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IArtist#getAlbums()
	 */
	@Override
	public List<Album> getAlbums() {
		return this.albums;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IArtist#setAlbums(java.util.List)
	 */
	@Override
	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IArtist#addAlbum(com.molinari.mp3.domain.Album)
	 */
	@Override
	public IAlbum addAlbum(Album album) {
		getAlbums().add(album);
		album.setArtist(this);

		return album;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IArtist#removeAlbum(com.molinari.mp3.domain.IAlbum)
	 */
	@Override
	public IAlbum removeAlbum(IAlbum album) {
		getAlbums().remove(album);
		album.setArtist(null);

		return album;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IArtist#getSongs()
	 */
	@Override
	public List<Song> getSongs() {
		return this.songs;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IArtist#setSongs(java.util.List)
	 */
	@Override
	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IArtist#addSong(com.molinari.mp3.domain.Song)
	 */
	@Override
	public ISong addSong(Song song) {
		getSongs().add(song);
		song.setArtist(this);

		return song;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IArtist#removeSong(com.molinari.mp3.domain.Song)
	 */
	@Override
	public ISong removeSong(ISong song) {
		getSongs().remove(song);
		song.setArtist(null);

		return song;
	}

	@Override
	public String getIdEntita() {
		return Integer.toString(getIdArtist());
	}

	@Override
	public void setIdEntita(String idEntita) {
		this.setIdArtist(new Integer(idEntita));
	}

	@Override
	public String getNome() {
		return getName();
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IArtist#getIdArtist()
	 */
	@Override
	public Integer getIdArtist() {
		return idArtist;
	}

	/* (non-Javadoc)
	 * @see com.molinari.mp3.domain.IArtist#setIdArtist(java.lang.Integer)
	 */
	@Override
	public void setIdArtist(Integer idArtist) {
		this.idArtist = idArtist;
	}

}