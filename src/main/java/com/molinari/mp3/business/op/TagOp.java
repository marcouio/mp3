package com.molinari.mp3.business.op;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.function.Function;
import java.util.logging.Level;

import org.farng.mp3.TagException;

import com.molinari.mp3.business.check.CheckFile;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagUtil;
import com.molinari.utility.controller.ControlloreBase;
import com.molinari.utility.io.csv.BeanOperationFile;
import com.molinari.utility.io.csv.WriterCSV;

public abstract class TagOp implements Function<File, Mp3> {

	private FinderMp3Tag finderTag = new FinderMp3Tag(true);
	
	WriterCSV<BeanOperationFile> csv = new WriterCSV<>(BeanOperationFile.class, new File("target").getAbsolutePath() + "/report.csv");
	
	@Override
	public Mp3 apply(File f) {
		
		BeanOperationFile b = new BeanOperationFile(); 
		Mp3 ret = null;
		try {
			b.setInput(f.getName());
			if(f.getAbsolutePath().toLowerCase().endsWith(CheckFile.ESTENSIONE_MP3)){
				ret = execute(f);
			}
			
			Files.deleteIfExists(new File(getNamePlusOriginal(f.getAbsolutePath())).toPath());
			b.setOutput(ret != null ? ret.getNome() : null);
			
		} catch (Exception e) {
			ControlloreBase.getLog().log(Level.SEVERE, e.getMessage(), e);
			b.setError(e.getMessage());
		}
		write(b);
		return null;
	}

	public void write(BeanOperationFile b) {
		try {
			csv.write(Arrays.asList(b));
		} catch (IOException e) {
			ControlloreBase.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public Mp3 execute(File f) throws IOException, TagException {
		Mp3 mp3 = new Mp3(f);
		if(TagUtil.isValidTag(mp3.getTag(), isForceFindTag())) {
			return operationTag(mp3);
		}else {
			return operationNoTag(mp3);
		}
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

	protected Mp3 operationNoTag(Mp3 mp3) {
		Tag newTag = getFinderTag().find(mp3);
		if(TagUtil.isValidTag(newTag, false)) {
			return operationTag(mp3);
		}
		return null;
	};

	protected abstract Mp3 operationTag(Mp3 mp3);

	public FinderMp3Tag getFinderTag() {
		return finderTag;
	}

	public void setFinderTag(FinderMp3Tag finderTag) {
		this.finderTag = finderTag;
	}

}
