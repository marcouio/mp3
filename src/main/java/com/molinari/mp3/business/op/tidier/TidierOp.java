package com.molinari.mp3.business.op.tidier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.farng.mp3.TagException;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3Exception;
import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.check.CheckFile;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagUtil;
import com.molinari.mp3.business.op.GenericTagOp;
import com.molinari.mp3.business.op.UtilOp;
import com.molinari.mp3.business.operation.renamer.Rinominatore;
import com.molinari.utility.graphic.component.alert.Alert;
import com.molinari.utility.math.UtilMath;

public class TidierOp extends GenericTagOp {

	private static final String MP3 = ".mp3";
	private String input;
	private String output;
	
	public TidierOp(final String input, final String output) {
		super();
		if (CheckFile.checkCartelle(input, output)) {
			setInput(input);
			setOutput(output);
		} else {
			Alert.errore("Errori presenti nella scelta delle cartelle", "Non va");
			throw new Mp3Exception("Errori presenti nella scelta delle cartelle");
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
			Rinominatore.safeRename(pathCartellaAlbum + Mp3ReaderUtil.slash(), f, tag);
			
		} catch (IOException | TagException e) {
			throw new Mp3Exception(e);
		}
		return ret;
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


	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

}
