package omari.hamza.storyview.model;

import java.io.Serializable;
import java.util.Date;

public class MyStory implements Serializable {

    private String url;

    private String description;

    private String title;

    public MyStory(String url, String title, String description) {
        this.url = url;
        this.title = title;
        this.description = description;
    }

    public MyStory(String url, Date date) {
        this.url = url;
    }

    public MyStory(String url) {
        this.url = url;
    }

    public MyStory() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
