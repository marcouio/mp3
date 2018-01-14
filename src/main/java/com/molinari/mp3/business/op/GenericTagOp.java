package com.molinari.mp3.business.op;

import java.io.File;
import java.io.IOException;

import org.farng.mp3.TagException;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3Exception;
import com.molinari.mp3.business.check.CheckFile;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.operation.FinderMp3Tag;
import com.molinari.utility.controller.ControlloreBase;
import com.molinari.utility.io.FileOperationBase;

public abstract class GenericTagOp extends FileOperationBase {
	
	private FinderMp3Tag finderTag;
	private boolean forceFindTag;
	
	public GenericTagOp() {
		setFinderTag(new FinderMp3Tag(forceFindTag));
	}
	
	@Override
	public void execute(String pathFile, File f) {
		super.execute(pathFile, f);
		if(f.getAbsolutePath().toLowerCase().endsWith(CheckFile.ESTENSIONE_MP3)){
			workOnMp3(pathFile, f);
		}
	}

	public void workOnMp3(final String pathFile, final File f) {
		try {
			final Mp3 file = new Mp3(f);
			
			
			final Tag tag = file.getTag();
			if (tag != null) {
				Controllore.getLog().info("- > Tag già presenti su file");
				operazioneTagPresenti(pathFile, f, tag);
			} else {
				Controllore.getLog().info(" -> Tag non presenti su file");
				operazioneTagNonPresenti(pathFile, f);
			}
			
			if(f.getAbsolutePath().endsWith(".original.mp3")){
				ControlloreBase.getLog().info(() -> "Cancellazione dei file (" + f.getName() + ") avvenuta: " + f.delete());
				return;
			}
			
		} catch (IOException | TagException e) {
			throw new Mp3Exception(e);
		}
	}

	/**
	 * utilizzato per operazione su tag, nel caso di tag non presenti, specifica
	 * cosa fare da implementare nelle sottoclassi
	 * 
	 * @param pathFile2
	 * @param f
	 */
	protected abstract void operazioneTagNonPresenti(String pathFile2, File f);

	/**
	 * utilizzato per operazione su tag, nel caso di tag non presenti, specifica
	 * cosa fare da implementare nelle sottoclassi
	 * 
	 * @param pathFile2
	 * @param f
	 */
	protected abstract void operazioneTagPresenti(String pathFile2, File f, Tag tag) throws IOException;

	public boolean isForceFindTag() {
		return forceFindTag;
	}

	public void setForceFindTag(boolean forceFindTag) {
		this.forceFindTag = forceFindTag;
	}

	public FinderMp3Tag getFinderTag() {
		return finderTag;
	}

	public void setFinderTag(FinderMp3Tag finderTag) {
		this.finderTag = finderTag;
	}
}
