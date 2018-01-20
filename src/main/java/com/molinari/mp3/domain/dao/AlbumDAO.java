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

public class AlbumDAO implements IAlbum, IDAO<IAlbum>{

	private GenericDAO<IAlbum> dao;

	public AlbumDAO(IAlbum entita) {
		this.dao = new GenericDAO<>(entita);
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
	public IArtist getArtist() {
		return dao.getEntita().getArtist();
	}

	@Override
	public void setArtist(IArtist artist) {
		dao.getEntita().setArtist(artist);
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
	public Integer getIdAlbum() {
		return dao.getEntita().getIdAlbum();
	}

	@Override
	public void setIdAlbum(Integer idAlbum) {
		dao.getEntita().setIdAlbum(idAlbum);
	}

	@Override
	public IAlbum selectById(int id) {
		return dao.selectById(id);
	}

	@Override
	public List<IAlbum> selectAll() {
		return dao.selectAll();
	}

	@Override
	public boolean insert(IAlbum oggettoEntita) {
		return dao.insert(oggettoEntita);
	}

	@Override
	public boolean delete(int id) {
		return dao.delete(id);
	}

	public boolean update(Album oggettoEntita) {
		return dao.update(oggettoEntita);
	}

	@Override
	public boolean deleteAll() {
		return dao.deleteAll();
	}

	@Override
	public IAlbum getEntitaPadre() {
		return dao.getEntitaPadre();
	}

	@Override
	public List<IAlbum> selectWhere(List<Clausola> clausole, String appendToQuery) {
		return dao.selectWhere(clausole, appendToQuery);
	}

	public IAlbum getEntita() {
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
	public boolean update(IAlbum oggettoEntita) {
		return dao.update(oggettoEntita);
	}
}
