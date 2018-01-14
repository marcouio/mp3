package com.molinari.mp3.business.op.binder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagTipo1;
import com.molinari.mp3.business.op.GenericTagOp;
import com.molinari.utility.controller.ControlloreBase;

public class BinderOp extends GenericTagOp {

	private List<Mp3File>  listaFile = new ArrayList<>();
	
	@Override
	protected void operazioneTagNonPresenti(String pathFile2, File f) {
		final Tag t = new TagTipo1();
		t.setTitoloCanzone(f.getName());
		try {
			final Mp3 mp3 = new Mp3(f);
			mp3.setTag(t);
			Mp3File e = new Mp3File();
			addMp3ToList(f, mp3, e);
		} catch (final Exception e) {
			ControlloreBase.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
		
	}
	
	@Override
	protected void operazioneTagPresenti(String pathFile2, File f, Tag tag) throws IOException {
		try {
			Mp3 mp3 = new Mp3(f);
			mp3.setTag(tag);
			Mp3File mp3File = new Mp3File();
			addMp3ToList(f, mp3, mp3File);
		} catch (final Exception e) {
			ControlloreBase.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
		
	}
	
	private void addMp3ToList(final File f, Mp3 mp3, Mp3File mp3File) {
		mp3File.setNome(mp3.toString());
		mp3File.setPath(f.getAbsolutePath());
		listaFile.add(mp3File);
	}

}
