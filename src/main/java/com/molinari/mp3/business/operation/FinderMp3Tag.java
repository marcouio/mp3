package com.molinari.mp3.business.operation;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;

import org.farng.mp3.id3.ID3v2_4;

import com.molinari.mp3.business.Controllore;
import com.molinari.mp3.business.acoustid.TagAudioTrack;
import com.molinari.mp3.business.lookup.LookUp;
import com.molinari.mp3.business.lookup.LookUp.ChromaprintField;
import com.molinari.mp3.business.objects.Mp3;
import com.molinari.mp3.business.objects.Tag;
import com.molinari.mp3.business.objects.TagTipo2_4;
import com.molinari.mp3.business.objects.TagUtil;
import com.molinari.mp3.domain.ISong;
import com.molinari.mp3.domain.Song;
import com.molinari.mp3.domain.dao.SongDAO;
import com.molinari.mp3.views.ConfirmAssignTag;
import com.molinari.mp3.views.ConfirmAssignTag.BeanAssign;
import com.molinari.mp3.views.LoaderDialog;
import com.molinari.utility.controller.ControlloreBase;
import com.molinari.utility.io.URLUTF8Encoder;

public class FinderMp3Tag {

	private boolean forceFindTag = true;
	
	public FinderMp3Tag(boolean forceFindOnWeb) {
		this.forceFindTag = forceFindOnWeb;
	}
	
	public Tag find(File f){
		boolean toSave = false;
		Tag result = null;
		Tag resultFounded = null;
		Map<ChromaprintField, String> fp = null;
		try {
			Mp3 mp3 = new Mp3(f);

			boolean hasTitleAndArtist = TagUtil.isValidTag(mp3.getTag(), isForceFindTag());
			
			if (!hasTitleAndArtist || isForceFindTag()) {
				
				LookUp lookUp = new LookUp(KeyHolder.getSingleton().getKey());
				fp = lookUp.getFingerPrint(f);
				
				resultFounded = findTagByDb(fp);
				if(resultFounded == null) {
					toSave = true;
					resultFounded = findTagByWeb(mp3, fp, lookUp);
				}
				result = resultFounded;
			}	

			if(result == null) {
				result = mp3.getTag();
			}

			if ((resultFounded == null && !hasTitleAndArtist) || !TagUtil.hasTitleAndArtist(resultFounded)) {
				final Assegnatore assegna = new Assegnatore(f, "-");
				assegna.save(f);
				result = assegna.getFile().getTag();
				if(result == null){
					result =  new TagTipo2_4(new ID3v2_4());
					
				}
				final Tag resultToPass = result;
				ConfirmAssignTag confirm = new ConfirmAssignTag(Controllore.getApplicationframe(), resultToPass, f.getAbsolutePath());
				
				LoaderDialog ld = new LoaderDialog(confirm);
				ld.execute();
				BeanAssign beanAssign = confirm.getBeanAssign();
				
				result.setArtistaPrincipale(beanAssign.getArtist());
				result.setTitoloCanzone(beanAssign.getSong());
				result.setNomeAlbum(beanAssign.getAlbum());
				result.setTraccia(beanAssign.getTrack());
				ControlloreBase.getLog().info("Assegnati tag da info sul nome");
			}

			if(toSave && fp != null) {
				saveOnDb(fp, result);
			}
			
			mp3.setTag(result);
			mp3.save();
		} catch (Exception e) {
			Controllore.getLog().log(Level.SEVERE, e.getMessage(), e);
		}
		
		return result;
	
	}
	
	private void saveOnDb(Map<ChromaprintField, String> fp, Tag result) {
		int duration = Integer.parseInt(fp.get(ChromaprintField.DURATION));
		String fingerprint = fp.get(ChromaprintField.FINGERPRINT);
		
		ISong song = new Song();
		song.setAlbum(result.getNomeAlbum());
		song.setArtist(result.getArtistaPrincipale());
		song.setComments(result.getCommenti());
		song.setFpduration(duration);
		song.setGenre(result.getGenere());
		song.setKey(fingerprint);
		song.setName(result.getTitoloCanzone());
		if(result.getTraccia() != null && !result.getTraccia().equals("")) {
			song.setTracknumber(Integer.valueOf(result.getTraccia()));
		}
		
		SongDAO songDAO = new SongDAO(song);
		songDAO.insert(song);
		
	}

	private Tag findTagByDb(Map<ChromaprintField, String> fp) {
		if(fp != null) {
			SongDAO songDAO = new SongDAO(new Song());
			int duration = Integer.parseInt(fp.get(ChromaprintField.DURATION));
			String fingerprint = fp.get(ChromaprintField.FINGERPRINT);
			ISong song = songDAO.selectByKey(fingerprint, duration);
			if(song != null) {
				return createNewTag(song);
			}
		}
		return null;
	}

	public Tag findTagByWeb(Mp3 mp3, Map<ChromaprintField, String> fp, LookUp lookUp) {
		Tag result = null;
		try {
			
			TagAudioTrack tagFromUrl = lookUp.lookup(fp);
			result = mp3.getTag();
			if(tagFromUrl != null){
				
				String trackName = tagFromUrl.getTrackName();
				String artist = tagFromUrl.getArtist();
				
				if (trackName != null && artist != null) {
					
					return createNewTag(result, tagFromUrl);
				} else {
					
					Controllore.getLog().info("-> Tag da url non trovato o con informazioni mancanti");
				}
			}
		} catch (Exception e) {
			Controllore.getLog().log(Level.SEVERE, "-> Eccezione durante la ricerca del tag via web", e);
		}
		return result;
	}
	
	public Tag createNewTag(ISong song) {
		Tag webResult = new TagTipo2_4(new ID3v2_4());

		String trackName = song.getName();
		webResult.setTitoloCanzone(trackName);
		webResult.setTraccia(Integer.toString(song.getTracknumber()));
		webResult.setNomeAlbum(song.getAlbum());
		webResult.setArtistaPrincipale(song.getArtist());
		webResult.setCommenti(song.getComments());
		webResult.setGenere(song.getGenre());
		
		return webResult;
	}

	public Tag createNewTag(Tag tagFromMp3, TagAudioTrack tagFromUrl) {
		String trackName;
		String artist;
		Tag webResult = new TagTipo2_4(new ID3v2_4());
		
		trackName = URLUTF8Encoder.unescape(URLUTF8Encoder.encode(tagFromUrl.getTrackName()));
		artist = URLUTF8Encoder.unescape(URLUTF8Encoder.encode(tagFromUrl.getArtist()));
		String album = tagFromUrl.getAlbum();
		if(album != null){
			album = URLUTF8Encoder.unescape(URLUTF8Encoder.encode(tagFromUrl.getAlbum()));
		}
		webResult.setTitoloCanzone(trackName);
		webResult.setArtistaPrincipale(artist);
		webResult.setNomeAlbum(album);
		
		if(tagFromMp3 != null){
			if(tagFromMp3.getCommenti() != null){
				webResult.setCommenti(tagFromMp3.getCommenti());
			}
			if(tagFromMp3.getGenere() != null){
				webResult.setGenere(tagFromMp3.getGenere());
			}
			if(tagFromMp3.getTraccia() != null && !tagFromMp3.getTraccia().equals("")){
				webResult.setTraccia(tagFromMp3.getTraccia());
			}
		}
		
		return webResult;
	}

	public boolean isForceFindTag() {
		return forceFindTag;
	}

	public void setForceFindTag(boolean forceFindTag) {
		this.forceFindTag = forceFindTag;
	}
}
