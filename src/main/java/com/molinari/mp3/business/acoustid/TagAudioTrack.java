package com.molinari.mp3.business.acoustid;

import java.util.List;

import com.molinari.mp3.business.acoustid.AudioTrack.Artists;
import com.molinari.mp3.business.acoustid.AudioTrack.Recordings;
import com.molinari.mp3.business.acoustid.AudioTrack.Releasegroups;
import com.molinari.mp3.business.acoustid.AudioTrack.Results;

public class TagAudioTrack {

	private AudioTrack audioTrack;

	public TagAudioTrack(AudioTrack audioTrack) {
		this.audioTrack = audioTrack;
	}

	public String getAlbum() {

		Recordings recording = getRecording();
		if (recording != null) {
			List<Releasegroups> releasegroups = recording.getReleasegroups();
			if (releasegroups != null && !releasegroups.isEmpty()) {
				Releasegroups releasegroup = releasegroups.get(0);
				return releasegroup.getTitle();
			}
		}
		return "";
	}

	public Recordings getRecording() {
		List<Results> results = this.audioTrack.getResults();
		if (results != null && !results.isEmpty()) {
			Results result = results.get(0);
			List<Recordings> recordings = result.getRecordings();
			if (recordings != null && !recordings.isEmpty()) {
				return recordings.get(0);
			}
		}
		return null;
	}

	public String getTrackName() {
		Recordings recording = getRecording();
		if(recording != null){
			return recording.getTitle();
		}
		return "";
	}

	public String getArtist(){
		Recordings recording = getRecording();
		if(recording != null){
			List<Artists> artists = recording.getArtists();
			if(artists != null && !artists.isEmpty()){
				return artists.get(0).getName();
			}
		}
		return null;
	}
}
