package com.molinari.mp3.domain.dao;

import java.util.List;

import com.molinari.mp3.domain.Album;
import com.molinari.mp3.domain.IAlbum;
import com.molinari.mp3.domain.IArtist;
import com.molinari.mp3.domain.ISong;
import com.molinari.mp3.domain.Song;
import com.molinari.utility.database.Clausola;
import com.molinari.utility.database.dao.GenericDAO;
import com.molinari.utility.database.dao.IDAO;

public class ArtistDAO implements IArtist, IDAO<IArtist>{

	private GenericDAO<IArtist> dao;

	public ArtistDAO(IArtist entita) {
		dao = new GenericDAO<>(entita);
	}
	
	@Override
	public IArtist selectById(int id) {
		return dao.selectById(id);
	}

	@Override
	public List<IArtist> selectAll() {
		return dao.selectAll();
	}

	@Override
	public boolean insert(IArtist oggettoEntita) {
		return dao.insert(oggettoEntita);
	}

	@Override
	public boolean equals(Object obj) {
		return dao.equals(obj);
	}

	@Override
	public boolean delete(int id) {
		return dao.delete(id);
	}

	@Override
	public boolean update(IArtist oggettoEntita) {
		return dao.update(oggettoEntita);
	}

	public void deleteObservers() {
		dao.deleteObservers();
	}

	@Override
	public boolean deleteAll() {
		return dao.deleteAll();
	}

	@Override
	public IArtist getEntitaPadre() {
		return dao.getEntitaPadre();
	}

	@Override
	public List<IArtist> selectWhere(List<Clausola> clausole, String appendToQuery) {
		return dao.selectWhere(clausole, appendToQuery);
	}

	public IArtist getEntita() {
		return dao.getEntita();
	}

	@Override
	public String getIdEntita() {
		return dao.getEntita().getIdEntita();
	}

	@Override
	public void setIdEntita(String idEntita) {
		dao.getEntita().setIdEntita(idEntita);
	}

	@Override
	public String getNome() {
		return dao.getEntita().getNome();
	}

	@Override
	public String getName() {
		return dao.getEntita().getName();
	}

	@Override
	public void setName(String name) {
		dao.getEntita().setName(name);
	}

	@Override
	public List<Album> getAlbums() {
		return dao.getEntita().getAlbums();
	}

	@Override
	public void setAlbums(List<Album> albums) {
		dao.getEntita().setAlbums(albums);
	}

	@Override
	public IAlbum addAlbum(Album album) {
		return dao.getEntita().addAlbum(album);
	}

	@Override
	public IAlbum removeAlbum(IAlbum album) {
		return dao.getEntita().removeAlbum(album);
	}

	@Override
	public List<Song> getSongs() {
		return dao.getEntita().getSongs();
	}

	@Override
	public void setSongs(List<Song> songs) {
		dao.getEntita().setSongs(songs);
	}

	@Override
	public ISong addSong(Song song) {
		return dao.getEntita().addSong(song);
	}

	@Override
	public ISong removeSong(ISong song) {
		return dao.getEntita().removeSong(song);
	}

	@Override
	public Integer getIdArtist() {
		return dao.getEntita().getIdArtist();
	}

	@Override
	public void setIdArtist(Integer idArtist) {
		dao.getEntita().setIdArtist(idArtist);
	}

	
}
