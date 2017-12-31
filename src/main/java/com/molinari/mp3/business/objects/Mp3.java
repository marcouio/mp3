package com.molinari.mp3.business.objects;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.farng.mp3.AbstractMP3Tag;
import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.ID3v2_2;
import org.farng.mp3.id3.ID3v2_3;
import org.farng.mp3.id3.ID3v2_4;

import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.operation.OperazioniBase;
import com.molinari.utility.controller.ControlloreBase;

public class Mp3 extends MP3File {

	private File file;
	private Tag tag;
	private String nome;

	public Mp3() {
		super();
	}

	public Mp3(final File file) throws IOException, TagException {
		super(file);
		init(file);
	}

	public Mp3(final MP3File mp3) {
		super(mp3);
		init(mp3.getMp3file());
	}

	public Mp3(final String filename) throws IOException, TagException {
		super(filename);
		init(new File(filename));
	}

	public Mp3(final File file, final boolean parse) throws IOException, TagException {
		super(file, parse);
		init(file);

	}

	private void init(final File file) {
		setFile(file);
		initTag(this);
		setNome(this.getMp3file().getName());
	}

	/**
	 * Inizializza il tag interno alla classe verificando quale tipo di tag
	 * contiene all'interno
	 * 
	 * @param mp3
	 */
	private void initTag(final Mp3 mp3) {
		if (mp3.getID3v1Tag() != null) {
			final TagTipo1 tagv1 = new TagTipo1(mp3.getID3v1Tag());
			setTag(tagv1);
		} else if (mp3.getID3v2Tag() != null && mp3.getID3v2Tag() instanceof ID3v2_4) {
			final TagTipo2_4 tagv2 = new TagTipo2_4(mp3.getID3v2Tag());
			setTag(tagv2);
		} else if (mp3.getID3v2Tag() != null && mp3.getID3v2Tag() instanceof ID3v2_3) {
			final TagTipo2_3 tagv2 = new TagTipo2_3(mp3.getID3v2Tag());
			setTag(tagv2);
		} else if (mp3.getID3v2Tag() != null && mp3.getID3v2Tag() instanceof ID3v2_2) {
			final TagTipo2_2 tagv2 = new TagTipo2_2(mp3.getID3v2Tag());
			setTag(tagv2);
		} else {
			setTag(new TagTipo2_4(new ID3v2_4()));
		}
	}

	@Override
	public void save(final File f) {
		try {
			super.save(f);
		} catch (Exception e) {
			ControlloreBase.getLog().log(Level.SEVERE, e.getMessage(), e);
		}

	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public File getMp3file() {
		return this.file;
	}

	public void rename(final String nome_dopo) throws IOException {
		OperazioniBase.rename(this.getMp3file(), nome_dopo);
	}

	/**
	 * Estrae da un file la stringa contenente il path assoluto
	 * 
	 * @param f
	 * @return
	 */
	public static String getCartella(final File f) {
		final String pathAssoluto = f.getAbsolutePath();
		return pathAssoluto.substring(0, (pathAssoluto.lastIndexOf(Mp3ReaderUtil.slash()) + 1));
	}

	public String getCartella() {
		final String pathAssoluto = this.getMp3file().getAbsolutePath();
		return pathAssoluto.substring(0, pathAssoluto.lastIndexOf(Mp3ReaderUtil.slash()));
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(final Tag tag) {
		if(tag instanceof TagTipo1){
			setID3v1Tag((AbstractMP3Tag) tag);
		}else {
			setID3v2Tag((AbstractMP3Tag) tag);
		}
		
		this.tag = tag;
	}

	public void setFile(final File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		if (tag != null) {
			if (Mp3ReaderUtil.noNull(new String[] { tag.getNomeAlbum(), tag.getArtistaPrincipale(), tag.getTitoloCanzone() })) {
				return tag.getArtistaPrincipale() + " - " + tag.getTitoloCanzone() + " - " + tag.getNomeAlbum();
			} else if (Mp3ReaderUtil.noNull(new String[] { tag.getTitoloCanzone() })) {
				return tag.getTitoloCanzone();
			} else {
				return this.getMp3file().getName();
			}
		} else {
			return this.getMp3file().getName();
		}
	}
}
