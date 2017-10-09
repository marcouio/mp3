package com.molinari.mp3.business.operation.tidier;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3Exception;
import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.check.CheckFile;
import com.molinari.mp3.business.check.CheckFileOrdina;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagUtil;
import com.molinari.mp3.business.operation.IOperazioni;
import com.molinari.mp3.business.operation.OperazioniBaseTagFile;
import com.molinari.utility.graphic.component.alert.Alert;

public class Ordinatore extends OperazioniBaseTagFile {

	private String input;
	private String output;
	private ArrayList<File> fileDaSpostareAllaFine = new ArrayList<File>();;
	private String pathCartellaAlbum;

	public Ordinatore(final String input, final String output) {
		if (CheckFile.checkCartelle(input, output)) {
			setInput(input);
			setOutput(output);
			setPathFile(getOutput());
		} else {
			Alert.errore("Errori presenti nella scelta delle cartelle", "Non va");
			throw new Mp3Exception("Errori presenti nella scelta delle cartelle");
		}
	}

	@Override
	protected void operazioneTagNonPresenti(final String pathFile2, final File f) {
		try {
			
			if (f.getName().endsWith(CheckFile.ESTENSIONE_MP3)) {

				Tag tag = findTag(f);
				if (tag != null && isTagsValorizzati(tag)) {
						operazioneTagPresenti(pathFile2, f, tag);
					}
			} else {
//				fileDaSpostareAllaFine.add(f);
			}
		} catch (final IOException e) {
			// file non trovato
			e.printStackTrace();
		}

	}

	protected boolean isTagsValorizzati(final Tag tag) {
		return TagUtil.hasTitleAndArtist(tag);
	}

	@Override
	protected void operazioneTagPresenti(final String pathFile2, final File f, final Tag tag) throws IOException {
		try {
			if(TagUtil.hasTitleAndArtist(tag)){
				Controllore.getLog().info("-> Tag validi procedo ad ordinare");
				ordinaPerTag(pathFile2, f, tag);
			}else{
				Controllore.getLog().info("-> Tag non validi, li cerco");
				operazioneTagNonPresenti(pathFile2, f);
			}
		} catch (final Exception e) {
			Controllore.getLog().log(Level.SEVERE, e.getMessage(), e);
		}

	}

	@Override
	protected void setTipoOperazione() {
		Controllore.setOperazione(IOperazioni.ORDINA);
	}

	@Override
	protected void initCheckFile() {
		checkFile = new CheckFileOrdina();
	}

	@Override
	protected void operazioneFinale() {
		if (pathCartellaAlbum != null && !pathCartellaAlbum.equals("")) {
			for (int i = 0; i < fileDaSpostareAllaFine.size(); i++) {
				final File f = fileDaSpostareAllaFine.get(i);
				f.renameTo(new File(pathCartellaAlbum + Mp3ReaderUtil.slash() + f.getName()));
			}
		}
		// TODO generaReport();

	}

	public boolean ordina() {
		try {
			return scorriEdEseguiSuTuttiIFile(input);
		} catch (final ParserConfigurationException e) {
			Controllore.getLog().log(Level.SEVERE, e.getMessage(), e);
		} catch (final SAXException e) {
			Controllore.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
		return false;
	}

	public static void main(final String[] args) {
		// TODO Auto-generated method stub

	}

	public boolean moveFile(final File origine, final File destinazione) {
		return origine.renameTo(destinazione);
	}

	private boolean ordinaPerTag(final String pathFile2, final File f, final Tag tag) throws Exception {
		Mp3 mp3 = null;
		if (checkFile.checkFile(f, true)) {
			mp3 = new Mp3(f);
			mp3.setTag(tag);
			final File cartellaAlfabeto = creaCartellaAlfabeto(tag, mp3);
			final File cartellaArtista = creaCartellaArtista(tag, mp3, cartellaAlfabeto);
			final File cartellaAlbum = creaCartellaAlbum(tag, mp3, cartellaArtista);
			pathCartellaAlbum = cartellaAlbum.getAbsolutePath();
			String nome = mp3.getNome();
			nome = nome.replaceAll(":", "");
			nome = nome.replaceAll(">", "");
			nome = nome.replaceAll("<", "");
			nome = nome.replaceAll("'", " ");
			nome = nome.replaceAll("\\?", "");
			nome = nome.replaceAll("\"", "");
			nome = nome.trim();
			String pathname = pathCartellaAlbum + Mp3ReaderUtil.slash() + nome;
			File fileTo = new File(pathname);
			try{
				java.nio.file.Files.move(f.toPath(), fileTo.toPath(),StandardCopyOption.REPLACE_EXISTING);
				Controllore.getLog().info("File spostato in: " + pathname);
				return true;
			}catch (Exception e) {
				boolean renameTo = f.renameTo(fileTo);
				if(!renameTo){
					Controllore.getLog().info("Non sono riuscito a spostare il file");
					Controllore.getLog().log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		return false;
	}

	private File creaCartellaAlbum(final Tag tag, final Mp3 mp3, final File cartellaArtista) {
		File cartellaAlbum = null;
		final String pathCartellaArtista = cartellaArtista.getAbsolutePath();
		String nome = tag.getNomeAlbum();
		nome = nome.replaceAll(":", "");
		nome = nome.replaceAll(">", "");
		nome = nome.replaceAll("<", "");
		nome = nome.replaceAll("'", " ");
		nome = nome.replaceAll("\\?", "");
		nome = nome.replaceAll("\"", "");
		nome = nome.trim();
		String pathname = pathCartellaArtista + Mp3ReaderUtil.slash() + nome;
		cartellaAlbum = new File(pathname);
		if (!cartellaAlbum.exists()) {
			cartellaAlbum.mkdir();
		}
		return cartellaAlbum;

	}
	
	@Override
	public void doWithDirectory(File f) {
		File[] listFiles = f.listFiles();
		if(listFiles.length == 0){
			f.delete();
		}else{
			super.doWithDirectory(f);
		}
	}

	private File creaCartellaArtista(final Tag tag, final Mp3 mp3, final File cartellaAlfabeto) {
		File cartellaArtista = null;
		final String pathCartellaAlfabeto = cartellaAlfabeto.getAbsolutePath();
		cartellaArtista = new File(pathCartellaAlfabeto + Mp3ReaderUtil.slash() + tag.getArtistaPrincipale());
		if (!cartellaArtista.exists()) {
			cartellaArtista.mkdir();
		}
		return cartellaArtista;

	}

	private File creaCartellaAlfabeto(final Tag tag, final Mp3 mp3) {
		File dir = null;
		final String nomeAlfa = tag.getArtistaPrincipale();
		String lettera = "";
		if (nomeAlfa != null && nomeAlfa.length() > 1) {
			lettera = nomeAlfa.substring(0, 1).toUpperCase();
			try {
				Integer.parseInt(lettera);
				dir = new File(output + Mp3ReaderUtil.slash() + "0_9");
				if (!dir.exists()) {
					dir.mkdir();
				}
			} catch (final NumberFormatException e) {
				dir = new File(output + Mp3ReaderUtil.slash() + lettera);
				if (!dir.exists()) {
					dir.mkdir();
				}
			}
		} else {
			dir = new File(output + Mp3ReaderUtil.slash() + "NoLetter");
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
		return dir;
	}

	public void setOutput(final String output) {
		this.output = output;
	}

	public String getOutput() {
		return output;
	}

	public void setInput(final String input) {
		this.input = input;
	}

	public String getInput() {
		return input;
	}

}
