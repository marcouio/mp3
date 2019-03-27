package com.molinari.mp3.business.op.tidier;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.farng.mp3.TagException;

import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.op.KeyHolder;
import com.molinari.mp3.business.op.PathCreator;
import com.molinari.mp3.business.op.TagOp;
import com.molinari.mp3.business.op.renamer.Renamer;
import com.molinari.utility.controller.ControlloreBase;

public class Tidier extends TagOp {

	private String output;
	
	public Tidier(boolean forceFindTag, String output) {
		super(forceFindTag);
		this.output = output;
	}
	
	public Tidier(boolean forceFindTag, String key, String output) {
		super(forceFindTag);
		KeyHolder.getSingleton().setKey(key);
		this.output = output;
	}

	@Override
	protected Mp3 operationTag(Mp3 mp3) {
		final File cartellaAlbum = PathCreator.createPath(mp3.getTag(), output);
		String pathCartellaAlbum = cartellaAlbum.getAbsolutePath();
		String rename = Renamer.safeRename(pathCartellaAlbum + Mp3ReaderUtil.slash(), mp3.getMp3file(), mp3.getTag());
		try {
			return new Mp3(rename);
		} catch (IOException | TagException e) {
			ControlloreBase.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}
}
