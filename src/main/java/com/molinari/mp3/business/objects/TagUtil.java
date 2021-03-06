package com.molinari.mp3.business.objects;

public class TagUtil {

	private TagUtil() {
		// do nothing
	}
	
	public static boolean hasTitleAndArtist(Tag tag) {
		return tag != null && tag.getTitoloCanzone() != null && 
			   tag.getArtistaPrincipale() != null && 
			   !"".equals(tag.getArtistaPrincipale()) && 
			   !"".equals(tag.getTitoloCanzone());
	}
	
	public static boolean isValidTag(Tag tag, boolean forceFind) {
		return hasTitleAndArtist(tag) && !forceFind;
	}
}
