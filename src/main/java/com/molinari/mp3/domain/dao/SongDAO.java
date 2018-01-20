package com.molinari.mp3.domain.dao;

import java.util.List;

import com.molinari.mp3.domain.IAlbum;
import com.molinari.mp3.domain.IArtist;
import com.molinari.mp3.domain.ISong;
import com.molinari.mp3.domain.Song;
import com.molinari.utility.database.Clausola;
import com.molinari.utility.database.dao.GenericDAO;
import com.molinari.utility.database.dao.IDAO;

public class SongDAO implements ISong, IDAO<ISong>{

	private GenericDAO<ISong> dao;

	public SongDAO(ISong entita) {
		this.dao = new GenericDAO<>(entita);
	}
	
	@Override
	public String getComments() {
		return dao.getEntita().getComments();
	}

	@Override
	public void setComments(String comments) {
		dao.getEntita().setComments(comments);
	}

	@Override
	public String getGenre() {
		return dao.getEntita().getGenre();
	}

	@Override
	public void setGenre(String genre) {
		dao.getEntita().setGenre(genre);
	}

	@Override
	public Integer getIdsong() {
		return dao.getEntita().getIdsong();
	}

	@Override
	public void setIdsong(Integer idsong) {
		dao.getEntita().setIdsong(idsong);
	}

	@Override
	public String getKey() {
		return dao.getEntita().getKey();
	}

	@Override
	public void setKey(String key) {
		dao.getEntita().setKey(key);
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
	public int getTracknumber() {
		return dao.getEntita().getTracknumber();
	}

	@Override
	public void setTracknumber(int tracknumber) {
		dao.getEntita().setTracknumber(tracknumber);
	}

	@Override
	public IAlbum getAlbum() {
		return dao.getEntita().getAlbum();
	}

	@Override
	public void setAlbum(IAlbum album) {
		dao.getEntita().setAlbum(album);
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
	public ISong selectById(int id) {
		return dao.selectById(id);
	}

	@Override
	public List<ISong> selectAll() {
		return dao.selectAll();
	}

	public boolean insert(Song oggettoEntita) {
		return dao.insert(oggettoEntita);
	}

	@Override
	public boolean delete(int id) {
		return dao.delete(id);
	}

	public boolean update(Song oggettoEntita) {
		return dao.update(oggettoEntita);
	}

	@Override
	public boolean deleteAll() {
		return dao.deleteAll();
	}

	@Override
	public ISong getEntitaPadre() {
		return dao.getEntitaPadre();
	}

	@Override
	public List<ISong> selectWhere(List<Clausola> clausole, String appendToQuery) {
		return dao.selectWhere(clausole, appendToQuery);
	}

	public ISong getEntita() {
		return dao.getEntita();
	}

	@Override
	public boolean insert(ISong oggettoEntita) {
		return dao.insert(oggettoEntita);
	}

	@Override
	public boolean update(ISong oggettoEntita) {
		return dao.update(oggettoEntita);
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
}
