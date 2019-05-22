package com.molinari.mp3.business.op.renamer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.farng.mp3.TagException;

import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.op.KeyHolder;
import com.molinari.mp3.business.op.TagOp;
import com.molinari.mp3.business.op.UtilOp;
import com.molinari.utility.controller.ControlloreBase;
import com.molinari.utility.io.UtilIo;

public class Renamer extends TagOp {

	public Renamer(boolean forceFindTag, String key) {
		super(forceFindTag);
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
	protected Mp3 operationTag(Mp3 mp3) {
		Tag tagNew = mp3.getTag();
		File mp3file = mp3.getMp3file();
		String rename = safeRename(UtilIo.getParentPath(mp3file), mp3file, tagNew);
		try {
			return new Mp3(rename);
		} catch (IOException | TagException e) {
			ControlloreBase.getLog().log(Level.SEVERE, e.getMessage() + " on " + mp3.getNome(), e);
		}
		return null;
	}

}
