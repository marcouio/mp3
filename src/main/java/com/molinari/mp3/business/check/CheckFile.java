package com.molinari.mp3.business.check;

import java.io.File;

public class CheckFile {

	public static final String ESTENSIONE_MP3 = ".mp3";

	public CheckFile() {
		//do nothing
	}

	public static boolean checkCartelle(final String output2) {
		final File output = new File(output2);

		boolean isNotDirectory = output.exists() && !output.isDirectory();
		boolean notExistAndCannotCreate = !output.exists() && !output.mkdir();
		return !(isNotDirectory || notExistAndCannotCreate);
	}

	public static String checkSingleTag(final String tag) {
		return tag.replaceAll("�", "");
	}
}
