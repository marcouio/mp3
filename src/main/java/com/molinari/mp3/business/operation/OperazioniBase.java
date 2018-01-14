package com.molinari.mp3.business.operation;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.utility.io.UtilIo;

public class OperazioniBase {
	// per i file
	protected File file;
	protected String pathFile = "";
	protected ArrayList<String> righe = new ArrayList<>();

	protected ArrayList<String> cartelleDaScorrere = new ArrayList<>();

	public static boolean rename(final File mp3, final String nomedopo) throws IOException {
		if(!mp3.getAbsolutePath().equals(nomedopo)){
			try{
				UtilIo.moveFile(mp3, new File(nomedopo));
				java.nio.file.Files.deleteIfExists(mp3.toPath());
				String format = MessageFormat.format("-> Mp3 rinominato in: {0}", nomedopo);
				Controllore.getLog().info(format);
				return true;
			}catch (Exception e) {
				Controllore.getLog().severe("-> Cambio nome non riuscito");
				Controllore.getLog().severe(e.getMessage());
				return false;
			}
		}
		Controllore.getLog().info("-> Mp3 non rinominato perché già con lo stesso nome");
		return false;
	}
	
	public static boolean move(final File mp3, final String pathdopo) {
		final File file2 = new File(pathdopo + Mp3ReaderUtil.slash() + mp3.getName());
		return mp3.renameTo(file2);
	}

	public void setFile(final File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setPathFile(final String pathFile) {
		this.pathFile = pathFile;
	}

	public String getPathFile() {
		return pathFile;
	}

}
