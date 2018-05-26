package com.molinari.mp3.business.op.renamer;

import java.io.File;

import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.op.KeyHolder;
import com.molinari.mp3.business.op.TagOp;
import com.molinari.mp3.business.op.UtilOp;
import com.molinari.utility.io.UtilIo;

public class Renamer extends TagOp {

	public Renamer(String key) {
		KeyHolder.getSingleton().setKey(key);
	}
	
	public static String newName(final String pathFile, Tag tagNew) {
		StringBuilder sb = new StringBuilder();
		sb.append(pathFile);
		sb.append(UtilOp.adjust(tagNew.getArtistaPrincipale()));
		sb.append(" - ");
		sb.append(UtilOp.adjust(tagNew.getTitoloCanzone()));
		sb.append(".mp3");
		return sb.toString();
	}
	
	public static String safeRename(final String pathFile, final File f, Tag tagNew) {
		String newName = newName(pathFile, tagNew);
		UtilOp.rename(f, newName);
		return newName;
	}

	@Override
	protected void operationTag(Mp3 mp3) {
		Tag tagNew = mp3.getTag();
		File mp3file = mp3.getMp3file();
		safeRename(UtilIo.getParentPath(mp3file), mp3file, tagNew);
	}

}
