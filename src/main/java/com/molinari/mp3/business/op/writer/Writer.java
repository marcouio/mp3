package com.molinari.mp3.business.op.writer;

import java.io.File;

import com.google.common.collect.Lists;
import com.molinari.mp3.business.Mp3ReaderUtil;
import com.molinari.mp3.business.check.CheckFile;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.op.KeyHolder;
import com.molinari.mp3.business.op.TagOp;

public class Writer extends TagOp{

	private String fileToWrite;

	public Writer(String fileToWrite, String key) {
		KeyHolder.getSingleton().setKey(key);
		this.fileToWrite = fileToWrite;
	}

	@Override
	protected Mp3 operationTag(Mp3 mp3) {
		Tag tag = mp3.getTag();
		String artista = CheckFile.checkSingleTag(tag.getArtistaPrincipale());
		String traccia = CheckFile.checkSingleTag(tag.getTraccia());
		String riga = artista.toUpperCase() + " - " + traccia.toUpperCase();
		Mp3ReaderUtil.scriviFileSuPiuRighe(new File(fileToWrite), Lists.newArrayList(riga));
		return mp3;
	}
	
	
}
