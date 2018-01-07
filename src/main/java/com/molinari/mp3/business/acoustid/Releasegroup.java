
package com.molinari.mp3.business.acoustid;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Releasegroup {

    @SerializedName("secondarytypes")
    @Expose
    private List<String> secondarytypes = null;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;

    public List<String> getSecondarytypes() {
        return secondarytypes;
    }

    public void setSecondarytypes(List<String> secondarytypes) {
        this.secondarytypes = secondarytypes;
    }

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
