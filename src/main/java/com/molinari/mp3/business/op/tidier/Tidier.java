package com.molinari.mp3.business.op.tidier;

import java.io.File;

import com.molinari.mp3.business.ConnectionMp3;
import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.op.KeyHolder;
import com.molinari.mp3.business.op.PathCreator;
import com.molinari.mp3.business.op.TagOp;
import com.molinari.mp3.business.op.renamer.RenamerOp;
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
	protected void operationTag(Mp3 mp3) {
		final File cartellaAlbum = PathCreator.createPath(mp3.getTag(), output);
		String pathCartellaAlbum = cartellaAlbum.getAbsolutePath();
		RenamerOp.safeRename(pathCartellaAlbum + Mp3ReaderUtil.slash(), mp3.getMp3file(), mp3.getTag());
	}
}
