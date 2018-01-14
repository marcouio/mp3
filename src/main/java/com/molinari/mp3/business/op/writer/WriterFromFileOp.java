package com.molinari.mp3.business.op.writer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.check.CheckFile;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.op.GenericTagOp;

public class WriterFromFileOp extends GenericTagOp{

	protected ArrayList<String> righe = new ArrayList<>();
	
	private String fileToWrite;
	
	public WriterFromFileOp(String string) {
		fileToWrite = string;
	}
	
	@Override
	public void after() {
		super.after();
		
		Mp3ReaderUtil.scriviFileSuPiuRighe(new File(fileToWrite), righe);
	}
	
	@Override
	protected void operazioneTagNonPresenti(String pathFile2, File f) {
		// do nothing
		
	}

	@Override
	protected void operazioneTagPresenti(String pathFile2, File f, Tag tag) throws IOException {
		String riga = null;
		String artista = null;
		String album = null;
		if (tag != null) {
			artista = CheckFile.checkSingleTag(tag.getArtistaPrincipale());
			album = CheckFile.checkSingleTag(tag.getNomeAlbum());
			riga = artista.toUpperCase() + " - " + album.toUpperCase();
		}
		creaStringa(riga);
		
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
