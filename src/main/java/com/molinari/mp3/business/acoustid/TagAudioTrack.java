package com.molinari.mp3.business.acoustid;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.molinari.utility.CounterMap;

public class TagAudioTrack {

	private AudioTrack audioTrack;
	private String trackName;
	private Result bestResult;
	private Recording albumRecording;

	public TagAudioTrack(AudioTrack audioTrack) {
		this.audioTrack = audioTrack;
	}
	
	public String getAlbum() {

		Recording recording = getRecording();
		if (recording != null) {
			List<Releasegroup> releasegroups = recording.getReleasegroups();
			if (releasegroups != null && !releasegroups.isEmpty()) {
				Optional<Releasegroup> findAlbum = findAlbum(releasegroups);
				if(findAlbum.isPresent()) {
					return findAlbum.get().getTitle();
				}
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
			Recording albumRecording = getAlbumRecording(result.getRecordings());
			result.setCompilation(albumRecording.isCompilation());
			return albumRecording;
		}
		return null;
	}
	
	public Releasegroup getAlbumRelease(List<Releasegroup> releasegroups) {
		Optional<Releasegroup> rel = findAlbum(releasegroups);
		if(rel.isPresent()) {
			return rel.get();
		}
		return releasegroups.get(0);
	}
	
	public Recording getAlbumRecording(List<Recording> recordings) {
		
		if(albumRecording == null) {
		
			if(recordings != null) {
				
				CounterMap<String> map = countTrackName(recordings);
				
				String keyWithMaxCounter = map.getKeyWithMaxCounter();
				
				//cerco che non sia album e il titolo il più trovato
				for (Recording rec : recordings) {
					List<Releasegroup> releasegroups = rec.getReleasegroups();
					if (releasegroups != null && !releasegroups.isEmpty()) {
						
						Optional<Releasegroup> rel = findAlbum(releasegroups);
						if(rel.isPresent() && keyWithMaxCounter.equals(rec.getTitle())) {
							rec.setCompilation(false);
							albumRecording = rec;
							return albumRecording;
						}
					}
				}
				
				//cerco solo il titolo più trovato
				for (Recording rec : recordings) {
					List<Releasegroup> releasegroups = rec.getReleasegroups();
					if (releasegroups != null && !releasegroups.isEmpty()) {
						if(keyWithMaxCounter.equals(rec.getTitle())) {
							rec.setCompilation(false);
							albumRecording = rec;
							return albumRecording;
						}
					}
				}
				
				
				albumRecording = recordings.get(0);
			}
		}
		if(albumRecording == null) {
			albumRecording = new Recording();
		}
		return albumRecording;
	}

	public CounterMap<String> countTrackName(List<Recording> recordings) {
		CounterMap<String> map = new CounterMap<>();
		for (Recording rec : recordings) {
			List<Releasegroup> releasegroups = rec.getReleasegroups();
			if (releasegroups != null && !releasegroups.isEmpty()) {
				map.put(rec.getTitle());
			}
		}
		return map;
	}
	public String getTrackName() {
		if(trackName == null) {
			Recording recording = getRecording();
			if(recording != null){
				trackName = recording.getTitle();
			}
		}
		return trackName;
	}

	public Optional<Releasegroup> findAlbum(List<Releasegroup> releasegroups) {
		Optional<Releasegroup> rel = releasegroups.stream().filter((r) -> {
			boolean isAlbum = "ALBUM".equalsIgnoreCase(r.getType());
			boolean isCompilation = r.getSecondarytypes() != null && r.getSecondarytypes().stream().anyMatch(s -> s.equalsIgnoreCase("COMPILATION"));
			return isAlbum && !isCompilation;
		}).findFirst();
		return rel;
	}
	
	public Result getBestResult(List<Result> results){
		
		if(bestResult == null) {
		
			bestResult = results.stream().max(maxScore()).get();
			
			Optional<Result> findFirst = getResultNoCompilation(results).findFirst();
			if(findFirst.isPresent()) {
				bestResult = getResultNoCompilation(results).max(maxScore()).get();
			}
		}
		return bestResult;
	}

	public Stream<Result> getResultNoCompilation(List<Result> results) {
		return results.stream().filter(r -> !r.isCompilation());
	}

	public Comparator<? super Result> maxScore() {
		return (r1, r2) -> {
			if(r1.getScore() >= r2.getScore()) {
				return 1;
			}
			return -1;
		};
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
