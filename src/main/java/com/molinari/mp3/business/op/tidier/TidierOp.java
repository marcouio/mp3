package com.molinari.mp3.business.op.tidier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagUtil;
import com.molinari.mp3.business.op.GenericTagOp;
import com.molinari.mp3.business.op.UtilOp;
import com.molinari.mp3.business.op.renamer.RenamerOp;
import com.molinari.mp3.business.operation.KeyHolder;
import com.molinari.utility.GenericException;
import com.molinari.utility.controller.ControlloreBase;
import com.molinari.utility.graphic.component.alert.Alert;
import com.molinari.utility.io.ReturnFileOperation;
import com.molinari.utility.io.UtilIo;
import com.molinari.utility.math.UtilMath;

public class TidierOp extends GenericTagOp {

	private static final String MP3 = ".mp3";
	private String output;
	
	private ReturnFileOperation retOp = new ReturnFileOperation();
	
	public TidierOp(final String output, String key) {
		super();
		KeyHolder.getSingleton().setKey(key);
		if (CheckFile.checkCartelle(output)) {
			setOutput(output);
		} else {
			Alert.errore("Errori presenti nella scelta delle cartelle", "Non va");
			throw new Mp3Exception("Errori presenti nella scelta delle cartelle");
		}
		
	}
	
	@Override
	public <T> T execute(String pathFile, File f) {
		super.execute(pathFile, f);
		
		manageOtherFile(pathFile, f);
		return (T) retOp;
	}

	public void manageOtherFile(String pathFile, File f) {
		try {
			List<String> estensioneDaScompattare = ConfiguratoreEstensioni.getSingleton().getEstensioneDaScompattare();
			boolean daScompattare = ConfiguratoreEstensioni.getSingleton().containEstensione(f, estensioneDaScompattare);
			if(daScompattare){
				UtilIo.unZipIt(f.getAbsolutePath(), pathFile);
			}
			
			List<String> estensioneEliminare = ConfiguratoreEstensioni.getSingleton().getEstensioneEliminare();
			boolean daEliminare = ConfiguratoreEstensioni.getSingleton().containEstensione(f, estensioneEliminare);
			if(daEliminare){
				f.delete();
			}
			
			File dir = new File(pathFile);
			if(dir.isDirectory()){
				File[] listFiles = dir.listFiles();
				if(listFiles == null || listFiles.length == 0){
					dir.delete();
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new GenericException(e);
		}
	}
	
	@Override
	protected void operazioneTagNonPresenti(String pathFile2, File f) {

		if (f.getName().endsWith(CheckFile.ESTENSIONE_MP3)) {

			Tag tag = getFinderTag().find(f);
			if (tag != null && tag.hasTitleAndArtist()) {
				
				Controllore.getLog().info("-> Tag validi procedo ad ordinare");
				ordinaPerTag(f, tag);
			}
		}
		
	}

	@Override
	protected void operazioneTagPresenti(String pathFile2, File f, Tag tag) throws IOException {
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
	public boolean checkFile(File f) {
		if(super.checkFile(f)) {
		
			String estensione = MP3;
			if (f.getName().endsWith(estensione)) {
				return true;
			}
			
			List<String> estensioniPerOrdinatore = new ArrayList<>();
			
			for (int i = 0; i < estensioniPerOrdinatore.size(); i++) {
				final String est = estensioniPerOrdinatore.get(i);
				if (f.getName().endsWith(est)) {
					return true;
				}
			}
		}
		return false;
	}
	

	private boolean ordinaPerTag(final File f, final Tag tag) {
		boolean ret = false;
		try {
			Mp3 mp3 = new Mp3(f);
			mp3.setTag(tag);
			final File cartellaAlfabeto = creaCartellaAlfabeto(tag);
			final File cartellaArtista = creaCartellaArtista(tag, cartellaAlfabeto);
			final File cartellaAlbum = creaCartellaAlbum(tag, cartellaArtista);
			String pathCartellaAlbum = cartellaAlbum.getAbsolutePath();
			String name = RenamerOp.safeRename(pathCartellaAlbum + Mp3ReaderUtil.slash(), f, tag);
			
			retOp.setResponse(name);
			
		} catch (IOException | TagException e) {
			List<String> errs = new ArrayList<>();
			errs.add(e.getMessage());
 			retOp.setErrors(errs);
			throw new Mp3Exception(e);
		}
		return ret;
	}
	
	@Override
	public void before(String startingPathFile) {
		super.before(startingPathFile);
		try {
			Files.deleteIfExists(new File(startingPathFile + File.separator +"Report.csv").toPath());
		} catch (IOException e) {
			ControlloreBase.getLog().log(Level.SEVERE, "Report is impossible to delete", e);
		}
	}
	
	private File creaCartellaAlbum(final Tag tag, final File cartellaArtista) {
		File cartellaAlbum = null;
		final String pathCartellaArtista = cartellaArtista.getAbsolutePath();
		String nome = UtilOp.adjust(tag.getNomeAlbum());
		String pathname = pathCartellaArtista + Mp3ReaderUtil.slash() + nome;
		cartellaAlbum = new File(pathname.trim());
		if (!cartellaAlbum.exists()) {
			cartellaAlbum.mkdir();
		}
		return cartellaAlbum;

	}

	
	private File creaCartellaArtista(final Tag tag, final File cartellaAlfabeto) {
		File cartellaArtista = null;
		final String pathCartellaAlfabeto = cartellaAlfabeto.getAbsolutePath();
		String artistaPrincipale = UtilOp.adjust(tag.getArtistaPrincipale());
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

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

}
