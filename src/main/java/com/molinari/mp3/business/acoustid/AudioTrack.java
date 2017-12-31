package com.molinari.mp3.business.acoustid;

import java.io.Serializable;
import java.util.List;

public class AudioTrack implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String status;
	private transient List<Results> results;
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Results> getResults() {
		return results;
	}

	public void setResults(List<Results> results) {
		this.results = results;
	}

	public class Results{
		private List<Recordings> recordings;
		private String score;
		private String id;

		public List<Recordings> getRecordings() {
			return recordings;
		}

		public void setRecordings(List<Recordings> recordings) {
			this.recordings = recordings;
		}
		
		public String getScore() {
			return score;
		}

		public void setScore(String score) {
			this.score = score;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}

	public class Recordings {
		private List<Artists> artists;
		private int duration;
		private List<Releasegroups> releasegroups;
		private String title;

		public List<Artists> getArtists() {
			return artists;
		}

		public void setArtists(List<Artists> artists) {
			this.artists = artists;
		}

		public int getDuration() {
			return duration;
		}

		public void setDuration(int duration) {
			this.duration = duration;
		}

		public List<Releasegroups> getReleasegroups() {
			return releasegroups;
		}

		public void setReleasegroups(List<Releasegroups> releasegroups) {
			this.releasegroups = releasegroups;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
		
	}
	
	public class Artists {
		private String id;
		private String name;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	public class Releasegroups {
		private String type;
		private String id;
		private String title;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
	}
}
