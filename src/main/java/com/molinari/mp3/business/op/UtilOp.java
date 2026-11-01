package com.molinari.mp3.business.op;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3Exception;
import com.molinari.utility.io.UtilIo;

public class UtilOp {

	private UtilOp() {
		// do nothing
	}
	
	public static String adjust(String nome){
		if (nome == null) {
			return "";
		}
		String cleaned = nome.replaceAll("[\\\\/:*?\"<>|\\p{Cntrl}]", "")
				.replace('\'', ' ')
				.replaceAll("\\s+", " ")
				.replaceAll("[. ]+$", "")
				.trim();
		if (cleaned.matches("(?i)^(CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])$")) {
			cleaned = "_" + cleaned;
		}
		return cleaned;
	}

	/**
	 * @deprecated usare {@link #rename(File, File, boolean)}
	 */
	@Deprecated
	public static boolean rename(final File mp3, final String nomedopo) {
		return rename(mp3, new File(nomedopo), false);
	}

	public static boolean rename(final File source, final File target, final boolean overwrite) {
		if (isSameFile(source, target)) {
			String logStessoNome = MessageFormat.format("-> Mp3 {0} non rinominato perché già con lo stesso nome", source.getName());
			Controllore.getLog().info(logStessoNome);
			return false;
		}
		try{
			if (overwrite && target.exists()) {
				Controllore.getLog().warning(MessageFormat.format("-> Sovrascrittura del file esistente: {0}", target.getAbsolutePath()));
			}
			UtilIo.moveFile(source, target, overwrite);
			java.nio.file.Files.deleteIfExists(source.toPath());
			String format = MessageFormat.format("-> Mp3 rinominato in: {0}", target.getAbsolutePath());
			Controllore.getLog().info(format);
			return true;
		}catch (Exception e) {
			Controllore.getLog().severe(MessageFormat.format("-> Cambio nome non riuscito({0}) ", source.getName()));
			Controllore.getLog().log(Level.SEVERE, e.getMessage(), e);
			throw new Mp3Exception(e);
		}
	}

	public static boolean isSameFile(final File first, final File second) {
		try {
			if (first.exists() && second.exists()) {
				return java.nio.file.Files.isSameFile(first.toPath(), second.toPath());
			}
		} catch (IOException e) {
			Controllore.getLog().log(Level.FINE, "Confronto file fallito, uso i path", e);
		}
		return first.getAbsoluteFile().toPath().normalize().equals(second.getAbsoluteFile().toPath().normalize());
	}
}
