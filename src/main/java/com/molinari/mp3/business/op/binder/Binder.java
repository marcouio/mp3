package com.molinari.mp3.business.op.binder;

import java.util.ArrayList;
import java.util.List;

import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.op.TagOp;

public class Binder extends TagOp{
	
	private List<Mp3File>  listaFile = new ArrayList<>();
	
	private void addMp3ToList(Mp3 mp3, Mp3File mp3File) {
		mp3File.setNome(mp3.toString());
		mp3File.setPath(mp3.getMp3file().getAbsolutePath());
		getListaFile().add(mp3File);
	}
	
	@Override
	protected Mp3 operationTag(Mp3 mp3) {
		addMp3ToList(mp3, new Mp3File());
		return mp3;
		
	}

	public List<Mp3File> getListaFile() {
		return listaFile;
	}

	public void setListaFile(List<Mp3File> listaFile) {
		this.listaFile = listaFile;
	}
}
