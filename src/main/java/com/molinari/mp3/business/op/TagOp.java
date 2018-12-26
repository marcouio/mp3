package com.molinari.mp3.business.op;

import java.io.File;
import java.nio.file.Files;
import java.util.function.Function;
import java.util.logging.Level;

import com.molinari.mp3.business.check.CheckFile;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagUtil;
import com.molinari.utility.controller.ControlloreBase;

public abstract class TagOp implements Function<File, Mp3> {

	private FinderMp3Tag finderTag = new FinderMp3Tag(true);
	
	@Override
	public Mp3 apply(File f) {
		try {
			if(f.getAbsolutePath().toLowerCase().endsWith(CheckFile.ESTENSIONE_MP3)){
				Mp3 mp3 = new Mp3(f);
				if(TagUtil.isValidTag(mp3.getTag(), isForceFindTag())) {
					operationTag(mp3);
				}else {
					operationNoTag(mp3);
				}
			}
			
			Files.deleteIfExists(new File(getNamePlusOriginal(f.getAbsolutePath())).toPath());
	
			
		} catch (Exception e) {
			ControlloreBase.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
		
		return null;
	}

	public boolean isForceFindTag() {
		return getFinderTag().isForceFindTag();
	}
	
	public static String getNamePlusOriginal(String fileName){
		StringBuilder sb = new StringBuilder();
		sb.append(fileName.substring(0, fileName.length()-4));
		sb.append(".original");
		sb.append(fileName.substring(fileName.length()-4));
		return sb.toString();
	}

	protected void operationNoTag(Mp3 mp3) {
		Tag newTag = getFinderTag().find(mp3);
		if(TagUtil.isValidTag(newTag, false)) {
			operationTag(mp3);
		}
	};

	protected abstract void operationTag(Mp3 mp3);

	public FinderMp3Tag getFinderTag() {
		return finderTag;
	}

	public void setFinderTag(FinderMp3Tag finderTag) {
		this.finderTag = finderTag;
	}

}
