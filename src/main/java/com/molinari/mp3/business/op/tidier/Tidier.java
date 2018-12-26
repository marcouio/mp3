package com.molinari.mp3.business.op.tidier;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.farng.mp3.TagException;

import com.molinari.mp3.business.ConnectionMp3;
import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.op.KeyHolder;
import com.molinari.mp3.business.op.PathCreator;
import com.molinari.mp3.business.op.TagOp;
import com.molinari.mp3.business.op.renamer.Renamer;
import com.molinari.utility.controller.ControlloreBase;
import com.molinari.utility.io.func.CrosserFiles;

public class Tidier extends TagOp {

	public static void main(String[] args) {
		ControlloreBase.getSingleton().setConnectionClassName(ConnectionMp3.class.getName());
		new CrosserFiles().execute("C:\\Users\\molinaris\\Desktop\\song", new Tidier("C:\\Users\\molinaris\\Desktop\\song")::apply);
	}
	
	private String output;
	
	public Tidier(String output) {
		this.output = output;
	}
	
	public Tidier(String key, String output) {
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
