package com.molinari.mp3.business.op.renamer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.Mp3Exception;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagUtil;
import com.molinari.mp3.business.op.GenericTagOp;
import com.molinari.mp3.business.op.KeyHolder;
import com.molinari.mp3.business.op.UtilOp;

public class RenamerOp extends GenericTagOp{

	
	public RenamerOp(String key) {
		KeyHolder.getSingleton().setKey(key);
	}
	
	@Override
	protected void operazioneTagNonPresenti(String pathFile2, File f) {
		Tag tag = getFinderTag().find(f);
		boolean validTag = TagUtil.isValidTag(tag, isForceFindTag());
		
		if(validTag){
			
			safeRename(pathFile2, f, tag);
			
		}
		
	}

	@Override
	protected void operazioneTagPresenti(String pathFile2, File f, Tag tag) throws IOException {
		valorizzaTag(pathFile2, f, tag);
		
	}
	
	private void valorizzaTag(final String pathFile, final File f, final Tag tag) {
		
		Tag tagNew = tag;
		
		if(!TagUtil.isValidTag(tag, isForceFindTag())){
		
			logCallInternet(tag);
			
			tagNew = getFinderTag().find(f);
			if(tagNew == null) {
				throw new Mp3Exception("Tag non trovato in rete per il file: " + f.getAbsolutePath());
			}	
			
		}
		
		safeRename(pathFile, f, tagNew);	
	}
	
	private void logCallInternet(final Tag tag) {
		if(TagUtil.isValidTag(tag, isForceFindTag())){
			Controllore.getLog().info("-> Tag interno completo ma recupero i tag su internet");
		}else{
			Controllore.getLog().info("-> Tag interno non completo provo su internet");
		}
	}
	
	public static String safeRename(final String pathFile, final File f, Tag tagNew) {
		String newName = null;
		if (tagNew.hasTitleAndArtist()) {
			try {
				newName = newName(pathFile, tagNew);
				UtilOp.rename(f, newName);
				return newName;
					
			} catch (final IOException e) {
				Controllore.getLog().log(Level.SEVERE, e.getMessage(), e);
			}
		} else {
			Controllore.getLog().info("Impossibile trovare tag per rinominare il file " + f.getName());
		}
		try {

			if(f.getAbsolutePath().contains(".mp3")){
				String namePlusOriginal = getNamePlusOriginal(f.getName());
				File filePlusOriginal = new File(pathFile + namePlusOriginal);
				java.nio.file.Files.deleteIfExists(filePlusOriginal.toPath());
			}
			if(f.getAbsolutePath().contains(".MP3")){
				java.nio.file.Files.deleteIfExists(new File(f.getAbsolutePath().replaceAll(".MP3", ".original.MP3")).toPath());
			}
			
			if(newName != null){
				java.nio.file.Files.deleteIfExists(new File(getNamePlusOriginal(newName)).toPath());
			}
		} catch (IOException e) {
			Controllore.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
		
		return null;
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
}
