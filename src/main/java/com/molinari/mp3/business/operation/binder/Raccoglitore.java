package com.molinari.mp3.business.operation.binder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.check.CheckFile;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagTipo1;
import com.molinari.mp3.business.operation.IOperazioni;
import com.molinari.mp3.business.operation.OperazioniBaseTagFile;
import com.molinari.mp3.business.player.MyBasicPlayer;
import com.molinari.mp3.views.NewPlayList;
import com.molinari.utility.controller.ControlloreBase;

public class Raccoglitore extends OperazioniBaseTagFile implements IRaccoglitore {
	
	List<Mp3File>  listaFile = new ArrayList<>();
	
	private Mp3File[][] canzoni;

	public Raccoglitore() {
		//do nothing
	}
	
	public class Mp3File{
		private String nome;
		private String path;
		
		public String getNome() {
			return nome;
		}
		public void setNome(String nome) {
			this.nome = nome;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		
		@Override
		public String toString() {
			return nome;
		}
	}

	@Override
	protected void operazioneFinale() {
		costruisciMatrice();
	}

	private Mp3File[][] costruisciMatrice() {
		final Mp3File[][] dati = new Mp3File[listaFile.size()][1];
		
		
		MyBasicPlayer.setSize(listaFile.size());
		for (int i = 0; i < listaFile.size(); i++) {
			final Mp3File mp3 = listaFile.get(i);
			dati[i][0] = mp3;
			NewPlayList.getSingleton().put(Integer.toString(i), mp3);
		}
		setCanzoni(dati);
		return dati;
	}

	@Override
	protected void operazioneTagNonPresenti(final String pathFile2, final File f) {
		final Tag t = new TagTipo1();
		t.setTitoloCanzone(f.getName());
		try {
			final Mp3 mp3 = new Mp3(f);
			mp3.setTag(t);
			Mp3File e = new Mp3File();
			addMp3ToList(f, mp3, e);
		} catch (final Exception e) {
			ControlloreBase.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	protected void operazioneTagPresenti(final String pathFile2, final File f, final Tag tag)
	    throws IOException {
		Mp3 mp3;
		try {
			mp3 = new Mp3(f);
			mp3.setTag(tag);
			Mp3File mp3File = new Mp3File();
			addMp3ToList(f, mp3, mp3File);
		} catch (final Exception e) {
			ControlloreBase.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void addMp3ToList(final File f, Mp3 mp3, Mp3File mp3File) {
		mp3File.setNome(mp3.toString());
		mp3File.setPath(f.getAbsolutePath());
		listaFile.add(mp3File);
	}

	public void setCanzoni(final Mp3File[][] canzoni) {
		this.canzoni = canzoni;
	}

	public Mp3File[][] getCanzoni() {
		return canzoni;
	}

	@Override
	public void raccogli(final String pathFileInput) {
		try {
			scorriEdEseguiSuTuttiIFile(pathFileInput);
		} catch (ParserConfigurationException | SAXException e) {
			ControlloreBase.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	protected void setTipoOperazione() {
		Controllore.setOperazione(IOperazioni.RACCOGLI);
	}

	@Override
	protected void initCheckFile() {
		checkFile = new CheckFile();
	}

}
