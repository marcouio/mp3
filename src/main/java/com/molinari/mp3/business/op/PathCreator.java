package com.molinari.mp3.business.op;

import java.io.File;

import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.utility.math.UtilMath;

public class PathCreator {

	public static File creaCartellaAlbum(final Tag tag, final File cartellaArtista) {
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

	public static File creaCartellaArtista(final Tag tag, final File cartellaAlfabeto) {
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

	public static String createAlphabetDirName(String nomeAlfa) {
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

	public static File creaCartellaAlfabeto(final Tag tag, String output) {
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

	public static File createPath(final Tag tag, String output) {
		final File cartellaAlfabeto = creaCartellaAlfabeto(tag, output);
		final File cartellaArtista = creaCartellaArtista(tag, cartellaAlfabeto);
		return creaCartellaAlbum(tag, cartellaArtista);
	}

}
