package com.molinari.mp3.business.operation.tidier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import org.farng.mp3.TagException;
import org.xml.sax.SAXException;

import com.molinari.mp3.business.ConfiguratoreEstensioni;
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
import com.molinari.mp3.business.operation.renamer.Rinominatore;
import com.molinari.utility.graphic.component.alert.Alert;
import com.molinari.utility.io.UtilIo;
import com.molinari.utility.math.UtilMath;

public class Ordinatore extends OperazioniBaseTagFile {

	private String input;
	private String output;
	private ArrayList<File> fileDaSpostareAllaFine = new ArrayList<>();
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
	public void operazioniGenerica(String pathFile, File f) throws Exception {
		super.operazioniGenerica(pathFile, f);
		
		List<String> estensioneDaScompattare = ConfiguratoreEstensioni.getSingleton().getEstensioneDaScompattare();
		boolean daScompattare = ConfiguratoreEstensioni.getSingleton().containEstensione(f, estensioneDaScompattare);
		if(daScompattare){
			UtilIo.unZipIt(f.getAbsolutePath(), pathFile);
		}
		
		List<String> estensioneEliminare = ConfiguratoreEstensioni.getSingleton().getEstensioneEliminare();
		boolean daEliminare = ConfiguratoreEstensioni.getSingleton().containEstensione(f, estensioneEliminare);
		if(daEliminare){
			f.deleteOnExit();
		}
		
		File dir = new File(pathFile);
		if(dir.isDirectory()){
			File[] listFiles = dir.listFiles();
			if(listFiles == null || listFiles.length == 0){
				dir.deleteOnExit();
			}
		}
	}

	@Override
	protected void operazioneTagNonPresenti(final String pathFile2, final File f) {

		if (f.getName().endsWith(CheckFile.ESTENSIONE_MP3)) {

			Tag tag = findTag(f);
			if (tag != null && tag.hasTitleAndArtist()) {
				
				Controllore.getLog().info("-> Tag validi procedo ad ordinare");
				ordinaPerTag(f, tag);
			}
		}

	}

	protected boolean isTagsValorizzati(final Tag tag) {
		return TagUtil.isValidTag(tag, isForceFindTag());
	}

	@Override
	protected void operazioneTagPresenti(final String pathFile2, final File f, final Tag tag) throws IOException {
		try {
			if(TagUtil.isValidTag(tag, isForceFindTag())){
				Controllore.getLog().info("-> Tag validi procedo ad ordinare");
				ordinaPerTag(f, tag);
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
				UtilIo.moveFile(f, new File(pathCartellaAlbum + Mp3ReaderUtil.slash() + f.getName()));
			}
		}
		// TODO generaReport();

	}

	public boolean ordina() {
		try {
			return scorriEdEseguiSuTuttiIFile(input);
		} catch (ParserConfigurationException | SAXException e) {
			Controllore.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
		return false;
	}

	public boolean moveFile(final File origine, final File destinazione) {
		return origine.renameTo(destinazione);
	}

	private boolean ordinaPerTag(final File f, final Tag tag) {
		boolean ret = false;
		try {
			Mp3 mp3 = null;
			if (checkFile.checkFile(f, true)) {
				mp3 = new Mp3(f);
				mp3.setTag(tag);
				final File cartellaAlfabeto = creaCartellaAlfabeto(tag);
				final File cartellaArtista = creaCartellaArtista(tag, cartellaAlfabeto);
				final File cartellaAlbum = creaCartellaAlbum(tag, cartellaArtista);
				pathCartellaAlbum = cartellaAlbum.getAbsolutePath();
				
				Rinominatore.safeRename(pathCartellaAlbum + Mp3ReaderUtil.slash(), f, tag);
			}
			
		} catch (IOException | TagException e) {
			throw new Mp3Exception(e);
		}
		return ret;
	}

	public boolean moveMp3(final File f, String pathname, File fileTo) {
		try{
			UtilIo.moveFile(f, fileTo);
			Controllore.getLog().info(() -> "File spostato in: " + pathname);
			return true;
		}catch (Exception e) {
			boolean renameTo = f.renameTo(fileTo);
			if(!renameTo){
				Controllore.getLog().log(Level.SEVERE, "Non sono riuscito a spostare il file", e);
			}
			return false;
		}
	}

	private File creaCartellaAlbum(final Tag tag, final File cartellaArtista) {
		File cartellaAlbum = null;
		final String pathCartellaArtista = cartellaArtista.getAbsolutePath();
		String nome = adjust(tag.getNomeAlbum());
		String pathname = pathCartellaArtista + Mp3ReaderUtil.slash() + nome;
		cartellaAlbum = new File(pathname.trim());
		if (!cartellaAlbum.exists()) {
			cartellaAlbum.mkdir();
		}
		return cartellaAlbum;

	}
	
	@Override
	public void doWithDirectory(File f) {
		File[] listFiles = f.listFiles();
		if(listFiles.length == 0){
			f.deleteOnExit();
		}else{
			super.doWithDirectory(f);
		}
	}

	private File creaCartellaArtista(final Tag tag, final File cartellaAlfabeto) {
		File cartellaArtista = null;
		final String pathCartellaAlfabeto = cartellaAlfabeto.getAbsolutePath();
		String artistaPrincipale = adjust(tag.getArtistaPrincipale());
		String pathname = pathCartellaAlfabeto + Mp3ReaderUtil.slash() + artistaPrincipale;
		cartellaArtista = new File(pathname.trim());
		if (!cartellaArtista.exists()) {
			cartellaArtista.mkdir();
		}
		return cartellaArtista;

	}

	private File creaCartellaAlfabeto(final Tag tag) {
		File dir = null;
		final String nomeAlfa = tag.getArtistaPrincipale();

		StringBuilder pathname = new StringBuilder();
		pathname.append(output);

		pathname.append(createAlphabetDirName(nomeAlfa));
		dir = new File(pathname.toString().trim());
		if (!dir.exists()) {
			dir.mkdir();
		}
		return dir;
	}
	
	public String createAlphabetDirName(String nomeAlfa) {
		String ret = "";

		if (nomeAlfa != null && nomeAlfa.length() > 1) {
			String lettera = nomeAlfa.substring(0, 1).toUpperCase();
			boolean number = UtilMath.isNumber(lettera);
			if(number){
				ret = Mp3ReaderUtil.slash() + "0_9";

			}else{
				ret = Mp3ReaderUtil.slash() + lettera;
			}
		} else {
			ret =  Mp3ReaderUtil.slash() + "NoLetter";

		}
		return ret;

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
