package com.molinari.mp3.business.op.writer;

import java.io.File;
import java.util.ArrayList;

import com.molinari.utility.io.FileOperationBase;

public class WriterFromDirectoryOp extends FileOperationBase {
	
	protected ArrayList<String> righe = new ArrayList<>();
	
	@Override
	public boolean checkFile(File f) {
		return false;
	}

	@Override
	public void executeOnDirectory(File f) {
		super.executeOnDirectory(f);
		
		String path = f.getAbsolutePath();
		
		String artista = null;
		String album = null;
		if (path != null) {
			final String nomeFile = f.getName();
			if (nomeFile.contains("-")) {
				final int trattino = nomeFile.indexOf("-");

				artista = nomeFile.substring(0, trattino).trim();
				album = nomeFile.substring(trattino + 1, nomeFile.length()).trim();
			}
			final String riga = artista + " - " + album;
			creaStringa(riga);
		}
	}
	
	private void creaStringa(final String riga) {
		boolean ok = true;
		if (riga != null) {
			for (int i = 0; i < righe.size(); i++) {
				final String r = righe.get(i);
				if (r.equals(riga)) {
					ok = false;
				}
			}
			if (ok) {
				righe.add(riga);
			}
		}
	}

}
