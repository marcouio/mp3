
package com.molinari.mp3.business.acoustid;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AudioTrack {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
