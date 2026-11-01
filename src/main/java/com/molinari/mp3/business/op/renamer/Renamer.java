package com.molinari.mp3.business.op.renamer;

import java.io.File;

import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagUtil;
import com.molinari.mp3.business.op.KeyHolder;
import com.molinari.mp3.business.op.TagOp;
import com.molinari.mp3.business.op.UtilOp;

public class Renamer extends TagOp {

	public Renamer(boolean forceFindTag, String key) {
		super(forceFindTag);
		KeyHolder.getSingleton().setKey(key);
	}

	public static File buildRenamedFile(final File parentDir, final File original, final Tag tagNew) {
		File dir = parentDir != null ? parentDir : original.getAbsoluteFile().getParentFile();
		if (!TagUtil.hasTitleAndArtist(tagNew)) {
			return new File(dir, original.getName());
		}
		String artista = UtilOp.adjust(tagNew.getArtistaPrincipale());
		String titolo = UtilOp.adjust(tagNew.getTitoloCanzone());
		if (artista.isEmpty() || titolo.isEmpty()) {
			return new File(dir, original.getName());
		}
		return new File(dir, artista + " - " + titolo + extension(original.getName()));
	}

	public static File safeRename(final File parentDir, final File original, final Tag tagNew) {
		File target = buildRenamedFile(parentDir, original, tagNew);
		if (UtilOp.isSameFile(original, target)) {
			return original;
		}
		UtilOp.rename(original, target, true);
		return target;
	}

	private static String extension(final String fileName) {
		int dot = fileName.lastIndexOf('.');
		if (dot > 0 && dot < fileName.length() - 1) {
			return fileName.substring(dot);
		}
		return ".mp3";
	}

	@Override
	protected Mp3 operationTag(Mp3 mp3) {
		File mp3file = mp3.getMp3file();
		File renamed = safeRename(mp3file.getParentFile(), mp3file, mp3.getTag());
		mp3.setFile(renamed);
		mp3.setNome(renamed.getName());
		return mp3;
	}

}
