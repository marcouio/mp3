package com.molinari.mp3.business.op.tidier;

import java.io.File;

import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.op.KeyHolder;
import com.molinari.mp3.business.op.PathCreator;
import com.molinari.mp3.business.op.TagOp;

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
		File renamed = safeRenameInRun(cartellaAlbum, mp3.getMp3file(), mp3.getTag());
		mp3.setFile(renamed);
		mp3.setNome(renamed.getName());
		return mp3;
	}
}
