package com.molinari.mp3.business.acoustid;

import java.util.List;

public class TagAudioTrack {

	private AudioTrack audioTrack;

	public TagAudioTrack(AudioTrack audioTrack) {
		this.audioTrack = audioTrack;
	}

	public String getAlbum() {

		Recording recording = getRecording();
		if (recording != null) {
			List<Releasegroup> releasegroups = recording.getReleasegroups();
			if (releasegroups != null && !releasegroups.isEmpty()) {
				Releasegroup releasegroup = releasegroups.get(0);
				return releasegroup.getTitle();
			}
		}
		return "";
	}

	public Recording getRecording() {
		List<Result> results = this.audioTrack.getResults();
		if (results != null && !results.isEmpty()) {
			Result result = getBestResult(results);
			List<Recording> recordings = result.getRecordings();
			if (recordings != null && !recordings.isEmpty()) {
				return recordings.get(0);
			}
		}
		return null;
	}
	
	private Result getBestResult(List<Result> results){
		return results.stream().max((r1, r2) -> {
			if(r1.getScore() > r2.getScore()) {
				return 1;
			}
			return -1;
		}).get();
	}

	public String getTrackName() {
		Recording recording = getRecording();
		if(recording != null){
			return recording.getTitle();
		}
		return "";
	}

	public String getArtist(){
		Recording recording = getRecording();
		if(recording != null){
			List<Artist> artists = recording.getArtists();
			if(artists != null && !artists.isEmpty()){
				return artists.get(0).getName();
			}
		}
		return null;
	}
}
