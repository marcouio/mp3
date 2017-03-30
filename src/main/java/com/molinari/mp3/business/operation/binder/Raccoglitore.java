package com.molinari.mp3.business.operation.binder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

public class Raccoglitore extends OperazioniBaseTagFile implements IRaccoglitore {

	public Raccoglitore() {}

	ArrayList<Mp3>  listaFile = new ArrayList<Mp3>();

	private Mp3[][] canzoni;

	@Override
	protected void operazioneFinale() {
		costruisciMatrice();
	}

	private Mp3[][] costruisciMatrice() {
		final Mp3[][] dati = new Mp3[listaFile.size()][1];
		MyBasicPlayer.setSize(listaFile.size());
		for (int i = 0; i < listaFile.size(); i++) {
			final Mp3 mp3 = listaFile.get(i);
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
			listaFile.add(mp3);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void operazioneTagPresenti(final String pathFile2, final File f, final Tag tag)
	    throws IOException {
		Mp3 mp3;
		try {
			mp3 = new Mp3(f);
			mp3.setTag(tag);
			listaFile.add(mp3);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void setCanzoni(final Mp3[][] canzoni) {
		this.canzoni = canzoni;
	}

	public Mp3[][] getCanzoni() {
		return canzoni;
	}

	@Override
	public void raccogli(final String pathFileInput) {
		try {
			scorriEdEseguiSuTuttiIFile(pathFileInput);
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		} catch (final SAXException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Mp3> getListaFile() {
		return listaFile;
	}

	public void setListaFile(final ArrayList<Mp3> listaFile) {
		this.listaFile = listaFile;
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
