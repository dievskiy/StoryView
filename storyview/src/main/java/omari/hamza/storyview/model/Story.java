package omari.hamza.storyview.model;

import java.io.Serializable;

public abstract class Story implements Serializable {

    private String description;

    private String title;

    public Story(String title, String description) {
        this.description = description;
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
