
package com.molinari.mp3.business.acoustid;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recording {

    @SerializedName("releasegroups")
    @Expose
    private List<Releasegroup> releasegroups = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("artists")
    @Expose
    private List<Artist> artists = null;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    
    private boolean compilation = true;
    
    public List<Releasegroup> getReleasegroups() {
        return releasegroups;
    }

    public void setReleasegroups(List<Releasegroup> releasegroups) {
        this.releasegroups = releasegroups;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

	public boolean isCompilation() {
		return compilation;
	}

	public void setCompilation(boolean compilation) {
		this.compilation = compilation;
	}

}
