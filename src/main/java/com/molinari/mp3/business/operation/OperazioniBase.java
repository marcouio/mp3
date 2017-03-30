package main.java.com.molinari.mp3.business.operation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import main.java.com.molinari.mp3.business.Mp3ReaderUtil;

public class OperazioniBase {
	// per i file
	protected File file;
	protected String pathFile = "";
	protected ArrayList<String> righe = new ArrayList<String>();

	protected ArrayList<String> cartelleDaScorrere = new ArrayList<String>();

	public static boolean rename(final File mp3, final String nome_dopo) throws IOException {
		final File file2 = new File(nome_dopo);
		final boolean success = mp3.renameTo(file2);
		return success;
	}

	public static boolean move(final File mp3, final String path_dopo) {
		final File file2 = new File(path_dopo + Mp3ReaderUtil.slash() + mp3.getName());
		final boolean success = mp3.renameTo(file2);
		return success;
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
