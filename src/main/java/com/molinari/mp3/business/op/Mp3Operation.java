package com.molinari.mp3.business.op;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.farng.mp3.TagException;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3Exception;
import com.molinari.mp3.business.check.CheckFile;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.utility.controller.ControlloreBase;

public class Mp3Operation {

	public void exec(BiConsumer<File, Tag> operationWithTag, Consumer<File> operationNoTag, File f) {
		if(f.getAbsolutePath().toLowerCase().endsWith(CheckFile.ESTENSIONE_MP3)){
			workOnMp3(operationWithTag, operationNoTag, f);
		}
	}

	public void workOnMp3(BiConsumer<File, Tag> operationWithTag, Consumer<File> operationNoTag, final File f) {
		try {
			final Mp3 file = new Mp3(f);
			
			final Tag tag = file.getTag();
			if (tag != null) {
				Controllore.getLog().info("- > Tag già presenti su file");
				operationWithTag.accept(f, tag);
				operazioneTagPresenti(f, tag);
			} else {
				Controllore.getLog().info(" -> Tag non presenti su file");
				operationNoTag.accept(f);
			}
			
			if(f.getAbsolutePath().endsWith(".original.mp3")){
				ControlloreBase.getLog().info(() -> "Cancellazione dei file (" + f.getName() + ") avvenuta: " + f.delete());
				return;
			}
			String namePlusOriginal = getNamePlusOriginal(f.getAbsolutePath());
			Files.deleteIfExists(new File(namePlusOriginal).toPath());
			
		} catch (IOException | TagException e) {
			throw new Mp3Exception(e);
		}
	}
	
	public static String getNamePlusOriginal(String fileName){
		StringBuilder sb = new StringBuilder();
		sb.append(fileName.substring(0, fileName.length()-4));
		sb.append(".original.mp3");
		return sb.toString();
	}

	private void operazioneTagNonPresenti(File f) {
		// TODO Auto-generated method stub
		
	}

	private void operazioneTagPresenti(File f, Tag tag) {
		// TODO Auto-generated method stub
		
	}
}
