
package com.molinari.mp3.business.acoustid;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("recordings")
    @Expose
    private List<Recording> recordings = null;
    @SerializedName("score")
    @Expose
    private Double score;
    @SerializedName("id")
    @Expose
    private String id;

    public List<Recording> getRecordings() {
        return recordings;
    }

    public void setRecordings(List<Recording> recordings) {
        this.recordings = recordings;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
